package com.os467.pokemonhelper;

import com.os467.constant.Constant;
import com.os467.pokemonhelper.utils.MsgSender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

import java.time.Instant;
import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

public class VersionInfo implements ClientModInitializer {

    private static boolean initFlag = false;


    @Override
    public void onInitializeClient() {
        // 监听客户端 Tick 事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && client.player != null) {
                if (!initFlag){
                    initInfo(client);
                    initFlag = true;
                }
            }
        });
    }

    private void initInfo(MinecraftClient client) {

        String modVersion = FabricLoader.getInstance()
                .getModContainer(Constant.MOD_ID) // 你的mod id
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        client.player.sendMessage(Text.of("§a宝可梦辅助mod初始化成功!"), false);
        // 发送系统提示消息
        client.player.sendMessage(Text.of("§a=====================mod功能====================="), false);
        client.player.sendMessage(Text.of("§a支持宝可梦蛋属性闪光等详细信息标注，可以配合终端搜索"), false);
        client.player.sendMessage(Text.of("§a默认追踪神兽幻兽悖谬异兽,开启追踪即可，其它请手动追踪"), false);
        client.player.sendMessage(Text.of("§a支持指定宝可梦范围追踪(已支持中英文搜索，忽略大小写)"), false);
        client.player.sendMessage(Text.of("§a支持范围追踪时声音播报提示(主音量开启即可,其它可以关闭)"), false);
        client.player.sendMessage(Text.of("§a范围追踪时请打开粒子效果，会有粒子效果追踪目标宝可梦"), false);
        client.player.sendMessage(Text.of("§a范围追踪功能默认逗号键位，可在按键设置中更改"), false);
        client.player.sendMessage(Text.of("§a================================================="), false);
        client.player.sendMessage(Text.of("§a版本号: "+modVersion + "  @dyingend"), false);

        MsgSender.sendChatMessage(client,"【v3】宝可梦辅助mod初始化成功! author: dyingend");
    }


}
