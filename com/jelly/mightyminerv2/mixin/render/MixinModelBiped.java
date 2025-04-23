/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MathHelper
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.jelly.mightyminerv2.mixin.render;

import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.mixin.client.EntityPlayerSPAccessor;
import com.jelly.mightyminerv2.mixin.client.MinecraftAccessor;
import com.jelly.mightyminerv2.util.AngleUtil;
import com.jelly.mightyminerv2.util.helper.RotationConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ModelBiped.class}, priority=0x7FFFFFFF)
public class MixinModelBiped {
    @Unique
    private final Minecraft mightyMinerV2$mc = Minecraft.func_71410_x();
    @Shadow
    public ModelRenderer field_78116_c;

    @Inject(method={"setRotationAngles"}, at={@At(value="FIELD", target="Lnet/minecraft/client/model/ModelBiped;swingProgress:F")})
    public void onSetRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!RotationHandler.getInstance().isEnabled() || RotationHandler.getInstance().getConfiguration() != null && RotationHandler.getInstance().getConfiguration().rotationType() != RotationConfiguration.RotationType.SERVER || entityIn == null || !entityIn.equals((Object)this.mightyMinerV2$mc.field_71439_g)) {
            return;
        }
        this.field_78116_c.field_78795_f = ((EntityPlayerSPAccessor)entityIn).getLastReportedPitch() / 57.295776f;
        float partialTicks = ((MinecraftAccessor)this.mightyMinerV2$mc).getTimer().field_74281_c;
        float yawOffset = this.mightyMinerV2$mc.field_71439_g.field_70761_aq + AngleUtil.normalizeAngle(this.mightyMinerV2$mc.field_71439_g.field_70761_aq - this.mightyMinerV2$mc.field_71439_g.field_70760_ar) * partialTicks;
        float calcNetHead = MathHelper.func_76142_g((float)(((EntityPlayerSPAccessor)entityIn).getLastReportedYaw() - yawOffset));
        this.field_78116_c.field_78796_g = calcNetHead / 57.295776f;
    }
}

