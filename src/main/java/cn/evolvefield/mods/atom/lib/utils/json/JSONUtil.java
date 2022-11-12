package cn.evolvefield.mods.atom.lib.utils.json;

import cn.evolvefield.mods.atom.lib.utils.misc.LogUtil;
import com.google.gson.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/8 21:58
 * Version: 1.0
 */
public class JSONUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String getStringOr(String pKey, JsonObject pJson, String pDefaultValue) {
        JsonElement jsonelement = pJson.get(pKey);
        if (jsonelement != null) {
            return jsonelement.isJsonNull() ? pDefaultValue : jsonelement.getAsString();
        } else {
            return pDefaultValue;
        }
    }

    public static int getIntOr(String pKey, JsonObject pJson, int pDefaultValue) {
        JsonElement jsonelement = pJson.get(pKey);
        if (jsonelement != null) {
            return jsonelement.isJsonNull() ? pDefaultValue : jsonelement.getAsInt();
        } else {
            return pDefaultValue;
        }
    }

    public static long getLongOr(String pKey, JsonObject pJson, long pDefaultValue) {
        JsonElement jsonelement = pJson.get(pKey);
        if (jsonelement != null) {
            return jsonelement.isJsonNull() ? pDefaultValue : jsonelement.getAsLong();
        } else {
            return pDefaultValue;
        }
    }

    public static double getDoubleOr(String pKey, JsonObject pJson, double pDefaultValue) {
        JsonElement jsonelement = pJson.get(pKey);
        if (jsonelement != null) {
            return jsonelement.isJsonNull() ? pDefaultValue : jsonelement.getAsDouble();
        } else {
            return pDefaultValue;
        }
    }

    public static boolean getBooleanOr(String pKey, JsonObject pJson, boolean pDefaultValue) {
        JsonElement jsonelement = pJson.get(pKey);
        if (jsonelement != null) {
            return jsonelement.isJsonNull() ? pDefaultValue : jsonelement.getAsBoolean();
        } else {
            return pDefaultValue;
        }
    }

    public static Date getDateOr(String pKey, JsonObject pJson) {
        JsonElement jsonelement = pJson.get(pKey);
        return jsonelement != null ? new Date(Long.parseLong(jsonelement.getAsString())) : new Date();
    }

    public static JsonArray getArray(String string, JsonObject obj) {
        if (obj.get(string) != null) {
            JsonElement el = obj.get(string);
            if (el.isJsonArray()) {
                return obj.get(string).getAsJsonArray();
            } else {
                return new JsonArray();
            }
        } else {
            return new JsonArray();
        }
    }


    /**
     * @param file      the file to be parsed into json
     * @param print_err notify in logs about errors while parsing or file missing
     * @return JsonElement from given file, or null
     */
    public static JsonElement read(File file, boolean print_err) {
        return read(file, print_err, null);
    }

    /**
     * @param file      the file to be parsed into json
     * @param print_err notify in logs about errors while parsing or file missing
     * @param def       default element to return in case there are errors while parsing
     * @return JsonElement from the given file, can be null
     */
    public static JsonElement read(File file, boolean print_err, @Nullable JsonElement def) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileReader fr = new FileReader(file);
            JsonElement obj = JsonParser.parseReader(fr);
            fr.close();
            return obj;
        } catch (Exception e) {
            if (print_err) {
                e.printStackTrace();
            }
            LogUtil.logInfo("File '" + file + "' seems to be missing, or has invalid format.");
            return def;
        }
    }

    /**
     * @param file the file to be parsed into json
     * @return JsonObject from file or a new JsonObject if there are errors in previous/super methods
     */
    public static JsonObject get(File file) {
        JsonElement e = read(file, false);
        if (e == null || !e.isJsonObject()) {
            return new JsonObject();
        } else return e.getAsJsonObject();
    }

    /**
     * Writes a JsonObject into the specified file (using Gson Pretty Printing)
     *
     * @param file  target file
     * @param obj   JsonElement to be written into the file
     * @param check check if the parent file/folder exists
     */
    public static void write(File file, JsonElement obj, boolean check) {
        try {
            if (check) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
            }
            FileWriter fw = new FileWriter(file);
            fw.write(gson.toJson(obj));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a JsonObject into the specified file (using Gson Pretty Printing)
     *
     * @param file target file
     * @param obj  JsonElement to be written into the file
     */
    public static void write(File file, JsonElement obj) {
        write(file, obj, false);
    }

    /**
     * @param file   File to be updated
     * @param string Target value/object
     * @param value
     */
    public static void update(File file, String string, String value) {
        JsonObject obj = get(file);
        obj.addProperty(string, value);
        write(file, obj);
    }

    /**
     * @param file   File to be updated
     * @param string Target value/object
     * @param value
     */
    public static void update(File file, String string, boolean value) {
        JsonObject obj = get(file);
        obj.addProperty(string, value);
        write(file, obj);
    }

    /**
     * @param file   File to be updated
     * @param string Target value/object
     * @param value
     */
    public static void update(File file, String string, Number value) {
        JsonObject obj = get(file);
        obj.addProperty(string, value);
        write(file, obj);
    }

    /**
     * @param file    File to be updated
     * @param string  Target value/object
     * @param element
     */
    public static void update(File file, String string, JsonElement element) {
        JsonObject obj = get(file);
        obj.add(string, element);
        write(file, obj);
    }
}
