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

}
