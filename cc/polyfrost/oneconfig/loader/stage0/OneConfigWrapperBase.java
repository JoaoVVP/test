/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package cc.polyfrost.oneconfig.loader.stage0;

import cc.polyfrost.oneconfig.loader.stage0.ssl.SSLStore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public abstract class OneConfigWrapperBase {
    public static final Color GRAY_900 = new Color(13, 14, 15, 255);
    public static final Color GRAY_700 = new Color(34, 35, 38);
    public static final Color PRIMARY_500 = new Color(26, 103, 255);
    public static final Color PRIMARY_500_80 = new Color(26, 103, 204);
    public static final Color WHITE_80 = new Color(255, 255, 255, 204);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public OneConfigWrapperBase() {
        try {
            if (this.shouldSSLStore()) {
                this.addSSLStore();
            }
            LoaderInfo loaderInfo = this.provideLoaderInfo();
            System.out.println("OneConfig has detected the version " + loaderInfo.mcVersion + ". If this is false, report this at https://inv.wtf/polyfrost");
            File oneconfigFile = this.provideFile(loaderInfo);
            if (!this.isInitialized(oneconfigFile) && this.shouldUpdate()) {
                JsonElement json = OneConfigWrapperBase.getRequest("https://api.polyfrost.org/oneconfig/" + loaderInfo.mcVersion + "-" + loaderInfo.modLoader);
                if (json != null && json.isJsonObject()) {
                    JsonObject jsonObject = json.getAsJsonObject();
                    JsonInfo jsonInfo = this.provideJsonInfo(jsonObject, loaderInfo);
                    if (!(!jsonInfo.success || oneconfigFile.exists() && jsonInfo.checksum.equals(this.getChecksum(oneconfigFile)))) {
                        System.out.println("Updating OneConfig " + loaderInfo.stageLoading + "...");
                        File newLoaderFile = new File(oneconfigFile.getParentFile(), oneconfigFile.getName().substring(0, oneconfigFile.getName().lastIndexOf(".")) + "-NEW.jar");
                        this.downloadFile(jsonInfo.downloadUrl, newLoaderFile);
                        if (newLoaderFile.exists() && jsonInfo.checksum.equals(this.getChecksum(newLoaderFile))) {
                            try {
                                Files.move(newLoaderFile.toPath(), oneconfigFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Updated OneConfig " + loaderInfo.stageLoading + "!");
                            }
                            catch (IOException iOException) {}
                        } else {
                            if (newLoaderFile.exists()) {
                                newLoaderFile.delete();
                            }
                            System.out.println("Failed to update OneConfig " + loaderInfo.stageLoading + ", trying to continue...");
                        }
                    }
                }
                if (!oneconfigFile.exists()) {
                    this.showErrorScreen();
                }
                this.addToClasspath(oneconfigFile);
            }
            if (!this.getNextInstance()) {
                this.showErrorScreen();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.showErrorScreen();
        }
    }

    protected boolean shouldSSLStore() {
        return false;
    }

    protected abstract LoaderInfo provideLoaderInfo();

    protected abstract File provideFile(LoaderInfo var1);

    protected boolean shouldUpdate() {
        return true;
    }

    protected abstract JsonInfo provideJsonInfo(JsonObject var1, LoaderInfo var2);

    protected abstract void addToClasspath(File var1);

    protected abstract boolean isInitialized(File var1);

    protected abstract boolean getNextInstance();

    protected void downloadFile(String url, File location) {
        try {
            URLConnection con = new URL(url).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OneConfigWrapper");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            InputStream in = con.getInputStream();
            Files.copy(in, location.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static JsonElement getRequest(String site) {
        try {
            String inputLine;
            URL url = new URL(site);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OneConfigWrapper");
            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            int status = con.getResponseCode();
            if (status != 200) {
                System.out.println("API request failed, status code " + status);
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            JsonParser parser = new JsonParser();
            return parser.parse(content.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected String getChecksum(File file) {
        try (FileInputStream in = new FileInputStream(file);){
            int count;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[1024];
            while ((count = in.read(buffer)) != -1) {
                digest.update(buffer, 0, count);
            }
            byte[] digested = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digested) {
                sb.append(Integer.toString((b & 0xFF) + 256, 16).substring(1));
            }
            String string = sb.toString();
            return string;
        }
        catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void showErrorScreen() {
        this.showErrorScreen("OneConfig has failed to download!", "OneConfig has failed to download!\nPlease join our discord server at https://polyfrost.org/discord\nfor support, or try again later.");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void showErrorScreen(String title, String message) {
        try {
            ImageIcon icon = null;
            try {
                icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/assets/oneconfig-loader/oneconfig-icon.png")));
            }
            catch (Exception exception) {
                // empty catch block
            }
            UIManager.put("OptionPane.background", GRAY_900);
            UIManager.put("Panel.background", GRAY_900);
            UIManager.put("OptionPane.messageForeground", WHITE_80);
            UIManager.put("Button.background", PRIMARY_500);
            UIManager.put("Button.select", PRIMARY_500_80);
            UIManager.put("Button.foreground", WHITE_80);
            UIManager.put("Button.focus", TRANSPARENT);
            int response = JOptionPane.showOptionDialog(null, message, title, 2, 0, icon, new Object[]{"Join Discord", "Close"}, "Join Discord");
            if (response == 0 && !this.browse(new URI("https://polyfrost.org/discord"))) {
                System.out.println("Failed to open browser.");
            }
        }
        catch (Exception e) {
            try {
                Method exit = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
                exit.setAccessible(true);
                exit.invoke(null, 1);
                return;
            }
            catch (Exception e2) {
                System.exit(1);
                return;
            }
        }
        catch (Throwable throwable) {
            try {
                Method exit = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
                exit.setAccessible(true);
                exit.invoke(null, 1);
                throw throwable;
            }
            catch (Exception e) {
                System.exit(1);
            }
            throw throwable;
        }
        try {
            Method exit = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
            exit.setAccessible(true);
            exit.invoke(null, 1);
            return;
        }
        catch (Exception e) {
            System.exit(1);
            return;
        }
    }

    private boolean browse(URI uri) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(uri);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void addSSLStore() {
        try {
            SSLStore sslStore = new SSLStore();
            System.out.println("Attempting to load Polyfrost certificate.");
            sslStore = sslStore.load("/ssl/polyfrost.der");
            SSLContext context = sslStore.finish();
            SSLContext.setDefault(context);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add Polyfrost certificate to keystore.");
        }
    }

    protected static class JsonInfo {
        public String checksum;
        public String downloadUrl;
        public boolean success;

        public JsonInfo(String checksum, String downloadUrl, boolean success) {
            this.checksum = checksum;
            this.downloadUrl = downloadUrl;
            this.success = success;
        }
    }

    protected static class LoaderInfo {
        public String stageLoading;
        public String mcVersion;
        public String modLoader;
        public String platformName;

        public LoaderInfo(String stageLoading, String mcVersion, String modLoader, String platformName) {
            this.stageLoading = stageLoading;
            this.mcVersion = mcVersion;
            this.modLoader = modLoader;
            this.platformName = platformName;
        }
    }
}

