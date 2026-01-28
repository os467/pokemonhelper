package com.os467.pokemonhelper.translation;

import com.os467.pokemonhelper.utils.JsonReader;

import java.util.Locale;
import java.util.Map;

public class PokemonNatureTranslation implements ToCNTranslation {

    private static Map<String, String> enCN;

    static {
        initTranslations();
    }

    public static void initTranslations() {
        // 加载翻译数据
        enCN = JsonReader.loadTranslationMap("nature");
    }

    public String translateToChinese(String input) {
        return enCN.getOrDefault(input.toLowerCase(Locale.ROOT), input);
    }

}
