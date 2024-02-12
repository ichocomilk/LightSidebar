package io.github.ichocomilk.lightsidebar.nms.v1_8R3;

import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

final class CustomScore extends ScoreboardScore {

    private final String playerName;
    private final int score;

    public CustomScore(Scoreboard scoreboard, ScoreboardObjective objective, String name, int score) {
        super(scoreboard, objective, "");
        this.playerName = name;
        this.score = score;
    }   

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getScore() {
        return score;
    }

}