package com.unseen.nb.init;

import com.unseen.nb.Main;
import com.unseen.nb.common.entity.entities.EntityPiglin;
import com.unseen.nb.common.entity.entities.EntityPiglinBrute;
import com.unseen.nb.common.entity.entities.EntityPiglinZombie;
import com.unseen.nb.common.entity.entities.EntityStrider;
import com.unseen.nb.util.ModReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeHell;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {

    private static final Map<Class<? extends Entity>, String> ID_MAP = new HashMap<>();

    private static int ENTITY_START_ID = 100;
    //temporary
    public static Vec3i nether = new Vec3i(6433126, 0xeb4034, 0);

    public static void registerEntities() {
        //Piglin
    registerEntityWithID("piglin", EntityPiglin.class, ENTITY_START_ID++, 50, nether);
    //Strider
    registerEntityWithID("strider", EntityStrider.class, ENTITY_START_ID++, 50, nether);
    //Piglin Brute
        registerEntityWithID("piglin_brute", EntityPiglinBrute.class, ENTITY_START_ID++, 50, nether);
        //Piglin Zombie
        registerEntityWithID("piglin_zombie", EntityPiglinZombie.class, ENTITY_START_ID++, 50, nether);
    }

    public static void RegisterEntitySpawns() {
    spawnRateBiomeSpecific(EntityPiglin.class, EnumCreatureType.MONSTER, 15, 2, 4, Biomes.HELL);
    spawnRateBiomeSpecific(EntityStrider.class, EnumCreatureType.CREATURE, 10, 1, 2, Biomes.HELL);
    spawnRateBiomeSpecific(EntityPiglinZombie.class, EnumCreatureType.MONSTER, 50, 2, 4, Biomes.HELL);

    }

    public static String getID(Class<? extends Entity> entity) {
        if (ID_MAP.containsKey(entity)) {
            return ModReference.MOD_ID + ":" + ID_MAP.get(entity);
        }
        throw new IllegalArgumentException("Mapping of an entity has not be registered for the end expansion mob spawner system.");
    }

    private static void registerEntityWithID(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
        ID_MAP.put(entity, name);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true);
    }

    private static void registerFastProjectile(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, false);
    }

    private static void registerTileEntity(Class<? extends TileEntity> entity, String name) {
        GameRegistry.registerTileEntity(entity, new ResourceLocation(ModReference.MOD_ID + ":" + name));
    }

    private static void spawnRate(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, BiomeDictionary.Type biomesAllowed) {
        for(Biome biome: BiomeDictionary.getBiomes(biomesAllowed)) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }
        }
    }

    private static void spawnRateBiomeSpecific(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, Biome biome) {
        if(biome != null && weight > 0) {
            EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

        }

    }
}