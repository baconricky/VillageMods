package maexono.mods.villagemods.biomes;


import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;

import java.util.HashSet;
import java.util.Set;

public class BiomeRegistrant {

    private static Set<BiomeGenBase> biomeSet;

    public static void init() {
        biomeSet = fetchAllBiomes();
    }

    public static void addBiomeById(int id) {
        BiomeGenBase biomeForId = null;
        for (BiomeGenBase biome : biomeSet) {
            if (biome.biomeID == id)
                biomeForId = biome;
        }

        if (biomeForId != null)
            addBiome(biomeForId);
        else
            VillageBiomes.log.warn("Can't find biome with ID " + id);
    }

    public static void addBiomeByName(String name) {
        BiomeGenBase biomeForId = null;
        for (BiomeGenBase biome : biomeSet) {
            if (biome.biomeName.equals(name))
                biomeForId = biome;
        }

        if (biomeForId != null)
            addBiome(biomeForId);
        else
            VillageBiomes.log.warn("Can't find biome with name " + name);
    }

    public static void addBiome(BiomeGenBase biome) {
        BiomeManager.addVillageBiome(biome, true);
        VillageBiomes.log.info(String.format("Added %s (ID:%d) as a village biome.", biome.biomeName, biome.biomeID));
    }

    public static void addBiomesByType(Type type) {
        for (BiomeGenBase biome : BiomeDictionary.getBiomesForType(type)) {
            addBiome(biome);
        }
    }

    public static void addBiomesByTypeName(String name) {
        Type type = Type.valueOf(name);
        if (type != null)
            addBiomesByType(type);
        else
            VillageBiomes.log.warn("Can't find type with name " + name);
    }

    // Removals

    public static void removeBiomeById(int id) {
        BiomeGenBase biomeForId = null;
        for (BiomeGenBase biome : biomeSet) {
            if (biome.biomeID == id)
                biomeForId = biome;
        }

        if (biomeForId != null)
            removeBiome(biomeForId);
        else
            VillageBiomes.log.warn("Can't find biome with ID " + id);
    }

    public static void removeBiomeByName(String name) {
        BiomeGenBase biomeForId = null;
        for (BiomeGenBase biome : biomeSet) {
            if (biome.biomeName.equals(name))
                biomeForId = biome;
        }

        if (biomeForId != null)
            removeBiome(biomeForId);
        else
            VillageBiomes.log.warn("Can't find biome with name " + name);
    }

    public static void removeBiome(BiomeGenBase biome) {
        BiomeManager.removeVillageBiome(biome);
        VillageBiomes.log.info(String.format("Removed %s (ID:%d) from village biomes.", biome.biomeName, biome.biomeID));
    }

    public static void removeBiomesByType(BiomeDictionary.Type type) {
        for (BiomeGenBase biome : BiomeDictionary.getBiomesForType(type)) {
            removeBiome(biome);
        }
    }

    public static void removeBiomesByTypeName(String name) {
        Type type = Type.valueOf(name);
        if (type != null)
            removeBiomesByType(type);
        else
            VillageBiomes.log.warn("Can't find type with name " + name);
    }


    private static Set<BiomeGenBase> fetchAllBiomes() {
        HashSet<BiomeGenBase> biomes = new HashSet<BiomeGenBase>();
        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray()) {
            if (biome != null)
                biomes.add(biome);
        }

        return biomes;
    }

}

