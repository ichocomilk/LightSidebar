package io.github.ichocomilk.lightsidebar.nms.v1_8R3;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;

final class CustomObjective extends ScoreboardObjective {
  
    private String title = "Default title";

    public CustomObjective(Scoreboard serverScoreboard) {
        super(serverScoreboard, "sidebar", IScoreboardCriteria.b);
    }
    
    public String getDisplayName() {
        return title;
    }

    @Override
    public void setDisplayName(String title) {
        this.title = title;
    }
}