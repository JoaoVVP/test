/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.gui.GuiPlayerTabOverlay
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.network.play.server.S0CPacketSpawnPlayer
 *  net.minecraft.network.play.server.S0FPacketSpawnMob
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S18PacketEntityTeleport
 *  net.minecraft.network.play.server.S1CPacketEntityMetadata
 *  net.minecraft.network.play.server.S25PacketBlockBreakAnim
 *  net.minecraft.network.play.server.S2APacketParticles
 *  net.minecraft.network.play.server.S38PacketPlayerListItem
 *  net.minecraft.network.play.server.S3BPacketScoreboardObjective
 *  net.minecraft.network.play.server.S3CPacketUpdateScore
 *  net.minecraft.network.play.server.S3DPacketDisplayScoreboard
 *  net.minecraft.network.play.server.S3EPacketTeams
 *  net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.LocalCapture
 */
package com.jelly.mightyminerv2.mixin.network;

import com.jelly.mightyminerv2.event.BlockDestroyEvent;
import com.jelly.mightyminerv2.event.SpawnParticleEvent;
import com.jelly.mightyminerv2.event.UpdateEntityEvent;
import com.jelly.mightyminerv2.event.UpdateScoreboardEvent;
import com.jelly.mightyminerv2.event.UpdateTablistEvent;
import com.jelly.mightyminerv2.event.UpdateTablistFooterEvent;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.ScoreboardUtil;
import com.jelly.mightyminerv2.util.TablistUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Unique
    private final List<String> mightyMinerV2$previousTablist = new ArrayList<String>();
    @Unique
    private final List<String> mightyMinerV2$previousFooter = new ArrayList<String>();
    @Unique
    Map<String, SortedMap<Integer, String>> mightyMinerv2$scoreboard = new HashMap<String, SortedMap<Integer, String>>();
    @Unique
    String[] mightyMinerv2$objectiveNames = new String[19];

    @Inject(method={"handleParticles"}, at={@At(value="HEAD")})
    public void handleParticles(S2APacketParticles packetIn, CallbackInfo ci) {
        SpawnParticleEvent event = new SpawnParticleEvent(packetIn.func_179749_a(), packetIn.func_179750_b(), packetIn.func_149220_d(), packetIn.func_149226_e(), packetIn.func_149225_f(), packetIn.func_149221_g(), packetIn.func_149224_h(), packetIn.func_149223_i(), packetIn.func_179748_k());
        MinecraftForge.EVENT_BUS.post((Event)event);
    }

    @Inject(method={"handlePlayerListItem"}, at={@At(value="RETURN")})
    public void handlePlayerListItem(S38PacketPlayerListItem packetIn, CallbackInfo ci) {
        ArrayList<String> tablist = new ArrayList<String>();
        List players = TablistUtil.playerOrdering.sortedCopy((Iterable)Minecraft.func_71410_x().func_147114_u().func_175106_d());
        GuiPlayerTabOverlay tabOverlay = Minecraft.func_71410_x().field_71456_v.func_175181_h();
        for (NetworkPlayerInfo info : players) {
            tablist.add(StringUtils.func_76338_a((String)tabOverlay.func_175243_a(info)));
        }
        if (tablist.equals(this.mightyMinerV2$previousTablist)) {
            return;
        }
        this.mightyMinerV2$previousTablist.clear();
        this.mightyMinerV2$previousTablist.addAll(tablist);
        TablistUtil.setCachedTablist(tablist);
        MinecraftForge.EVENT_BUS.post((Event)new UpdateTablistEvent(tablist, System.currentTimeMillis()));
    }

    @Inject(method={"handlePlayerListHeaderFooter"}, at={@At(value="RETURN")})
    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn, CallbackInfo ci) {
        ArrayList<String> footer = new ArrayList<String>();
        if (packetIn.func_179701_b() == null) {
            return;
        }
        for (String s : packetIn.func_179701_b().func_150254_d().split("\n")) {
            footer.add(StringUtils.func_76338_a((String)s));
        }
        if (footer.equals(this.mightyMinerV2$previousFooter)) {
            return;
        }
        this.mightyMinerV2$previousFooter.clear();
        this.mightyMinerV2$previousFooter.addAll(footer);
        TablistUtil.setCachedTabListFooter(footer);
        MinecraftForge.EVENT_BUS.post((Event)new UpdateTablistFooterEvent(footer));
    }

    @Inject(method={"handleBlockBreakAnim"}, at={@At(value="HEAD")})
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new BlockDestroyEvent(packetIn.func_179821_b(), packetIn.func_148846_g()));
    }

    @Inject(method={"handleEntityMetadata"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/DataWatcher;updateWatchedObjectsFromList(Ljava/util/List;)V", shift=At.Shift.AFTER)}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn, CallbackInfo ci, Entity entity) {
        if (entity instanceof EntityArmorStand && entity.func_145818_k_()) {
            MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent((EntityLivingBase)entity));
        }
    }

    @Inject(method={"handleSpawnMob"}, at={@At(value="TAIL")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleSpawnMob(S0FPacketSpawnMob packetIn, CallbackInfo ci, double d0, double d1, double d2, float f, float f1, EntityLivingBase entitylivingbase, Entity[] aentity, List list) {
        MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent(entitylivingbase));
    }

    @Inject(method={"handleSpawnPlayer"}, at={@At(value="TAIL")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn, CallbackInfo ci, double d0, double d1, double d2, float f, float f1, EntityOtherPlayerMP entityotherplayermp, int i, List list) {
        MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent((EntityLivingBase)entityotherplayermp));
    }

    @Redirect(method={"handleDestroyEntities"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;removeEntityFromWorld(I)Lnet/minecraft/entity/Entity;"))
    public Entity handleDestroyEntities(WorldClient instance, int entityID) {
        Entity entity = instance.func_73028_b(entityID);
        if (entity instanceof EntityLivingBase) {
            MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent((EntityLivingBase)entity, 1));
        }
        return entity;
    }

    @Inject(method={"handleEntityMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;setPositionAndRotation2(DDDFFIZ)V")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleEntityMovement(S14PacketEntity packetIn, CallbackInfo ci, Entity entity, double d0, double d1, double d2, float f, float f1) {
        if (entity instanceof EntityLivingBase) {
            int nX = (int)Math.round(d0) / 3;
            int nZ = (int)Math.round(d2) / 3;
            if ((int)Math.round(entity.field_70165_t) != nX || (int)Math.round(entity.field_70161_v) != nZ) {
                MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent((EntityLivingBase)entity, this.pack(nX, nZ)));
            }
        }
    }

    @Inject(method={"handleEntityTeleport"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/play/server/S18PacketEntityTeleport;getYaw()B")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleEntityTeleport(S18PacketEntityTeleport packetIn, CallbackInfo ci, Entity entity, double d0, double d1, double d2) {
        if (entity instanceof EntityLivingBase) {
            int nX = (int)Math.round(d0) / 3;
            int nZ = (int)Math.round(d2) / 3;
            if ((int)Math.round(entity.field_70165_t) != nX || (int)Math.round(entity.field_70161_v) != nZ) {
                MinecraftForge.EVENT_BUS.post((Event)new UpdateEntityEvent((EntityLivingBase)entity, this.pack(nX, nZ)));
            }
        }
    }

    @Unique
    private long pack(int x, int z) {
        return (long)x << 32 | (long)z & 0xFFFFFFFFL;
    }

    @Inject(method={"handleDisplayScoreboard"}, at={@At(value="TAIL")})
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn, CallbackInfo ci) {
        this.mightyMinerv2$objectiveNames[packetIn.func_149371_c()] = packetIn.func_149370_d();
        ScoreboardUtil.scoreObjNames = Arrays.copyOf(this.mightyMinerv2$objectiveNames, 19);
    }

    @Inject(method={"handleScoreboardObjective"}, at={@At(value="TAIL")})
    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn, CallbackInfo ci) {
        String objName = packetIn.func_149339_c();
        TreeMap removedValue = this.mightyMinerv2$scoreboard.remove(objName);
        if (packetIn.func_149338_e() != 1) {
            this.mightyMinerv2$scoreboard.put(objName, packetIn.func_149338_e() == 0 ? new TreeMap(Comparator.reverseOrder()) : removedValue);
        }
        String sidebarObjName = this.mightyMinerv2$objectiveNames[1];
        SortedMap<Integer, String> sidebar = this.mightyMinerv2$scoreboard.get(sidebarObjName);
        if (objName.equals(sidebarObjName) && sidebar != null) {
            MinecraftForge.EVENT_BUS.post((Event)new UpdateScoreboardEvent(new ArrayList<String>(sidebar.values()), System.currentTimeMillis()));
        }
        ScoreboardUtil.scoreboard = new HashMap<String, SortedMap<Integer, String>>(this.mightyMinerv2$scoreboard);
    }

    @Inject(method={"handleUpdateScore"}, at={@At(value="INVOKE", target="Lnet/minecraft/scoreboard/Scoreboard;getObjective(Ljava/lang/String;)Lnet/minecraft/scoreboard/ScoreObjective;")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleUpdateScorePRE(S3CPacketUpdateScore packetIn, CallbackInfo ci, Scoreboard scoreboard) {
        try {
            if (!StringUtils.func_151246_b((String)packetIn.func_149321_d())) {
                this.mightyMinerv2$updateScoreboard(packetIn.func_149321_d(), scoreboard.func_96529_a(packetIn.func_149324_c(), scoreboard.func_96518_b(packetIn.func_149321_d())).func_96652_c(), packetIn.func_180751_d().ordinal(), packetIn.func_149323_e());
            } else {
                scoreboard.func_96510_d(packetIn.func_149324_c()).forEach((k, v) -> this.mightyMinerv2$updateScoreboard(k.func_96679_b(), v.func_96652_c(), packetIn.func_180751_d().ordinal(), packetIn.func_149323_e()));
            }
            String sidebarObjName = this.mightyMinerv2$objectiveNames[1];
            SortedMap<Integer, String> sidebar = this.mightyMinerv2$scoreboard.get(sidebarObjName);
            if (sidebarObjName.equals(packetIn.func_149321_d()) && sidebar != null) {
                MinecraftForge.EVENT_BUS.post((Event)new UpdateScoreboardEvent(new ArrayList<String>(sidebar.values()), System.currentTimeMillis()));
            }
            ScoreboardUtil.scoreboard = new HashMap<String, SortedMap<Integer, String>>(this.mightyMinerv2$scoreboard);
        }
        catch (Exception e) {
            Logger.sendNote("Couldn't Handle Update Score. Action: " + packetIn.func_180751_d());
            e.printStackTrace();
        }
    }

    @Unique
    public void mightyMinerv2$updateScoreboard(String objName, int score, int action, int packetScore) {
        SortedMap<Integer, String> objective = this.mightyMinerv2$scoreboard.get(objName);
        String text = (String)objective.remove(score);
        if (text != null && action == 0) {
            objective.put(packetScore, text);
        }
    }

    @Inject(method={"handleTeams"}, at={@At(value="TAIL")}, locals=LocalCapture.CAPTURE_FAILHARD)
    public void handleTeams(S3EPacketTeams packetIn, CallbackInfo ci, Scoreboard scoreboard, ScorePlayerTeam scoreplayerteam) {
        if (scoreplayerteam == null || scoreplayerteam.func_96661_b() == null) {
            return;
        }
        if (packetIn.func_149307_h() == 1 || packetIn.func_149307_h() == 4) {
            scoreplayerteam.func_96670_d().forEach(it -> scoreboard.func_96510_d(it).forEach((a, b) -> this.mightyMinerv2$scoreboard.get(a.func_96679_b()).remove(b.func_96652_c())));
        } else {
            scoreplayerteam.func_96670_d().forEach(it -> scoreboard.func_96510_d(it).forEach((a, b) -> this.mightyMinerv2$scoreboard.get(a.func_96679_b()).put(b.func_96652_c(), this.mightyMinerv2$sanitizeString(scoreplayerteam.func_96668_e() + b.func_96653_e() + scoreplayerteam.func_96663_f()))));
        }
        SortedMap<Integer, String> sidebar = this.mightyMinerv2$scoreboard.get(this.mightyMinerv2$objectiveNames[1]);
        if (sidebar != null) {
            MinecraftForge.EVENT_BUS.post((Event)new UpdateScoreboardEvent(new ArrayList<String>(sidebar.values()), System.currentTimeMillis()));
        }
        ScoreboardUtil.scoreboard = new HashMap<String, SortedMap<Integer, String>>(this.mightyMinerv2$scoreboard);
    }

    @Unique
    public String mightyMinerv2$sanitizeString(String scoreboard) {
        char[] arr = scoreboard.toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            char c = arr[i];
            if (c >= ' ' && c < '\u007f' || c == '\u23e3' || c == '\u0444') {
                cleaned.append(c);
            }
            if (c != '\u00a7') continue;
            ++i;
        }
        return cleaned.toString();
    }
}

