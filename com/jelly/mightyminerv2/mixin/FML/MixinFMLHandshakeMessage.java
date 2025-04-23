/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage$ModList
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.jelly.mightyminerv2.mixin.FML;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={FMLHandshakeMessage.ModList.class}, remap=false, priority=0x7FFFFFFF)
public abstract class MixinFMLHandshakeMessage {
    @Shadow(remap=false)
    private Map<String, String> modTags;

    @Inject(method={"<init>(Ljava/util/List;)V"}, at={@At(value="RETURN")}, remap=false)
    private void init(List<ModContainer> modList, CallbackInfo ci) {
        if (Minecraft.func_71410_x().func_71387_A()) {
            return;
        }
        this.modTags.keySet().removeIf(s -> s.contains("mightyminerv2"));
    }
}

