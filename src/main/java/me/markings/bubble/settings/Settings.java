package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class NotificationSettings {

        /**
         * Broadcast Settings
         */
        public static Boolean ENABLE_BROADCASTS;
        public static Boolean RANDOM_MESSAGE;
        public static Boolean CENTER_ALL;
        public static Boolean SEND_ASYNC;

        public static SimpleTime BROADCAST_DELAY;

        /**
         * PushOver Settings
         */
        public static String APPLICATION_TOKEN;

        public static String USER_KEY;

        private static void init() {
            setPathPrefix("Notifications.Broadcast");
            ENABLE_BROADCASTS = getBoolean("Enable");
            BROADCAST_DELAY = getTime("Delay");
            RANDOM_MESSAGE = getBoolean("Random_Message");
            CENTER_ALL = getBoolean("Center_All");
            SEND_ASYNC = getBoolean("Send_Asynchronously");

            setPathPrefix("Notifications.Pushover");
            APPLICATION_TOKEN = getString("Application_Token");
            USER_KEY = getString("User_Key");
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class WelcomeSettings {

        /**
         * MOTD Settings
         */
        public static Boolean ENABLE_JOIN_MOTD;

        public static List<String> JOIN_MOTD = new ArrayList<>();

        public static SimpleTime MOTD_DELAY;

        public static SimpleSound MOTD_SOUND;

        private static void init() {
            setPathPrefix("Notifications.Welcome");
            ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
            MOTD_DELAY = getTime("MOTD_Delay");
            MOTD_SOUND = getSound("Sound");
            JOIN_MOTD = getStringList("Join_MOTD");
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class ChatSettings {

        /**
         * Mention Settings
         */
        public static Boolean ENABLE_MENTIONS;

        public static String MENTION_COLOR;
        public static String MENTION_IGNORE_PERMISSION;

        public static SimpleSound MENTION_SOUND;

        private static void init() {
            setPathPrefix("Chat.Mentions");
            ENABLE_MENTIONS = getBoolean("Enable");
            MENTION_IGNORE_PERMISSION = getString("Ignore_Permission");
            MENTION_COLOR = getString("Color");
            MENTION_SOUND = getSound("Sound");
        }
    }

    public final static class DatabaseSettings {

        /**
         * MySQL Settings
         */

        private static void init() {
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class HookSettings {

        public static Boolean VAULT;
        public static Boolean PAPI;
        public static Boolean BSTATS;

        private static void init() {
            setPathPrefix("Hooks");
            VAULT = getBoolean("Vault");
            PAPI = getBoolean("PlaceholderAPI");
            BSTATS = getBoolean("BStats");
        }
    }
}
