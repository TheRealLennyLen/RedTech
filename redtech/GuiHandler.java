package thereallennylen.redtech;

import thereallennylen.redtech.RedTech;
import thereallennylen.redtech.refinery.ContainerRefinery;
import thereallennylen.redtech.refinery.GuiRefinery;
import thereallennylen.redtech.refinery.TileEntityRefinery;
import thereallennylen.redtech.sawmill.ContainerSawmill;
import thereallennylen.redtech.sawmill.GuiSawmill;
import thereallennylen.redtech.sawmill.TileEntitySawmill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	public GuiHandler() {
		NetworkRegistry.instance().registerGuiHandler(RedTech.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y, z);

		if (entity != null) {
			switch (id) {
			case RedTech.guiIdRefinery:
				if (entity instanceof TileEntityRefinery) {
					return new ContainerRefinery(player.inventory,
							(TileEntityRefinery) entity);}
					
				case RedTech.guiIdSawmill:
					if (entity instanceof TileEntitySawmill) {
						return new ContainerSawmill(player.inventory,
								(TileEntitySawmill) entity);
				}

				return null;
			default:
				return null;
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y, z);

		if (entity != null) {
			switch (id) {
			case RedTech.guiIdRefinery:
				if (entity instanceof TileEntityRefinery) {
					return new GuiRefinery(player.inventory,
							(TileEntityRefinery) entity);
				}
				
			case RedTech.guiIdSawmill:
				if (entity instanceof TileEntitySawmill) {
					return new GuiSawmill(player.inventory,
							(TileEntitySawmill) entity);
				}

				return null;
			default:
				return null;
			}
		}

		return null;
	}
}
