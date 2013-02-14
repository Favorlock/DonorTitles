package com.favorlock.donortitles;

import java.sql.SQLException;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.favorlock.donortitles.cmd.CommandHandler;
import com.favorlock.donortitles.cmd.commands.ClearTitle;
import com.favorlock.donortitles.cmd.commands.CreateTitle;
import com.favorlock.donortitles.cmd.commands.DeleteTitle;
import com.favorlock.donortitles.cmd.commands.GrantTitle;
import com.favorlock.donortitles.cmd.commands.RevokeTitle;
import com.favorlock.donortitles.cmd.commands.SetTitle;
import com.favorlock.donortitles.config.Configuration;
import com.favorlock.donortitles.db.SQLDatabase;

public class DonorTitles extends JavaPlugin {
	private final CommandHandler commandHandler = new CommandHandler(this);
	public static Permission perms = null;
    public static Chat chat = null;
	
	public void onEnable() {
		// Load the configuration.
		new Configuration(this);
		
		try {
			new SQLDatabase(this);
			
			if(!(SQLDatabase.isDBSchemaValid() || SQLDatabase.setupTables())) {
				getLogger().log(Level.SEVERE, "Unable to validate schema or create proper tables." +
												" Plugin will not load until issue is fixed.");
				disable();
			}
		} catch (SQLException ex) {
			getLogger().log(Level.SEVERE, "Error loading database during initialization.\n" +
											ex.getMessage());
			ex.printStackTrace();
			disable();
		}
		
		if (this.isEnabled()) {
			setupCommands();
			setupChat();
			setupPermissions();
		}
	}
	
	public void onDisable() {
		getServer().getScheduler().cancelTask(SQLDatabase.taskID);
	}
	
	public void disable() {
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public CommandHandler getCommandHandler() {
		return this.commandHandler;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return this.commandHandler.dispatch(sender, command.getName(), label, args);
	}
	
	private void setupCommands() {
		this.commandHandler.addCommand(new ClearTitle(this));
		this.commandHandler.addCommand(new CreateTitle(this));
		this.commandHandler.addCommand(new DeleteTitle(this));
		this.commandHandler.addCommand(new GrantTitle(this));
		this.commandHandler.addCommand(new RevokeTitle(this));
		this.commandHandler.addCommand(new SetTitle(this));
	}
	
    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

}
