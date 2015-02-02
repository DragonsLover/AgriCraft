package com.InfinityRaider.AgriCraft.handler;

import com.InfinityRaider.AgriCraft.world.StructureGreenhouse;
import com.InfinityRaider.AgriCraft.world.StructureGreenhouseIrrigated;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class VillageCreationHandler {

    public static class GreenhouseHandler implements VillagerRegistry.IVillageCreationHandler {
        //get the weight to spawn the greenhouse
        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
            //args: structure, weight, max spawns
            return new StructureVillagePieces.PieceWeight(StructureGreenhouse.class, 10, (int) random.nextDouble());
        }

        @Override
        public Class<?> getComponentClass() {
            return StructureGreenhouse.class;
        }

        @Override
        public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
            return StructureGreenhouse.func_175857_a(startPiece, pieces, random, p1, p2, p3, facing, p5);
        }
    }

    public static class GreenhouseIrrigatedHandler implements VillagerRegistry.IVillageCreationHandler {
        //get the weight to spawn the greenhouse
        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
            //args: structure, weight, max spawns
            return new StructureVillagePieces.PieceWeight(StructureGreenhouseIrrigated.class, 2, (int) (random.nextDouble()*5/6));
        }

        @Override
        public Class<?> getComponentClass() {
            return StructureGreenhouseIrrigated.class;
        }

        @Override
        public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
            return StructureGreenhouseIrrigated.func_175857_a(startPiece, pieces, random, p1, p2, p3, facing, p5);
        }
    }
}
