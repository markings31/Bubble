package me.markings.bubble.settings;

import lombok.Getter;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class DatabaseFile extends YamlConfig {

	@Getter
	private static final DatabaseFile instance = new DatabaseFile();

	private String host;
	private String name;
	private String username;
	private String password;
	private String tableName;

	private int port;

	@Override
	protected boolean saveComments() {
		return true;
	}

	private DatabaseFile() {
		loadConfiguration("mysql.yml");
	}

	@Override
	protected void onLoad() {
		host = getString("Host");
		port = getInteger("Port");
		name = getString("Database_Name");
		username = getString("Username");
		password = getString("Password");
		tableName = getString("Table_Name");
	}

	@Override
	public SerializedMap saveToMap() {
		return SerializedMap.ofArray(
				"Host", host,
				"Port", port,
				"Name", name,
				"Username", username,
				"Password", password,
				"Table_Name", tableName);
	}
}
