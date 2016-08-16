package com.ef.newlead.util;

import android.content.Context;

import com.ef.newlead.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SystemText {

    private static final String DEFAULT_LANGUAGE = "en";

    private static JSONObject systemText;

    public static void init(Context context) {

        File resourceFolder = new File(context.getFilesDir() + File.separator + Constant.RESOURCE_FOLDER);

        if (!resourceFolder.exists()) {
            FileUtils.CopyAssets(context, Constant.RESOURCE_FOLDER, FileUtils.getInternalFolderPath(context, Constant.RESOURCE_FOLDER));
        }

        String language = MiscUtils.getSystemLanguage(context);

        JSONObject mapping = FileUtils.readJsonFile(resourceFolder.getAbsolutePath(), Constant.MAPPING_FILE);
        String textFile;

        if (mapping == null || mapping.optString(language).isEmpty()) {
            textFile = DEFAULT_LANGUAGE;
        } else {
            textFile = mapping.optString(language);
        }

        String textFolder = FileUtils.getInternalFolderPath(context, Constant.SYSTEM_TEXT_FOLDER);
        systemText = FileUtils.readJsonFile(textFolder, textFile + ".json");
    }

    public static String getSystemText(Context context, String key) {
        if (systemText == null) {
            init(context);
        }

        if (systemText.optString(key) == null || systemText.optString(key).isEmpty()) {
            return key;
        }

        return systemText.optString(key);
    }
}
