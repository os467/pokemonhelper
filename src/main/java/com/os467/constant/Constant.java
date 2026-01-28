package com.os467.constant;

import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class Constant {

    public static final String MOD_ID = "pokemonhelper";

    public static final Identifier POKEMON_EGG_ID = Identifier.of("cobbreeding", "pokemon_egg");

    public static final ModelIdentifier CUSTOM_SHINY_EGG =
            new ModelIdentifier(Identifier.of("cobbreeding", "custom_shiny_egg"), "inventory");
}
