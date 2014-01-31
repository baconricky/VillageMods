package maexono.mods.villageMods.biomes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.common.BiomeDictionary;
import maexono.mods.villageMods.biomes.util.Pair;
import cpw.mods.fml.common.FMLLog;

public class ConfigHandler {

	private static List<String> configLines = new ArrayList<String>();

	public static void loadConfig(File file) {
		try {
			if (file.createNewFile()) {
				FileWriter writer = new FileWriter(file);
				writer.write("# This file controls which biomes and block replacements are added.\n\n");
				
				writer.write("# Biomes can be either targeted by ID (b:123), by name (b:Forest) or by BiomeDictionary tag (t:FOREST).\n");
				writer.write("# These targeting methods are interchangeable in the following examples.\n");
				
				writer.write("# Valid tags are \n# ");
				for(BiomeDictionary.Type t : BiomeDictionary.Type.values()) {
					writer.write(t.toString()+", ");
				}
				writer.write("\n# at the time of creation of this file.\n\n");
				
				writer.write("# A biome can be added with a line like this: +b:Forest\n");
				writer.write("# A biome can be removed with a line like this: -b:Plains\n");
				writer.write("# The lines are parsed in the order add first, then remove.");
				
				writer.write("# Block Replacements:\n");
				writer.write("# Biome specific block replacements can be added, by writing a line like this:\n");
				writer.write("# ~1,2:3,t:JUNGLE\n");
				writer.write("# Where 1 is the ID of the block that should be replaced,\n");
				writer.write("# 2 is the ID of the replacement block and 3 the damage value for the new block.\n");
				writer.write("# The damage value may be omitted in order to let standard generation select a value. E.g. ~1,2,t:JUNGLE\n");
				writer.write("# Finally t:JUNGLE may be replaced with any biome target.\n\n");
				
				writer.close();
			}
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				configLines.add(sc.nextLine().trim());
			}
			sc.close();
		}
		catch (IOException e) {
			FMLLog.severe("[%s] Can't load or create its config in %s.",
					Constants.modId, file.getAbsolutePath());
		}
	}

	private static List<String> getAfterPrefix(String prefix) {
		List<String> result = new ArrayList<String>();
		for (String line : configLines) {
			if (line.startsWith(prefix))
				result.add(line.substring(prefix.length()));
		}
		return result;
	}

	public static List<String> getAddBiomes() {
		return getAfterPrefix("+b:");
	}

	public static List<String> getAddTypes() {
		return getAfterPrefix("+t:");
	}

	public static List<String> getRemoveBiomes() {
		return getAfterPrefix("-b:");
	}

	public static List<String> getRemoveTypes() {
		return getAfterPrefix("-t:");
	}
	
	public static Map<Integer, List<Pair<String,Integer>>> getReplacements() {
		Map<Integer, List<Pair<String,Integer>>> map = new HashMap<Integer, List<Pair<String,Integer>>>();
		
		Pattern p = Pattern.compile("^~(\\d+),(\\d+)(?::\\d+)?,([bt]:.+)");
		for(String line : configLines){
			Matcher m = p.matcher(line);
			if(m.matches()) {
				Integer id = Integer.valueOf(m.group(1));
				Integer replacement = Integer.valueOf(m.group(2));
				String condition = m.group(3);
				
				if(id.equals(replacement)) continue;
				
				if(!map.containsKey(id))
					map.put(id, new ArrayList<Pair<String,Integer>>());
				map.get(id).add(new Pair<String,Integer>(condition,replacement));
			}
		}
		return map;
	}

	public static Map<Integer, List<Pair<String, Integer>>> getMetadata() {
Map<Integer, List<Pair<String,Integer>>> map = new HashMap<Integer, List<Pair<String,Integer>>>();
		
		Pattern p = Pattern.compile("^~(\\d+),\\d+:(\\d+),([bt]:.+)");
		for(String line : configLines){
			Matcher m = p.matcher(line);
			if(m.matches()) {
				Integer id = Integer.valueOf(m.group(1));
				Integer meta = Integer.valueOf(m.group(2));
				String condition = m.group(3);
				
				if(!map.containsKey(id))
					map.put(id, new ArrayList<Pair<String,Integer>>());
				map.get(id).add(new Pair<String,Integer>(condition,meta));
			}
		}
		return map;
	}
}
