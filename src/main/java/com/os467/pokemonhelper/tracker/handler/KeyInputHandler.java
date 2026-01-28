package com.os467.pokemonhelper.tracker.handler;

import com.os467.pokemonhelper.tracker.screen.EntityTrackingScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

// 在客户端初始化类中
public class KeyInputHandler {
    public static final KeyBinding OPEN_GUI_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pokemontracker.toggle", // 按键绑定的 ID
            InputUtil.Type.KEYSYM, // 按键类型（键盘按键）
            GLFW.GLFW_KEY_COMMA, // 默认按键（,键）
            "category.pokemontracker.keys" // 按键分类
    ));

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_GUI_KEY.wasPressed()) {
                client.setScreen(new EntityTrackingScreen());
            }
        });
    }
}