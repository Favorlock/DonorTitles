package com.favorlock.donortitles.cmd.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.dthielke.herochat.command.CommandHandler;
import com.favorlock.donortitles.DonorTitles;
import com.favorlock.donortitles.cmd.BaseCommand;
import com.favorlock.donortitles.cmd.Command;
import com.favorlock.donortitles.util.FontFormat;

public class HelpTitle extends BaseCommand {

	private final DonorTitles plugin;

	public HelpTitle(DonorTitles plugin) {
		super("Title Help");
		this.plugin = plugin;
		setDescription("List DonorTitle commands");
		setUsage("/title help");
		setArgumentRange(0, 0);
		setIdentifiers(new String[] { "title help", "title ?" });
	}

	public boolean execute(CommandSender sender, String identifier,
			String[] args) {

		if (DonorTitles.perms.has(sender, "donortitles.help")) {
			List<Command> sortCommands = plugin.getCommandHandler()
					.getCommands();
			List<Command> commands = new ArrayList<Command>();

			for (Command command : sortCommands) {
				if ((command.isShownOnHelpMenu())
						&& (CommandHandler.hasPermission(sender,
								command.getPermission()))) {
					commands.add(command);
				}
			}

			sender.sendMessage(FontFormat
					.translateString("&c-----[ &fDonorTitles Help &c]-----"));
			for (Command command : commands) {
				sender.sendMessage(FontFormat.translateString("&a"
						+ command.getUsage()));
			}
			return true;
		}
		sender.sendMessage("You do not have permission");
		return false;
	}

}
