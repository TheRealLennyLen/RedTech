package thereallennylen.redtech.sawmill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntitySawmill extends TileEntity implements ISidedInventory {
	private static final int[] slots_top = new int[] { 0 };
	private static final int[] slots_bottom = new int[] { 2, 1 };
	private static final int[] slots_sides = new int[] { 1 };

	private ItemStack[] slots = new ItemStack[3];

	public int sawmillingSpeed = 60;

	public int power;
	public int maxPower = 3800;

	public int cookTime;

	private String field_94130_e;

	@Override
	public int getSizeInventory() {
		return this.slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.slots[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.slots[par1] != null) {
			ItemStack itemstack;

			if (this.slots[par1].stackSize <= par2) {
				itemstack = this.slots[par1];
				this.slots[par1] = null;
				return itemstack;
			} else {
				itemstack = this.slots[par1].splitStack(par2);

				if (this.slots[par1].stackSize == 0) {
					this.slots[par1] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.slots[par1] != null) {
			ItemStack itemstack = this.slots[par1];
			this.slots[par1] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.slots[par1] = par2ItemStack;

		if (par2ItemStack != null
				&& par2ItemStack.stackSize > this.getInventoryStackLimit()) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return this.isInvNameLocalized() ? this.field_94130_e
				: "container.Sawmill";
	}

	@Override
	public boolean isInvNameLocalized() {
		return this.field_94130_e != null && this.field_94130_e.length() > 0;
	}

	public void setGuiDisplayName(String par1Str) {
		this.field_94130_e = par1Str;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.slots.length) {
				this.slots[b0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.power = par1NBTTagCompound.getShort("power");
		this.cookTime = par1NBTTagCompound.getShort("CookTime");

		if (par1NBTTagCompound.hasKey("CustomName")) {
			this.field_94130_e = par1NBTTagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("power", (short) this.power);
		par1NBTTagCompound.setShort("CookTime", (short) this.cookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.slots.length; ++i) {
			if (this.slots[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.slots[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);

		if (this.isInvNameLocalized()) {
			par1NBTTagCompound.setString("CustomName", this.field_94130_e);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1) {
		return this.cookTime * par1 / this.sawmillingSpeed;
	}

	public int getPowerRemainingScaled(int par1) {
		return this.power * par1 / this.maxPower;
	}

	public boolean hasPower() {
		return this.power > 0;
	}

	public boolean isRefining() {
		return this.cookTime > 0;
	}

	@Override
	public void updateEntity() {
		boolean flag = this.power > 0;
		boolean flag1 = false;

		if (hasPower() && isRefining()) {
			this.power--;
		}

		if (!this.worldObj.isRemote) {
			if (this.power < this.maxPower
					&& TileEntitySawmill.getItemPower(this.slots[1]) > 0) {
				this.power += getItemPower(this.slots[1]);

				flag1 = true;

				if (this.slots[1] != null) {
					this.slots[1].stackSize--;

					if (this.slots[1].stackSize == 0) {
						this.slots[1] = this.slots[1].getItem()
								.getContainerItemStack(slots[1]);
					}
				}
			}

			if (this.hasPower() && this.canSmelt()) {
				++this.cookTime;

				if (this.cookTime == this.sawmillingSpeed) {
					this.cookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			} else {
				this.cookTime = 0;
			}

			if (flag != this.power > 0) {
				flag1 = true;
				BlockSawmill.updateFurnaceBlockState(this.power > 0,
						this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (flag1) {
			this.onInventoryChanged();
		}
	}

	public boolean isOre(ItemStack itemstack){
    	//For Wood
    	if(OreRecipesSawmill.ores().getSmeltingResult(itemstack) != null){
    		return true;
    	}
    	
    	//For ores
    	/*String[] oreNames = OreDictionary.getOreNames();
    	
    	for(int i = 0; i < oreNames.length; i++){
    		if(oreNames[i].contains("ore")){
	    		if(OreDictionary.getOres(oreNames[i]).size() > 0){
	    			if(OreDictionary.getOres(oreNames[i]).get(0).itemID == itemstack.itemID){
	    				return true;        			
	    			}
	    		}
    		}
    	}*/
    	

		return false;
	}

	private boolean canSmelt() {
		if (this.slots[0] == null) {
			return false;
		} else {
			ItemStack itemstack = OreRecipesSawmill.ores().getSmeltingResult(
					this.slots[0]);
			if (itemstack == null)
				return false;
			if (!isOre(this.slots[0]))
				return false;
			if (this.slots[2] == null)
				return true;
			if (!this.slots[2].isItemEqual(itemstack))
				return false;
			int result = slots[2].stackSize + (itemstack.stackSize * 2);
			return (result <= getInventoryStackLimit() && result <= itemstack
					.getMaxStackSize());
		}
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = OreRecipesSawmill.ores().getSmeltingResult(
					this.slots[0]);

			if (this.slots[2] == null) {
				this.slots[2] = itemstack.copy();
				this.slots[2].stackSize *= 2;
			} else if (this.slots[2].isItemEqual(itemstack)) {
				slots[2].stackSize += (itemstack.stackSize * 2);
			}

			--this.slots[0].stackSize;

			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
		}
	}

	public static int getItemPower(ItemStack par0ItemStack) {
		if (par0ItemStack == null) {
			return 0;
		} else {
			int i = par0ItemStack.getItem().itemID;

			if (i == Item.redstone.itemID)
				return 60;

			if (i == Block.blockRedstone.blockID)
				return 540;
			
			if (i == Item.redstoneRepeater.itemID)
				return 180;
			
			if (i == Block.torchRedstoneActive.blockID)
				return 60;
			
			if (i == Block.redstoneLampIdle.blockID)
				return 240;
			
			if (i == Block.blockRedstone.blockID)
				return 540;
			if (i == Item.comparator.itemID)
				return 180;

			return 0;
		}
	}

	public static boolean isItemFuel(ItemStack par0ItemStack) {
		return getItemPower(par0ItemStack) > 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false
				: par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D,
						this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
		return par1 == 2 ? false : (par1 == 1 ? isItemFuel(par2ItemStack)
				: true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int par1) {
		return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
		return this.isItemValidForSlot(par1, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
		return par3 != 0 || par1 != 1
				|| par2ItemStack.itemID == Item.bucketEmpty.itemID;
	}
}
