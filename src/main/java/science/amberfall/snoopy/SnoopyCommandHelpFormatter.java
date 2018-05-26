package science.amberfall.snoopy;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandHelpFormatter;
import co.aikar.commands.CommandManager;
import co.aikar.commands.HelpEntry;

public class SnoopyCommandHelpFormatter extends CommandHelpFormatter {

	public SnoopyCommandHelpFormatter(CommandManager manager) {
		super(manager);
	}

	@Override
	public String[] getEntryFormatReplacements(CommandHelp help, HelpEntry entry) {
		return new String[]{
				"{command}", entry.getCommand(),
				"{commandprefix}", help.getCommandPrefix(),
				// If the subcommand is "help" (since we only need one help page) or there are no paramaters, replace with nothing
				// Otherwise, append a space to the parameters so we don't have to have extra whitespace for commands that have none
				"{parameters}", entry.getCommand().split(" ")[1].equals("help") || entry.getParameters().length > 0 ? "" : entry.getParameterSyntax() + " ",
				"{separator}", entry.getDescription().isEmpty() ? "" : "-",
				"{description}", entry.getDescription()
		};
	}
}
