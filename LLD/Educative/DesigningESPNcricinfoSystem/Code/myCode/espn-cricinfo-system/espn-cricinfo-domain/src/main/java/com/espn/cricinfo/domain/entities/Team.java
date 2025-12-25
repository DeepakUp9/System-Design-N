package com.espn.cricinfo.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Entity representing a cricket team.
 */
@Data
@Builder
public class Team {
    private Long id;
    private String name;
    private String shortName;
    private String country;
    private List<Player> players;
    private Player captain;
    private Player wicketKeeper;

    public boolean hasPlayer(Player player) {
        return players != null && players.contains(player);
    }
}