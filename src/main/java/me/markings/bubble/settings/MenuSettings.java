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
		protected void onLoadFinish() {
			pathPrefix("Preferences");
			prefMenuTitle = getString(preferencesPath + menuTitle);
			prefMenuSize = getInteger(preferencesPath + menuSize);

			pathPrefix("Preferences.Chat_Settings_Button");
			chatSettingsButtonTitle = getString("Chat_Title");
			chatSettingsButtonMaterial = getMaterial("Chat_Material");
			chatSettingsButtonLore = getStringArray("Chat_Lore");
			chatSettingsButtonSlot = getInteger("Chat_Slot");

			pathPrefix("Preferences.MOTD_Settings_Button");
			motdSettingsButtonTitle = getString("MOTD_Title");
			motdSettingsButtonMaterial = getMaterial("MOTD_Material");
			motdSettingsButtonLore = getStringArray("MOTD_Lore");
			motdSettingsButtonSlot = getInteger("MOTD_Slot");

			pathPrefix("Preferences.Mentions_Settings_Button");
			mentionsSettingsButtonTitle = getString("Mentions_Title");
			mentionsSettingsButtonMaterial = getMaterial("Mentions_Material");
			mentionsSettingsButtonLore = getStringArray("Mentions_Lore");
			mentionsSettingsButtonSlot = getInteger("Mentions_Slot");
		}

		@Override
		protected SerializedMap serialize() {
			val map = new SerializedMap();

			pathPrefix("Preferences");
			map.put(preferencesPath + menuTitle, prefMenuTitle);
			map.put(preferencesPath + menuSize, prefMenuSize);

			pathPrefix("Preferences.Chat_Settings_Button");
			map.put("Chat_Title", chatSettingsButtonTitle);
			map.put("Chat_Material", chatSettingsButtonMaterial);
			map.put("Chat_Lore", chatSettingsButtonLore);
			map.put("Chat_Slot", chatSettingsButtonSlot);

			pathPrefix("Preferences.MOTD_Settings_Button");
			map.put("MOTD_Title", motdSettingsButtonTitle);
			map.put("MOTD_Material", motdSettingsButtonMaterial);
			map.put("MOTD_Lore", motdSettingsButtonLore);
			map.put("MOTD_Slot", motdSettingsButtonSlot);

			pathPrefix("Preferences.Mentions_Settings_Button");
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
		protected void onLoadFinish() {
			pathPrefix("Chat_Settings");
			chatMenuTitle = getString(chatPath + menuTitle);
			chatMenuSize = getInteger(chatPath + menuSize);

			pathPrefix("Chat_Settings.Toggle_Broadcasts_Button");
			broadcastsEnabledButtonTitle = getString("Broadcasts_Enabled_Title");
			broadcastsDisabledButtonTitle = getString("Broadcasts_Disabled_Title");
			broadcastsEnabledButtonMaterial = getMaterial("Broadcasts_Enabled_Material");
			broadcastsDisabledButtonMaterial = getMaterial("Broadcasts_Disabled_Material");
			broadcastStatusButtonLore = getStringArray("Broadcasts_Lore");
			broadcastStatusButtonSlot = getInteger("Broadcasts_Slot");

			pathPrefix("Chat_Settings.Toggle_Broadcast_Sound_Button");
			broadcastSoundEnabledButtonTitle = getString("Broadcast_Sound_Enabled_Title");
			broadcastSoundDisabledButtonTitle = getString("Broadcast_Sound_Disabled_Title");
			broadcastSoundButtonMaterial = getMaterial("Broadcast_Sound_Material");
			broadcastSoundButtonLore = getStringArray("Sound_Lore");
			broadcastSoundButtonSlot = getInteger("Sound_Slot");
		}

		@Override
		protected SerializedMap serialize() {
			val map = new SerializedMap();

			pathPrefix("Chat_Settings");
			map.put(chatPath + menuTitle, chatMenuTitle);
			map.put(chatPath + menuSize, chatMenuSize);

			pathPrefix("Chat_Settings.Toggle_Broadcasts_Button");
			map.put("Broadcasts_Enabled_Title", broadcastsDisabledButtonTitle);
			map.put("Broadcasts_Disabled_Title", broadcastsDisabledButtonTitle);
			map.put("Broadcasts_Enabled_Material", broadcastsEnabledButtonMaterial);
			map.put("Broadcasts_Disabled_Material", broadcastsDisabledButtonMaterial);
			map.put("Broadcasts_Lore", broadcastStatusButtonLore);
			map.put("Broadcasts_Slot", broadcastStatusButtonSlot);

			pathPrefix("Chat_Settings.Toggle_Broadcast_Sound_Button");
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
		protected void onLoadFinish() {
			pathPrefix("MOTD_Settings");
			motdMenuTitle = getString(motdPath + menuTitle);
			motdMenuSize = getInteger(motdPath + menuSize);

			pathPrefix("MOTD_Settings.Toggle_MOTD_Button");
			motdEnabledButtonTitle = getString("MOTD_Enabled_Title");
			motdDisabledButtonTitle = getString("MOTD_Disabled_Title");
			motdStatusButtonMaterial = getMaterial("MOTD_Status_Material");
			motdStatusButtonLore = getStringArray("MOTD_Status_Lore");
			motdStatusButtonSlot = getInteger("MOTD_Status_Slot");
		}

		@Override
		protected SerializedMap serialize() {
			val map = new SerializedMap();

			pathPrefix("MOTD_Settings.Toggle_MOTD_Button");
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
		protected void onLoadFinish() {
			pathPrefix("Mentions_Settings");
			mentionMenuTitle = getString(mentionsPath + menuTitle);
			mentionMenuSize = getInteger(mentionsPath + menuSize);

			pathPrefix("Mentions_Settings.Toggle_Mentions_Button");
			mentionsEnabledButtonTitle = getString("Mentions_Enabled_Title");
			mentionsDisabledButtonTitle = getString("Mentions_Disabled_Title");
			mentionsStatusButtonMaterial = getMaterial("Mentions_Status_Material");
			mentionsStatusButtonLore = getStringArray("Mentions_Status_Lore");
			mentionsStatusButtonSlot = getInteger("Mentions_Status_Slot");

			pathPrefix("Mentions_Settings.Toggle_Mention_Sound_Button");
			mentionSoundEnabledButtonTitle = getString("Mention_Sound_Enabled_Title");
			mentionSoundDisabledButtonTitle = getString("Mention_Sound_Disabled_Title");
			mentionSoundStatusButtonMaterial = getMaterial("Mention_Sound_Status_Material");
			mentionSoundStatusButtonLore = getStringArray("Mention_Sound_Status_Lore");
			mentionSoundStatusButtonSlot = getInteger("Mention_Sound_Status_Slot");

			pathPrefix("Mentions_Settings.Toggle_Mention_Toast_Button");
			mentionToastEnabledButtonTitle = getString("Mention_Toast_Enabled_Title");
			mentionToastDisabledButtonTitle = getString("Mention_Toast_Disabled_Title");
			mentionToastStatusButtonMaterial = getMaterial("Mention_Toast_Status_Material");
			mentionToastStatusButtonLore = getStringArray("Mention_Toast_Status_Lore");
			mentionToastStatusButtonSlot = getInteger("Mention_Toast_Status_Slot");
		}

		@Override
		protected SerializedMap serialize() {
			val map = new SerializedMap();

			pathPrefix("Mentions_Settings.Toggle_Mentions_Button");
			map.put("Mentions_Enabled_Title", mentionsEnabledButtonTitle);
			map.put("Mentions_Disabled_Title", mentionsDisabledButtonTitle);
			map.put("Mentions_Status_Material", mentionsStatusButtonMaterial);
			map.put("Mentions_Status_Lore", mentionsStatusButtonLore);
			map.put("Mentions_Status_Slot", mentionsStatusButtonSlot);

			pathPrefix("Mentions_Settings.Toggle_Mention_Sound_Button");
			map.put("Mention_Sound_Enabled_Title", mentionSoundEnabledButtonTitle);
			map.put("Mention_Sound_Disabled_Title", mentionSoundDisabledButtonTitle);
			map.put("Mention_Sound_Status_Material", mentionSoundStatusButtonMaterial);
			map.put("Mention_Sound_Status_Lore", mentionSoundStatusButtonLore);
			map.put("Mention_Sound_Status_Slot", mentionSoundStatusButtonSlot);

			pathPrefix("Mentions_Settings.Toggle_Mention_Toast_Button");
			map.put("Mention_Toast_Enabled_Title", mentionToastEnabledButtonTitle);
			map.put("Mention_Toast_Disabled_Title", mentionToastDisabledButtonTitle);
			map.put("Mention_Toast_Status_Material", mentionToastStatusButtonMaterial);
			map.put("Mention_Toast_Status_Lore", mentionToastStatusButtonLore);
			map.put("Mention_Toast_Status_Slot", mentionToastStatusButtonSlot);


			return map;
		}
	}

	@Getter
	public static class EditMenuSettings extends YamlConfig {

		@Getter
		public static EditMenuSettings instance = new EditMenuSettings();

		private String editMenuTitle;
		private int editMenuSize;

		private String editMessageButtonTitle;
		private CompMaterial editMessageButtonMaterial;
		private String[] editMessageButtonLore;
		private int editMessageButtonSlot;

		private String centerMessageButtonTitle;
		private CompMaterial centerMessageButtonMaterial;
		private String[] centerMessageButtonLore;
		private int centerMessageButtonSlot;

		private String changePermissionButtonTitle;
		private CompMaterial changePermissionButtonMaterial;
		private String[] changePermissionButtonLore;
		private int changePermissionButtonSlot;

		@Override
		protected boolean saveComments() {
			return true;
		}

		protected EditMenuSettings() {
			loadConfiguration(menusFile);
		}

		@Override
		protected void onLoadFinish() {
			pathPrefix("Edit_Settings");
			editMenuTitle = getString("Edit_Menu_Title");
			editMenuSize = getInteger("Edit_Menu_Size");

			pathPrefix("Edit_Settings.Edit_Message_Button");
			editMessageButtonTitle = getString("Edit_Message_Title");
			editMessageButtonMaterial = getMaterial("Edit_Message_Material");
			editMessageButtonLore = getStringArray("Edit_Message_Lore");
			editMessageButtonSlot = getInteger("Edit_Message_Slot");

			pathPrefix("Edit_Settings.Center_Message_Button");
			centerMessageButtonTitle = getString("Center_Message_Title");
			centerMessageButtonMaterial = getMaterial("Center_Message_Material");
			centerMessageButtonLore = getStringArray("Center_Message_Lore");
			centerMessageButtonSlot = getInteger("Center_Message_Slot");

			pathPrefix("Edit_Settings.Change_Permission_Button");
			changePermissionButtonTitle = getString("Change_Permission_Title");
			changePermissionButtonMaterial = getMaterial("Change_Permission_Material");
			changePermissionButtonLore = getStringArray("Change_Permission_Lore");
			changePermissionButtonSlot = getInteger("Change_Permission_Slot");
		}

		@Override
		protected SerializedMap serialize() {
			val map = new SerializedMap();

			pathPrefix("Edit_Settings");
			map.put("Edit_Menu_Title", editMenuTitle);
			map.put("Edit_Menu_Size", editMenuSize);

			pathPrefix("Edit_Settings.Edit_Message_Button");
			map.put("Edit_Message_Title", editMessageButtonTitle);
			map.put("Edit_Message_Material", editMessageButtonMaterial);
			map.put("Edit_Message_Lore", editMessageButtonLore);
			map.put("Edit_Message_Slot", editMessageButtonSlot);

			pathPrefix("Edit_Settings.Center_Message_Button");
			map.put("Center_Message_Title", centerMessageButtonTitle);
			map.put("Center_Message_Material", centerMessageButtonMaterial);
			map.put("Center_Message_Lore", centerMessageButtonLore);
			map.put("Center_Message_Slot", centerMessageButtonSlot);

			pathPrefix("Edit_Settings.Change_Permission_Button");
			map.put("Change_Permission_Title", changePermissionButtonTitle);
			map.put("Change_Permission_Material", changePermissionButtonMaterial);
			map.put("Change_Permission_Lore", changePermissionButtonLore);
			map.put("Change_Permission_Slot", changePermissionButtonSlot);

			return map;
		}
	}

}
