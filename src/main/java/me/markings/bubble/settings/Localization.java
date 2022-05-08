package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.settings.SimpleLocalization;

@SuppressWarnings("unused")
public class Localization extends SimpleLocalization {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class NotificationMessages {

		public static String MENTIONED_MESSAGE;

		private static void init() {
			setPathPrefix("Notifications");
			MENTIONED_MESSAGE = getString("Mentioned_Toast");
		}
	}

	// TODO: Add separate option for the note at the end of each message.
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class PromptMessages {

		public static String EDIT_PROMPT_MESSAGE;
		public static String PERMISSION_PROMPT_MESSAGE;
		public static String LABEL_PROMPT_MESSAGE;
		public static String HEADER_PROMPT_MESSAGE;
		public static String FOOTER_PROMPT_MESSAGE;
		public static String JOIN_PROMPT_MESSAGE;
		public static String QUIT_PROMPT_MESSAGE;
		public static String WORLDS_PROMPT_MESSAGE;
		public static String FORMAT_PROMPT_MESSAGE;
		public static String COLOR_PROMPT_MESSAGE;

		private static void init() {
			setPathPrefix("Prompt");
			EDIT_PROMPT_MESSAGE = getString("Edit_Prompt_Message");
			PERMISSION_PROMPT_MESSAGE = getString("Permission_Prompt_Message");
			LABEL_PROMPT_MESSAGE = getString("Label_Prompt_Message");
			HEADER_PROMPT_MESSAGE = getString("Header_Prompt_Message");
			FOOTER_PROMPT_MESSAGE = getString("Footer_Prompt_Message");
			JOIN_PROMPT_MESSAGE = getString("Join_Prompt_Message");
			QUIT_PROMPT_MESSAGE = getString("Quit_Prompt_Message");
			WORLDS_PROMPT_MESSAGE = getString("Worlds_Prompt_Message");
			FORMAT_PROMPT_MESSAGE = getString("Format_Prompt_Message");
			COLOR_PROMPT_MESSAGE = getString("Color_Prompt_Message");
		}

	}

}
