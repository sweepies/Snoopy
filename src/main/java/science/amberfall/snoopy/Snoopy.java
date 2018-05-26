package science.amberfall.snoopy;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import science.amberfall.snoopy.Command.SnoopyCommand;
import science.amberfall.snoopy.EventHandler.BlockBreakHandler;
import science.amberfall.snoopy.EventHandler.BlockPlaceHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

public final class Snoopy extends JavaPlugin implements Listener {
	@Getter private static Snoopy snoopy;
	@Getter private SnoopyCommandManager commandManager;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		final File langFile = new File(getDataFolder(), "lang_en.yml");
		InputStream langStream = getResource("lang_en.yml");

		try {
			FileUtils.copyInputStreamToFile(langStream, langFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				langStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		snoopy = this;
		commandManager = new SnoopyCommandManager(this);

		Bukkit.getPluginManager().registerEvents(new BlockBreakHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceHandler(this), this);
		registerCommands();
	}

	@Override
	public void onDisable() {
		commandManager.unregisterCommands();
	}

	/**
	 * Register commands for the plugin
	 */
	private void registerCommands() {
		commandManager.registerCommand(new SnoopyCommand(this));
	}

	/**
	 * Find all the ores in a vein
	 *
	 * @param type      The type of ore to look for
	 * @param anchor    The originally mined block
	 * @param collected The HashSet to store the blocks in
	 */
	public void getVein(Material type, Block anchor, HashSet<Block> collected) {
		if (!anchor.getType().equals(type) || collected.contains(anchor))
			return; // Break if already counted or wrong type of block

		// Add this block (anchor) to collected list.
		collected.add(anchor);

		// Mark as already counted with metadata
		anchor.setMetadata("snoopy_dontcount", new FixedMetadataValue(this, true));

		// Repeat this procedure for every adjacent block
		final BlockFace[] blockFaces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

		Arrays.stream(blockFaces).forEach(blockFace -> getVein(type, anchor.getRelative(blockFace), collected));
	}
}
