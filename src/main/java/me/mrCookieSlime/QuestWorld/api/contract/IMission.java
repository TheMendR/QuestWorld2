package me.mrCookieSlime.QuestWorld.api.contract;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.QuestWorld.api.MissionType;
import me.mrCookieSlime.QuestWorld.api.annotation.NoImpl;

@NoImpl
public interface IMission extends IStateful {
	int          getAmount();
	int          getCustomInt();
	String       getCustomString();
	boolean      getDeathReset();
	String       getDescription();
	List<String> getDialogue();
	String       getDisplayName();
	EntityType   getEntity();
	ItemStack    getItem();
	Location     getLocation();
	boolean      getSpawnerSupport();
	int          getTimeframe();
	MissionType  getType();

	int          getIndex();
	IQuest       getQuest();
	ItemStack     getDisplayItem();
	
	@Override
	IMissionState getState();
	String        getText();
}
