package science.amberfall.snoopy.EventHandler;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import science.amberfall.snoopy.Snoopy;

public class BlockPlaceHandler implements Listener {
	private Snoopy snoopy;

	public BlockPlaceHandler(Snoopy snoopy) {
		this.snoopy = snoopy;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent ev) {
		if (!ev.isCancelled()) {
			if (ev.canBuild()) {

				final Block block = ev.getBlockPlaced();
				final Material type = block.getType();

				// If the placed block matches one defined in the config
				if (snoopy.getConfig().getMapList("blocks").stream().anyMatch(m -> m.get("type") != null && m.get("type").equals(type.toString()))) {
					// If the block doesn't already have Snoopy metadaya
					if (!block.hasMetadata("snoopy_dontcount")) {
						// Give the block metadata to note that it was placed by a player
						block.setMetadata("snoopy_dontcount", new FixedMetadataValue(snoopy, true));
					}
				}
			}
		}
	}
}
