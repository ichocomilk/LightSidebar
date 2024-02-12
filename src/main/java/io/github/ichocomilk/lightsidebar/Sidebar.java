package io.github.ichocomilk.lightsidebar;

import java.util.Collection;

import org.bukkit.entity.Player;

public interface Sidebar {

    void sendLines(Collection<Player> players);
    void sendLines(Player player);   

    void sendTitle(Collection<Player> players);
    void sendTitle(Player player);

    void delete(Collection<Player> players);
    void delete(Player player);

    void setTitle(String title);
    void setLines(Object[] lines);

    Object[] createLines(String[] lines);
}