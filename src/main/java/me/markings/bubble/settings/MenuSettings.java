package me.markings.bubble.settings;

import lombok.Getter;
import lombok.val;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class MenuSettings extends YamlConfig {

	@Getter
	private static final MenuSettings instance = new MenuSettings();

	private static final String preferencesPath = "Preferences_";
	private static final String chatPath = "Chat_";
	private static final String motdPath = "MOTD_";
	private static final String mentionsPath = "Mentions_";

	private static final String menuTitle = "Menu_Title";
	private static final String menuSize = "Menu_Size";

	private String prefMenuTitle;
	private String chatSettingsButtonTitle;
	private String motdSettingsButtonTitle;
	private String mentionsSettingsButtonTitle;

	private String broadcastsEnabledButtonTitle;
	private String broadcastsDisabledButtonTitle;

	private String broadcastSoundEnabledButtonTitle;
	private String broadcastSoundDisabledButtonTitle;

	private String motdEnabledButtonTitle;
	private String motdDisabledButtonTitle;

	private String mentionsEnabledButtonTitle;
	private String mentionsDisabledButtonTitle;

	private String mentionSoundEnabledButtonTitle;
	private String mentionSoundDisabledButtonTitle;

	private String mentionToastEnabledButtonTitle;
	private String mentionToastDisabledButtonTitle;

	private String chatMenuTitle;

	private String motdMenuTitle;

	private String mentionMenuTitle;

	private CompMaterial chatSettingsButtonMaterial;
	private CompMaterial motdSettingsButtonMaterial;
	private CompMaterial mentionsSettingsButtonMaterial;

	private CompMaterial broadcastsEnabledButtonMaterial;
	private CompMaterial broadcastsDisabledButtonMaterial;

	private CompMaterial broadcastSoundButtonMaterial;

	private CompMaterial motdStatusButtonMaterial;

	private CompMaterial mentionsStatusButtonMaterial;

	private CompMaterial mentionSoundStatusButtonMaterial;

	private CompMaterial mentionToastStatusButtonMaterial;

	private String[] chatSettingsButtonLore;
	private String[] motdSettingsButtonLore;
	private String[] mentionsSettingsButtonLore;

	private String[] broadcastStatusButtonLore;

	private String[] broadcastSoundButtonLore;

	private String[] motdStatusButtonLore;

	private String[] mentionsStatusButtonLore;

	private String[] mentionSoundStatusButtonLore;

	private String[] mentionToastStatusButtonLore;

	private int prefMenuSize;
	private int chatMenuSize;
	private int motdMenuSize;
	private int mentionMenuSize;

	private int broadcastStatusButtonSlot;
	private int motdSettingsButtonSlot;
	private int mentionsSettingsButtonSlot;

	private int broadcastSoundButtonSlot;

	private int motdStatusButtonSlot;

	private int mentionsStatusButtonSlot;

	private int mentionSoundStatusButtonSlot;

	private int mentionToastStatusButtonSlot;

	@Override
	protected boolean saveComments() {
		return true;
	}

	protected MenuSettings() {
		loadConfiguration("menus.yml");
	}

	// TODO: Create data for remaining slots.
	@Override
	protected void onLoadFinish() {
		pathPrefix("Preferences");
		prefMenuTitle = getString(preferencesPath + menuTitle);
		prefMenuSize = getInteger(preferencesPath + menuSize);

		pathPrefix("Preferences.Chat_Settings_Button");
		chatSettingsButtonTitle = getString("Title_Chat");
		chatSettingsButtonMaterial = getMaterial("Material_Chat");
		chatSettingsButtonLore = getStringArray("Lore_Chat");

		pathPrefix("Preferences.MOTD_Settings_Button");
		motdSettingsButtonTitle = getString("Title_MOTD");
		motdSettingsButtonMaterial = getMaterial("Material_MOTD");
		motdSettingsButtonLore = getStringArray("Lore_MOTD");
		motdSettingsButtonSlot = getInteger("Slot_MOTD");

		pathPrefix("Preferences.Mentions_Settings_Button");
		mentionsSettingsButtonTitle = getString("Title_Mentions");
		mentionsSettingsButtonMaterial = getMaterial("Material_Mentions");
		mentionsSettingsButtonLore = getStringArray("Lore_Mentions");
		mentionsSettingsButtonSlot = getInteger("Slot_Mentions");

		pathPrefix("Chat_Settings");
		chatMenuTitle = getString(chatPath + menuTitle);
		chatMenuSize = getInteger(chatPath + menuSize);

		pathPrefix("Chat_Settings.Toggle_Broadcasts_Button");
		broadcastsEnabledButtonTitle = getString("Title_Broadcasts_Enabled");
		broadcastsDisabledButtonTitle = getString("Title_Broadcasts_Disabled");
		broadcastsEnabledButtonMaterial = getMaterial("Material_Broadcasts_Enabled");
		broadcastsDisabledButtonMaterial = getMaterial("Material_Broadcasts_Disabled");
		broadcastStatusButtonLore = getStringArray("Lore_Broadcasts");
		broadcastStatusButtonSlot = getInteger("Slot_Broadcasts");

		pathPrefix("Chat_Settings.Toggle_Broadcast_Sound_Button");
		broadcastSoundEnabledButtonTitle = getString("Title_Broadcast_Sound_Enabled");
		broadcastSoundDisabledButtonTitle = getString("Title_Broadcast_Sound_Disabled");
		broadcastSoundButtonMaterial = getMaterial("Material_Broadcast_Sound");
		broadcastSoundButtonLore = getStringArray("Lore_Sound");
		broadcastSoundButtonSlot = getInteger("Slot_Sound");

		pathPrefix("MOTD_Settings");
		motdMenuTitle = getString(motdPath + menuTitle);
		motdMenuSize = getInteger(motdPath + menuSize);

		pathPrefix("MOTD_Settings.Toggle_MOTD_Button");
		motdEnabledButtonTitle = getString("Title_MOTD_Enabled");
		motdDisabledButtonTitle = getString("Title_MOTD_Disabled");
		motdStatusButtonMaterial = getMaterial("Material_MOTD_Status");
		motdStatusButtonLore = getStringArray("Lore_MOTD_Status");
		motdStatusButtonSlot = getInteger("Slot_MOTD_Status");

		pathPrefix("Mentions_Settings");
		mentionMenuTitle = getString(mentionsPath + menuTitle);
		mentionMenuSize = getInteger(mentionsPath + menuSize);

		pathPrefix("Mentions_Settings.Toggle_Mentions_Button");
		mentionsEnabledButtonTitle = getString("Title_Mentions_Enabled");
		mentionsDisabledButtonTitle = getString("Title_Mentions_Disabled");
		mentionsStatusButtonMaterial = getMaterial("Material_Mentions_Status");
		mentionsStatusButtonLore = getStringArray("Lore_Mentions_Status");
		mentionsStatusButtonSlot = getInteger("Slot_Mentions_Status");

		pathPrefix("Mentions_Settings.Toggle_Mention_Sound_Button");
		mentionSoundEnabledButtonTitle = getString("Title_Mention_Sound_Enabled");
		mentionSoundDisabledButtonTitle = getString("Title_Mention_Sound_Disabled");
		mentionSoundStatusButtonMaterial = getMaterial("Material_Mention_Sound_Status");
		mentionSoundStatusButtonLore = getStringArray("Lore_Mention_Sound_Status");
		mentionSoundStatusButtonSlot = getInteger("Slot_Mention_Sound_Status");

		pathPrefix("Mentions_Settings.Toggle_Mention_Toast_Button");
		mentionToastEnabledButtonTitle = getString("Title_Mention_Toast_Enabled");
		mentionToastDisabledButtonTitle = getString("Title_Mention_Toast_Disabled");
		mentionToastStatusButtonMaterial = getMaterial("Material_Mention_Toast_Status");
		mentionToastStatusButtonLore = getStringArray("Lore_Mention_Toast_Status");
		mentionToastStatusButtonSlot = getInteger("Slot_Mention_Toast_Status");
	}

	@Override
	protected SerializedMap serialize() {
		val map = new SerializedMap();

		pathPrefix("Preferences");
		map.put(preferencesPath + menuTitle, prefMenuTitle);
		map.put(preferencesPath + menuSize, prefMenuSize);

		pathPrefix("Preferences.Chat_Settings_Button");
		map.put("Title_Chat", chatSettingsButtonTitle);
		map.put("Material_Chat", chatSettingsButtonMaterial);
		map.put("Lore_Chat", chatSettingsButtonLore);

		pathPrefix("Preferences.MOTD_Settings_Button");
		map.put("Title_MOTD", motdSettingsButtonTitle);
		map.put("Material_MOTD", motdSettingsButtonMaterial);
		map.put("Lore_MOTD", motdSettingsButtonLore);
		map.put("Slot_MOTD", motdSettingsButtonSlot);

		pathPrefix("Preferences.Mentions_Settings_Button");
		map.put("Title_Mentions", mentionsSettingsButtonTitle);
		map.put("Material_Mentions", mentionsSettingsButtonMaterial);
		map.put("Lore_Mentions", mentionsSettingsButtonLore);
		map.put("Slot_Mentions", mentionsSettingsButtonSlot);

		pathPrefix("Chat_Settings");
		map.put(chatPath + menuTitle, chatMenuTitle);
		map.put(chatPath + menuSize, chatMenuSize);

		pathPrefix("Chat_Settings.Toggle_Broadcasts_Button");
		map.put("Title_Broadcasts_Enabled", broadcastsDisabledButtonTitle);
		map.put("Title_Broadcasts_Disabled", broadcastsDisabledButtonTitle);
		map.put("Material_Broadcasts_Enabled", broadcastsEnabledButtonMaterial);
		map.put("Material_Broadcasts_Disabled", broadcastsDisabledButtonMaterial);
		map.put("Lore_Broadcasts", broadcastStatusButtonLore);
		map.put("Slot_Broadcasts", broadcastStatusButtonSlot);

		pathPrefix("Chat_Settings.Toggle_Broadcast_Sound_Button");
		map.put("Title_Broadcast_Sound_Enabled", broadcastSoundEnabledButtonTitle);
		map.put("Title_Broadcast_Sound_Disabled", broadcastSoundDisabledButtonTitle);
		map.put("Material_Broadcast_Sound", broadcastSoundButtonMaterial);
		map.put("Lore_Sound", broadcastSoundButtonLore);
		map.put("Slot_Sound", broadcastSoundButtonSlot);

		pathPrefix("MOTD_Settings.Toggle_MOTD_Button");
		map.put(motdPath + menuTitle, motdMenuTitle);
		map.put(motdPath + menuSize, motdMenuSize);
		map.put("Title_MOTD_Enabled", motdEnabledButtonTitle);
		map.put("Title_MOTD_Disabled", motdDisabledButtonTitle);
		map.put("Material_MOTD_Status", motdStatusButtonMaterial);
		map.put("Lore_MOTD_Status", motdStatusButtonLore);
		map.put("Slot_MOTD_Status", motdStatusButtonSlot);

		pathPrefix("Mentions_Settings");
		map.put(mentionsPath + menuTitle, mentionMenuTitle);
		map.put(mentionsPath + menuSize, mentionMenuSize);

		pathPrefix("Mentions_Settings.Toggle_Mentions_Button");
		map.put("Title_Mentions_Enabled", mentionsEnabledButtonTitle);
		map.put("Title_Mentions_Disabled", mentionsDisabledButtonTitle);
		map.put("Material_Mentions_Status", mentionsStatusButtonMaterial);
		map.put("Lore_Mentions_Status", mentionsStatusButtonLore);
		map.put("Slot_Mentions_Status", mentionsStatusButtonSlot);

		pathPrefix("Mentions_Settings.Toggle_Mention_Sound_Button");
		map.put("Title_Mention_Sound_Enabled", mentionSoundEnabledButtonTitle);
		map.put("Title_Mention_Sound_Disabled", mentionSoundDisabledButtonTitle);
		map.put("Material_Mention_Sound_Status", mentionSoundStatusButtonMaterial);
		map.put("Lore_Mention_Sound_Status", mentionSoundStatusButtonLore);
		map.put("Slot_Mention_Sound_Status", mentionSoundStatusButtonSlot);

		pathPrefix("Mentions_Settings.Toggle_Mention_Toast_Button");
		map.put("Title_Mention_Toast_Enabled", mentionToastEnabledButtonTitle);
		map.put("Title_Mention_Toast_Disabled", mentionToastDisabledButtonTitle);
		map.put("Material_Mention_Toast_Status", mentionToastStatusButtonMaterial);
		map.put("Lore_Mention_Toast_Status", mentionToastStatusButtonLore);
		map.put("Slot_Mention_Toast_Status", mentionToastStatusButtonSlot);


		return map;
	}

	public int getBroadcastStatusButtonSlot() {
		return broadcastStatusButtonSlot;
	}
}
