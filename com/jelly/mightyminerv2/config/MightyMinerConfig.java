/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.config.Config
 *  cc.polyfrost.oneconfig.config.annotations.Button
 *  cc.polyfrost.oneconfig.config.annotations.Color
 *  cc.polyfrost.oneconfig.config.annotations.Dropdown
 *  cc.polyfrost.oneconfig.config.annotations.DualOption
 *  cc.polyfrost.oneconfig.config.annotations.HUD
 *  cc.polyfrost.oneconfig.config.annotations.Info
 *  cc.polyfrost.oneconfig.config.annotations.KeyBind
 *  cc.polyfrost.oneconfig.config.annotations.Number
 *  cc.polyfrost.oneconfig.config.annotations.Slider
 *  cc.polyfrost.oneconfig.config.annotations.Switch
 *  cc.polyfrost.oneconfig.config.annotations.Text
 *  cc.polyfrost.oneconfig.config.core.OneColor
 *  cc.polyfrost.oneconfig.config.core.OneKeyBind
 *  cc.polyfrost.oneconfig.config.data.InfoType
 *  cc.polyfrost.oneconfig.config.data.Mod
 *  cc.polyfrost.oneconfig.config.data.ModType
 *  cc.polyfrost.oneconfig.libs.universal.UKeyboard
 *  net.minecraft.client.Minecraft
 */
package com.jelly.mightyminerv2.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.DualOption;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import com.jelly.mightyminerv2.feature.impl.RouteBuilder;
import com.jelly.mightyminerv2.hud.CommissionHUD;
import com.jelly.mightyminerv2.hud.DebugHUD;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.helper.AudioManager;
import java.io.File;
import net.minecraft.client.Minecraft;

public class MightyMinerConfig
extends Config {
    private static final transient Minecraft mc = Minecraft.func_71410_x();
    private static final transient String GENERAL = "General";
    private static final transient String MISCELLANEOUS = "Miscellaneous";
    private static final transient String SCHEDULER = "Scheduler";
    private static final transient String MITHRIL = "Mithril";
    private static final transient String COMMISSION = "Commission";
    private static final transient String GEMSTONE = "Gemstone";
    private static final transient String POWDER = "Powder";
    private static final transient String AOTV = "AOTV";
    private static final transient String ROUTE_BUILDER = "Route Builder";
    private static final transient String DELAY = "Delays";
    private static final transient String AUTO_SELL = "Auto Sell";
    private static final transient String FAILSAFE = "Failsafe";
    private static final transient String HUD = "HUD";
    private static final transient String DEBUG = "Debug";
    private static final transient String DISCORD_INTEGRATION = "Discord Integration";
    private static final transient String EXPERIMENTAL = "Experimental";
    private static final transient File WAYPOINTS_FILE = new File(MightyMinerConfig.mc.field_71412_D, "mm_waypoints.json");
    @Dropdown(name="Macro Type", category="General", description="Select the macro type you want to use", options={"Commission", "Mithril", "Gemstone", "Powder", "AOTV"})
    public static int macroType = 0;
    @KeyBind(name="Toggle Macro", category="General", description="The Button To Click To Toggle The Macro")
    public static OneKeyBind toggleMacro = new OneKeyBind(new int[]{41});
    @Switch(name="Ungrab Mouse", description="Ungrabs Mouse; Duh", category="General")
    public static boolean ungrabMouse = true;
    @Switch(name="Mute Game", description="Mute Game", category="General")
    public static boolean muteGame = true;
    @Text(name="Mining Tool", description="The tool to use during mithrill macro", category="Mithril", placeholder="Mining Tool Name")
    public static String mithrilMiningTool = "Pickonimbus 2000";
    @Switch(name="Strafe While Mining", description="Walk Around The Vein While Mining", category="Mithril")
    public static boolean mithrilStrafe = false;
    @Switch(name="Sneak While Mining", category="Mithril")
    public static boolean mithrilMinerSneakWhileMining = false;
    @Switch(name="Precision Miner", description="Looks at particles spawned by precision miner perk (Might/Will Mess Up TickGLide)", category="Mithril")
    public static boolean mithrilMinerPrecisionMiner = false;
    @Slider(name="Rotation Time", category="Mithril", description="Time it takes to rotate to the next block while mining mithril", min=50.0f, max=1000.0f)
    public static int mithrilMinerRotationTime = 300;
    @Slider(name="Rotation Time Randomization", category="Mithril", min=50.0f, max=1000.0f)
    public static int mithrilMinerRotationTimeRandomizer = 300;
    @Slider(name="Tick Glide Offset", category="Mithril", min=0.0f, max=10.0f)
    public static int mithrilMinerTickGlideOffset = 4;
    @Slider(name="Sneak Time", category="Mithril", min=0.0f, max=2000.0f)
    public static int mithrilMinerSneakTime = 500;
    @Slider(name="Sneak Time Randomizer", category="Mithril", min=0.0f, max=2000.0f)
    public static int mithrilMinerSneakTimeRandomizer = 300;
    @Dropdown(name="Ore Type", category="Mithril", options={"Diamond", "Emerald", "Redstone", "Lapis", "Gold", "Iron", "Coal", "Mithril"})
    public static int oreType = 0;
    @Slider(name="Gray Mithril Priority", category="Mithril", min=1.0f, max=10.0f, step=1)
    public static int grayMithrilPriority = 5;
    @Slider(name="Green Mithril Priority", category="Mithril", min=1.0f, max=10.0f, step=1)
    public static int greenMithrilPriority = 5;
    @Slider(name="Blue Mithril Priority", category="Mithril", min=1.0f, max=10.0f, step=1)
    public static int blueMithrilPriority = 5;
    @Slider(name="Titanium Priority", category="Mithril", min=1.0f, max=10.0f, step=1)
    public static int titaniumPriority = 5;
    @Text(name="Mining Tool", description="The tool to use during comm macro", category="Commission", placeholder="Mining Tool Name")
    public static String commMiningTool = "Pickonimbus 2000";
    @Text(name="Slayer Weapon", description="Weapon to use for slayers", category="Commission", placeholder="Slayer Weapon Name")
    public static String commSlayerWeapon = "Sword";
    @Dropdown(name="Claim Method", category="Commission", options={"NPC", "Royal Pigeon", "Abiphone"})
    public static int commClaimMethod = 0;
    @Switch(name="Always Mine Titanium", description="Mines titanium even if it isnt a titanium commission", category="Commission")
    public static boolean commMineTitanium = false;
    @Switch(name="Sprint During MobKiller", description="Allow Sprinting while mobkiller is active (looks sussy with sprint)", category="Commission", subcategory="MobKiller")
    public static boolean commMobKillerSprint = true;
    @Switch(name="Interpolate During MobKiller", description="Helps reduce sliding", category="Commission", subcategory="MobKiller")
    public static boolean commMobKillerInterpolate = true;
    @Switch(name="Refuel Drill", category="Commission", subcategory="Refuel")
    public static boolean commDrillRefuel = false;
    @Dropdown(name="Machine Fuel", category="Commission", subcategory="Refuel", options={"Enchanted Poppy", "Goblin Egg", "Green Goblin Egg", "Yellow Goblin Egg", "Red Goblin Egg", "Blue Goblin Egg", "Volta", "Oil Barrel"})
    public static int commMachineFuel = 6;
    @DualOption(name="Fuel Retrieval Method", category="Commission", subcategory="Refuel", left="Buy From Bazaar", right="Get From Sack")
    public static boolean commFuelRetrievalMethod = false;
    @DualOption(name="Mechanics GUI Access Method", category="Commission", subcategory="Refuel", left="Abiphone", right="From NPC")
    public static boolean commMechaGuiAccessMethod = false;
    @Slider(name="Time to wait before toggling failsafe(in ms)", category="Failsafe", subcategory="Delays", min=0.0f, max=15000.0f, step=100)
    public static int failsafeToggleDelay = 3000;
    @KeyBind(name="Enable RouteBuilder", description="They key to click to enable RouteBuilder", category="Route Builder", size=2)
    public static OneKeyBind routeBuilder = new OneKeyBind(new int[]{26});
    @KeyBind(name="Add Block To Route(AOTV)", description="The Key to click to add the block player is standing on block to the route", category="Route Builder")
    public static OneKeyBind routeBuilderAotvAddKeybind = new OneKeyBind(new int[]{25});
    @KeyBind(name="Add Block To Route(ETHERWARP)", description="The Key to click to add the block player is standing on block to the route", category="Route Builder")
    public static OneKeyBind routeBuilderEtherwarpAddKeybind = new OneKeyBind(new int[]{23});
    @KeyBind(name="Remove Block From Route", description="The Key To Remove the block player is standing on from the route", category="Route Builder")
    public static OneKeyBind routeBuilderRemoveKeybind = new OneKeyBind(new int[]{24});
    @Color(name="Route Node Color", description="The Color of The Blocks On a Route", category="Route Builder")
    public static OneColor routeBuilderNodeColor = new OneColor(0, 255, 255, 100);
    @Color(name="Route Tracer Color", description="The Color of The Line Between Blocks On a Route", category="Route Builder")
    public static OneColor routeBuilderTracerColor = new OneColor(0, 255, 255, 100);
    @KeyBind(name="RouteBuilder Add UNIDI", category="Route Builder", subcategory="Graph")
    public static OneKeyBind routeBuilderUnidi = new OneKeyBind(new int[]{71});
    @KeyBind(name="RouteBuilder Add BIDI", category="Route Builder", subcategory="Graph")
    public static OneKeyBind routeBuilderBidi = new OneKeyBind(new int[]{72});
    @KeyBind(name="RouteBuilder Select", category="Route Builder", subcategory="Graph")
    public static OneKeyBind routeBuilderSelect = new OneKeyBind(new int[]{75});
    @KeyBind(name="RouteBuilder Move", category="Route Builder", subcategory="Graph")
    public static OneKeyBind routeBuilderMove = new OneKeyBind(new int[]{76});
    @KeyBind(name="RouteBuilder Move", category="Route Builder", subcategory="Graph")
    public static OneKeyBind routeBuilderDelete = new OneKeyBind(new int[]{77});
    @Slider(name="GUI Delay", description="Time to wait in a gui", category="Delays", subcategory="GUI", min=50.0f, max=2000.0f)
    public static int delaysGuiDelay = 450;
    @Slider(name="GUI Delay Randomizer", description="Maximum random time to add to GUI Delay Time", category="Delays", subcategory="GUI", min=50.0f, max=1000.0f)
    public static int delaysGuiDelayRandomizer = 250;
    @Slider(name="Aotv Look Delay (Right Click)", description="Rotation time to look at next block while aotving", category="Delays", subcategory="AutoAotv", min=50.0f, max=1000.0f)
    public static int delayAutoAotvLookDelay = 250;
    @Slider(name="Aotv Look Delay (Etherwarp)", description="Rotation time to look at next block while Etherwarping", category="Delays", subcategory="AutoAotv", min=50.0f, max=2000.0f)
    public static int delayAutoAotvEtherwarpLookDelay = 500;
    @Slider(name="Server Side Rotation Time", description="Rotation time to look at next block with client side rotation", category="Delays", subcategory="AutoAotv", min=0.0f, max=2000.0f)
    public static int delayAutoAotvServerRotation = 500;
    @Slider(name="Rotation", category="Dev", subcategory="MithrilMiner", min=0.0f, max=10.0f)
    public static int devMithRot = 3;
    @Slider(name="MobKiller Dist Cost", category="Dev", subcategory="MobKiller", min=0.0f, max=1000.0f)
    public static int devMKillDist = 100;
    @Slider(name="MobKiller Rot Cost", category="Dev", subcategory="MobKiller", min=0.0f, max=1000.0f)
    public static int devMKillRot = 5;
    @Text(name="MobKiller Mob Name", category="Dev", subcategory="MobKiller", placeholder="Enter Mobname Here")
    public static String devMKillerMob = "Goblin";
    @Text(name="MobKiller Weapon", category="Dev", subcategory="MobKiller", placeholder="Enter Weapon Name Here")
    public static String devMKillerWeapon = "Aspect of the Dragon";
    @Slider(name="MobKiller Check Timer", category="Dev", subcategory="MobKiller", min=0.0f, max=5000.0f)
    public static int devMKillTimer = 1000;
    @Slider(name="Rotation Curve", category="Dev", subcategory="Cost", min=0.0f, max=5.0f)
    public static int cost4 = 1;
    @Switch(name="Use Fixed Rotation Time", category="Dev", subcategory="Path")
    public static boolean fixrot = false;
    @Slider(name="Rotation time", category="Dev", subcategory="Path", min=0.0f, max=2000.0f)
    public static int rottime = 500;
    @Slider(name="Rota mult", category="Dev", subcategory="Path", min=0.0f, max=10.0f)
    public static float rotmult = 2.0f;
    @Switch(name="Debug Mode", category="Debug", description="Enable debug mode")
    public static boolean debugMode = false;
    @HUD(name="DebugHUD", category="Debug")
    public static DebugHUD debugHUD = DebugHUD.getInstance();
    @HUD(name="CommissionHUD", category="Commission")
    public static CommissionHUD commissionHUD = CommissionHUD.getInstance();
    @Switch(name="Enable Failsafe Trigger Sound", category="Failsafe", subcategory="Failsafe Trigger Sound", size=2, description="Makes a sound when a failsafe has been triggered")
    public static boolean enableFailsafeSound = true;
    @DualOption(name="Failsafe Sound Type", category="Failsafe", subcategory="Failsafe Trigger Sound", description="The failsafe sound type to play when a failsafe has been triggered", left="Minecraft", right="Custom", size=2)
    public static boolean failsafeSoundType = false;
    @Dropdown(name="Minecraft Sound", category="Failsafe", subcategory="Failsafe Trigger Sound", description="The Minecraft sound to play when a failsafe has been triggered", options={"Ping", "Anvil"})
    public static int failsafeMcSoundSelected = 1;
    @Dropdown(name="Custom Sound", category="Failsafe", subcategory="Failsafe Trigger Sound", description="The custom sound to play when a failsafe has been triggered", options={"Custom", "Voice", "Metal Pipe", "AAAAAAAAAA", "Loud Buzz"})
    public static int failsafeSoundSelected = 1;
    @Number(name="Number of times to play custom sound", category="Failsafe", subcategory="Failsafe Trigger Sound", description="The number of times to play custom sound when a failsafe has been triggered", min=1.0f, max=10.0f)
    public static int failsafeSoundTimes = 13;
    @Slider(name="Failsafe Sound Volume (in %)", category="Failsafe", subcategory="Failsafe Trigger Sound", description="The volume of the failsafe sound", min=0.0f, max=100.0f)
    public static float failsafeSoundVolume = 50.0f;
    @Switch(name="Max out Master category sounds while pinging", category="Failsafe", subcategory="Failsafe Trigger Sound", description="Maxes out the sounds while failsafe")
    public static boolean maxOutMinecraftSounds = false;
    @Button(name="", category="Failsafe", subcategory="Failsafe Trigger Sound", description="Plays the selected sound", text="Play")
    Runnable _playFailsafeSoundButton = () -> AudioManager.getInstance().playSound();
    @Button(name="", category="Failsafe", subcategory="Failsafe Trigger Sound", description="Stops playing the selected sound", text="Stop")
    Runnable _stopFailsafeSoundButton = () -> AudioManager.getInstance().resetSound();
    @Info(text="Will probaply get you banned", type=InfoType.WARNING, category="Experimental")
    public static boolean ignore;
    @KeyBind(name="Nuker", category="Experimental")
    public static OneKeyBind nuker_keyBind;
    @Switch(name="Nuker Toggle", category="Experimental")
    public static boolean nuker_toggle;
    @Switch(name="Full Blocks", category="Miscellaneous", description="Gives a full block hitbox to blocks without a full block hitbox")
    public static boolean miscFullBlock;

    public static int getRandomGuiWaitDelay() {
        return delaysGuiDelay + (int)(Math.random() * (double)delaysGuiDelayRandomizer);
    }

    public MightyMinerConfig() {
        super(new Mod("Mighty Miner", ModType.HYPIXEL), "/MightMinerV2/config.json");
        this.initialize();
        this.registerKeyBind(routeBuilder, RouteBuilder.getInstance()::toggle);
        this.registerKeyBind(toggleMacro, MacroManager.getInstance()::toggle);
        this.save();
    }

    static {
        nuker_keyBind = new OneKeyBind(new int[]{UKeyboard.KEY_COMMA});
        nuker_toggle = false;
        miscFullBlock = false;
    }
}

