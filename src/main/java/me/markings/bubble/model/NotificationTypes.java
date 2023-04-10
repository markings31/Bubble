package me.markings.bubble.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public enum NotificationTypes {

    CHAT("chat"),
    TITLE("title"),
    ACTIONBAR("actionbar"),
    BOSSBAR("bossbar"),
    TOAST("toast"),
    IMAGE("image");

    @Getter
    private final String label;

    // Returns the labels of all the notification types separated with a comma.
    public static String getLabels() {
        final StringBuilder labels = new StringBuilder();
        for (final NotificationTypes type : NotificationTypes.values()) {
            labels.append(type.getLabel()).append(", ");
        }
        return labels.substring(0, labels.length() - 2);
    }

    // Returns the notification type if the label string matches.
    public static NotificationTypes fromLabel(final String label) {
        for (final NotificationTypes type : NotificationTypes.values()) {
            if (type.getLabel().equalsIgnoreCase(label)) {
                return type;
            }
        }
        return null;
    }

}
