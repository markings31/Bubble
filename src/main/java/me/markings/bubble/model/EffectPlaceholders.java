package me.markings.bubble.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public enum EffectPlaceholders {

    COMMAND("<command>"),
    TITLE("<title>"),
    ACTIONBAR("<actionbar>"),
    BOSSBAR("<bossbar>"),
    TOAST("<toast>"),
    ANIMATE("{animate:"),
    SCROLL("{scroll:"),
    FLASH("{flash:"),
    GRADIENT_END("</>");

    @Getter
    private final String prefix;

}
