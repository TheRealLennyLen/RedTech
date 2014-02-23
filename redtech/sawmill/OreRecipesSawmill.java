package thereallennylen.redtech.sawmill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class OreRecipesSawmill{

    private static final OreRecipesSawmill oreBase = new OreRecipesSawmill();

    /** The list of smelting results. */
    private Map smeltingList = new HashMap();
    private Map experienceList = new HashMap();
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<List<Integer>, ItemStack>();
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>();
    
	public static OreRecipesSawmill ores(){
		return oreBase;
	}
	
	public OreRecipesSawmill(){
		//For Wood
		List recipes = CraftingManager.getInstance().getRecipeList();
		
		for(int i = 0; i < recipes.size(); i++){
			if(recipes.get(i) != null && recipes.get(i) instanceof ShapedRecipes){
				ShapedRecipes recipe = (ShapedRecipes) recipes.get(i);
				
				if(recipe.recipeItems[0] != null && OreDictionary.getOreName(OreDictionary.getOreID(recipe.recipeItems[0])).contains("log")){
					this.addSmelting(recipe.recipeItems[0].itemID, recipe.recipeItems[0].getItemDamage(), recipe.getRecipeOutput(), 1F);
				}
			}
		}
	}
	
	/**
     * Adds a smelting recipe.
     */
    public void addSmelting(int par1, ItemStack par2ItemStack, float par3)
    {
        this.smeltingList.put(Integer.valueOf(par1), par2ItemStack);
        this.experienceList.put(Integer.valueOf(par2ItemStack.itemID), Float.valueOf(par3));
    }
    
    /**
     * A metadata sensitive version of adding a furnace recipe.
     */
    public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience)
    {
        metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
        metaExperience.put(Arrays.asList(itemstack.itemID, itemstack.getItemDamage()), experience);
    }
	

	/**
     * Used to get the resulting ItemStack form a source ItemStack
     * @param item The Source ItemStack
     * @return The result ItemStack
     */
    public ItemStack getSmeltingResult(ItemStack item) 
    {
        if (item == null)
        {
            return null;
        }
        ItemStack ret = (ItemStack)metaSmeltingList.get(Arrays.asList(item.itemID, item.getItemDamage()));
        if (ret != null) 
        {
            return ret;
        }
        return (ItemStack)smeltingList.get(Integer.valueOf(item.itemID));
    }
	
}