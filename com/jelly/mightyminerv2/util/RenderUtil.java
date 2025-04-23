/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.StringUtils
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  org.lwjgl.opengl.GL11
 */
package com.jelly.mightyminerv2.util;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static void drawPoint(Vec3 vec, Color color) {
        RenderUtil.drawBox(new AxisAlignedBB(vec.field_72450_a - 0.05, vec.field_72448_b - 0.05, vec.field_72449_c - 0.05, vec.field_72450_a + 0.05, vec.field_72448_b + 0.05, vec.field_72449_c + 0.05), color);
    }

    private static void startGL() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179090_x();
    }

    private static void endGL() {
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179117_G();
        GlStateManager.func_179121_F();
    }

    private static void drawLine(double mix, double miy, double miz, double max, double may, double maz, float red, float green, float blue, float alpha, float scale) {
        GL11.glLineWidth((float)scale);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)mix, (double)miy, (double)miz);
        GL11.glVertex3d((double)max, (double)may, (double)maz);
        GL11.glEnd();
        GL11.glLineWidth((float)1.0f);
    }

    private static void outline(double mix, double miy, double miz, double max, double may, double maz, float red, float green, float blue, float alpha, float scale) {
        GL11.glLineWidth((float)scale);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)mix, (double)miy, (double)miz);
        GL11.glVertex3d((double)max, (double)miy, (double)miz);
        GL11.glVertex3d((double)max, (double)miy, (double)miz);
        GL11.glVertex3d((double)max, (double)miy, (double)maz);
        GL11.glVertex3d((double)max, (double)miy, (double)maz);
        GL11.glVertex3d((double)mix, (double)miy, (double)maz);
        GL11.glVertex3d((double)mix, (double)miy, (double)maz);
        GL11.glVertex3d((double)mix, (double)miy, (double)miz);
        GL11.glVertex3d((double)mix, (double)may, (double)miz);
        GL11.glVertex3d((double)max, (double)may, (double)miz);
        GL11.glVertex3d((double)max, (double)may, (double)miz);
        GL11.glVertex3d((double)max, (double)may, (double)maz);
        GL11.glVertex3d((double)max, (double)may, (double)maz);
        GL11.glVertex3d((double)mix, (double)may, (double)maz);
        GL11.glVertex3d((double)mix, (double)may, (double)maz);
        GL11.glVertex3d((double)mix, (double)may, (double)miz);
        GL11.glVertex3d((double)mix, (double)miy, (double)miz);
        GL11.glVertex3d((double)mix, (double)may, (double)miz);
        GL11.glVertex3d((double)max, (double)miy, (double)miz);
        GL11.glVertex3d((double)max, (double)may, (double)miz);
        GL11.glVertex3d((double)max, (double)miy, (double)maz);
        GL11.glVertex3d((double)max, (double)may, (double)maz);
        GL11.glVertex3d((double)mix, (double)miy, (double)maz);
        GL11.glVertex3d((double)mix, (double)may, (double)maz);
        GL11.glEnd();
        GL11.glLineWidth((float)1.0f);
    }

    public static void drawLine(Vec3 start2, Vec3 end, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        double vx = renderManager.field_78730_l;
        double vy = renderManager.field_78731_m;
        double vz = renderManager.field_78728_n;
        RenderUtil.startGL();
        RenderUtil.drawLine(start2.field_72450_a - vx, start2.field_72448_b - vy, start2.field_72449_c - vz, end.field_72450_a - vx, end.field_72448_b - vy, end.field_72449_c - vz, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f, 1.5f);
        RenderUtil.endGL();
    }

    public static void outlineBlock(BlockPos pos, Color color) {
        RenderUtil.outlineBox(new AxisAlignedBB((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), (double)(pos.func_177958_n() + 1), (double)(pos.func_177956_o() + 1), (double)(pos.func_177952_p() + 1)), color);
    }

    public static void outlineBox(AxisAlignedBB bb, Color color) {
        RenderManager renderManager = mc.func_175598_ae();
        bb = bb.func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n);
        RenderUtil.startGL();
        RenderUtil.outline(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d, bb.field_72337_e, bb.field_72334_f, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f, 2.0f);
        RenderUtil.endGL();
    }

    public static void drawBlock(BlockPos blockPos, Color color) {
        double x = blockPos.func_177958_n();
        double y = blockPos.func_177956_o();
        double z = blockPos.func_177952_p();
        RenderUtil.drawBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), color);
    }

    public static void drawBox(AxisAlignedBB aabb, Color color) {
        RenderUtil.startGL();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        RenderManager renderManager = mc.func_175598_ae();
        aabb = aabb.func_72317_d(-renderManager.field_78730_l, -renderManager.field_78731_m, -renderManager.field_78728_n);
        double miX = aabb.field_72340_a;
        double miY = aabb.field_72338_b;
        double miZ = aabb.field_72339_c;
        double maX = aabb.field_72336_d;
        double maY = aabb.field_72337_e;
        double maZ = aabb.field_72334_f;
        float a = (float)color.getAlpha() / 255.0f;
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)a);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b(miX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, maZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, maZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(miX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(miX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, maZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, miZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(miX, miY, miZ).func_181675_d();
        worldrenderer.func_181662_b(miX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(maX, miY, maZ).func_181675_d();
        worldrenderer.func_181662_b(maX, maY, maZ).func_181675_d();
        worldrenderer.func_181662_b(miX, maY, maZ).func_181675_d();
        tessellator.func_78381_a();
        RenderUtil.outline(miX, miY, miZ, maX, maY, maZ, r, g, b, a, 2.0f);
        RenderUtil.endGL();
    }

    public static void drawMultiLineText(List<String> lines, RenderGameOverlayEvent event, Color color, float scale) {
        ScaledResolution scaledResolution = event.resolution;
        int scaledWidth = scaledResolution.func_78326_a();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(scaledWidth / 2), (float)50.0f, (float)0.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179152_a((float)scale, (float)scale, (float)scale);
        FontRenderer fontRenderer = RenderUtil.mc.field_71466_p;
        int yOffset = 0;
        for (String line : lines) {
            fontRenderer.func_175065_a(line, (float)(-fontRenderer.func_78256_a(line)) / 2.0f, (float)yOffset, color.getRGB(), true);
            yOffset += fontRenderer.field_78288_b * 2;
        }
        GlStateManager.func_179121_F();
    }

    public static void drawCenterTopText(String text, RenderGameOverlayEvent event, Color color) {
        RenderUtil.drawCenterTopText(text, event, color, 3.0f);
    }

    public static void drawCenterTopText(String text, RenderGameOverlayEvent event, Color color, float scale) {
        ScaledResolution scaledResolution = event.resolution;
        int scaledWidth = scaledResolution.func_78326_a();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(scaledWidth / 2), (float)50.0f, (float)0.0f);
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179152_a((float)scale, (float)scale, (float)scale);
        RenderUtil.mc.field_71466_p.func_175065_a(text, (float)(-RenderUtil.mc.field_71466_p.func_78256_a(text)) / 2.0f, 0.0f, color.getRGB(), true);
        GlStateManager.func_179121_F();
    }

    public static void drawText(String str, double X, double Y, double Z, float scale) {
        float lScale = scale;
        FontRenderer fontRenderer = RenderUtil.mc.field_71466_p;
        double renderPosX = X - RenderUtil.mc.func_175598_ae().field_78730_l;
        double renderPosY = Y - RenderUtil.mc.func_175598_ae().field_78731_m;
        double renderPosZ = Z - RenderUtil.mc.func_175598_ae().field_78728_n;
        double distance = Math.sqrt(renderPosX * renderPosX + renderPosY * renderPosY + renderPosZ * renderPosZ);
        double multiplier = Math.max(distance / 150.0, (double)0.1f);
        lScale *= (float)((double)0.45f * multiplier);
        float xMultiplier = RenderUtil.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)renderPosX, (double)renderPosY, (double)renderPosZ);
        RenderManager renderManager = mc.func_175598_ae();
        GlStateManager.func_179114_b((float)(-renderManager.field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(renderManager.field_78732_j * xMultiplier), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)(-lScale), (float)(-lScale), (float)lScale);
        GlStateManager.func_179140_f();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        int textWidth = fontRenderer.func_78256_a(StringUtils.func_76338_a((String)str));
        float j = (float)textWidth / 2.0f;
        GlStateManager.func_179090_x();
        Tessellator tessellator = Tessellator.func_178181_a();
        WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179131_c((float)0.0f, (float)0.0f, (float)0.0f, (float)0.5f);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)(-j - 1.0f), -1.0, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)(-j - 1.0f), 8.0, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)(j + 1.0f), 8.0, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)(j + 1.0f), -1.0, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        fontRenderer.func_78276_b(str, -textWidth / 2, 0, 0x20FFFFFF);
        GlStateManager.func_179132_a((boolean)true);
        fontRenderer.func_78276_b(str, -textWidth / 2, 0, -1);
        GlStateManager.func_179126_j();
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179121_F();
    }

    public static void drawTracer(Vec3 to, Color color) {
        RenderUtil.drawLine(new Vec3(RenderUtil.mc.field_71439_g.field_70165_t, RenderUtil.mc.field_71439_g.field_70163_u + (double)RenderUtil.mc.field_71439_g.func_70047_e(), RenderUtil.mc.field_71439_g.field_70161_v), to, color);
    }
}

