package com.os467.pokemonhelper.translation;

import com.os467.pokemonhelper.utils.JsonReader;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PokemonNameTranslation implements ToCNTranslation {

    private static Map<String, String> cnEN;
    private static Map<String, String> enCN;

    static {
        initTranslations();
    }

    public static void initTranslations() {
        // 加载翻译数据
        cnEN = JsonReader.loadTranslationMap("translations");
        buildReverseMap();
    }

    // 提供翻译方法
    public static String translateToEnglish(String chinese) {
        return cnEN.getOrDefault(chinese, chinese);
    }


    public static void buildReverseMap() {
        enCN = new HashMap<>();
        cnEN.forEach((cn, en) -> enCN.put(en.toLowerCase(Locale.ROOT), cn));
    }

    public String translateToChinese(String input) {
        if (enCN == null) {
            buildReverseMap();
        }
        return enCN.getOrDefault(input.toLowerCase(Locale.ROOT), input);
    }


}
