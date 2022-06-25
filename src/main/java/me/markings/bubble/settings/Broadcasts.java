package me.markings.bubble.settings;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Broadcasts extends YamlConfig {

	private static final ConfigItems<Broadcasts> broadcasts = ConfigItems.fromFolder("broadcasts", Broadcasts.class);

	@Getter
	private final String broadcastName;

	private List<String> messages;
	private String permissions;
	private Boolean centered;
	private List<String> worlds;

	@SuppressWarnings("unused")
	private Broadcasts(final String broadcastName) {
		this(broadcastName, null, null, null, null);
	}

	private Broadcasts(final String broadcastName, @Nullable final List<String> messages, @Nullable final String permissions, @Nullable final Boolean centered, @Nullable final List<String> worlds) {
		this.broadcastName = broadcastName;
		this.messages = messages;
		this.permissions = permissions;
		this.centered = centered;
		this.worlds = worlds;

		this.loadConfiguration("broadcasts/example-message.yml", "broadcasts/" + broadcastName + ".yml");
	}

	@Override
	protected void onLoad() {
		if (this.messages != null && this.permissions != null && this.centered != null && this.worlds != null) {
			this.save();

			return;
		}

		this.permissions = getString("Permission");
		this.centered = getBoolean("Centered");
		this.messages = getStringList("Message");
		this.worlds = getStringList("Worlds");
	}

	@Override
	protected void onSave() {
		this.set("Permission", this.permissions);
		this.set("Centered", this.centered);
		this.set("Worlds", this.worlds);
		this.set("Message", this.messages);
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Broadcasts && ((Broadcasts) obj).getBroadcastName().equals(this.getBroadcastName());
	}

	@Nullable
	public List<String> getMessages() {
		return this.messages;
	}

	@Nullable
	public String getPermissons() {
		return this.permissions;
	}

	@Nullable
	public List<String> getWorlds() {
		return this.worlds;
	}

	@Nullable
	public Boolean getCentered() {
		return this.centered;
	}

	public static void loadBroadcasts() {
		broadcasts.loadItems();
	}

	// ---------------------------------------------------------------------------------------------------------------------
	// Static Methods
	// ---------------------------------------------------------------------------------------------------------------------

	public static Broadcasts createBroadcast(@NonNull final String broadcastName, @NonNull final List<String> messages, @NonNull final String permissions, @NonNull final Boolean centered, @NonNull final List<String> worlds) {
		return broadcasts.loadOrCreateItem(broadcastName, () -> new Broadcasts(broadcastName, messages, permissions, centered, worlds));
	}

	public static void removeBroadcast(@NonNull final Broadcasts broadcast) {
		broadcasts.removeItem(broadcast);
	}

	public static Collection<Broadcasts> getAllBroadcasts() {
		return broadcasts.getItems();
	}

	public static List<List<String>> getAllMessages() {
		val allMessages = new ArrayList<List<String>>();
		broadcasts.getItems().forEach(broadcast -> allMessages.add(broadcast.getMessages()));

		return allMessages;
	}

	public static List<String> getAllWorlds() {
		val allWorlds = new ArrayList<String>();
		for (final Broadcasts broadcast : broadcasts.getItems()) {
			assert broadcast.getWorlds() != null;
			for (final String world : broadcast.getWorlds())
				if (!allWorlds.contains(world))
					allWorlds.add(world);
		}

		return allWorlds;
	}


	public static List<String> getAllPermissions() {
		val allPermissions = new ArrayList<String>();
		broadcasts.getItems().forEach(broadcast -> allPermissions.add(broadcast.getPermissons()));

		return allPermissions;
	}

	public static List<Boolean> getAllCentered() {
		val allCentered = new ArrayList<Boolean>();
		broadcasts.getItems().forEach(broadcast -> allCentered.add(broadcast.getCentered()));

		return allCentered;
	}

	public static String getPermissionFromMessage(@NonNull final List<String> messages) {
		for (final Broadcasts broadcast : getAllBroadcasts()) {
			assert broadcast.getMessages() != null;
			if (broadcast.getMessages().equals(messages))
				return broadcast.getPermissons();
		}

		return null;
	}

	public static Boolean getCenteredFromMessage(@NonNull final List<String> messages) {
		for (final Broadcasts broadcast : getAllBroadcasts()) {
			assert broadcast.getMessages() != null;
			if (broadcast.getMessages().equals(messages))
				return broadcast.getCentered();
		}

		return null;
	}
}

