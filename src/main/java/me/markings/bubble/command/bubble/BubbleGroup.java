package me.markings.bubble.command.bubble;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.model.Permissions;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.DebugCommand;
import org.mineacademy.fo.command.PermsCommand;
import org.mineacademy.fo.command.ReloadCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BubbleGroup extends SimpleCommandGroup {

	@Getter
	private static final BubbleGroup instance = new BubbleGroup();

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new NotificationCommand());
		registerSubcommand(new ReloadCommand());
		registerSubcommand(new AddCommand());
		registerSubcommand(new RemoveCommand());
		registerSubcommand(new GUICommand());
		registerSubcommand(new EditCommand());
		registerSubcommand(new ShowCommand());
		registerSubcommand(new CenterCommand());
		registerSubcommand(new SetHeaderCommand());
		registerSubcommand(new SetFooterCommand());
		registerSubcommand(new SetDelayCommand());
		registerSubcommand(new DebugCommand());
		registerSubcommand(new ForceCommand());
		registerSubcommand(new PermsCommand(Permissions.class, "bubble.command.permissions"));
	}

	@Override
	protected @NotNull
	String getCredits() {
		return "&7Visit &f&nhttps://markings.me/&r &7for more information.";
	}

	@Override
	protected @NotNull
	String getHeaderPrefix() {
		return "&b&l";
	}
}
