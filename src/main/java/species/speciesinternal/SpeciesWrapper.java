package species.speciesinternal;

import core.Florial;
import mysql.PlayerData;
import org.bukkit.entity.Player;
import species.Cat;
import species.Fox;
import species.Human;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SpeciesWrapper {

    private Florial plugin;
    // Define the different species that a player can be
    public static final int HUMAN = 0;
    public static final int CAT = 1;
    public static final int FOX = 2;

    public static final String[] SPECIES_NAMES = {
            "human",
            "cat",
            "fox"};

    public SpeciesWrapper(Florial plugin) {
        this.plugin = plugin;
    }

    // Method to set the species of a player
    public void setSpecies(UUID player, int species) throws SQLException {
        PlayerData data = Florial.getInstance().getDatabase().getPlayerStatsfromDatabase(player);
        data.setSpecies(species);

        plugin.getDatabase().updatePlayerStats(data);
    }

    // Method to get the species of a player
    public String getSpecies(UUID player, Boolean obj) throws SQLException {
        PlayerData data = Florial.getInstance().getDatabase().findPlayerStatsbyUUID(player.toString());
        Integer type =  data.getSpecies();
        if (obj == true) return type.toString();
        return SPECIES_NAMES[type];
    }

    public String getSpeciesName(Player p) {

        return null;
    }

    public Species getSpeciesObject(UUID player) throws SQLException {
        int species = Integer.valueOf(getSpecies(player, true));
        switch (species) {
            case FOX:
                return new Fox();
            case CAT:
                return new Cat();
            case HUMAN:
                return new Human();
        }
        return null;
    }
}
