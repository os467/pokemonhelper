package com.os467.pokemonhelper.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.os467.constant.Constant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JsonReader {


    public static Map<String, String> loadTranslationMap(String translationFileName) {
        try {
            // 获取资源文件的输入流
            InputStream inputStream = JsonReader.class.getClassLoader()
                .getResourceAsStream("assets/"+ Constant.MOD_ID +"/translations/"+translationFileName+".json");

            // 使用 BufferedReader 读取文件内容
            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            }

            if (inputStream == null) {
                throw new RuntimeException("无法找到 "+translationFileName+".json 文件");
            }

            // 定义要转换的类型
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();

            // 使用Gson解析JSON到Map
            return new Gson().fromJson(jsonContent.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(); // 返回空Map而不是null
        }
    }
}