package io.wispforest.gadget.network;

import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.desc.FieldObjects;
import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.gadget.desc.edit.PrimitiveEditTypes;
import io.wispforest.gadget.path.MapPathStepType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.text.Text;

public final class GadgetNetworking {
    public static final OwoNetChannel CHANNEL = OwoNetChannel.createOptional(Gadget.id("data"));

    private GadgetNetworking() {

    }

    public static void init() {
        MapPathStepType.init();
        PrimitiveEditTypes.init();

        CHANNEL.registerServerbound(RequestDataC2SPacket.class, (packet, access) -> {
            if (!Permissions.check(access.player(), "gadget.inspect", 4)) {
                access.player().sendMessage(Text.translatable("message.gadget.fail.permissions"), true);
                return;
            }


            Object target = packet.target().resolve(access.player().world);

            if (target == null) {
                access.player().sendMessage(Text.translatable("message.gadget.fail.notfound"), true);
                return;
            }

            Object o = packet.path().follow(target);

            var fields = FieldObjects.collectAllData(packet.path(), o);

            CHANNEL.serverHandle(access.player()).send(new DataS2CPacket(packet.target(), fields));
        });

        CHANNEL.registerServerbound(SetPrimitiveC2SPacket.class, (packet, access) -> {
            if (!Permissions.check(access.player(), "gadget.inspect", 4)) {
                access.player().sendMessage(Text.translatable("message.gadget.fail.permissions"), true);
                return;
            }


            Object target = packet.target().resolve(access.player().world);

            if (target == null) {
                access.player().sendMessage(Text.translatable("message.gadget.fail.notfound"), true);
                return;
            }

            packet.path().set(target, packet.data().toObject());

            var parentPath = packet.path().parent();

            Object o = parentPath.follow(target);

            var fields = FieldObjects.collectAllData(parentPath, o);

            CHANNEL.serverHandle(access.player()).send(new DataS2CPacket(packet.target(), fields));
        });

        CHANNEL.registerClientboundDeferred(DataS2CPacket.class);
    }
}