package maexono.mods.villageMods.density;

import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.IEventListener;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;

public class VillageGenReplacer implements IEventListener {

	@Override
	@ForgeSubscribe
	public void invoke(Event event) {
		if(event instanceof InitMapGenEvent){
			InitMapGenEvent e = (InitMapGenEvent)event;
			if(e.type == EventType.VILLAGE){
				if(!(e.newGen == e.originalGen)) {
					VillageDensity.instance.log.severe("The village map generator was overwritten by another mod. There might be crashes! \n The new generator class is " + e.newGen.getClass().getCanonicalName());
				}
				
				//e.newGen = new MapGenVillageModded();
				//VillageDensity.instance.log.info("Injected modded MapGenVillage.");
				try {
					((MapGenVillage)(e.newGen)).terrainType = VillageDensity.instance.size.getInt(0);
					((MapGenVillage)(e.newGen)).field_82665_g = VillageDensity.instance.density.getInt(32);
					((MapGenVillage)(e.newGen)).field_82666_h = VillageDensity.instance.minDist.getInt(8);
					VillageDensity.instance.log.info("Modified MapGenVillage fields.");
				}
				catch(Exception exc) {
					VillageDensity.instance.log.severe("Could not modify MapGenVillage.");
					exc.printStackTrace();
				}
			}
		}

	}

}
