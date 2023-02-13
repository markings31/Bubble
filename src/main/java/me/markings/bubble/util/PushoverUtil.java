package me.markings.bubble.util;

import me.markings.bubble.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PushoverUtil {

	public static void pushoverNotification(final String message, final Player player) throws Exception {
		final URL url = new URL("https://api.pushover.net/1/messages.json");
		final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);

		final String body =
				"token=" + Settings.NotificationSettings.APPLICATION_TOKEN +
						"&user=" + Settings.NotificationSettings.USER_KEY +
						"&message=" + URLEncoder.encode(message, "UTF-8");
		final OutputStream os = con.getOutputStream();
		os.write(body.getBytes());
		os.flush();
		os.close();

		con.connect();

		final int responseCode = con.getResponseCode();
		final String responseMessage = con.getResponseMessage();

		if (responseCode != 200)
			Common.logFramed("ERROR with Pushover notification request: " + responseMessage);
		else Messenger.success(player, "&aPushover notification has been sent to your devices!");
	}

}
