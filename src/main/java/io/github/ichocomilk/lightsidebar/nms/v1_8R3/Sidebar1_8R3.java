package io.github.ichocomilk.lightsidebar.nms.v1_8R3;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.Sidebar;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;

public final class Sidebar1_8R3 implements Sidebar {

    private static final Scoreboard SCOREBARD = MinecraftServer.getServer().getWorld().scoreboard;

    private PacketPlayOutScoreboardScore[] lines;
    private final CustomObjective objective = new CustomObjective(SCOREBARD);

    static {
        if (SCOREBARD.getObjective("sidebar") == null) {
            SCOREBARD.registerObjective("sidebar", IScoreboardCriteria.b);
        }
        final CustomObjective OBJECTIVE = new CustomObjective(SCOREBARD);
        SCOREBARD.setDisplaySlot(1, OBJECTIVE);
    }

    @Override
    public void sendLines(Collection<Player> players) {
        final PacketPlayOutScoreboardObjective delete = new PacketPlayOutScoreboardObjective(objective, 1);
        final PacketPlayOutScoreboardObjective create = new PacketPlayOutScoreboardObjective(objective, 0);
        final PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, objective);

        for (final Player player : players) {
            final NetworkManager connection = ((CraftPlayer)player).getHandle().playerConnection.networkManager;
            connection.handle(delete);
            connection.handle(create);
            connection.handle(display);
            for (final PacketPlayOutScoreboardScore line : lines) {
                connection.handle(line);
            }
        }
    }

    @Override
    public void sendLines(Player player) {
        final NetworkManager connection = ((CraftPlayer)player).getHandle().playerConnection.networkManager;
        connection.handle(new PacketPlayOutScoreboardObjective(objective, 1));
        connection.handle(new PacketPlayOutScoreboardObjective(objective, 0));
        connection.handle(new PacketPlayOutScoreboardDisplayObjective(1, objective));

        for (final PacketPlayOutScoreboardScore line : lines) {
            connection.handle(line);
        }
    }

    @Override
    public void sendTitle(Collection<Player> players) {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective, 2);
        for (final Player player : players) {
            ((CraftPlayer)player).getHandle().playerConnection.networkManager.handle(packet);
        }
    }

    @Override
    public void sendTitle(Player player) {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective, 2);
        ((CraftPlayer)player).getHandle().playerConnection.networkManager.handle(packet);
    }

    @Override
    public void delete(Collection<Player> players) {
        final PacketPlayOutScoreboardObjective delete = new PacketPlayOutScoreboardObjective(objective, 1);
        for (final Player player : players) {
            ((CraftPlayer)player).getHandle().playerConnection.networkManager.handle(delete);
        }
    }

    @Override
    public void delete(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        entityPlayer.playerConnection.networkManager.handle(new PacketPlayOutScoreboardObjective(objective, 1));
    }

    @Override
    public void setTitle(String title) {
        objective.setDisplayName(title);
    }

    @Override
    public void setLines(Object[] lines) {
        if (lines instanceof PacketPlayOutScoreboardScore[] newLines) {
            this.lines = newLines;
        }
    }

    @Override
    public Object[] createLines(String[] lines) {
        int blankSpaces = 0;
        final PacketPlayOutScoreboardScore[] packetLines = new PacketPlayOutScoreboardScore[lines.length];
        for (int i = 0; i < lines.length; i++) {
            final String line = (lines[i] == null || lines[i].isEmpty())
                ? StringUtils.repeat(' ', blankSpaces++)
                : lines[i];

            packetLines[i] = new PacketPlayOutScoreboardScore(new CustomScore(SCOREBARD, objective, line, lines.length - i));
        }
        return packetLines;
    }
}