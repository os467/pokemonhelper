package com.os467.pokemonhelper.translation;

import com.os467.pokemonhelper.utils.JsonReader;

import java.util.Map;

public class EggMovesTranslation implements ToCNTranslation {

    private static Map<String, String> map;
    private static StringBuilder stringBuilder = new StringBuilder();

    static {
        initTranslations();
    }

    public static void initTranslations() {
        // 加载翻译数据
        map = JsonReader.loadTranslationMap("egg_move");
    }


    @Override
    public String translateToChinese(String input) {
        String[] moves = input.split(",");
        for (int i = 0; i < moves.length; i++) {
            stringBuilder.append(map.get(moves[i]));
            stringBuilder.append(",");
        }
        String s = stringBuilder.toString();
        stringBuilder.setLength(0);
        return s.substring(0,s.length()-1);
    }
}
