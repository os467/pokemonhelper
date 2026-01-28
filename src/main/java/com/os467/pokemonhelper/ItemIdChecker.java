package com.os467.pokemonhelper;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemIdChecker {

    public static boolean equals(ItemStack stack, Identifier id){
        Identifier item = Registries.ITEM.getId(stack.getItem());
        return item.equals(id);
    }

    public static boolean contains(ItemStack stack, String id){
        Identifier item = Registries.ITEM.getId(stack.getItem());
        if (!"cobbreeding".equals(item.getNamespace())){
            return false;
        }
        return item.getPath().contains(id);
    }

}
