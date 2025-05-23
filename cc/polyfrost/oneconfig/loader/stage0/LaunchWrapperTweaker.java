/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.launchwrapper.ITweaker
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraft.launchwrapper.LaunchClassLoader
 *  net.minecraftforge.common.ForgeVersion
 */
package cc.polyfrost.oneconfig.loader.stage0;

import cc.polyfrost.oneconfig.loader.stage0.OneConfigWrapperBase;
import com.google.gson.JsonObject;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.ForgeVersion;

public class LaunchWrapperTweaker
extends OneConfigWrapperBase
implements ITweaker {
    private static ITweaker loader = null;

    @Override
    protected boolean shouldSSLStore() {
        return true;
    }

    @Override
    protected OneConfigWrapperBase.LoaderInfo provideLoaderInfo() {
        String mcVersion = "1.8.9";
        try {
            mcVersion = (String)ForgeVersion.class.getDeclaredField("mcVersion").get(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting the Minecraft version failed, defaulting to 1.8.9. Please report this to https://inv.wtf/polyfrost");
        }
        return new OneConfigWrapperBase.LoaderInfo("Loader", mcVersion, "forge", "launchwrapper");
    }

    @Override
    protected File provideFile(OneConfigWrapperBase.LoaderInfo loaderInfo) {
        File oneConfigDir = new File(new File("./OneConfig"), loaderInfo.platformName);
        if (!oneConfigDir.exists() && !oneConfigDir.mkdirs()) {
            throw new IllegalStateException("Could not create OneConfig dir!");
        }
        return new File(oneConfigDir, "OneConfig-Loader.jar");
    }

    @Override
    protected OneConfigWrapperBase.JsonInfo provideJsonInfo(JsonObject object, OneConfigWrapperBase.LoaderInfo loaderInfo) {
        try {
            if (object.has("loader") && object.getAsJsonObject("loader").has("url") && object.getAsJsonObject("loader").has("sha256")) {
                String checksum = object.getAsJsonObject("loader").get("sha256").getAsString();
                String downloadUrl = object.getAsJsonObject("loader").get("url").getAsString();
                return new OneConfigWrapperBase.JsonInfo(checksum, downloadUrl, true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new OneConfigWrapperBase.JsonInfo(null, null, false);
    }

    @Override
    protected boolean isInitialized(File file) {
        try {
            URL url = file.toURI().toURL();
            return Arrays.asList(((URLClassLoader)Launch.classLoader.getClass().getClassLoader()).getURLs()).contains(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean getNextInstance() {
        try {
            loader = (ITweaker)Launch.classLoader.findClass("cc.polyfrost.oneconfig.loader.OneConfigLoader").newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return loader != null;
    }

    @Override
    protected void addToClasspath(File file) {
        try {
            URL url = file.toURI().toURL();
            Launch.classLoader.addURL(url);
            ClassLoader classLoader = Launch.classLoader.getClass().getClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        if (loader != null) {
            loader.acceptOptions(args, gameDir, assetsDir, profile);
        }
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        if (loader != null) {
            loader.injectIntoClassLoader(classLoader);
        }
    }

    public String getLaunchTarget() {
        return loader != null ? loader.getLaunchTarget() : null;
    }

    public String[] getLaunchArguments() {
        return loader != null ? loader.getLaunchArguments() : new String[]{};
    }
}

