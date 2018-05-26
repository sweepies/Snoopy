package science.amberfall.snoopy;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Locale;

public class SnoopyCommandManager extends PaperCommandManager {

	SnoopyCommandManager(Plugin plugin) {
		super(plugin);
		this.enableUnstableAPI("help");
		this.setFormat(MessageType.INFO, ChatColor.WHITE, ChatColor.DARK_AQUA, ChatColor.GRAY, ChatColor.RED);
		this.setFormat(MessageType.HELP, ChatColor.WHITE, ChatColor.DARK_AQUA, ChatColor.GRAY, ChatColor.RED);
		this.setFormat(MessageType.ERROR, ChatColor.WHITE, ChatColor.DARK_AQUA, ChatColor.GRAY, ChatColor.RED);

		try {
			this.getLocales().loadYamlLanguageFile("lang_en.yml", Locale.ENGLISH);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
