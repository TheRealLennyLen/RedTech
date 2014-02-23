	package thereallennylen.redtech.refinery;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

	public class OreRecipesRefinery{

		private static final FurnaceRecipes oreBase = FurnaceRecipes.smelting();
		
		public static FurnaceRecipes ores(){
			return oreBase;
		}
		
		static{
			oreBase.addSmelting(Block.oreCoal.blockID, new ItemStack(Item.coal), 1F);
		}
		
	}