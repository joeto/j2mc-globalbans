package to.joe.j2mc.globalbans;

import org.bukkit.entity.Player;

public interface BanSystem {
    public void playerJoin(Player player);

    public void playerQuit(Player player);
}
