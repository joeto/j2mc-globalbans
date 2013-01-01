package to.joe.j2mc.globalbans.mcbans;

import java.util.HashMap;

import org.bukkit.entity.Player;

import to.joe.j2mc.globalbans.BanSystem;
import to.joe.j2mc.globalbans.J2MC_GlobalBans;

public class MCBans implements BanSystem {
    private final J2MC_GlobalBans plugin;
    private static final String mcbans = "http://72.10.39.172/v2/";
    private static String url;

    private static void APICall(HashMap<String, String> POSTData) {
        J2MC_GlobalBans.APICall(MCBans.url, POSTData);
    }

    public MCBans(J2MC_GlobalBans plugin, String api) {
        this.plugin = plugin;
        MCBans.url = MCBans.mcbans + api;
        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {
                MCBans.this.callBack();
            }

        }, 600, 12000);
    }

    @Override
    public void playerJoin(Player player) {
        final HashMap<String, String> POSTData = new HashMap<String, String>();
        POSTData.put("player", player.getName());
        POSTData.put("playerip", player.getAddress().getAddress().getHostAddress());
        POSTData.put("exec", "playerConnect");
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                MCBans.APICall(POSTData);
            }
        });
    }

    @Override
    public void playerQuit(Player player) {
        final HashMap<String, String> POSTData = new HashMap<String, String>();
        POSTData.put("player", player.getName());
        POSTData.put("exec", "playerDisconnect");
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                MCBans.APICall(POSTData);
            }
        });
    }

    private void callBack() {
        final HashMap<String, String> POSTData = new HashMap<String, String>();
        POSTData.put("maxPlayers", String.valueOf(this.plugin.getServer().getMaxPlayers()));
        final StringBuilder playerList = new StringBuilder();
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (playerList.length() != 0) {
                playerList.append(",");
            }
            playerList.append(player.getName());
        }
        POSTData.put("playerList", playerList.toString());
        POSTData.put("version", "1.3.3.7");
        POSTData.put("exec", "callBack");
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                MCBans.APICall(POSTData);
            }
        });
    }

}
