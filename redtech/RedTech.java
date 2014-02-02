package thereallennylen.redtech;

import thereallennylen.redtech.refinery.BlockRefinery;
import thereallennylen.redtech.refinery.GuiHandlerRefinery;
import thereallennylen.redtech.refinery.TileEntityRefinery;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = RedTech.modid, name = "RedTech", version = "Alpha")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class RedTech {
	public static final String modid = "RedTech";

	@Instance(modid)
	public static RedTech instance;

	public static Block blockRefineryIdle;
	public static Block blockRefineryActive;

	public static CreativeTabs RedTechTab;

	public static final int guiIdRefinery = 37;

	@EventHandler
	public void load(FMLInitializationEvent e) {
		RedTechTab = new CreativeTabs("RedTech") {
			@Override
			@SideOnly(Side.CLIENT)
			public int getTabIconItemIndex() {
				return RedTech.blockRefineryActive.blockID;
			}
		};

		ItemStack redstoneStack = new ItemStack(Item.redstone);
		ItemStack ironStack = new ItemStack(Item.ingotIron);
		ItemStack furnaceStack = new ItemStack(Block.furnaceIdle);
		ItemStack pickStack = new ItemStack(Item.pickaxeIron);

		blockRefineryIdle = new BlockRefinery(3700, false)
				.setUnlocalizedName("Refinery_Idle").setHardness(3.7F)
				.setCreativeTab(RedTechTab)
				.setStepSound(Block.soundAnvilFootstep);
		
		blockRefineryActive = new BlockRefinery(3701, true)
				.setUnlocalizedName("Refinery_Active").setHardness(3.7F)
				.setLightValue(0.9F).setStepSound(Block.soundAnvilFootstep);


		registerBlock(blockRefineryIdle, "Refinery");
		registerBlock(blockRefineryActive, "Refinery");
		

		GameRegistry.registerTileEntity(TileEntityRefinery.class, "RedtechRefinery");

		LanguageRegistry.instance().addStringLocalization("container.Refinery",
				"Refinery");

		LanguageRegistry.instance().addStringLocalization(
				RedTechTab.getTranslatedTabLabel(), "RedTech");

		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandlerRefinery());

		GameRegistry.addRecipe(new ItemStack(RedTech.blockRefineryIdle), "rir",
				"ipi", "rfr", 'r', redstoneStack, 'i', ironStack, 'f',
				furnaceStack, 'p', pickStack);
	}

	public static void registerBlock(Block block, String name) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName());
		LanguageRegistry.addName(block, name);
	}

	public static void registerItem(Item item, String name) {
		GameRegistry.registerItem(item, item.getUnlocalizedName());
		LanguageRegistry.addName(item, name);
	}
}
