/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.StringUtils
 */
package com.jelly.mightyminerv2.util;

import com.jelly.mightyminerv2.util.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StringUtils;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean holdItem(String item) {
        int slot = InventoryUtil.getHotbarSlotOfItem(item);
        if (slot == -1) {
            return false;
        }
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        return true;
    }

    public static int getSlotIdOfItemInContainer(String item) {
        return InventoryUtil.getSlotIdOfItemInContainer(item, false);
    }

    public static int getSlotIdOfItemInContainer(String item, boolean equals) {
        Slot slot = InventoryUtil.getSlotOfItemInContainer(item, equals);
        return slot != null ? slot.field_75222_d : -1;
    }

    public static Slot getSlotOfItemInContainer(String item) {
        return InventoryUtil.getSlotOfItemInContainer(item, false);
    }

    public static Slot getSlotOfItemInContainer(String item, boolean equals) {
        for (Slot slot : InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b) {
            if (!slot.func_75216_d()) continue;
            String itemName = StringUtils.func_76338_a((String)slot.func_75211_c().func_82833_r());
            if (!(equals ? itemName.equalsIgnoreCase(item) : itemName.contains(item))) continue;
            return slot;
        }
        return null;
    }

    public static int getHotbarSlotOfItem(String items) {
        if (items.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack slot = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (slot == null || !slot.func_82837_s() || !slot.func_82833_r().contains(items)) continue;
            return i;
        }
        return -1;
    }

    public static boolean areItemsInInventory(Collection<String> items) {
        ArrayList<String> itemsToFind = new ArrayList<String>(items);
        for (ItemStack stack : InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a) {
            if (stack == null || !stack.func_82837_s()) continue;
            itemsToFind.removeIf(it -> stack.func_82833_r().contains((CharSequence)it));
        }
        return itemsToFind.isEmpty();
    }

    public static boolean areItemsInHotbar(Collection<String> items) {
        ArrayList<String> itemsToFind = new ArrayList<String>(items);
        for (int i = 0; i < 8; ++i) {
            ItemStack stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == null || !stack.func_82837_s()) continue;
            itemsToFind.removeIf(it -> stack.func_82833_r().contains((CharSequence)it));
        }
        return itemsToFind.isEmpty();
    }

    public static Pair<List<Integer>, List<String>> getAvailableHotbarSlots(Collection<String> items) {
        ArrayList<String> itemsToMove = new ArrayList<String>(items);
        ArrayList<Integer> slotsToMoveTo = new ArrayList<Integer>();
        for (int i = 0; i < 8; ++i) {
            ItemStack stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == null || !stack.func_82837_s()) {
                slotsToMoveTo.add(i);
            } else if (!itemsToMove.removeIf(item -> stack.func_82833_r().contains((CharSequence)item))) {
                slotsToMoveTo.add(i);
            }
            if (itemsToMove.isEmpty()) break;
        }
        return new Pair(slotsToMoveTo, itemsToMove);
    }

    public static String getInventoryName(Container container) {
        if (container instanceof ContainerChest) {
            IInventory inv = ((ContainerChest)container).func_85151_d();
            return inv != null && inv.func_145818_k_() ? inv.func_70005_c_() : "";
        }
        return "";
    }

    public static String getInventoryName() {
        return InventoryUtil.getInventoryName(InventoryUtil.mc.field_71439_g.field_71070_bA);
    }

    public static void clickContainerSlot(int slot, ClickType mouseButton, ClickMode mode) {
        InventoryUtil.clickContainerSlot(slot, mouseButton.ordinal(), mode.ordinal());
    }

    public static void clickContainerSlot(int slot, int mouseButton, ClickMode mode) {
        InventoryUtil.clickContainerSlot(slot, mouseButton, mode.ordinal());
    }

    public static void clickContainerSlot(int slot, int mouseButton, int clickMode) {
        InventoryUtil.mc.field_71442_b.func_78753_a(InventoryUtil.mc.field_71439_g.field_71070_bA.field_75152_c, slot, mouseButton, clickMode, (EntityPlayer)InventoryUtil.mc.field_71439_g);
    }

    public static void swapSlots(int slot, int hotbarSlot) {
        InventoryUtil.mc.field_71442_b.func_78753_a(InventoryUtil.mc.field_71439_g.field_71069_bz.field_75152_c, slot, hotbarSlot, 2, (EntityPlayer)InventoryUtil.mc.field_71439_g);
    }

    public static void openInventory() {
        KeyBinding.func_74507_a((int)InventoryUtil.mc.field_71474_y.field_151445_Q.func_151463_i());
    }

    public static void closeScreen() {
        if (InventoryUtil.mc.field_71462_r != null && InventoryUtil.mc.field_71439_g != null) {
            InventoryUtil.mc.field_71439_g.func_71053_j();
        }
    }

    public static List<String> getItemLoreFromOpenContainer(String name) {
        Container openContainer = InventoryUtil.mc.field_71439_g.field_71070_bA;
        for (int i = 0; i < openContainer.field_75151_b.size(); ++i) {
            ItemStack stack;
            Slot slot = openContainer.func_75139_a(i);
            if (slot == null || !slot.func_75216_d() || !(stack = slot.func_75211_c()).func_82837_s() || !StringUtils.func_76338_a((String)stack.func_82833_r()).contains(name)) continue;
            return InventoryUtil.getItemLore(stack);
        }
        return new ArrayList<String>();
    }

    public static List<String> getItemLoreFromInventory(String name) {
        Container container = InventoryUtil.mc.field_71439_g.field_71069_bz;
        for (int i = 0; i < container.field_75151_b.size(); ++i) {
            ItemStack stack;
            Slot slot = container.func_75139_a(i);
            if (slot == null || !slot.func_75216_d() || !(stack = slot.func_75211_c()).func_82837_s() || !StringUtils.func_76338_a((String)stack.func_82833_r()).contains(name)) continue;
            return InventoryUtil.getItemLore(stack);
        }
        return new ArrayList<String>();
    }

    public static List<String> getItemLore(ItemStack itemStack) {
        NBTTagList loreTag = itemStack.func_77978_p().func_74775_l("display").func_150295_c("Lore", 8);
        ArrayList<String> loreList = new ArrayList<String>();
        for (int i = 0; i < loreTag.func_74745_c(); ++i) {
            loreList.add(StringUtils.func_76338_a((String)loreTag.func_150307_f(i)));
        }
        return loreList;
    }

    public static List<String> getLoreOfItemInContainer(int slot) {
        if (slot == -1) {
            return new ArrayList<String>();
        }
        ItemStack itemStack = InventoryUtil.mc.field_71439_g.field_71070_bA.func_75139_a(slot).func_75211_c();
        if (itemStack == null) {
            return new ArrayList<String>();
        }
        return InventoryUtil.getItemLore(itemStack);
    }

    public static int getAmountOfItemInInventory(String item) {
        int amount = 0;
        for (Slot slot : InventoryUtil.mc.field_71439_g.field_71069_bz.field_75151_b) {
            String itemName;
            if (!slot.func_75216_d() || !slot.func_75211_c().func_82837_s() || !(itemName = StringUtils.func_76338_a((String)slot.func_75211_c().func_82833_r())).equals(item)) continue;
            amount += slot.func_75211_c().field_77994_a;
        }
        return amount;
    }

    public static String getItemId(ItemStack stack) {
        if (stack == null || !stack.func_82837_s()) {
            return "";
        }
        try {
            return stack.func_77978_p().func_74775_l("ExtraAttributes").func_74779_i("id");
        }
        catch (Exception ignored) {
            return StringUtils.func_76338_a((String)stack.func_82833_r());
        }
    }

    public static boolean isInventoryLoaded() {
        if (InventoryUtil.mc.field_71439_g == null || InventoryUtil.mc.field_71439_g.field_71070_bA == null) {
            return false;
        }
        if (!(InventoryUtil.mc.field_71462_r instanceof GuiChest)) {
            return false;
        }
        ContainerChest chest = (ContainerChest)InventoryUtil.mc.field_71439_g.field_71070_bA;
        ItemStack lastSlot = chest.func_85151_d().func_70301_a(chest.func_85151_d().func_70302_i_() - 1);
        return lastSlot != null && lastSlot.func_77973_b() != null;
    }

    public static boolean isInventoryEmpty() {
        for (int i = 0; i < InventoryUtil.mc.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
            if (InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i) == null) continue;
            return false;
        }
        return true;
    }

    public static String getFullName(String name) {
        for (int i = 0; i < InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.size(); ++i) {
            String itemName;
            ItemStack stack;
            Slot slot = InventoryUtil.mc.field_71439_g.field_71070_bA.func_75139_a(i);
            if (slot == null || !slot.func_75216_d() || !(stack = slot.func_75211_c()).func_82837_s() || !(itemName = StringUtils.func_76338_a((String)stack.func_82833_r())).toLowerCase().contains(name.toLowerCase())) continue;
            return itemName;
        }
        return "";
    }

    public static int getDrillFuelCapacity(String drillName) {
        List<String> loreList = InventoryUtil.getItemLoreFromInventory(drillName);
        if (loreList.isEmpty()) {
            return -1;
        }
        for (String lore : loreList) {
            if (!lore.startsWith("Fuel: ")) continue;
            try {
                return Integer.parseInt(lore.split("/")[1].replace("k", "000"));
            }
            catch (Exception e) {
                Logger.sendNote("Could not retrieve fuel capacity. Lore: " + lore + ", Splitted: " + Arrays.toString(lore.split("/")));
                e.printStackTrace();
                break;
            }
        }
        return -1;
    }

    public static int getDrillRemainingFuel(String drillName) {
        List<String> loreList = InventoryUtil.getItemLoreFromInventory(drillName);
        if (loreList.isEmpty()) {
            return -1;
        }
        for (String lore : loreList) {
            if (!lore.startsWith("Fuel: ")) continue;
            try {
                return Integer.parseInt(lore.split(" ")[1].split("/")[0].replace(",", ""));
            }
            catch (Exception e) {
                Logger.sendNote("Could not retrieve fuel. Lore: " + lore + ", Splitted: " + Arrays.toString(lore.split("/")));
                e.printStackTrace();
                break;
            }
        }
        return -1;
    }

    public static enum ClickMode {
        PICKUP,
        QUICK_MOVE,
        SWAP;

    }

    public static enum ClickType {
        LEFT,
        RIGHT;

    }
}

