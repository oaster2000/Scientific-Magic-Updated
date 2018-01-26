package net.oaster2000.newmod.items;

public class CrystalItem extends BasicItem {

	public CrystalItem(String unlocalizedName) {
		super(unlocalizedName);
		this.setMaxStackSize(1);
		this.setContainerItem(this);
		this.setMaxDamage(128);
	}

}
