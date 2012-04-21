package to.joe.j2mc.globalbans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class J2MC_GlobalBans extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        
    }
    
    public void schedule(Runnable run){
        getServer().getScheduler().scheduleAsyncDelayedTask(this, run);
    }
    
    protected String APICall(String url, HashMap<String, String> POSTData) {
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
    
}
