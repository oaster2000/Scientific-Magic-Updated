package net.oaster2000.newmod.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.oaster2000.newmod.Main;

public class TomeItem extends BasicItem{

	public TomeItem(String unlocalizedName) {
		super(unlocalizedName);
		this.setMaxStackSize(1);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(worldIn.isRemote) {
			player.openGui(Main.instance, 6, worldIn, (int) player.posX, (int) player.posY, (int) player.posZ);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
    }

}
