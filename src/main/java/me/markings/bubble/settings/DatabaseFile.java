package me.markings.bubble.settings;

import lombok.Getter;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class DatabaseFile extends YamlConfig {

	@Getter
	private static final DatabaseFile instance = new DatabaseFile();

	private String HOST;
	private String NAME;
	private String USERNAME;
	private String PASSWORD;
	private String TABLE_NAME;

	private int PORT;

	@Override
	protected boolean saveComments() {
		return true;
	}

	private DatabaseFile() {
		loadConfiguration("mysql.yml");
	}

	@Override
	protected void onLoadFinish() {
		pathPrefix(null);
		HOST = getString("Host");
		PORT = getInteger("Port");
		NAME = getString("Database_Name");
		USERNAME = getString("Username");
		PASSWORD = getString("Password");
		TABLE_NAME = getString("Table_Name");
	}

	@Override
	protected SerializedMap serialize() {
		return SerializedMap.ofArray(
				"Host", HOST,
				"Port", PORT,
				"Name", NAME,
				"Username", USERNAME,
				"Password", PASSWORD,
				"Table_Name", TABLE_NAME);
	}
}
