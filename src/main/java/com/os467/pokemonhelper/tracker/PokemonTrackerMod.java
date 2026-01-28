package com.os467.pokemonhelper.tracker;

import com.os467.pokemonhelper.tracker.handler.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PokemonTrackerMod implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        KeyInputHandler.register();

        // 注册到客户端Tick事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> EntityTracker.checkEntities());
    }



}