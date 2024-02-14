package com.freefish.arknightsmobs.server;

import com.freefish.arknightsmobs.ArknightsMobs;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerNetwork {
    private static int nextMessageId = 0;

    public static void initNetwork() {
        final String VERSION = "1";
        ArknightsMobs.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ArknightsMobs.MOD_ID, "net"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equals)
                .serverAcceptedVersions(VERSION::equals)
                .simpleChannel();
        registerMessage(AnimationMessage.class, AnimationMessage::serialize, AnimationMessage::deserialize, new AnimationMessage.Handler());
    }

    private static  <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, PacketBuffer> encoder, final Function<PacketBuffer, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        ArknightsMobs.NETWORK.messageBuilder(clazz, nextMessageId++)
                .encoder(encoder).decoder(decoder)
                .consumer(consumer)
                .add();
    }
}
