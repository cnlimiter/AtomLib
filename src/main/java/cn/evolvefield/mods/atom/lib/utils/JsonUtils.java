package cn.evolvefield.mods.atom.lib.utils;

import cn.evolvefield.mods.atom.lib.Static;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/8 21:58
 * Version: 1.0
 */
public class JsonUtils {
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
            Static.LOGGER.info("File '" + file + "' seems to be missing, or has invalid format.");
            return def;
        }
    }
}
