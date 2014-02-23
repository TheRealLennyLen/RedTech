package thereallennylen.redtech.sawmill;

import thereallennylen.redtech.RedTech;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandlerSawmill implements IGuiHandler {
	public GuiHandlerSawmill() {
		NetworkRegistry.instance().registerGuiHandler(RedTech.instance, this);
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y, z);

		if (entity != null) {
			switch (id) {
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
