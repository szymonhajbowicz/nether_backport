package com.unseen.nb.common.world.biome;

import com.unseen.nb.client.particles.ParticlePixel;
import com.unseen.nb.common.entity.entities.EntityStrider;
import com.unseen.nb.common.world.base.WorldGenNB;
import com.unseen.nb.common.world.terrain.basaltHeights.BasaltFlatAreas;
import com.unseen.nb.common.world.terrain.basaltPillars.WorldGenBasaltTop;
import com.unseen.nb.common.world.terrain.fire.WorldGenSoulFire;
import com.unseen.nb.common.world.terrain.fossils.WorldGenFossils;
import com.unseen.nb.common.world.terrain.fossils.WorldGenReplaceOnRand;
import com.unseen.nb.common.world.terrain.plants.WorldGenCrimsonVines;
import com.unseen.nb.common.world.terrain.plants.WorldGenWarpedPlant;
import com.unseen.nb.common.world.terrain.plants.WorldGenWarpedVines;
import com.unseen.nb.common.world.terrain.soulsandPatches.WorldGenSoulSandPatches;
import com.unseen.nb.common.world.terrain.trees.WorldGenWarpedTree;
import com.unseen.nb.config.NBWorldConfig;
import com.unseen.nb.init.ModBlocks;
import com.unseen.nb.init.ModSoundHandler;
import com.unseen.nb.util.ModRand;
import com.unseen.nb.util.ModUtils;
import com.unseen.nb.util.integration.ModIntegration;
import git.jbredwards.nether_api.api.audio.IMusicType;
import git.jbredwards.nether_api.api.audio.ISoundAmbience;
import git.jbredwards.nether_api.api.audio.impl.DarkSoundAmbience;
import git.jbredwards.nether_api.api.audio.impl.SoundAmbience;
import git.jbredwards.nether_api.api.biome.IAmbienceBiome;
import git.jbredwards.nether_api.api.biome.INetherBiome;
import git.jbredwards.nether_api.api.event.NetherAPIBiomeSizeEvent;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import git.jbredwards.nether_api.mod.common.config.NetherAPIConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thedarkcolour.futuremc.registry.FBlocks;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeSoulValley extends Biome implements INetherBiome, INetherAPIRegistryListener, IAmbienceBiome {
    public static BiomeProperties properties = new BiomeProperties("Soul Sand Valley");
    private static final IBlockState CRIMSON_FLOOR = ModBlocks.SOUL_SOIL.getDefaultState();


    private final WorldGenNB[] fossils = {new WorldGenFossils("fossil_1"), new WorldGenFossils("fossil_2"), new WorldGenFossils("fossil_3"), new WorldGenFossils("fossil_4"),
            new WorldGenFossils("fossil_5"), new WorldGenFossils("fossil_6"), new WorldGenFossils("fossil_7"), new WorldGenFossils("fossil_8")};
    private Random random;

    private WorldGenSoulSandPatches soul_sand_patches = new WorldGenSoulSandPatches();

    private final WorldGenNB[] basaltPillarsSmall = {new WorldGenBasaltTop("basalt_pillar_1"), new WorldGenBasaltTop("basalt_pillar_2"), new WorldGenBasaltTop("basalt_pillar_3")
            , new WorldGenBasaltTop("basalt_pillar_4"), new WorldGenBasaltTop("basalt_pillar_5"), new WorldGenBasaltTop("basalt_pillar_6")};

    private final WorldGenSoulFire soulFire = new WorldGenSoulFire(ModBlocks.SOUL_FIRE.getDefaultState());

    private final WorldGenReplaceOnRand soul_sand_replacer = new WorldGenReplaceOnRand();
    /** '1 / this' chance for a particle to spawn. */
    public int particleRate = NBWorldConfig.soulSandValleyparticleRate;

    public BiomeSoulValley() {
        super(properties.setRainDisabled());
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 4, 1, 1));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 40, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 36, 1, 2));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityStrider.class, 60, 2, 4));
        //Add Strider weight 10, min 2, max 4
        this.topBlock = CRIMSON_FLOOR;
        random = new Random();
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        //Fossils
        if (rand.nextInt(8) == 0) {
            for (int k2 = 0; k2 < ModRand.range(1, 2); k2++) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int depthSignature = 2;
                for (int y = NetherAPIConfig.tallNether ? 240 : 110; y > 32; y--) {
                    IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                    if (depthSignature == 1) {
                        WorldGenNB fossils_from = ModRand.choice(fossils);
                        fossils_from.generate(world, rand, pos.add(l6 - 2, y + 1, k10 - 2));
                    }

                    if (currentBlock == ModBlocks.SOUL_SOIL.getDefaultState() || currentBlock == Blocks.SOUL_SAND.getDefaultState()) {
                        depthSignature++;
                    } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                        depthSignature = 0;
                    }
                }
            }
        }

        //Basalt Flat Parts
        for(int k2 = 0; k2 < ModRand.range(1, 3);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = NetherAPIConfig.tallNether ? 240 : 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    if(!world.isAirBlock(pos.add(l6, y, k10))) {
                        soul_sand_patches.generate(world, rand, pos.add(l6, y, k10));
                    }
                }

                if(currentBlock == ModBlocks.SOUL_SOIL.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }


        //FIRE
        if(world.rand.nextInt(8) == 0) {
            for (int k2 = 0; k2 < ModRand.range(2, 5); k2++) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int depthSignature = 2;
                for (int y = NetherAPIConfig.tallNether ? 240 : 110; y > 32; y--) {
                    IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                    if (depthSignature == 1) {
                        soulFire.generate(world, rand, pos.add(l6, y + 1, k10));
                    }
                    if (currentBlock == ModBlocks.SOUL_SOIL.getDefaultState() || currentBlock == Blocks.SOUL_SAND.getDefaultState()) {
                        depthSignature++;
                    } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                        depthSignature = 0;
                    }
                }
            }
        }

        //Basalt Pillars
        if(world.rand.nextInt(5) == 0) {
            for (int k2 = 0; k2 < ModRand.range(0, 3); k2++) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                BlockPos posToo = new BlockPos(pos.getX() + l6, 31, pos.getZ() + k10);
                if (world.getBlockState(posToo).getBlock() == Blocks.LAVA && world.getBlockState(posToo.add(5, 0, 5)).getBlock() == Blocks.LAVA) {
                    WorldGenNB basalt = ModRand.choice(basaltPillarsSmall);
                    basalt.generate(world, rand, pos.add(l6 - 3, 0, k10 - 3));
                }
            }
        }

    }
    @Override
    public void buildSurface(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, @Nonnull ChunkPrimer primer, int x, int z, double[] soulSandNoise, double[] gravelNoise, double[] depthBuffer, double terrainNoise) {
        int currDepth = -1;
        for(int y = chunkGenerator.getWorld().getActualHeight() - 1; y >= 0; --y) {
            final IBlockState here = primer.getBlockState(x, y, z);
            if(here.getMaterial() == Material.AIR) currDepth = -1;
            else if(here.getBlock() == Blocks.NETHERRACK) {
                if(currDepth == -1) {
                    currDepth = 2 + chunkGenerator.getRand().nextInt(2);
                        primer.setBlockState(x, y, z, topBlock);
                }
                else if(currDepth > 0) {
                    --currDepth;
                    fillerBlock = Blocks.SOUL_SAND.getDefaultState();
                    primer.setBlockState(x, y, z, fillerBlock);
                }
            }
        }
    }

    @Override
    public void populate(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ) {
        INetherBiome.super.populate(chunkGenerator, chunkX, chunkZ);
    }

    @Nonnull
    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0,0.475,0.569);
    }

    @Nonnull
    @Override
    public IMusicType getMusicType() {
        return INetherBiome.super.getMusicType();
    }

    private int getNetherSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int maxY = max;
        int minY = min;
        int currentY = maxY;

        while(currentY >= minY)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)))
                return currentY;
            currentY--;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IParticleFactory[] getAmbientParticles()
    {
        if (random.nextInt(particleRate) == 0) return new IParticleFactory[] {new ParticlePixel.AshSoulFactory()} ;
        else return null;
    }

    @Override
    public ISoundAmbience getRandomAmbientSound() {
        return new DarkSoundAmbience(ModSoundHandler.SOUL_SAND_VALLEY_AMBIENT, 0.001, 12, 6);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return ModSoundHandler.SOUL_SAND_VALLEY_AMBIENT_CONSTANT;
    }
}
