package me.markings.bubble;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class PlayerData extends YamlConfig {

    private static final Map<UUID, PlayerData> playerData = new HashMap<>();

    private final String playerName;
    private final UUID uuid;

    private boolean broadcastStatus;
    private boolean broadcastSoundStatus;

    private boolean motdStatus;

    private boolean mentionsStatus;
    private boolean mentionSoundStatus;
    private boolean mentionToastStatus;

    private PlayerData(final String playerName, final UUID uuid) {
        this.playerName = playerName;
        this.uuid = uuid;

        this.loadConfiguration(NO_DEFAULT, "players/" + uuid + ".yml");
        this.save();
    }

    @Override
    protected void onLoad() {
        broadcastStatus = getBoolean("Receive_Broadcasts", true);
        broadcastSoundStatus = getBoolean("Receive_Broadcast_Sound", true);

        motdStatus = getBoolean("Receive_MOTD", true);

        mentionsStatus = getBoolean("Receive_Mentions", true);
        mentionSoundStatus = getBoolean("Receive_Mention_Sound", true);
        mentionToastStatus = getBoolean("Receive_Mentions_Toast", true);
    }

    @Override
    protected void onSave() {
        super.onSave();
    }

    @Override
    public SerializedMap saveToMap() {
        final SerializedMap map = new SerializedMap();

        map.putIfExist("Receive_Broadcasts", this.broadcastStatus);
        map.putIfExist("Receive_Broadcast_Sound", this.broadcastSoundStatus);
        map.putIfExist("Receive_MOTD", this.motdStatus);
        map.putIfExist("Receive_Mentions", this.mentionsStatus);
        map.putIfExist("Receive_Mention_Sound", this.mentionSoundStatus);
        map.putIfExist("Receive_Mentions_Toast", this.mentionToastStatus);

        return map;
    }

    public void setBroadcastStatus(final boolean broadcastStatus) {
        this.broadcastStatus = broadcastStatus;

        save();
    }

    public void setBroadcastSoundStatus(final boolean broadcastSoundStatus) {
        this.broadcastSoundStatus = broadcastSoundStatus;

        save();
    }

    public void setMotdStatus(final boolean motdStatus) {
        this.motdStatus = motdStatus;

        save();
    }

    public void setMentionsStatus(final boolean mentionsStatus) {
        this.mentionsStatus = mentionsStatus;

        save();
    }

    public void setMentionSoundStatus(final boolean mentionSoundStatus) {
        this.mentionSoundStatus = mentionSoundStatus;

        save();
    }

    public void setMentionToastStatus(final boolean mentionToastStatus) {
        this.mentionToastStatus = mentionToastStatus;

        save();
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof PlayerData && ((PlayerData) obj).uuid.equals(this.uuid);
    }

    /* ------------------------------------------------------------------------------- */
	/* Misc methods
	/* ------------------------------------------------------------------------------- */

    @Nullable
    public Player toPlayer() {
        final Player player = Remain.getPlayerByUUID(this.uuid);

        return player != null && player.isOnline() ? player : null;
    }

    // --------------------------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------------------------

    public static PlayerData from(final UUID uuid) {
        return from(Remain.getPlayerByUUID(uuid));
    }

    public static PlayerData from(final Player player) {
        final UUID uuid = player.getUniqueId();
        PlayerData data = playerData.get(uuid);

        if (data == null) {
            data = new PlayerData(player.getName(), uuid);

            playerData.put(uuid, data);
        }

        return data;
    }

    public static PlayerData fromPlayerFile(final File file) {
        return fromPlayerFile(UUID.fromString(file.getName().replace(".yml", "")));
    }

    public static PlayerData fromPlayerFile(final UUID uuid) {
        PlayerData data = playerData.get(uuid);

        if (data == null)
            data = new PlayerData(null, uuid);

        return data;
    }

    public static void remove(final Player player) {
        playerData.remove(player.getUniqueId());
    }
}
