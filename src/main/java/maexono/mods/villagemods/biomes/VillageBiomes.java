package maexono.mods.villagemods.biomes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

@Mod(modid = Constants.modId, name = Constants.modName, version = Constants.modVersion)
public class VillageBiomes {

    @Instance(Constants.modId)
    public static VillageBiomes instance;

    public static Logger log = LogManager.getLogger(VillageBiomes.class);

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        // Load Config
        ConfigHandler.loadConfig(ev.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent ev) {
        // All other mods should be done registering by now.
        BiomeRegistrant.init();

        for (String name : ConfigHandler.getAddBiomes()) {
            if (Pattern.matches("\\d+", name))
                BiomeRegistrant.addBiomeById(Integer.parseInt(name));
            else
                BiomeRegistrant.addBiomeByName(name);
        }
        for (String name : ConfigHandler.getAddTypes()) {
            log.info(String.format("Adding all %s biomes as village biomes.", name));
            BiomeRegistrant.addBiomesByTypeName(name);
        }

        for (String name : ConfigHandler.getRemoveBiomes()) {
            if (Pattern.matches("\\d+", name))
                BiomeRegistrant.removeBiomeById(Integer.parseInt(name));
            else
                BiomeRegistrant.removeBiomeByName(name);
        }
        for (String name : ConfigHandler.getRemoveTypes()) {
            log.info(String.format("Removing all %s biomes from village biomes.", name));
            BiomeRegistrant.removeBiomesByTypeName(name);
        }

        // Register the custom village block replacer
        MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeBlockReplacer());
    }


}
