package com.os467.pokemonhelper.tracker;

import com.os467.pokemonhelper.tracker.config.Config;
import com.os467.pokemonhelper.translation.PokemonNameTranslation;
import com.os467.pokemonhelper.translation.ToCNTranslation;
import com.os467.pokemonhelper.utils.MsgSender;
import com.os467.pokemonhelper.utils.SetReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class EntityTracker {

    private static final ToCNTranslation pokemonNameTranslation = new PokemonNameTranslation();
    public static final String MOD_ID = "pokemonshinychecker-mod";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static Map<String,Boolean> lastCheckMap = new HashMap<>();
    private static final Set<Integer> markedEntities = new HashSet<>();

    private static boolean hasRareChecked = false;

    private static long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 1000; // 1秒 = 1000毫秒

    private static HashSet<String> rareSet = new HashSet<>();

    static {
        HashSet<String> en = SetReader.loadSet("rarepokemon");
        Set<String> enL = en.stream().map(i -> i.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
        rareSet.addAll(enL);
    }

    @Environment(EnvType.CLIENT)
    public static void checkEntities() {

        if (!Config.isTrackingEnabled()) return;

        //优化检测
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime < CHECK_INTERVAL) {
            return; // 如果还没到1秒间隔，直接返回
        }
        lastCheckTime = currentTime;

        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;

            ClientWorld world = client.world;

            // 清除已不存在实体的标记
            markedEntities.removeIf(id -> world.getEntityById(id) == null);

            Iterable<Entity> entities = world.getEntities();

/*            boolean hasRare = false;
            String rareName = "";*/
/*            for (Entity entity : entities) {
                NbtCompound nbt = new NbtCompound();
                entity.writeNbt(nbt);
                if (!nbt.toString().contains("cobblemon")) continue;
                if (nbt.toString().contains("Owner:")) continue;
*//*                if (nbt.toString().contains("PokemonOriginalTrainerType:\"PLAYER\""))continue;*//*
                String nowPokeName = getSpecies(nbt).toLowerCase(Locale.ROOT);
                //检测是否存在稀有宝可梦
                if (isRare(nowPokeName)){
                    if (!markedEntities.contains(entity.getId())) {
                        markedEntities.add(entity.getId());
                    }
                    // 生成光束
                    spawnDirectionalBeam(client.player, entity, world);

                    playDetectionSound(client);

                    client.player.sendMessage(
                            Text.literal("【检测到稀有宝可梦】 " + pokemonNameTranslation.translateToChinese(nowPokeName)).formatted(Formatting.GREEN), false
                    );

                    System.out.println(nbt);
                }
            }*/
/*
            if (hasRare){
                if (!hasRareChecked){
                    MsgSender.sendChatMessage(client,"出货啦！ 【" + rareName +"】");
                    hasRareChecked = true;
                }
            }else {
                hasRareChecked = false;
            }*/

            String[] trackedEntityNames = Config.getTrackedEntityNames();

            if (trackedEntityNames.length == 0 || trackedEntityNames[0].isEmpty()) {
                trackedEntityNames = new String[]{" "};
            }


            boolean hasRare = false;
            String rareName = "";

            boolean hasTarget = false;

            int dis = Integer.MAX_VALUE;

            boolean[] hitFlag = new boolean[trackedEntityNames.length];


            for (Entity entity : entities) {

                if (entity.distanceTo(client.player) > Config.getSearchRange()) {
                    removeMarkers(entity);
                    continue;
                }

                double cd = Math.abs(entity.distanceTo(client.player) - Config.getSearchRange());
                if (cd < dis){
                    dis = (int)cd;
                }

                NbtCompound nbt = new NbtCompound();
                entity.writeNbt(nbt);
                if (!nbt.toString().contains("cobblemon")) continue;
                if (nbt.toString().contains("Owner:")) continue;
                if (nbt.toString().contains("PokemonOriginalTrainerType:\"PLAYER\""))continue;
                if(nbt.toString().contains("BattleId:"))continue;

                System.out.println(nbt);
                String nowPokeName = getSpecies(nbt).toLowerCase(Locale.ROOT);
                System.out.println(nowPokeName);

                //中英文都处理成英文
                for (int i = 0; i < trackedEntityNames.length; i++) {
                    trackedEntityNames[i] = PokemonNameTranslation.translateToEnglish(trackedEntityNames[i]);

                    //targetName = PokemonNameTranslation.translateToEnglish(targetName);

                    if (trackedEntityNames[i].toLowerCase(Locale.ROOT).equals(nowPokeName)) {
                        hitFlag[i] = true;
                        hasTarget = true;
                        // 生成光束
                        spawnDirectionalBeam(client.player, entity, world);

                    } else {
                        removeMarkers(entity);
                    }
                }

                //检测是否为稀有宝可梦
                if (isRare(nowPokeName)){
                    if (!markedEntities.contains(entity.getId())) {
                        markedEntities.add(entity.getId());
                    }
                    // 生成光束
                    spawnDirectionalBeam(client.player, entity, world);

                    playDetectionSound(client);

                    client.player.sendMessage(
                            Text.literal("【检测到稀有宝可梦】 " + pokemonNameTranslation.translateToChinese(nowPokeName)).formatted(Formatting.GREEN), false
                    );

                    hasRare = true;
                    rareName = pokemonNameTranslation.translateToChinese(nowPokeName);
                }else {
                    removeMarkers(entity);
                }

            }

            if (hasRare){
                if (!hasRareChecked){
                    MsgSender.sendChatMessage(client,"出货啦！ 【" + rareName +"】");
                    hasRareChecked = true;
                }
            }else {
                hasRareChecked = false;
            }

            if (hasTarget) {
                for (int i = 0; i < hitFlag.length; i++) {
                    if (hitFlag[i]){
                        Boolean lastCheck = lastCheckMap.get(trackedEntityNames[i].toLowerCase(Locale.ROOT));
                        if (lastCheck != null && lastCheck){
                            continue;
                        }else {
                            lastCheckMap.put(trackedEntityNames[i].toLowerCase(Locale.ROOT),true);
                        }

                        playDetectionSound(client);
                        client.player.sendMessage(
                                Text.literal("【检测到目标宝可梦】 " + pokemonNameTranslation.translateToChinese(trackedEntityNames[i])).formatted(Formatting.GREEN), false
                        );
                    } else {
                        lastCheckMap.remove(trackedEntityNames[i].toLowerCase(Locale.ROOT));
                    }
                }

            } else if (lastCheckMap.size() != 0){
                resetLastCheck(trackedEntityNames);
            }




        }catch (Exception e){
            LOGGER.error(e.getMessage() + e.getLocalizedMessage());
        }
    }

    private static boolean isRare(String pokeName) {
        return rareSet.contains(pokeName);
    }


    @Environment(EnvType.CLIENT)
    private static void spawnDirectionalBeam(Entity player, Entity target, ClientWorld world) {
        // 唯一修改点：将起点从 player.getEyeHeight() 改为 player.getHeight() * 0.5
        Vec3d playerPos = player.getPos().add(0, player.getHeight() * 0.5, 0); // 腰部高度
        Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);
        Vec3d direction = targetPos.subtract(playerPos).normalize();

        // 保持原有光束参数
        double beamLength = playerPos.distanceTo(targetPos);
        int particleCount = (int) (beamLength * 5);
        double step = beamLength / particleCount;

        // 保持原有粒子生成逻辑
        for (int i = 0; i <= particleCount; i++) {
            Vec3d point = playerPos.add(direction.multiply(step * i));
            world.addParticle(
                    ParticleTypes.ELECTRIC_SPARK, // 保持原粒子类型
                    point.x,
                    point.y,
                    point.z,
                    0, 0, 0 // 保持原运动参数
            );
        }
    }

    @Environment(EnvType.CLIENT)
    private static void playDetectionSound(MinecraftClient client) {
        // 播放原版"叮"声（经验球拾取音效）
        client.getSoundManager().play(
                PositionedSoundInstance.master(
                        SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, // 音效类型
                        1.0f,                                    // 音量
                        1.0f                                     // 音调
                )
        );

    }

    @Deprecated
    @Environment(EnvType.CLIENT)
    private static void spawnBeamMarker(Entity entity, ClientWorld world) {
        // 在实体头顶生成垂直光束（每tick执行）
        double centerX = entity.getX();
        double centerZ = entity.getZ();
        double startY = entity.getY() + entity.getHeight();
        double endY = startY + 20; // 光束高度

        // 生成粒子光束（绿色，使用DUST粒子）
        for (double y = startY; y < endY; y += 0.5) {
            world.addParticle(
                    ParticleTypes.ELECTRIC_SPARK, // 粒子类型（可换成ParticleTypes.END_ROD）
                    centerX,
                    y,
                    centerZ,
                    0, 0.02, 0 // 轻微向上飘动
            );
        }
    }

    @Environment(EnvType.CLIENT)
    private static void removeMarkers(Entity entity) {
        if (markedEntities.contains(entity.getId())) {
            markedEntities.remove(entity.getId());
        }
    }

    private static String getSpecies(NbtCompound nbt) {
        String s = nbt.toString();
        try {
            s = s.substring(s.indexOf("Species:\"cobblemon:") + 19);
            s = s.substring(0, s.indexOf("\","));
        }catch (Exception e){
            return "";
        }
        return s;
    }

    public static void resetLastCheck(String[] trackedEntityNames){
        for (String trackedEntityName : trackedEntityNames) {
            lastCheckMap.remove(PokemonNameTranslation.translateToEnglish(trackedEntityName).toLowerCase(Locale.ROOT));
        }
    }

    private static String getUUID(NbtCompound nbt) {
        String s = nbt.toString();
        s = s.substring(s.indexOf("UUID:[I;") + 8);
        return s.substring(0, s.indexOf("]}"));
    }
}