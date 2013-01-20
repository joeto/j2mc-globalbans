package to.joe.j2mc.globalbans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;

public class BanFisher {
    private final J2MC_GlobalBans plugin;

    public BanFisher(J2MC_GlobalBans plugin) {
        this.plugin = plugin;
    }

    public FishHaul goFishing(String username) {
        try {
            final URL url = new URL("http://www.fishbans.com/api/bans/" + username + "/force");
            final URLConnection connection = url.openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-agent", "joeto");
            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder json = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                json.append(line);
            }
            in.close();
            return new Gson().fromJson(json.toString(), FishHaul.class);
        } catch (final Exception e) {
            this.plugin.getLogger().severe("Oops. Failed to get ban information for \"" + username + "\":");
            e.printStackTrace();
            return null;
        }
    }

}
