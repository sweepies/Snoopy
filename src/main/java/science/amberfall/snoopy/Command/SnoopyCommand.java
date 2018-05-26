package science.amberfall.snoopy.Command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitLocales;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.MessageType;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import science.amberfall.snoopy.Locale.SnoopyMessageKeys;
import science.amberfall.snoopy.Snoopy;
import science.amberfall.snoopy.SnoopyCommandHelpFormatter;
import science.amberfall.snoopy.SnoopyCommandManager;

import java.io.IOException;
import java.util.Locale;

/**
 * This class handles the /snoopy basecommand, and all its subcommands.
 */
@CommandAlias("snoopy")
public class SnoopyCommand extends BaseCommand {
	private Snoopy snoopy;
	private BukkitLocales locales;
	private SnoopyCommandManager manager;

	public SnoopyCommand(Snoopy snoopy) {
		this.snoopy = snoopy;
		this.manager = snoopy.getCommandManager();
		this.locales = manager.getLocales();
		manager.setHelpFormatter(new SnoopyCommandHelpFormatter(manager));
	}

	/**
	 * Configuration reload command.
	 *
	 * @param sender The command sender.
	 */
	@Subcommand("reload|rl")
	@CommandPermission("snoopy.reload")
	@Description("Reload the Snoopy configuration")
	public boolean onSnoopyReload(CommandSender sender) {
		final CommandIssuer issuer = manager.getCommandIssuer(sender);
		final String messagePrefix = locales.getMessage(issuer, SnoopyMessageKeys.MESSAGE_PREFIX);

		snoopy.reloadConfig();

		try {
			locales.loadYamlLanguageFile("lang_en.yml", Locale.ENGLISH);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			issuer.sendMessage(MessageType.ERROR, SnoopyMessageKeys.RELOAD_LANG_ERROR, "{prefix}", messagePrefix);
			return true;
		}

		issuer.sendMessage(MessageType.INFO, SnoopyMessageKeys.RELOAD_MESSAGE_FORMAT, "{prefix}", messagePrefix);
		return true;
	}

	/**
	 * Plugin version command.
	 *
	 * @param sender The command sender.
	 */
	@Subcommand("version|ver")
	@CommandPermission("snoopy.version")
	@Description("Get the Snoopy version")
	public boolean onSnoopyVersion(CommandSender sender) {
		final CommandIssuer issuer = manager.getCommandIssuer(sender);
		final String messagePrefix = locales.getMessage(issuer, SnoopyMessageKeys.MESSAGE_PREFIX);

		issuer.sendMessage(MessageType.INFO, SnoopyMessageKeys.VERSION_MESSAGE_FORMAT, "{prefix}", messagePrefix, "{version}", snoopy.getDescription().getVersion());
		return true;
	}

	/**
	 * Plugin help command.
	 *
	 * @param help The Help command.
	 */
	@HelpCommand
	@Subcommand("help|h")
	@CommandPermission("snoopy.help")
	@Description("Show the Snoopy help")
	public void onSnoopyHelp(CommandHelp help) {
		help.showHelp();
	}
}
