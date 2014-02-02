package thereallennylen.redtech.refinery;

import thereallennylen.redtech.RedTech;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandlerRefinery implements IGuiHandler {
	public GuiHandlerRefinery() {
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
							(TileEntityRefinery) entity);
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

				return null;
			default:
				return null;
			}
		}

		return null;
	}
}
