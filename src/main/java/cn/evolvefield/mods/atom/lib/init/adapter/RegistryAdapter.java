package cn.evolvefield.mods.atom.lib.init.adapter;

import cn.evolvefield.mods.atom.lib.annotations.OnlyIf;
import cn.evolvefield.mods.atom.lib.annotations.RegistryName;
import cn.evolvefield.mods.atom.lib.annotations.Setup;
import cn.evolvefield.mods.atom.lib.annotations.SimplyRegister;
import cn.evolvefield.mods.atom.lib.annotations.client.ClientSetup;
import cn.evolvefield.mods.atom.lib.api.blocks.ICreativeTabBlock;
import cn.evolvefield.mods.atom.lib.api.blocks.ICustomBlockItem;
import cn.evolvefield.mods.atom.lib.api.blocks.IItemPropertySupplier;
import cn.evolvefield.mods.atom.lib.api.blocks.INoItemBlock;
import cn.evolvefield.mods.atom.lib.api.fml.IRegisterListener;
import cn.evolvefield.mods.atom.lib.api.registry.RegistryMapping;
import cn.evolvefield.mods.atom.lib.utils.java.Cast;
import cn.evolvefield.mods.atom.lib.utils.java.ReflectionUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:12
 * Description:
 */
public class RegistryAdapter {


    private static final Map<Class<?>, List<Tuple<Block, ResourceLocation>>> blocks = new ConcurrentHashMap<>();

    public static <T> BiConsumer<ResourceLocation, T> createRegisterer(IForgeRegistry<T> registry, String prefix) {
        return (name, entry) ->
        {
            name = new ResourceLocation(name.getNamespace(), prefix + name.getPath());
            IRegisterListener l = Cast.cast(entry, IRegisterListener.class);
            if (l != null)
                l.onPreRegistered();
            registry.register(name, entry);
            if (l != null)
                l.onPostRegistered();
        };
    }

    public static <T> int register(IForgeRegistry<T> registry, Class<?> source, String modid, String prefix) {
        var superType = RegistryMapping.getSuperType(registry);

        if (superType == null) {
            // Unknown registry.
            return 0;
        }

        List<Tuple<Block, ResourceLocation>> blockList = blocks.computeIfAbsent(source, s -> new ArrayList<>());

        BiConsumer<ResourceLocation, T> grabber = createRegisterer(registry, prefix).andThen((key, handler) ->
        {
            if (handler instanceof Block b)
                blockList.add(new Tuple<>(b, key));
        });

        if (Item.class.equals(superType)) for (Tuple<Block, ResourceLocation> e : blockList) {
            Block blk = e.getA();
            if (blk instanceof INoItemBlock) continue;
            BlockItem item;
            IItemPropertySupplier gen = Cast.cast(blk, IItemPropertySupplier.class);
            if (blk instanceof ICustomBlockItem customBlockItem) item = customBlockItem.createBlockItem();
            else {
                Item.Properties props = gen != null ? gen.createItemProperties(new Item.Properties()) : new Item.Properties();
                if (blk instanceof ICreativeTabBlock creativeTabBlock)
                    props = props.tab(creativeTabBlock.getCreativeTab());
                item = new BlockItem(blk, props);
            }
            grabber.accept(e.getB(), Cast.cast(item));
        }

        int prevSize = registry.getValues().size();

        Arrays
                .stream(source.getDeclaredMethods())
                .filter(m -> m.getAnnotation(SimplyRegister.class) != null
                        && m.getParameterCount() == 1
                        && BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0])
                        && ReflectionUtil.doesParameterTypeArgsMatch(m.getParameters()[0], ResourceLocation.class, superType))
                .forEach(method ->
                {
                    final String prefix2 = Optional.ofNullable(method.getAnnotation(SimplyRegister.class)).map(SimplyRegister::prefix).orElse("");

                    if (Modifier.isStatic(method.getModifiers()))
                        try {
                            method.setAccessible(true);

                            BiConsumer<ResourceLocation, T> grabber2 = (id, obj) ->
                            {
                                id = new ResourceLocation(id.getNamespace(), prefix2 + id.getPath());
                                grabber.accept(id, obj);
                            };

                            method.invoke(null, grabber2);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                });

        Arrays
                .stream(source.getDeclaredFields())
                .filter(f -> superType.isAssignableFrom(f.getType()))
                .forEach(field ->
                {
                    if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                        try {
                            field.setAccessible(true);
                            var name = field.getAnnotation(RegistryName.class);
                            var rl = new ResourceLocation(modid, name.value());
                            var t = superType.cast(field.get(null));
                            var onlyIf = field.getAnnotation(OnlyIf.class); // Bring back OnlyIf, for registries that are non-intrusive. (Mostly, for custom registry types)
                            if (!RegistryMapping.isNonIntrusive(registry)
                                    || OnlyIfAdapter.checkCondition(onlyIf, source.toString(), superType.getSimpleName(), t, rl)) {
                                grabber.accept(rl, t);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                });

        return registry.getValues().size() - prevSize;
    }


    public static void setup(FMLCommonSetupEvent event, Class<?> source, String memberName) {
        String methodName = memberName.substring(0, memberName.indexOf('('));

        Arrays
                .stream(source.getDeclaredMethods())
                .filter(m -> m.getAnnotation(Setup.class) != null && m.getName().equals(methodName))
                .forEach(method ->
                {
                    if (Modifier.isStatic(method.getModifiers()))
                        try {
                            OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
                            if (!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "Setup", null, null)) return;
                            method.setAccessible(true);
                            if (method.getParameterCount() == 0)
                                method.invoke(null);
                            else if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == FMLCommonSetupEvent.class)
                                method.invoke(null, event);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            RuntimeException re = null;
                            if (e instanceof InvocationTargetException && e.getCause() instanceof RuntimeException)
                                re = (RuntimeException) e.getCause();
                            if (e instanceof RuntimeException)
                                re = (RuntimeException) e;
                            if (re != null)
                                throw re;
                            e.printStackTrace();
                        }
                });
    }

    public static void clientSetup(FMLClientSetupEvent event, Class<?> source, String memberName) {
        String methodName = memberName.substring(0, memberName.indexOf('('));

        Arrays
                .stream(source.getDeclaredMethods())
                .filter(m -> m.getAnnotation(ClientSetup.class) != null && m.getName().equals(methodName))
                .forEach(method ->
                {
                    if (Modifier.isStatic(method.getModifiers()))
                        try {
                            OnlyIf onlyIf = method.getAnnotation(OnlyIf.class);
                            if (!OnlyIfAdapter.checkCondition(onlyIf, source.toString(), "ClientSetup", null, null))
                                return;
                            method.setAccessible(true);
                            if (method.getParameterCount() == 0)
                                method.invoke(null);
                            else if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == FMLClientSetupEvent.class)
                                method.invoke(null, event);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            RuntimeException re = null;
                            if (e instanceof InvocationTargetException && e.getCause() instanceof RuntimeException)
                                re = (RuntimeException) e.getCause();
                            if (e instanceof RuntimeException)
                                re = (RuntimeException) e;
                            if (re != null)
                                throw re;
                            e.printStackTrace();
                        }
                });
    }

}
