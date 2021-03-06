package me.mrCookieSlime.QuestWorld.command;

import java.util.UUID;

import me.mrCookieSlime.QuestWorld.api.QuestWorld;
import me.mrCookieSlime.QuestWorld.api.Translation;
import me.mrCookieSlime.QuestWorld.api.contract.ICategory;
import me.mrCookieSlime.QuestWorld.api.menu.QuestBook;
import me.mrCookieSlime.QuestWorld.container.PagedMapping;
import me.mrCookieSlime.QuestWorld.manager.PlayerManager;
import me.mrCookieSlime.QuestWorld.party.Party;
import me.mrCookieSlime.QuestWorld.util.PlayerTools;
import me.mrCookieSlime.QuestWorld.util.Text;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			if (args.length == 0) QuestBook.openLastMenu(p);
			else {
				if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
					Party party = PlayerManager.of(UUID.fromString(args[1])).getParty();
					if (party != null && party.hasInvited(p)) {
						int maxParty = QuestWorld.getPlugin().getConfig().getInt("party.max-members");
						if (party.getSize() >= maxParty) {
							PlayerTools.sendTranslation(sender, true, Translation.PARTY_ERROR_FULL, Integer.toString(maxParty));
						}
						else party.playerJoin(p);
					}
				}
				else {
					int c_id = -1;
					int q_id = -1;
					try {
						c_id = Integer.parseInt(args[0]);
						q_id = Integer.parseInt(args[1]);
					}
					catch(NumberFormatException exception) {
						sender.sendMessage(Text.colorize("&4Error: invalid number for category (", args[0], ") or quest (", args[1], ")"));
						return true;
					}
					
					ICategory category = QuestWorld.getFacade().getCategory(c_id);
					if (category != null)  {
						PagedMapping.clearPages(p);
						if (args.length == 2)
							QuestBook.openQuest(p, category.getQuest(q_id), false, false);
						else
							QuestBook.openCategory(p, category, false);
					}
					else
						sender.sendMessage(Text.colorize("&4Unknown Category: &c", args[0]));
				}
			}
		}
		else
			sender.sendMessage(Text.colorize("&4You are not a Player"));
		return true;
	}

}
