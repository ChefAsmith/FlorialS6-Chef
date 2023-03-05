package net.florial.models;

import dev.morphia.ReplaceOptions;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Data;
import net.florial.database.FlorialDatabase;
import net.florial.utils.GeneralUtils;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.bukkit.scheduler.BukkitRunnable;

@Data
@Entity("discorduser")
public class DiscordUser {
    @Id
    private ObjectId id;

    private String uuid;
    private int exp;
    private int level;
    private int coins;

    @BsonIgnore
    public void save(boolean async) {
        if (async) {
            GeneralUtils.runAsync(new BukkitRunnable() {
                @Override
                public void run() {
                    FlorialDatabase.getDatastore().replace(this, new ReplaceOptions().upsert(true));
                }
            });
        }
        else FlorialDatabase.getDatastore().replace(this, new ReplaceOptions().upsert(true));
    }
}
