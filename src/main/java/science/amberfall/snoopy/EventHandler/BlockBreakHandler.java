package science.amberfall.snoopy.EventHandler;

import co.aikar.commands.CommandIssuer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import science.amberfall.snoopy.Locale.SnoopyMessageKeys;
import science.amberfall.snoopy.Snoopy;
import science.amberfall.snoopy.SnoopyCommandManager;

import java.util.HashSet;
import java.util.Map;

public class BlockBreakHandler implements Listener {
	private Snoopy snoopy;
	private SnoopyCommandManager snoopyCommandManager;
	private boolean usePrefix;

	public BlockBreakHandler(Snoopy snoopy) {
		this.snoopy = snoopy;
		this.snoopyCommandManager = snoopy.getCommandManager();
		usePrefix = snoopy.getConfig().getBoolean("usePrefix", true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		if (!ev.isCancelled()) {
			if (ev.isDropItems()) {

				final Player player = ev.getPlayer();
				final Block block = ev.getBlock();
				final Material type = block.getType();
				final Location location = block.getLocation();

				// Loop through all types defined in the config
				for (Map m : snoopy.getConfig().getMapList("blocks")) {

					// Check if the mined block matches an ore defined, or has already been counted
					if (m.get("type") == null || !m.get("type").equals(type.toString()) || block.hasMetadata("snoopy_dontcount"))
						continue;

					// Get the amount in the vein
					final HashSet<Block> vein = new HashSet<>();
					Snoopy.getVein(block.getType(), block, vein);
					final int veinSize = vein.size();

					// Define variables to replace message placeholders with
					final String playerName = player.getName();
					final String amount = Integer.toString(veinSize);
					final String friendlyName = (String) m.get(veinSize > 1 ? "pluralName" : "name");
					final String blockType = (String) m.get("type");
					final String locationX = Integer.toString(location.getBlockX());
					final String locationY = Integer.toString(location.getBlockY());
					final String locationZ = Integer.toString(location.getBlockZ());
					final String lightLevel = Integer.toString(player.getLocation().getBlock().getLightLevel());

					// Send notification messages
					Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("snoopy.getnotified")).forEach(p -> {

						final CommandIssuer fauxIssuer = snoopyCommandManager.getCommandIssuer(p);
						final String messagePrefix = snoopyCommandManager.getLocales().getMessage(fauxIssuer, SnoopyMessageKeys.MESSAGE_PREFIX);

						// Send the notification replacing placeholders from the locale file
						fauxIssuer.sendInfo(SnoopyMessageKeys.NOTIFY_MESSAGE_FORMAT,
								"{prefix}", usePrefix ? messagePrefix : "",
								"{name}", playerName,
								"{amount}", amount,
								"{friendly name}", friendlyName,
								"{type}", blockType,
								"{locX}", locationX,
								"{locY}", locationY,
								"{locZ}", locationZ,
								"{light level}", lightLevel);
					});

					// Send console message
					if (snoopy.getConfig().getBoolean("notifyConsole", true)) {
						final CommandIssuer fauxConsoleIssuer = snoopyCommandManager.getCommandIssuer(Bukkit.getConsoleSender());
						final String messagePrefix = snoopyCommandManager.getLocales().getMessage(fauxConsoleIssuer, SnoopyMessageKeys.MESSAGE_PREFIX);

						fauxConsoleIssuer.sendInfo(SnoopyMessageKeys.NOTIFY_MESSAGE_FORMAT,
								"{prefix}", usePrefix ? messagePrefix : "",
								"{name}", playerName,
								"{amount}", amount,
								"{friendly name}", friendlyName,
								"{type}", blockType,
								"{locX}", locationX,
								"{locY}", locationY,
								"{locZ}", locationZ,
								"{light level}", lightLevel);
					}
				}
			}
		}
	}
}
