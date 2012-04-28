package to.joe.j2mc.globalbans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.globalbans.mcbans.MCBans;

public class J2MC_GlobalBans extends JavaPlugin implements Listener {

    public static String APICall(String url, HashMap<String, String> POSTData) {
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final Map.Entry<String, String> entry : POSTData.entrySet()) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            final String POSTstring = stringBuilder.toString();
            final URL urlTarget = new URL(url.replace(" ", "%20"));
            final URLConnection connection = urlTarget.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-agent", "joeto");
            final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(POSTstring);
            writer.flush();
            final StringBuilder input = new StringBuilder();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                input.append(line);
            }
            writer.close();
            reader.close();
            return input.toString();
        } catch (final Exception e) {
            return "";
        }
    }

    private HashSet<BanSystem> systems;

    @Override
    public void onEnable() {
        this.systems = new HashSet<BanSystem>();
        this.getServer().getPluginManager().registerEvents(this, this);
        final String mcbansapi = this.getConfig().getString("mcbans.api");
        if ((mcbansapi != null) && (mcbansapi.length() > 1)) {
            this.systems.add(new MCBans(this, mcbansapi));
            this.getLogger().info("Hooking mcbans");
        } else {
            this.getLogger().info("Not hooking mcbans");
        }
    }

    public void onJoin(PlayerJoinEvent event) {
        for (final BanSystem system : this.systems) {
            system.playerJoin(event.getPlayer());
        }
    }

    public void onQuit(PlayerQuitEvent event) {
        for (final BanSystem system : this.systems) {
            system.playerQuit(event.getPlayer());
        }
    }

}
