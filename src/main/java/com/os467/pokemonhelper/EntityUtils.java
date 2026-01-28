package com.os467.pokemonhelper;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;


public class EntityUtils {

    /**
     * 获取实体的 SNBT 字符串
     *
     * @param entity 目标实体
     * @return 实体的 SNBT 字符串
     */
    public static String getEntitySNBT(Entity entity) {
        if (entity == null) {
            return null;
        }

        // 创建一个 NbtCompound 对象
        NbtCompound nbt = new NbtCompound();

        // 将实体的 NBT 数据写入 NbtCompound
        entity.writeNbt(nbt);

        // 将 NbtCompound 转换为 SNBT 字符串
        return nbt.toString();
    }

    /**
     * 通过实体 ID 获取实体并返回其 SNBT
     *
     * @param world    世界对象
     * @param entityId 实体 ID
     * @return 实体的 SNBT 字符串
     */
    public static String getEntitySNBTById(ServerWorld world, int entityId) {
        // 获取实体
        Entity entity = world.getEntityById(entityId);
        if (entity == null) {
            return null;
        }

        // 调用上面的方法获取 SNBT
        return getEntitySNBT(entity);
    }
}