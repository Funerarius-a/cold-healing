package net.funerarius.coldhealing.client;

import net.funerarius.coldhealing.item.StimPouchTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ClientStimPouchTooltip implements ClientTooltipComponent {
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/inventory.png");
    private final StimPouchTooltip tooltip;

    public ClientStimPouchTooltip(StimPouchTooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight() {
        return 38;
    }

    @Override
    public int getWidth(Font font) {
        return 54;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        int columns = 3;
        int rows = 2;
        int slotSize = 18;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int slotX = x + (c * slotSize);
                int slotY = y + (r * slotSize);
                guiGraphics.blit(INVENTORY_TEXTURE, slotX, slotY, 7, 83, 18, 18);
            }
        }

        int index = 0;
        for (ItemStack stack : tooltip.getItems()) {
            if (index >= 6) break;

            int r = index / columns;
            int c = index % columns;
            int itemX = x + (c * slotSize) + 1;
            int itemY = y + (r * slotSize) + 1;

            guiGraphics.renderItem(stack, itemX, itemY, index);
            guiGraphics.renderItemDecorations(font, stack, itemX, itemY);
            index++;
        }
    }
}