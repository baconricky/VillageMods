package maexono.mods.villagemods.density;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Constants.modId, name=Constants.modName, version=Constants.modVersion)
public class VillageDensity {

    @Instance(Constants.modId)
    public static VillageDensity instance;

    // Settings
    public Property enabled, density, minDist, size;

    // Log
    public Logger log = LogManager.getLogger(VillageDensity.class);;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        final String cat = Configuration.CATEGORY_GENERAL;

        config.load();
        enabled = config.get(cat, "enabled", false, "Should the custom generator be injected? (Enables/Disables the mod)");
        density = config.get(cat, "density", 32, "Minecraft will try to generate 1 village per NxN chunk area. \nDefault: 32");
        minDist = config.get(cat, "minimumDistance", 8, "Village centers will be at least N chunks apart. Must be smaller than density. \nDefault: 8");
        size = config.get(cat, "size", 0, "A higher size increases the overall spawn weight of buildings. (Don't ask, I have no idea) \nDefault: 0");
        config.save();

        if (minDist.getInt() < 0) {
            log.fatal("Invalid config: Minimal distance must be non-negative.");
            enabled.set(false);
        }
        if (minDist.getInt() >= density.getInt()) {
            log.fatal("Invalid config: Minimal distance must be smaller than density.");
            enabled.set(false);
        }
        if (size.getInt() < 0) {
            enabled.set(false);
            log.fatal("Invalid config: Size must be non-negative.");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if(!enabled.getBoolean(false)) return;

        log.info("Registering replacer for village generation.");
        MinecraftForge.TERRAIN_GEN_BUS.register(new VillageGenReplacer());
    }


}