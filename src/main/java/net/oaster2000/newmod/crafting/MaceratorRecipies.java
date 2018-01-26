package net.oaster2000.newmod.crafting;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.oaster2000.newmod.blocks.ModBlocks;
import net.oaster2000.newmod.items.ModItems;

public class MaceratorRecipies
{
    private static final MaceratorRecipies SMELTING_BASE = new MaceratorRecipies();
    private final Map<ItemStack, ItemStack> smeltingList = Maps.<ItemStack, ItemStack>newHashMap();
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static MaceratorRecipies instance()
    {
        return SMELTING_BASE;
    }

    private MaceratorRecipies()
    {
    	this.addSmeltingRecipeForBlock(Blocks.IRON_ORE, new ItemStack(ModItems.ironDust, 2), 5.0f);
    	this.addSmeltingRecipeForBlock(Blocks.GOLD_ORE, new ItemStack(ModItems.goldDust, 2), 5.0f);
    	this.addSmelting(ModItems.dStone, new ItemStack(ModItems.dDust), 5.0f);
    	this.addSmelting(ModItems.dGem, new ItemStack(ModItems.dDust, 2), 5.0f);
    	this.addSmelting(ModItems.dCrystal, new ItemStack(ModItems.dDust, 4), 5.0f);
    	
    	this.addSmeltingRecipeForBlock(ModBlocks.copperOre, new ItemStack(ModItems.copperDust, 2), 5.0f);
    	this.addSmeltingRecipeForBlock(ModBlocks.tinOre, new ItemStack(ModItems.tinDust, 2), 5.0f);
    	this.addSmeltingRecipeForBlock(ModBlocks.silverOre, new ItemStack(ModItems.silverDust, 2), 5.0f);

    	this.addSmelting(ModItems.bronze, new ItemStack(ModItems.bronzeDust, 2), 5.0f);
    	this.addSmelting(ModItems.copper, new ItemStack(ModItems.copperDust, 2), 5.0f);
    	this.addSmelting(ModItems.tin, new ItemStack(ModItems.tinDust, 2), 5.0f);
    	this.addSmelting(ModItems.silver, new ItemStack(ModItems.silverDust, 2), 5.0f);
    	
    	this.addSmelting(Items.IRON_INGOT, new ItemStack(ModItems.ironDust), 5.0f);
    	this.addSmelting(Items.GOLD_INGOT, new ItemStack(ModItems.goldDust), 5.0f);
    }

    /**
     * Adds a smelting recipe, where the input item is an instance of Block.
     */
    public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience)
    {
        this.addSmelting(Item.getItemFromBlock(input), stack, experience);
    }

    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addSmelting(Item input, ItemStack stack, float experience)
    {
        this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience)
    {
        if (getSmeltingResult(input) != null) { net.minecraftforge.fml.common.FMLLog.info("Ignored smelting recipe with conflicting input: " + input + " = " + stack); return; }
        this.smeltingList.put(input, stack);
        this.experienceList.put(stack, Float.valueOf(experience));
    }

    /**
     * Returns the smelting result of an item.
     */
    @Nullable
    public ItemStack getSmeltingResult(ItemStack stack)
    {
        for (Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet())
        {
            if (this.compareItemStacks(stack, (ItemStack)entry.getKey()))
            {
                return (ItemStack)entry.getValue();
            }
        }

        return null;
    }

    /**
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getSmeltingList()
    {
        return this.smeltingList;
    }

    public float getSmeltingExperience(ItemStack stack)
    {
        float ret = stack.getItem().getSmeltingExperience(stack);
        if (ret != -1) return ret;

        for (Entry<ItemStack, Float> entry : this.experienceList.entrySet())
        {
            if (this.compareItemStacks(stack, (ItemStack)entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }
}