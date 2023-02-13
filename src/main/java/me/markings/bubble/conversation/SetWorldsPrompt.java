package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.Localization;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetWorldsPrompt extends SimplePrompt {

    @Getter
    private static final SetWorldsPrompt instance = new SetWorldsPrompt();

    @Override
    protected String getPrompt(final ConversationContext context) {
        Remain.sendTitle((Player) context.getForWhom(), "&9Set Worlds", "Please type your message in the chat.");
        return Localization.PromptMessages.WORLDS_PROMPT_MESSAGE;
    }

    @Override
    protected String getCustomPrefix() {
        return "";
    }

    @Override
    protected boolean isInputValid(final ConversationContext context, final String input) {
        final String[] inputs = input.contains(" ") ? input.split(", ") : input.split(",");
        int validIndicator = 0;

        for (final String s : inputs)
            validIndicator += !Bukkit.getWorlds().contains(Bukkit.getWorld(s)) ? 1 : 0;

        return validIndicator == 0;
    }

    @Override
    protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
        return Messenger.getErrorPrefix() + "One or more of those world names are invalid!";
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
        Valid.checkPermission(getPlayer(conversationContext), Permissions.BroadcastEditing.WORLDS);

        final List<String> worlds = new ArrayList<>(Arrays.asList(s.split(", ")));

        Broadcasts.getBroadcast(EditCommand.getInput()).setWorlds(worlds);

        return Prompt.END_OF_CONVERSATION;
    }
}
