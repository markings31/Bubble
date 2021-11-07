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

	private String menuTitle;
	private String chatSettingsButtonTitle;
	private String motdSettingsButtonTitle;
	private String mentionsSettingsButtonTitle;

	private String[] chatSettingsButtonLore;
	private String[] motdSettingsButtonLore;
	private String[] mentionsSettingsButtonLore;

	private CompMaterial chatSettingsButtonMaterial;
	private CompMaterial motdSettingsButtonMaterial;
	private CompMaterial mentionsSettingsButtonMaterial;

	private int menuSize;

	@Override
	protected boolean saveComments() {
		return true;
	}

	protected MenuSettings() {
		loadConfiguration("menus.yml");
	}

	// TODO: Create data for child menus.
	@Override
	protected void onLoadFinish() {
		pathPrefix("Preferences");
		menuTitle = getString("Menu_Title");
		menuSize = getInteger("Menu_Size");

		pathPrefix("Preferences.Chat_Settings_Button");
		chatSettingsButtonTitle = getString("Title_Chat");
		chatSettingsButtonMaterial = getMaterial("Material_Chat");
		chatSettingsButtonLore = getStringArray("Lore_Chat");

		pathPrefix("Preferences.MOTD_Settings_Button");
		motdSettingsButtonTitle = getString("Title_MOTD");
		motdSettingsButtonMaterial = getMaterial("Material_MOTD");
		motdSettingsButtonLore = getStringArray("Lore_MOTD");

		pathPrefix("Preferences.Mentions_Settings_Button");
		mentionsSettingsButtonTitle = getString("Title_Mentions");
		mentionsSettingsButtonMaterial = getMaterial("Material_Mentions");
		mentionsSettingsButtonLore = getStringArray("Lore_Mentions");
	}

	@Override
	protected SerializedMap serialize() {
		val map = new SerializedMap();

		pathPrefix("Preferences");
		map.put("Menu_Title", menuTitle);
		map.put("Menu_Size", menuSize);

		pathPrefix("Preferences.Chat_Settings_Button");
		map.put("Title_Chat", chatSettingsButtonTitle);
		map.put("Material_Chat", chatSettingsButtonMaterial);
		map.put("Lore_Chat", chatSettingsButtonLore);

		pathPrefix("Preferences.MOTD_Settings_Button");
		map.put("Title_MOTD", motdSettingsButtonTitle);
		map.put("Material_MOTD", motdSettingsButtonMaterial);
		map.put("Lore_MOTD", motdSettingsButtonLore);

		pathPrefix("Preferences.Mentions_Settings_Button");
		map.put("Title_Mentions", mentionsSettingsButtonTitle);
		map.put("Material_Mentions", mentionsSettingsButtonMaterial);
		map.put("Lore_Mentions", mentionsSettingsButtonLore);

		return map;
	}
}
