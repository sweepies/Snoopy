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

	public BlockBreakHandler(Snoopy snoopy) {
		this.snoopy = snoopy;
		this.snoopyCommandManager = snoopy.getCommandManager();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		if (!ev.isCancelled()) {
			if (ev.isDropItems()) {

				Player player = ev.getPlayer();
				Block block = ev.getBlock();
				Material type = block.getType();
				Location location = block.getLocation();


				// Loop through all types defined in the config
				for (Map m : snoopy.getConfig().getMapList("blocks")) {
					// If the mined block matches one defined
					if (m.get("type") != null && m.get("type").equals(type.toString())) {
						// If block was not already counted
						if (!block.hasMetadata("snoopy_dontcount")) {

							// Get the amount in the vein
							HashSet<Block> vein = new HashSet<>();
							snoopy.getVein(block.getType(), block, vein);
							final int veinSize = vein.size();

							// Define variables to replace message placeholders with
							final boolean usePrefix = snoopy.getConfig().getBoolean("usePrefix", false);
							final String playerName = player.getName();
							final String amount = Integer.toString(veinSize);
							final String friendlyName = (String) m.get(veinSize > 1 ? "pluralName" : "name");
							final String blockType = (String) m.get("type");
							final String locationX = Integer.toString(location.getBlockX());
							final String locationY = Integer.toString(location.getBlockY());
							final String locationZ = Integer.toString(location.getBlockZ());
							final String lightLevel = Integer.toString(player.getLocation().getBlock().getLightLevel());

							// Send out notification messages
							Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("snoopy.getnotified")).forEach(p -> {

								final CommandIssuer issuer = snoopyCommandManager.getCommandIssuer(p);
								final String messagePrefix = snoopyCommandManager.getLocales().getMessage(issuer, SnoopyMessageKeys.MESSAGE_PREFIX);

								// Send the notification replacing placeholders from the locale file
								issuer.sendInfo(SnoopyMessageKeys.NOTIFY_MESSAGE_FORMAT,
										"{prefix}", usePrefix ? messagePrefix : "", "{name}", playerName,
										"{amount}", amount,
										"{friendly name}", friendlyName,
										"{type}", blockType,
										"{locX}", locationX,
										"{locY}", locationY,
										"{locZ}", locationZ,
										"{light level}", lightLevel);
							});
						}
						break;
					}
				}
			}
		}
	}
}
