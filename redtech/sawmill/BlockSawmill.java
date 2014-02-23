package thereallennylen.redtech.sawmill;

import java.util.Random;

import thereallennylen.redtech.RedTech;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockSawmill extends BlockContainer {

	private final Random SawmillRand = new Random();

	private final boolean isActive;

	private static boolean keepSawmillInventory;
	@SideOnly(Side.CLIENT)
	private Icon SawmillIconTop;

	public BlockSawmill(int id, boolean isActive) {
		super(id, Material.anvil);

		this.isActive = isActive;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(RedTech.modid + ":"
				+ (this.isActive ? "sawmill_side_lit" : "sawmill_side_idle"));
		this.SawmillIconTop = iconRegister
				.registerIcon(RedTech.modid
						+ ":"
						+ (this.isActive ? "sawmill_front_lit"
								: "sawmill_front_idle"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return par1 == 1 ? this.blockIcon : (par1 == 0 ? this.blockIcon
				: (par1 != par2 ? this.blockIcon : this.SawmillIconTop));
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return RedTech.blockSawmillIdle.blockID;
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		this.setDefaultDirection(par1World, par2, par3, par4);
	}

	private void setDefaultDirection(World par1World, int par2, int par3,
			int par4) {
		if (!par1World.isRemote) {
			int l = par1World.getBlockId(par2, par3, par4 - 1);
			int i1 = par1World.getBlockId(par2, par3, par4 + 1);
			int j1 = par1World.getBlockId(par2 - 1, par3, par4);
			int k1 = par1World.getBlockId(par2 + 1, par3, par4);
			byte b0 = 3;

			if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				b0 = 3;
			}

			if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				b0 = 2;
			}

			if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				b0 = 5;
			}

			if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				b0 = 4;
			}

			par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			FMLNetworkHandler.openGui(player, RedTech.instance,
					RedTech.guiIdSawmill, world, x, y, z);
		}

		return true;
	}

	public static void updateFurnaceBlockState(boolean par0, World par1World,
			int par2, int par3, int par4) {
		int l = par1World.getBlockMetadata(par2, par3, par4);
		TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
		keepSawmillInventory = true;

		if (par0) {
			par1World.setBlock(par2, par3, par4,
					RedTech.blockSawmillActive.blockID);
		} else {
			par1World.setBlock(par2, par3, par4,
					RedTech.blockSawmillIdle.blockID);
		}

		keepSawmillInventory = false;
		par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);

		if (tileentity != null) {
			tileentity.validate();
			par1World.setBlockTileEntity(par2, par3, par4, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntitySawmill();
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		int l = MathHelper
				.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (l == 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
		}

		if (l == 1) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
		}

		if (l == 2) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
		}

		if (l == 3) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
		}

		if (par6ItemStack.hasDisplayName()) {
			((TileEntitySawmill) par1World
					.getBlockTileEntity(par2, par3, par4))
					.setGuiDisplayName(par6ItemStack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		if (!keepSawmillInventory) {
			TileEntitySawmill TileEntitySawmill = (TileEntitySawmill) par1World
					.getBlockTileEntity(par2, par3, par4);

			if (TileEntitySawmill != null) {
				for (int j1 = 0; j1 < TileEntitySawmill.getSizeInventory(); ++j1) {
					ItemStack itemstack = TileEntitySawmill.getStackInSlot(j1);

					if (itemstack != null) {
						float f = this.SawmillRand.nextFloat() * 0.8F + 0.1F;
						float f1 = this.SawmillRand.nextFloat() * 0.8F + 0.1F;
						float f2 = this.SawmillRand.nextFloat() * 0.8F + 0.1F;

						while (itemstack.stackSize > 0) {
							int k1 = this.SawmillRand.nextInt(21) + 10;

							if (k1 > itemstack.stackSize) {
								k1 = itemstack.stackSize;
							}

							itemstack.stackSize -= k1;
							EntityItem entityitem = new EntityItem(par1World,
									par2 + f, par3 + f1, par4 + f2,
									new ItemStack(itemstack.itemID, k1,
											itemstack.getItemDamage()));

							if (itemstack.hasTagCompound()) {
								entityitem.getEntityItem().setTagCompound(
										(NBTTagCompound) itemstack
												.getTagCompound().copy());
							}

							float f3 = 0.05F;
							entityitem.motionX = (float) this.SawmillRand
									.nextGaussian() * f3;
							entityitem.motionY = (float) this.SawmillRand
									.nextGaussian() * f3 + 0.2F;
							entityitem.motionZ = (float) this.SawmillRand
									.nextGaussian() * f3;
							par1World.spawnEntityInWorld(entityitem);
						}
					}
				}

				par1World.func_96440_m(par2, par3, par4, par5);
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3,
			int par4, int par5) {
		return Container.calcRedstoneFromInventory((IInventory) par1World
				.getBlockTileEntity(par2, par3, par4));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int idPicked(World par1World, int par2, int par3, int par4) {
		return RedTech.blockSawmillIdle.blockID;
	}

}
