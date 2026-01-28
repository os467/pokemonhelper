package com.os467.pokemonhelper.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.time.Instant;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

public class MsgSender {

    public static void sendChatMessage(MinecraftClient client, String message){
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
