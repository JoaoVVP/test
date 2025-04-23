/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cc.polyfrost.oneconfig.utils.commands.CommandManager
 *  com.google.common.reflect.TypeToken
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.opengl.Display
 */
package com.jelly.mightyminerv2;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jelly.mightyminerv2.command.OsamaTestCommandNobodyTouchPleaseLoveYou;
import com.jelly.mightyminerv2.command.RouteBuilderCommand;
import com.jelly.mightyminerv2.config.MightyMinerConfig;
import com.jelly.mightyminerv2.failsafe.FailsafeManager;
import com.jelly.mightyminerv2.feature.FeatureManager;
import com.jelly.mightyminerv2.handler.GameStateHandler;
import com.jelly.mightyminerv2.handler.GraphHandler;
import com.jelly.mightyminerv2.handler.RotationHandler;
import com.jelly.mightyminerv2.handler.RouteHandler;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.jelly.mightyminerv2.util.Logger;
import com.jelly.mightyminerv2.util.ReflectionUtils;
import com.jelly.mightyminerv2.util.ScoreboardUtil;
import com.jelly.mightyminerv2.util.TablistUtil;
import com.jelly.mightyminerv2.util.helper.AudioManager;
import com.jelly.mightyminerv2.util.helper.graph.Graph;
import com.jelly.mightyminerv2.util.helper.graph.GraphSerializer;
import com.jelly.mightyminerv2.util.helper.route.RouteWaypoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.opengl.Display;

@Mod(modid="mightyminerv2", useMetadata=true)
public class MightyMiner {
    public final String VERSION = "2.6.2";
    public static final String modid = "mightyminerv2";
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    public static final Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Graph<RouteWaypoint>>(){}.getType(), (Object)new GraphSerializer()).excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    public static MightyMinerConfig config;
    private static final Minecraft mc;
    private static final List<String> expectedRoutes;
    public static final Path routesDirectory;
    public static final Path routePath;
    @Mod.Instance
    public static MightyMiner instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File[] files;
        File routesDir = routesDirectory.toFile();
        if (!routesDir.exists()) {
            System.out.println("Routes directory not found, creating it now.");
            routesDir.mkdirs();
        }
        if ((files = routesDir.listFiles()) == null || files.length != expectedRoutes.size()) {
            Arrays.stream(files).forEach(it -> {
                try {
                    Files.deleteIfExists(it.toPath());
                }
                catch (Exception e) {
                    System.out.println("Failed to delete " + it);
                    e.printStackTrace();
                }
            });
            for (String file : expectedRoutes) {
                Path filePath = routesDir.toPath().resolve(file);
                try {
                    Files.copy(this.getClass().getResourceAsStream("/" + file), filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception e) {
                    System.out.println("Failed to copy " + file);
                    continue;
                }
                if (this.loadGraph(filePath)) continue;
                System.out.println("Filed to load graph " + file);
            }
            return;
        }
        for (File file : files) {
            if (!file.isFile() || !expectedRoutes.contains(file.getName()) || this.loadGraph(file.toPath())) continue;
            System.out.println("Couldn't load " + file.getName());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean loadGraph(Path path) {
        String graphKey = path.getFileName().toString().replace(".json", "");
        try (BufferedReader reader = Files.newBufferedReader(path);){
            Graph graph = (Graph)gson.fromJson((Reader)reader, new TypeToken<Graph<RouteWaypoint>>(){}.getType());
            GraphHandler.instance.graphs.put(graphKey, graph);
            System.out.println("Loaded graph for: " + graphKey);
            boolean bl = true;
            return bl;
        }
        catch (Exception e) {
            System.out.println("Something went wrong while loading the graph for: " + graphKey);
            e.printStackTrace();
            return false;
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.initializeFields();
        this.initializeListeners();
        this.initializeCommands();
        MightyMiner.mc.field_71474_y.field_74333_Y = 1000.0f;
        MightyMiner.mc.field_71474_y.field_82881_y = false;
        Display.setTitle((String)("Mighty Miner \u3014v2.6.2\u3015 " + (MightyMinerConfig.debugMode ? "wazzadev!" : "Chilling huh?") + " \u261b " + mc.func_110432_I().func_111285_a()));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (ReflectionUtils.hasPackageInstalled("feather")) {
            Logger.sendNotification("Mighty Miner", "Feather Client is not supported! Might cause issues or a lot of bugs!", 5000L);
            Logger.sendWarning("Feather Client is not supported! Might cause issues or a lot of bugs!");
        }
    }

    private void initializeFields() {
        config = new MightyMinerConfig();
    }

    private void initializeListeners() {
        MinecraftForge.EVENT_BUS.register((Object)GameStateHandler.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)RotationHandler.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)RouteHandler.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)GraphHandler.instance);
        MinecraftForge.EVENT_BUS.register((Object)MacroManager.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)FailsafeManager.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)AudioManager.getInstance());
        FeatureManager.getInstance().allFeatures.forEach(arg_0 -> ((EventBus)MinecraftForge.EVENT_BUS).register(arg_0));
        MinecraftForge.EVENT_BUS.register((Object)OsamaTestCommandNobodyTouchPleaseLoveYou.getInstance());
        MinecraftForge.EVENT_BUS.register((Object)new ScoreboardUtil());
        MinecraftForge.EVENT_BUS.register((Object)new TablistUtil());
    }

    private void initializeCommands() {
        CommandManager.register((Object)OsamaTestCommandNobodyTouchPleaseLoveYou.getInstance());
        CommandManager.register((Object)new RouteBuilderCommand());
    }

    public static Executor executor() {
        return executor;
    }

    static {
        mc = Minecraft.func_71410_x();
        expectedRoutes = Arrays.asList("Commission Macro.json");
        routesDirectory = Paths.get("./config/mightyminerv2/predefined", new String[0]);
        routePath = Paths.get("./config/mightyminerv2/mighty_miner_routes.json", new String[0]);
    }
}

