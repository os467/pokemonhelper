package com.os467.pokemonhelper;

import net.minecraft.text.Text;

import java.util.List;

public class ShinyModifier {

    public static void modify(List<Text> lines){
        // 检查是否已经添加了 [闪光] 提示
        boolean checked = lines.stream().anyMatch(line -> line.getString().contains("[闪光]"));
        if (!checked) {
            // 在第二行插入 [闪光] 提示
            if (lines.size() > 1) {
                lines.add(1, Text.of("§e[shiny] 蛋闪"));
            } else {
                lines.add(Text.of("§e[shiny] 蛋闪"));
            }
        }
    }

}
