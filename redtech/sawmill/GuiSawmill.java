package thereallennylen.redtech.sawmill;

import org.lwjgl.opengl.GL11;

import thereallennylen.redtech.RedTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSawmill extends GuiContainer {
	public static final ResourceLocation texture = new ResourceLocation(
			RedTech.modid.toLowerCase(), "textures/gui/Sawmill.png");

	public TileEntitySawmill Sawmill;

	public GuiSawmill(InventoryPlayer invPlayer, TileEntitySawmill entity) {
		super(new ContainerSawmill(invPlayer, entity));

		this.Sawmill = entity;

		this.xSize = 176;
		this.ySize = 165;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = this.Sawmill.isInvNameLocalized() ? this.Sawmill
				.getInvName() : I18n.getString(this.Sawmill.getInvName());
		this.fontRenderer.drawString(s,
				this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6,
				4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8,
				this.ySize - 96 + 5, 4210752);
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int i1;

		if (this.Sawmill.hasPower()) {
			i1 = this.Sawmill.getPowerRemainingScaled(45);
			this.drawTexturedModalRect(guiLeft + 8, guiTop + 53 - i1, 176,
					62 - i1, 16, i1);
		}

		i1 = this.Sawmill.getCookProgressScaled(24);
		this.drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 0, i1 + 1,
				16);
	}
}