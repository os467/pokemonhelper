package com.os467.pokemonhelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.time.Instant;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

public class ThunderWeatherChecker implements ClientModInitializer {
    private boolean hasNotified = false; // 用于防止重复发送消息
    private static final String TARGET_PLAYER_ID = "dyingend"; // 目标玩家 ID
    private static final String WEATHER_MESSAGE = "qq当前天气：雷暴"; // 提示文本
    private static final String SYSTEM_MESSAGE = "[系统] 检测到雷暴天气！"; // 系统提示文本


    @Override
    public void onInitializeClient() {
        // 监听客户端 Tick 事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && client.player != null && client.player.getName().equals(TARGET_PLAYER_ID)) {
                checkWeatherAndSendMessage(client);
            }
        });
    }

    private void checkWeatherAndSendMessage(MinecraftClient client) {
        World world = client.world;

        // 检查是否是雷暴天气
        if (world.isThundering()) {
            if (!hasNotified) {
                // 发送系统提示消息
                client.player.sendMessage(Text.of(SYSTEM_MESSAGE), false);

                // 直接发送聊天消息
                //sendChatMessage(client, WEATHER_MESSAGE);

                hasNotified = true; // 标记已发送消息
            }
        } else {
            // 如果不是雷暴天气，重置标记
            hasNotified = false;
        }
    }

    private void sendChatMessage(MinecraftClient client, String message) {
        if (client.getNetworkHandler() != null) {
            // 获取当前时间戳
            Instant timestamp = Instant.now();

            // 生成一个随机的 salt（用于消息签名）
            long salt = ThreadLocalRandom.current().nextLong();

            // 创建 Acknowledgment 对象
            int offset = 0; // 偏移量
            BitSet acknowledged = new BitSet(20); // 确认位集
            LastSeenMessageList.Acknowledgment acknowledgment = new LastSeenMessageList.Acknowledgment(offset, acknowledged);

            // 创建聊天消息包
            ChatMessageC2SPacket packet = new ChatMessageC2SPacket(
                    message, // 聊天消息内容
                    timestamp, // 时间戳
                    salt, // 随机 salt
                    null, // 消息签名（可以为 null）
                    acknowledgment // 消息确认方式
            );

            // 发送包到服务器
            client.getNetworkHandler().sendPacket(packet);
        }
    }
}