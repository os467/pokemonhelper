package com.os467.pokemonhelper;

import net.minecraft.text.Text;

import java.util.List;

public class SixVModifier {

    public static void modify(List<Text> lines){
        boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[6V!!!]"));
        if (!checked){
            if (lines.size() > 1){
                lines.add(1, Text.of("§b[6V!!!]"));
            } else {
                lines.add(Text.of("§b[6V!!!]"));
            }
        }
    }

}
