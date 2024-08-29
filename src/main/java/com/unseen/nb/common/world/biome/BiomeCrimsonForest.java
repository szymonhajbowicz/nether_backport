package com.unseen.nb.common.world.biome;

import com.unseen.nb.common.entity.entities.EntityPiglin;
import com.unseen.nb.common.entity.entities.EntityPiglinZombie;
import com.unseen.nb.common.entity.entities.EntityStrider;
import com.unseen.nb.common.world.base.WorldGenNB;
import com.unseen.nb.common.world.terrain.plants.WorldGenCrimsonPlant;
import com.unseen.nb.common.world.terrain.plants.WorldGenCrimsonVines;
import com.unseen.nb.common.world.terrain.trees.WorldGenCrimsonTree;
import com.unseen.nb.init.ModBlocks;
import com.unseen.nb.util.ModRand;
import git.jbredwards.nether_api.api.audio.IMusicType;
import git.jbredwards.nether_api.api.biome.INetherBiome;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistry;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nonnull;
import java.util.OptionalInt;
import java.util.Random;

public class BiomeCrimsonForest extends Biome implements INetherBiome, INetherAPIRegistryListener {

    private final WorldGenNB[] small_trees = {new WorldGenCrimsonTree("s_c_tree_1", 1), new WorldGenCrimsonTree("s_c_tree_2", 1), new WorldGenCrimsonTree("s_c_tree_3", 1), new WorldGenCrimsonTree("s_c_tree_4", 1), new WorldGenCrimsonTree("s_c_tree_5", 1)};
    private final WorldGenNB[] medium_trees = {new WorldGenCrimsonTree("m_c_tree_1", 2),new WorldGenCrimsonTree("m_c_tree_2", 2), new WorldGenCrimsonTree("m_c_tree_3", 2), new WorldGenCrimsonTree("m_c_tree_4", 2), new WorldGenCrimsonTree("m_c_tree_5", 2) };

    private final WorldGenNB[] large_trees = {new WorldGenCrimsonTree("l_c_tree_1", 3), new WorldGenCrimsonTree("l_c_tree_2", 3), new WorldGenCrimsonTree("l_c_tree_3", 3), new WorldGenCrimsonTree("l_c_tree_4", 3), new WorldGenCrimsonTree("l_c_tree_5", 3)};
    public static final WorldGenCrimsonPlant crimson_roots = new WorldGenCrimsonPlant(ModBlocks.CRIMSON_ROOTS.getDefaultState());
    public static final WorldGenCrimsonPlant crimson_fungus = new WorldGenCrimsonPlant(ModBlocks.CRIMSON_FUNGUS.getDefaultState());

    public static final WorldGenCrimsonVines crimson_vines = new WorldGenCrimsonVines(ModBlocks.CRIMSON_VINES.getDefaultState());
    public static BiomeProperties properties = new BiomeProperties("Crimson Forest");
    private static final IBlockState CRIMSON_FLOOR = ModBlocks.CRIMSON_GRASS.getDefaultState();
    private Random random;

    public BiomeCrimsonForest() {
        super(properties.setRainDisabled());
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPiglin.class, 5, 2, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPiglinZombie.class, 1, 2,4));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityStrider.class, 10, 1, 2));
        //Add Hoglin weight 8, min 3, max 4
        this.topBlock = CRIMSON_FLOOR;
        random = new Random();
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos)
    {
        //Trees
        //for (int k2 = 0; k2 < this.trees_per_chunk; ++k2)
        //{
            //int l6 = random.nextInt(16) + 8;
            //int k10 = random.nextInt(16) + 8;
            //int yHieght = getNetherSurfaceHeight(world, pos.add(16, 0, 16), 32, 110);
            //if(yHieght > 32) {
                //int randM = ModRand.range(1, 4);
                //if(randM == 1) { //small
                    //WorldGenNB tree = ModRand.choice(small_trees);
                  //  tree.generate(world, rand, pos.add(-3, 0, -3));
                //} else if (randM == 2) { //medium
                    //WorldGenNB tree = ModRand.choice(medium_trees);
                  //  tree.generate(world, rand, pos.add(-3, 0, -3));
                //} else { //large

                //    WorldGenNB tree = ModRand.choice(small_trees);
              //      tree.generate(world, rand, pos.add(-3, 0, -3));
            //    }
          //  }
        //}


        //Trees
        for(int k2 = 0; k2 < ModRand.range(8, 15);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    int randTreeSize = ModRand.range(1, 4);
                    if(randTreeSize == 1) { //small
                        WorldGenNB tree = ModRand.choice(small_trees);
                        tree.generate(world, rand, pos.add(l6 -3, y + 1, k10 -3));
                    } else if (randTreeSize == 2) { //medium
                        WorldGenNB tree = ModRand.choice(medium_trees);
                        tree.generate(world, rand, pos.add(l6 -3, y + 1, k10 -3));
                    } else { //large
                        WorldGenNB tree = ModRand.choice(small_trees);
                        WorldGenNB large_tree = ModRand.choice(large_trees);
                        //Incase it can't spawn a large tree here, it'll try to spawn a small one
                        if(!large_tree.generate(world, rand, pos.add(l6 - 4, y + 1, k10 - 4))) {
                            tree.generate(world, rand, pos.add(l6 -3, y + 1, k10 -3));
                        }
                    }
                }

                if(currentBlock == ModBlocks.CRIMSON_GRASS.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }

        //Plants
        for(int k2 = 0; k2 < ModRand.range(30, 50);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    if(rand.nextInt(10) == 0) {
                        crimson_fungus.generate(world, rand, pos.add(l6, y + 1, k10));
                    } else {
                        crimson_roots.generate(world, rand, pos.add(l6, y + 1, k10));
                    }
                }

                if(currentBlock == ModBlocks.CRIMSON_GRASS.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }


        //Vines
        for(int k2 = 0; k2< ModRand.range(10, 30); k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            int vineLength = ModRand.range(1, 4);
            for(int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    //world.setBlockState(pos.add(l6, y + 1, k10), ModBlocks.CRIMSON_VINES.getDefaultState());
                    crimson_vines.generate(world, rand, pos.add(l6, y + 1, k10));
                }
                if(currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.NETHERRACK.getDefaultState() || currentBlock == ModBlocks.CRIMSON_GRASS.getDefaultState()) {
                    depthSignature = 0;
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
                    currDepth = 40 + chunkGenerator.getRand().nextInt(2);
                    primer.setBlockState(x, y, z, topBlock);
                }
                else if(currDepth > 0) {
                    --currDepth;
                    fillerBlock = Blocks.NETHERRACK.getDefaultState();
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
        return INetherBiome.super.getFogColor(celestialAngle, partialTicks);
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
}