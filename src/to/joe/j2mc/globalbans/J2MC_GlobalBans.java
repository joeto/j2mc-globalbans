package to.joe.j2mc.globalbans;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.globalbans.FishHaul.Service;

public class J2MC_GlobalBans extends JavaPlugin implements Listener {
    private BanFisher banFisher;
    private ConcurrentHashMap<String, FishHaul> banCache;
    public static final int threatAmount = 3;

    /**
     * Returns the number of bans a user has.
     * <b>If the user hasn't (re)joined in the past 60 minutes this method will return 0.</b>
     * 
     * @param username
     * @return
     */
    public int getBans(String username) {
        if (!banCache.containsKey(username)) {
            return 0;
        }
        int bans = 0;
        for (Entry<String, Service> entry : banCache.get(username).bans.services.entrySet()) {
            bans += entry.getValue().bans;
        }
        return bans;
    }

    @Override
    public void onEnable() {
        this.banFisher = new BanFisher(this);
        this.banCache = new ConcurrentHashMap<String, FishHaul>();

        this.getServer().getPluginManager().registerEvents(this, this);

        final Player[] online = this.getServer().getOnlinePlayers();
        this.getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : online) {
                    FishHaul haul = banFisher.goFishing(player.getName());
                    banCache.put(player.getName(), haul);
                    if (getBans(player.getName()) >= threatAmount) {
                        J2MC_Manager.getPermissions().addFlag(player, 'B');
                    }
                }
            }
        });
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String player = event.getName();
        if (!banCache.containsKey(player)) {
            FishHaul haul = banFisher.goFishing(player);
            banCache.put(player, haul);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        if (!banCache.containsKey(name)) {
            this.getLogger().severe("Oops. Couldn't find bans for " + name);
            return;
        }
        if (this.getBans(name) >= threatAmount) {
            J2MC_Manager.getPermissions().addFlag(event.getPlayer(), 'B');
        }
    }

}
