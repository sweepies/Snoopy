package science.amberfall.snoopy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public final class Snoopy extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Find all the ores in a vein
     *
     * @param type      The type of ore to look for
     * @param anchor    The originally mined block
     * @param collected The HashSet to store the blocks in
     */
    private void getVein(Material type, Block anchor, HashSet<Block> collected) {

        if (!anchor.getType().equals(type)) return;

        if (collected.contains(anchor)) return; // Important! Prevents loop of death.

        collected.add(anchor); // Add this block (anchor) to collected list.
        anchor.setMetadata("snoopy_dontcount", new FixedMetadataValue(this, true));

        // Move to adjacent block and repeat the procedure by using this same method (recursion):
        getVein(type, anchor.getRelative(BlockFace.NORTH), collected);
        getVein(type, anchor.getRelative(BlockFace.SOUTH), collected);
        getVein(type, anchor.getRelative(BlockFace.EAST), collected);
        getVein(type, anchor.getRelative(BlockFace.WEST), collected);
        getVein(type, anchor.getRelative(BlockFace.UP), collected);
        getVein(type, anchor.getRelative(BlockFace.DOWN), collected);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        if (!ev.isCancelled()) {

            if (ev.isDropItems()) {

                Player player = ev.getPlayer();
                Block block = ev.getBlock();
                Material type = block.getType();
                Location location = block.getLocation();

                for (String b : this.getConfig().getStringList("blocks")) {
                    if (b.equals(type.toString())) {
                        if (!block.hasMetadata("snoopy_dontcount")) {

                            HashSet<Block> vein = new HashSet<>();
                            getVein(block.getType(), block, vein);

                            String message = this.getConfig().getString("format")
                                    .replace("{name}", player.getName())
                                    .replace("{amount}", Integer.toString(vein.size()))
                                    .replace("{type}", type.toString())
                                    .replace("{location}", String.format("x: %s, y: %s, z: %s", location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                                    .replace("{light level}", Integer.toString(player.getLocation().getBlock().getLightLevel()));

                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("snoopy.seeoremined")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        if (!ev.isCancelled()) {
            if (ev.canBuild()) {

                Block block = ev.getBlockPlaced();
                Material type = block.getType();

                for (String b : this.getConfig().getStringList("blocks")) {
                    if (b.equals(type.toString())) {
                        if (!block.hasMetadata("snoopy_dontcount")) {
                            block.setMetadata("snoopy_dontcount", new FixedMetadataValue(this, true));
                        }
                        break;
                    }
                }

            }
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(" ---- " + ChatColor.DARK_AQUA + "Snoopy Help" + ChatColor.RESET + " -- " + ChatColor.DARK_AQUA + "Page " + ChatColor.GRAY + "1" + ChatColor.DARK_AQUA + "/" + ChatColor.GRAY + "1" + ChatColor.RESET + " ----");
        sender.sendMessage(ChatColor.DARK_AQUA + "/snoopy reload" + ChatColor.RESET + ": Reloads the configuration file.");
        sender.sendMessage(ChatColor.DARK_AQUA + "/snoopy version" + ChatColor.RESET + ": Get the current version.");
    }

    private void sendNoAccess(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3Snoopy&7] &rYou don't have access to that command!"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("snoopy")) {
            if (args.length == 0) {
                sendHelp(sender);
                return true;
            } else {
                if (args[0].toLowerCase().matches("(reload|rl)")) {
                    if (sender.hasPermission("snoopy.reload")) {
                        reloadConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3Snoopy&7] &rConfiguration reloaded!"));
                        return true;
                    } else {
                        sendNoAccess(sender);
                        return true;
                    }
                } else if (args[0].toLowerCase().matches("(version|ver)")) {
                    if (sender.hasPermission("snoopy.version")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3Snoopy&7] &rRunning version " + this.getDescription().getVersion()));
                        return true;
                    } else {
                        sendNoAccess(sender);
                        return true;
                    }
                } else if (args[0].toLowerCase().matches("(help|h)")) {
                    if (sender.hasPermission("snoopy.help")) {
                        sendHelp(sender);
                        return true;
                    } else {
                        sendNoAccess(sender);
                        return true;
                    }
                }
                // If all else fails
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3Snoopy&7] &rUnknown argument! See /snoopy help for more info."));
            }
        }
        return true;
    }
}
