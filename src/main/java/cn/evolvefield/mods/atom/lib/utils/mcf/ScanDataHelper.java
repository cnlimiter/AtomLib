package cn.evolvefield.mods.atom.lib.utils.mcf;

import cn.evolvefield.mods.atom.lib.utils.java.Fetcher;
import cn.evolvefield.mods.atom.lib.utils.java.ReflectionUtil;
import com.google.common.base.Predicates;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.*;
import java.util.function.Predicate;

/**
 * Project: atomlib
 * Author: cnlimiter
 * Date: 2022/11/13 2:24
 * Description:扫描查询数据
 */
public class ScanDataHelper {


    public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(Class<? extends Annotation> annotation) {
        return lookupAnnotatedObjects(annotation, modAwareAnnotationData -> true);
    }

    public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(Class<? extends Annotation> annotation, Predicate<ModAwareAnnotationData> matcher) {
        List<ModAwareAnnotationData> data = new ArrayList<>();
        Type annotationType = Type.getType(annotation);
        ModList.get().getAllScanData().stream()
                .flatMap(d -> d.getAnnotations().stream().map(ad -> new ModAwareAnnotationData(ad, d)))
                .filter(ad -> annotationType.equals(ad.parent.annotationType()) && matcher.test(ad))
                .forEach(data::add);
        return data;
    }


    public static class ModAwareAnnotationData {
        public final ModFileScanData.AnnotationData parent;
        private final ModFileScanData modFile;
        private Fetcher<Optional<FMLModContainer>> ownerMod;

        public ModAwareAnnotationData(ModFileScanData.AnnotationData parent, ModFileScanData modFile) {
            this.parent = parent;
            this.modFile = modFile;
        }

        public Optional<FMLModContainer> getOwnerMod() {
            if (ownerMod == null) {
                ownerMod = Fetcher.fetchOnce(() -> modFile.getIModInfoData().stream()
                        .flatMap(inf -> inf.getMods().stream())
                        .map(IModInfo::getModId)
                        .map(ModList.get()::getModContainerById)
                        .map(modopt -> modopt.orElse(null))
                        .filter(Predicates.instanceOf(FMLModContainer.class))
                        .findFirst()
                        .map(FMLModContainer.class::cast));
            }

            return ownerMod.get();
        }

        public ModFileScanData getModFile() {
            return modFile;
        }

        public Class<?> getOwnerClass() {
            return ReflectionUtil.fetchClass(parent.clazz());
        }

        public Type clazz() {
            return parent.clazz();
        }

        public Optional<Object> getProperty(String key) {
            Map<String, Object> map = parent.annotationData();
            return map.containsKey(key) ? Optional.ofNullable(map.get(key)) : Optional.empty();
        }

        public ElementType getTargetType() {
            return parent.targetType();
        }

        public String getMemberName() {
            return parent.memberName();
        }
    }
}
