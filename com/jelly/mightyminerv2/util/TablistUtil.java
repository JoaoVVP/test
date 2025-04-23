/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Ordering
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.world.WorldSettings$GameType
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.jelly.mightyminerv2.util;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TablistUtil {
    public static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from((Comparator)new PlayerComparator());
    private static final CopyOnWriteArrayList<String> cachedTablist = new CopyOnWriteArrayList();
    private static final CopyOnWriteArrayList<String> cachedTablistFooter = new CopyOnWriteArrayList();

    public static void setCachedTablist(List<String> tablist) {
        cachedTablist.clear();
        cachedTablist.addAll(tablist);
    }

    public static void setCachedTabListFooter(List<String> tabListFooter) {
        cachedTablistFooter.clear();
        cachedTablistFooter.addAll(tabListFooter);
    }

    public static List<String> getTabListPlayersUnprocessed() {
        try {
            List players = playerOrdering.sortedCopy((Iterable)Minecraft.func_71410_x().field_71439_g.field_71174_a.func_175106_d());
            ArrayList<String> result = new ArrayList<String>();
            for (NetworkPlayerInfo info : players) {
                String name = Minecraft.func_71410_x().field_71456_v.func_175181_h().func_175243_a(info);
                result.add(name);
            }
            return result;
        }
        catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public static List<String> getTabListPlayersSkyblock() {
        try {
            List<String> tabListPlayersFormatted = TablistUtil.getTabListPlayersUnprocessed();
            ArrayList<String> playerList = new ArrayList<String>();
            tabListPlayersFormatted.remove(0);
            String firstPlayer = null;
            for (String s : tabListPlayersFormatted) {
                int a = s.indexOf("]");
                if (a == -1 || s.length() < a + 2) continue;
                s = s.substring(a + 2).replaceAll("\u00a7([0-9]|[a-z])", "").replace("\u2672", "").trim();
                if (firstPlayer == null) {
                    firstPlayer = s;
                } else if (s.equals(firstPlayer)) break;
                playerList.add(s);
            }
            return playerList;
        }
        catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public static CopyOnWriteArrayList<String> getCachedTablist() {
        return cachedTablist;
    }

    public static CopyOnWriteArrayList<String> getCachedTablistFooter() {
        return cachedTablistFooter;
    }

    @SideOnly(value=Side.CLIENT)
    static class PlayerComparator
    implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        @Override
        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            ScorePlayerTeam team1 = o1.func_178850_i();
            ScorePlayerTeam team2 = o2.func_178850_i();
            return ComparisonChain.start().compareTrueFirst(o1.func_178848_b() != WorldSettings.GameType.SPECTATOR, o2.func_178848_b() != WorldSettings.GameType.SPECTATOR).compare((Comparable)((Object)(team1 != null ? team1.func_96661_b() : "")), (Comparable)((Object)(team2 != null ? team2.func_96661_b() : ""))).compare((Comparable)((Object)o1.func_178845_a().getName()), (Comparable)((Object)o2.func_178845_a().getName())).result();
        }
    }
}

