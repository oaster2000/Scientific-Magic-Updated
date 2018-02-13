package net.oaster2000.newmod.research;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.oaster2000.newmod.items.ModItems;
import net.oaster2000.newmod.research.Research.EnumResearchState;

public class ResearchList {
	
	public static final Research start = new Research(0, null, "ALL", ModItems.magicalTome, 10, 10, EnumResearchState.UNDISCOVERED, "The Journey Begins");
	public static final Research hope = new Research(1, start, "HOPE", ModItems.hCrystal, 10, 40, EnumResearchState.HIDDEN, "Be Hopeful");
	public static final Research faith = new Research(2, start, "FAITH", ModItems.fCrystal, 40, 10, EnumResearchState.HIDDEN, "Be Faithful");
	public static final Research power = new Research(3, start, "POWER", ModItems.pCrystal, 40, 40, EnumResearchState.HIDDEN, "Be Powerful");
	
	static List<Research> list = new ArrayList<Research>();
	
	public static void initList() {
		list.add(start);
		list.add(hope);
		list.add(faith);
		list.add(power);
	}
	
	public static List<Research> getAll(){
		return list;
	}
}
