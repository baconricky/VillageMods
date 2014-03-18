package maexono.mods.villagemods.density;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.IEventListener;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;

import java.lang.reflect.Field;

public class VillageGenReplacer implements IEventListener {

    @Override
    @SubscribeEvent
    public void invoke(Event event) {
        if(event instanceof InitMapGenEvent){
            InitMapGenEvent e = (InitMapGenEvent)event;
            if (e.type == EventType.VILLAGE){
                if (!(e.newGen == e.originalGen)) {
                    VillageDensity.instance.log.fatal("The village map generator was overwritten by another mod. There might be crashes! \n The new generator class is " + e.getClass().getCanonicalName());
                }

                try { // Here be reflections.
                    Field type = null;
                    Field density = null;
                    Field minDist = null;

                    Field[] fields = e.newGen.getClass().getDeclaredFields();
                    for (Field f : fields) {
                        String name = f.getName();
                        if(name.equals("terrainType")) {
                            type = f;
                        }
                        else if (name.equals("field_82665_g")) {
                            density = f;
                        }
                        else if (name.equals("field_82666_h")) {
                            minDist = f;
                        }
                    }

                    if(type != null) {
                        type.setAccessible(true);
                        type.setInt(e.newGen, VillageDensity.instance.size.getInt(0));
                    }
                    if(density != null) {
                        density.setAccessible(true);
                        density.setInt(e.newGen, VillageDensity.instance.density.getInt(32));
                    }
                    if(minDist!= null) {
                        minDist.setAccessible(true);
                        minDist.setInt(e.newGen, VillageDensity.instance.minDist.getInt(8));
                    }
                    VillageDensity.instance.log.info("Modified MapGenVillage fields.");
                }
                catch (Exception exc) {
                    VillageDensity.instance.log.fatal("Could not modify MapGenVillage, consider disabling Village Density in VillageDensity.cfg");
                    exc.printStackTrace();
                }
            }
        }

    }

}