package me.basiqueevangelist.gadget.mixin.client;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import me.basiqueevangelist.gadget.client.dump.DumpPrimer;
import me.basiqueevangelist.gadget.pond.ContextMenuScreenAccess;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldListWidget.WorldEntry.class)
public abstract class WorldListWidgetWorldEntryMixin {
    @Shadow @Final private SelectWorldScreen screen;

    @Shadow public abstract void play();

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onRightClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;

        DropdownComponent dropdown = Components.dropdown(Sizing.content())
                .button(Text.translatable("text.gadget.join_with_dump"), dropdown2 -> {
                    DumpPrimer.isPrimed = true;
                    play();
                });
        dropdown.positioning(Positioning.absolute((int) mouseX, (int) mouseY));

        ((ContextMenuScreenAccess) screen).gadget$addDropdown(dropdown);

        cir.setReturnValue(true);
    }
}