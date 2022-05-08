package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuSettings {

	private static final String menusFile = "menus.yml";

	private static final String preferencesPath = "Preferences_";
	private static final String chatPath = "Chat_";
	private static final String motdPath = "MOTD_";
	private static final String mentionsPath = "Mentions_";

	private static final String menuTitle = "Menu_Title";
	private static final String menuSize = "Menu_Size";

	@Getter
	public static class PreferencesMenuSettings extends YamlConfig {

		@Getter
		public static PreferencesMenuSettings instance = new PreferencesMenuSettings();

		private String prefMenuTitle;
		private int prefMenuSize;

		private String chatSettingsButtonTitle;
		private CompMaterial chatSettingsButtonMaterial;
		private String[] chatSettingsButtonLore;
		private int chatSettingsButtonSlot;

		private String motdSettingsButtonTitle;
		private CompMaterial motdSettingsButtonMaterial;
		private String[] motdSettingsButtonLore;
		private int motdSettingsButtonSlot;

		private String mentionsSettingsButtonTitle;
		private CompMaterial mentionsSettingsButtonMaterial;
		private String[] mentionsSettingsButtonLore;
		private int mentionsSettingsButtonSlot;

		@Override
		protected boolean saveComments() {
			return true;
		}

		protected PreferencesMenuSettings() {
			loadConfiguration(menusFile);
		}

		@Override
		protected void onLoad() {
			setPathPrefix("Preferences");
			prefMenuTitle = getString(preferencesPath + menuTitle);
			prefMenuSize = getInteger(preferencesPath + menuSize);

			setPathPrefix("Preferences.Chat_Settings_Button");
			chatSettingsButtonTitle = getString("Chat_Title");
			chatSettingsButtonMaterial = getMaterial("Chat_Material");
			chatSettingsButtonLore = get("Chat_Lore", String[].class);
			chatSettingsButtonSlot = getInteger("Chat_Slot");

			setPathPrefix("Preferences.MOTD_Settings_Button");
			motdSettingsButtonTitle = getString("MOTD_Title");
			motdSettingsButtonMaterial = getMaterial("MOTD_Material");
			motdSettingsButtonLore = get("MOTD_Lore", String[].class);
			motdSettingsButtonSlot = getInteger("MOTD_Slot");

			setPathPrefix("Preferences.Mentions_Settings_Button");
			mentionsSettingsButtonTitle = getString("Mentions_Title");
			mentionsSettingsButtonMaterial = getMaterial("Mentions_Material");
			mentionsSettingsButtonLore = get("Mentions_Lore", String[].class);
			mentionsSettingsButtonSlot = getInteger("Mentions_Slot");
		}

		@Override
		public SerializedMap saveToMap() {
			val map = new SerializedMap();

			setPathPrefix("Preferences");
			map.put(preferencesPath + menuTitle, prefMenuTitle);
			map.put(preferencesPath + menuSize, prefMenuSize);

			setPathPrefix("Preferences.Chat_Settings_Button");
			map.put("Chat_Title", chatSettingsButtonTitle);
			map.put("Chat_Material", chatSettingsButtonMaterial);
			map.put("Chat_Lore", chatSettingsButtonLore);
			map.put("Chat_Slot", chatSettingsButtonSlot);

			setPathPrefix("Preferences.MOTD_Settings_Button");
			map.put("MOTD_Title", motdSettingsButtonTitle);
			map.put("MOTD_Material", motdSettingsButtonMaterial);
			map.put("MOTD_Lore", motdSettingsButtonLore);
			map.put("MOTD_Slot", motdSettingsButtonSlot);

			setPathPrefix("Preferences.Mentions_Settings_Button");
			map.put("Mentions_Title", mentionsSettingsButtonTitle);
			map.put("Mentions_Material", mentionsSettingsButtonMaterial);
			map.put("Mentions_Lore", mentionsSettingsButtonLore);
			map.put("Mentions_Slot", mentionsSettingsButtonSlot);

			return map;
		}
	}

	@Getter
	public static class ChatMenuSettings extends YamlConfig {

		@Getter
		public static ChatMenuSettings instance = new ChatMenuSettings();

		private String chatMenuTitle;
		private int chatMenuSize;

		private String broadcastsEnabledButtonTitle;
		private String broadcastsDisabledButtonTitle;
		private CompMaterial broadcastsEnabledButtonMaterial;
		private CompMaterial broadcastsDisabledButtonMaterial;
		private String[] broadcastStatusButtonLore;
		private int broadcastStatusButtonSlot;

		private String broadcastSoundEnabledButtonTitle;
		private String broadcastSoundDisabledButtonTitle;
		private CompMaterial broadcastSoundButtonMaterial;
		private String[] broadcastSoundButtonLore;
		private int broadcastSoundButtonSlot;

		@Override
		protected boolean saveComments() {
			return true;
		}

		protected ChatMenuSettings() {
			loadConfiguration(menusFile);
		}

		@Override
		protected void onLoad() {
			setPathPrefix("Preferences.Chat_Settings");
			chatMenuTitle = getString(chatPath + menuTitle);
			chatMenuSize = getInteger(chatPath + menuSize);

			setPathPrefix("Preferences.Chat_Settings.Toggle_Broadcasts_Button");
			broadcastsEnabledButtonTitle = getString("Broadcasts_Enabled_Title");
			broadcastsDisabledButtonTitle = getString("Broadcasts_Disabled_Title");
			broadcastsEnabledButtonMaterial = getMaterial("Broadcasts_Enabled_Material");
			broadcastsDisabledButtonMaterial = getMaterial("Broadcasts_Disabled_Material");
			broadcastStatusButtonLore = get("Broadcasts_Lore", String[].class);
			broadcastStatusButtonSlot = getInteger("Broadcasts_Slot");

			setPathPrefix("Preferences.Chat_Settings.Toggle_Broadcast_Sound_Button");
			broadcastSoundEnabledButtonTitle = getString("Broadcast_Sound_Enabled_Title");
			broadcastSoundDisabledButtonTitle = getString("Broadcast_Sound_Disabled_Title");
			broadcastSoundButtonMaterial = getMaterial("Broadcast_Sound_Material");
			broadcastSoundButtonLore = get("Sound_Lore", String[].class);
			broadcastSoundButtonSlot = getInteger("Sound_Slot");
		}

		@Override
		public SerializedMap saveToMap() {
			val map = new SerializedMap();

			setPathPrefix("Preferences.Chat_Settings");
			map.put(chatPath + menuTitle, chatMenuTitle);
			map.put(chatPath + menuSize, chatMenuSize);

			setPathPrefix("Preferences.Chat_Settings.Toggle_Broadcasts_Button");
			map.put("Broadcasts_Enabled_Title", broadcastsDisabledButtonTitle);
			map.put("Broadcasts_Disabled_Title", broadcastsDisabledButtonTitle);
			map.put("Broadcasts_Enabled_Material", broadcastsEnabledButtonMaterial);
			map.put("Broadcasts_Disabled_Material", broadcastsDisabledButtonMaterial);
			map.put("Broadcasts_Lore", broadcastStatusButtonLore);
			map.put("Broadcasts_Slot", broadcastStatusButtonSlot);

			setPathPrefix("Preferences.Chat_Settings.Toggle_Broadcast_Sound_Button");
			map.put("Broadcast_Sound_Enabled_Title", broadcastSoundEnabledButtonTitle);
			map.put("Broadcast_Sound_Disabled_Title", broadcastSoundDisabledButtonTitle);
			map.put("Broadcast_Sound_Material", broadcastSoundButtonMaterial);
			map.put("Sound_Lore", broadcastSoundButtonLore);
			map.put("Sound_Slot", broadcastSoundButtonSlot);

			return map;
		}

		public int getBroadcastStatusButtonSlot() {
			return broadcastStatusButtonSlot;
		}
	}

	@Getter
	public static class MOTDMenuSettings extends YamlConfig {

		@Getter
		public static MOTDMenuSettings instance = new MOTDMenuSettings();

		private String motdMenuTitle;
		private int motdMenuSize;

		private String motdEnabledButtonTitle;
		private String motdDisabledButtonTitle;
		private CompMaterial motdStatusButtonMaterial;
		private String[] motdStatusButtonLore;
		private int motdStatusButtonSlot;

		@Override
		protected boolean saveComments() {
			return true;
		}

		protected MOTDMenuSettings() {
			loadConfiguration(menusFile);
		}

		@Override
		protected void onLoad() {
			setPathPrefix("Preferences.MOTD_Settings");
			motdMenuTitle = getString(motdPath + menuTitle);
			motdMenuSize = getInteger(motdPath + menuSize);

			setPathPrefix("Preferences.MOTD_Settings.Toggle_MOTD_Button");
			motdEnabledButtonTitle = getString("MOTD_Enabled_Title");
			motdDisabledButtonTitle = getString("MOTD_Disabled_Title");
			motdStatusButtonMaterial = getMaterial("MOTD_Status_Material");
			motdStatusButtonLore = get("MOTD_Status_Lore", String[].class);
			motdStatusButtonSlot = getInteger("MOTD_Status_Slot");
		}

		@Override
		public SerializedMap saveToMap() {
			val map = new SerializedMap();

			setPathPrefix("Preferences.MOTD_Settings.Toggle_MOTD_Button");
			map.put(motdPath + menuTitle, motdMenuTitle);
			map.put(motdPath + menuSize, motdMenuSize);
			map.put("MOTD_Enabled_Title", motdEnabledButtonTitle);
			map.put("MOTD_Disabled_Title", motdDisabledButtonTitle);
			map.put("MOTD_Status_Material", motdStatusButtonMaterial);
			map.put("MOTD_Status_Lore", motdStatusButtonLore);
			map.put("MOTD_Status_Slot", motdStatusButtonSlot);

			return map;
		}
	}

	@Getter
	public static class MentionsMenuSettings extends YamlConfig {

		@Getter
		public static MentionsMenuSettings instance = new MentionsMenuSettings();

		private String mentionMenuTitle;
		private int mentionMenuSize;

		private String mentionsEnabledButtonTitle;
		private String mentionsDisabledButtonTitle;
		private CompMaterial mentionsStatusButtonMaterial;
		private String[] mentionsStatusButtonLore;
		private int mentionsStatusButtonSlot;

		private String mentionSoundEnabledButtonTitle;
		private String mentionSoundDisabledButtonTitle;
		private CompMaterial mentionSoundStatusButtonMaterial;
		private String[] mentionSoundStatusButtonLore;
		private int mentionSoundStatusButtonSlot;

		private String mentionToastEnabledButtonTitle;
		private String mentionToastDisabledButtonTitle;
		private CompMaterial mentionToastStatusButtonMaterial;
		private String[] mentionToastStatusButtonLore;
		private int mentionToastStatusButtonSlot;

		@Override
		protected boolean saveComments() {
			return true;
		}

		protected MentionsMenuSettings() {
			loadConfiguration(menusFile);
		}

		@Override
		protected void onLoad() {
			setPathPrefix("Preferences.Mentions_Settings");
			mentionMenuTitle = getString(mentionsPath + menuTitle);
			mentionMenuSize = getInteger(mentionsPath + menuSize);

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mentions_Button");
			mentionsEnabledButtonTitle = getString("Mentions_Enabled_Title");
			mentionsDisabledButtonTitle = getString("Mentions_Disabled_Title");
			mentionsStatusButtonMaterial = getMaterial("Mentions_Status_Material");
			mentionsStatusButtonLore = get("Mentions_Status_Lore", String[].class);
			mentionsStatusButtonSlot = getInteger("Mentions_Status_Slot");

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mention_Sound_Button");
			mentionSoundEnabledButtonTitle = getString("Mention_Sound_Enabled_Title");
			mentionSoundDisabledButtonTitle = getString("Mention_Sound_Disabled_Title");
			mentionSoundStatusButtonMaterial = getMaterial("Mention_Sound_Status_Material");
			mentionSoundStatusButtonLore = get("Mention_Sound_Status_Lore", String[].class);
			mentionSoundStatusButtonSlot = getInteger("Mention_Sound_Status_Slot");

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mention_Toast_Button");
			mentionToastEnabledButtonTitle = getString("Mention_Toast_Enabled_Title");
			mentionToastDisabledButtonTitle = getString("Mention_Toast_Disabled_Title");
			mentionToastStatusButtonMaterial = getMaterial("Mention_Toast_Status_Material");
			mentionToastStatusButtonLore = get("Mention_Toast_Status_Lore", String[].class);
			mentionToastStatusButtonSlot = getInteger("Mention_Toast_Status_Slot");
		}

		@Override
		public SerializedMap saveToMap() {
			val map = new SerializedMap();

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mentions_Button");
			map.put("Mentions_Enabled_Title", mentionsEnabledButtonTitle);
			map.put("Mentions_Disabled_Title", mentionsDisabledButtonTitle);
			map.put("Mentions_Status_Material", mentionsStatusButtonMaterial);
			map.put("Mentions_Status_Lore", mentionsStatusButtonLore);
			map.put("Mentions_Status_Slot", mentionsStatusButtonSlot);

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mention_Sound_Button");
			map.put("Mention_Sound_Enabled_Title", mentionSoundEnabledButtonTitle);
			map.put("Mention_Sound_Disabled_Title", mentionSoundDisabledButtonTitle);
			map.put("Mention_Sound_Status_Material", mentionSoundStatusButtonMaterial);
			map.put("Mention_Sound_Status_Lore", mentionSoundStatusButtonLore);
			map.put("Mention_Sound_Status_Slot", mentionSoundStatusButtonSlot);

			setPathPrefix("Preferences.Mentions_Settings.Toggle_Mention_Toast_Button");
			map.put("Mention_Toast_Enabled_Title", mentionToastEnabledButtonTitle);
			map.put("Mention_Toast_Disabled_Title", mentionToastDisabledButtonTitle);
			map.put("Mention_Toast_Status_Material", mentionToastStatusButtonMaterial);
			map.put("Mention_Toast_Status_Lore", mentionToastStatusButtonLore);
			map.put("Mention_Toast_Status_Slot", mentionToastStatusButtonSlot);


			return map;
		}
	}
}
