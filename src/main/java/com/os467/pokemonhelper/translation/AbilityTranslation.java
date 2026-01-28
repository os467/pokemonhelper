package com.os467.pokemonhelper.translation;

import com.os467.pokemonhelper.utils.JsonReader;

import java.util.Locale;
import java.util.Map;

public class AbilityTranslation implements ToCNTranslation{
    private static Map<String, String> map;

    static {
        initTranslations();
    }

    public static void initTranslations() {
        // 加载翻译数据
        map = JsonReader.loadTranslationMap("ability");
    }


    @Override
    public String translateToChinese(String input) {
        return map.getOrDefault(input.toLowerCase(Locale.ROOT), input);
    }
}
