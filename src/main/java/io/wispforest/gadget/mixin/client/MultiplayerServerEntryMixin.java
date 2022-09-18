package io.wispforest.gadget.mixin.client;

import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.pond.ContextMenuScreenAccess;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.gadget.client.dump.DumpPrimer;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public class MultiplayerServerEntryMixin {
    @Shadow @Final private MultiplayerScreen screen;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onRightClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;
        if (!Gadget.CONFIG.rightClickDump()) return;

        DropdownComponent dropdown = Components.dropdown(Sizing.content())
            .button(Text.translatable("text.gadget.join_with_dump"), dropdown2 -> {
                DumpPrimer.isPrimed = true;

                this.screen.select((MultiplayerServerListWidget.ServerEntry)(Object) this);
                this.screen.connect();
            });
        dropdown.positioning(Positioning.absolute((int) mouseX, (int) mouseY));

        ((ContextMenuScreenAccess) screen).gadget$addDropdown(dropdown);

        cir.setReturnValue(true);
    }
}
