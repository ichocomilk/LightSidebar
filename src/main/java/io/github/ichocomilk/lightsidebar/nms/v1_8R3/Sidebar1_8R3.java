package io.github.ichocomilk.lightsidebar.nms.v1_8R3;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.Sidebar;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;

public final class Sidebar1_8R3 implements Sidebar {

    private static final Scoreboard SCOREBARD = MinecraftServer.getServer().getWorld().scoreboard;

    private static final PacketPlayOutScoreboardObjective CREATE, DELETE;
    private static final PacketPlayOutScoreboardDisplayObjective DISPLAY;

    private PacketPlayOutScoreboardScore[] lines;
    private final CustomObjective objective = new CustomObjective(SCOREBARD);

    static {
        if (SCOREBARD.getObjective("sidebar") == null) {
            SCOREBARD.registerObjective("sidebar", IScoreboardCriteria.b);
        }
        final CustomObjective OBJECTIVE = new CustomObjective(SCOREBARD);
        SCOREBARD.setDisplaySlot(1, OBJECTIVE);
        CREATE = new PacketPlayOutScoreboardObjective(OBJECTIVE, 0);
        DELETE = new PacketPlayOutScoreboardObjective(OBJECTIVE, 1);
        DISPLAY = new PacketPlayOutScoreboardDisplayObjective(1, OBJECTIVE);
    }

    @Override
    public void sendLines(Collection<Player> players) {
        for (final Player player : players) {
            final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
            entityPlayer.playerConnection.sendPacket(DELETE);
            entityPlayer.playerConnection.sendPacket(CREATE);
            entityPlayer.playerConnection.sendPacket(DISPLAY);
            for (final PacketPlayOutScoreboardScore line : lines) {
                entityPlayer.playerConnection.sendPacket(line);
            }
        }
    }

    @Override
    public void sendLines(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        entityPlayer.playerConnection.sendPacket(DELETE);
        entityPlayer.playerConnection.sendPacket(CREATE);
        entityPlayer.playerConnection.sendPacket(DISPLAY);
        for (final PacketPlayOutScoreboardScore line : lines) {
            entityPlayer.playerConnection.sendPacket(line);
        }
    }

    @Override
    public void sendTitle(Collection<Player> players) {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective, 2);
        for (final Player player : players) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void sendTitle(Player player) {
        final PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(objective, 2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void delete(Collection<Player> players) {
        for (final Player player : players) {
            final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
            entityPlayer.playerConnection.sendPacket(DELETE);
        }
    }

    @Override
    public void delete(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        entityPlayer.playerConnection.sendPacket(DELETE);
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