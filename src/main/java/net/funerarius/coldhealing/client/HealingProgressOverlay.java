package net.funerarius.coldhealing.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.funerarius.coldhealing.ColdHealing;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HealingProgressOverlay {

    private static final ResourceLocation[] FRAMES = new ResourceLocation[12];

    static {
        for (int i = 0; i < 12; i++) {
            FRAMES[i] = new ResourceLocation(ColdHealing.MOD_ID, "textures/gui/progressbar/progress" + (i + 1) + ".png");
        }
    }

    public static final IGuiOverlay HUD = (gui, guiGraphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || !player.isUsingItem() || player.isSleeping()) return;

//      if (player == null || !player.isUsingItem()) return;

        ItemStack stack = player.getUseItem();
        CompoundTag tag = stack.getTag();

        if (tag != null && tag.contains("HealTimer") && tag.contains("MaxHealTimer")) {
            int currentTimer = tag.getInt("HealTimer");
            int maxTimer = tag.getInt("MaxHealTimer");

            if (currentTimer <= 0 || maxTimer <= 0) return;

            float progress = (float) currentTimer / maxTimer;
            progress = Math.max(0, Math.min(1, progress));

            int frameIndex = Math.min(11, (int) (progress * 12));

            int x = (width / 2) - 30;
            int y = (height / 2) + 5;

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);

            guiGraphics.blit(FRAMES[frameIndex], x, y, 0, 0, 32, 32, 32, 32);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    };
}