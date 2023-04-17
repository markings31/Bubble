package me.markings.bubble.settings;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public final class Broadcast extends YamlConfig {

    private static final ConfigItems<Broadcast> broadcasts = ConfigItems.fromFolder("broadcasts", Broadcast.class);

    @Getter
    private final String broadcastName;

    private String broadcastHeader;

    private String broadcastFooter;

    private SimpleSound sound;

    private List<String> message;
    private String permission;
    private Boolean centered;
    private List<String> worlds;

    @SuppressWarnings("unused")
    private Broadcast(final String broadcastName) {
        this(broadcastName, null, null, null, null, null, null, null);
    }

    private Broadcast(
            final String broadcastName,
            @Nullable final String broadcastHeader,
            @Nullable final String broadcastFooter,
            @Nullable final SimpleSound sound,
            @Nullable final List<String> message,
            @Nullable final String permission,
            @Nullable final Boolean centered,
            @Nullable final List<String> worlds
    ) {

        this.broadcastHeader = broadcastHeader;
        this.broadcastFooter = broadcastFooter;
        this.sound = sound;
        this.broadcastName = broadcastName;
        this.message = message;
        this.permission = permission;
        this.centered = centered;
        this.worlds = worlds;

        this.setHeader(
                Common.configLine(),
                "Broadcast Settings",
                Common.configLine(),
                "\n"
        );

        this.loadConfiguration(NO_DEFAULT, "broadcasts/" + broadcastName + ".yml");
        this.save();
    }

    @Override
    protected void onLoad() {
        // Only load if not created from a command.
        if (this.message != null && this.permission != null && this.centered != null && this.worlds != null) {
            this.save();

            return;
        }

        this.broadcastHeader = this.getString("Header", "&8{chat_line_smooth}");
        this.broadcastFooter = this.getString("Footer", "&8{chat_line_smooth}");
        this.sound = this.getSound("Sound", new SimpleSound(CompSound.NOTE_PLING.getSound(), 1F, 1.5F));
        this.permission = getString("Permission", "bubble.example");
        this.centered = getBoolean("Centered", false);
        this.message = getStringList("Message");
        this.worlds = getStringList("Worlds");
    }

    @Override
    protected void onSave() {
        super.onSave();
    }

    @Override
    public SerializedMap saveToMap() {
        final SerializedMap map = new SerializedMap();

        map.put("Header", this.broadcastHeader);
        map.put("Footer", this.broadcastFooter);
        map.put("Sound", this.sound);
        map.putIfExist("Permission", this.permission);
        map.putIfExist("Centered", this.centered);
        map.putIfExist("Message", this.message);
        map.putIfExist("Worlds", this.worlds);

        return map;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Broadcast && ((Broadcast) obj).getBroadcastName().equals(this.getBroadcastName());
    }

    public void setMessage(final List<String> message) {
        this.message = message;

        this.save();
    }

    public void setCentered(final Boolean centered) {
        this.centered = centered;

        this.save();
    }

    public void setPermission(final String permission) {
        this.permission = permission;

        this.save();
    }

    public void setWorlds(final List<String> worlds) {
        this.worlds = worlds;

        this.save();
    }

    public void setSound(final CompSound sound) {
        this.sound = new SimpleSound(sound.getSound(), 1F, 1.5F);

        this.save();
    }

    public void setHeader(final String header) {
        this.broadcastHeader = header;

        this.save();
    }

    public void setFooter(final String footer) {
        this.broadcastFooter = footer;

        this.save();
    }

    public void addWorld(final String world) {
        this.worlds.add(world);

        this.save();
    }

    public void removeWorld(final String world) {
        this.worlds.remove(world);

        this.save();
    }

    private boolean doesNotExist() {
        final Broadcast broadcast = broadcasts.findItem(broadcastName);
        if (broadcast == null) {
            createBroadcast(
                    broadcastName,
                    "&8{chat_line_smooth}",
                    "&8{chat_line_smooth}",
                    new SimpleSound(CompSound.NOTE_PLING.getSound(), 1F, 1.5F),
                    Collections.singletonList(""),
                    "bubble.example",
                    false,
                    Collections.singletonList("world"));

            return true;
        }

        return false;
    }

    public void addLineToBroadcast(final String message) {
        if (doesNotExist())
            return;

        this.message.add(message);
        this.save();
    }

    public void toggleCentered() {
        final Broadcast broadcast = broadcasts.findItem(broadcastName);
        if (doesNotExist())
            return;

        if (broadcast.getCentered() == null) {
            broadcast.setCentered(false);
        }

        broadcast.setCentered(!broadcast.getCentered());
        this.save();
    }

    public static void loadBroadcasts() {
        broadcasts.loadItems();
    }

    // ---------------------------------------------------------------------------------------------------------------------
    // Static Methods
    // ---------------------------------------------------------------------------------------------------------------------

    public static void createBroadcast(final String broadcastName) {
        createBroadcast(broadcastName, null, null, null, null, null, null, null);
    }

    public static void createBroadcast(
            @NonNull final String broadcastName,
            final String header,
            final String footer,
            final SimpleSound sound,
            final List<String> messages,
            final String permissions,
            final Boolean centered,
            final List<String> worlds) {
        broadcasts.loadOrCreateItem(broadcastName, () -> new Broadcast(broadcastName, header, footer, sound, messages, permissions, centered, worlds));
    }

    public static void removeBroadcast(@NonNull final Broadcast broadcast) {
        broadcasts.removeItem(broadcast);
    }

    public static void toggleCenteredAll() {
        for (final Broadcast broadcast : broadcasts.getItems()) {
            broadcast.setCentered(Boolean.FALSE.equals(broadcast.getCentered()));
        }
    }

    public static List<Broadcast> getAllBroadcasts() {
        return broadcasts.getItems();
    }

    public static Set<String> getAllBroadcastNames() {
        return broadcasts.getItemNames();
    }

    public static Broadcast getBroadcast(@NonNull final String broadcastName) {
        return broadcasts.findItem(broadcastName);
    }

    public static List<List<String>> getAllMessages() {
        final List<List<String>> allMessages = new ArrayList<>();
        broadcasts.getItems().forEach(broadcast -> allMessages.add(broadcast.getMessage()));

        return allMessages;
    }

    public static List<String> getAllWorlds() {
        final List<String> allWorlds = new ArrayList<>();
        for (final Broadcast broadcast : broadcasts.getItems()) {
            assert broadcast.getWorlds() != null;
            for (final String world : broadcast.getWorlds())
                if (!allWorlds.contains(world))
                    allWorlds.add(world);
        }

        return allWorlds;
    }


    public static List<String> getAllPermissions() {
        final List<String> allPermissions = new ArrayList<>();
        broadcasts.getItems().forEach(broadcast -> allPermissions.add(broadcast.getPermission()));

        return allPermissions;
    }

    public static Broadcast getBroadcastFromMessage(final List<String> message) {
        for (final Broadcast broadcast : broadcasts.getItems()) {
            if (broadcast.getMessage().equals(message)) {
                return broadcast;
            }
        }
        return null;
    }

    @NonNull
    public static String getPermissionFromMessage(@NonNull final List<String> messages) {
        for (final Broadcast broadcast : getAllBroadcasts()) {
            assert broadcast.getMessage() != null;
            if (broadcast.getMessage().equals(messages))
                return broadcast.getPermission();
        }

        return "";
    }

    public static Boolean getCenteredFromMessage(@NonNull final List<String> messages) {
        for (final Broadcast broadcast : getAllBroadcasts()) {
            assert broadcast.getMessage() != null;
            if (broadcast.getMessage().equals(messages))
                return broadcast.getCentered();
        }

        return null;
    }
}

