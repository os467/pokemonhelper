package com.os467.pokemonhelper.utils;

import com.os467.constant.Constant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;

public class SetReader {

    public static HashSet<String> loadSet(String translationFileName) {
        try {
            // 获取资源文件的输入流
            InputStream inputStream = JsonReader.class.getClassLoader()
                    .getResourceAsStream("assets/"+ Constant.MOD_ID +"/translations/"+translationFileName+".txt");

            // 使用 BufferedReader 读取文件内容
            StringBuilder s = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    s.append(line);
                }
            }

            if (inputStream == null) {
                throw new RuntimeException("无法找到 "+translationFileName+".txt 文件");
            }


            String[] ss = s.toString().split(",");
            HashSet<String> set = new HashSet<>();
            set.addAll(Arrays.asList(ss.clone()));
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

}
