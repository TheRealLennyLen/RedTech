package thereallennylen.redtech.sawmill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerSawmill extends Container {
	private TileEntitySawmill Sawmill;
	private int lastCookTime;
	private int lastBurnTime;

	public ContainerSawmill(InventoryPlayer par1InventoryPlayer,
			TileEntitySawmill par2TileEntityFurnace) {
		this.Sawmill = par2TileEntityFurnace;
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 56, 35));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 8, 56));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player,
				par2TileEntityFurnace, 2, 116, 35));
		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, i,
					8 + i * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.Sawmill.cookTime);
		par1ICrafting.sendProgressBarUpdate(this, 1, this.Sawmill.power);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.lastCookTime != this.Sawmill.cookTime) {
				icrafting
						.sendProgressBarUpdate(this, 0, this.Sawmill.cookTime);
			}

			if (this.lastBurnTime != this.Sawmill.power) {
				icrafting.sendProgressBarUpdate(this, 1, this.Sawmill.power);
			}
		}

		this.lastCookTime = this.Sawmill.cookTime;
		this.lastBurnTime = this.Sawmill.power;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.Sawmill.cookTime = par2;
		}

		if (par1 == 1) {
			this.Sawmill.power = par2;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.Sawmill.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (par2 != 1 && par2 != 0) {
				OreRecipesSawmill.ores();
				if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}
				} else if (TileEntitySawmill.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				} else if (par2 >= 3 && par2 < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return null;
					}
				} else if (par2 >= 30 && par2 < 39
						&& !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}
}