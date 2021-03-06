package me.mrCookieSlime.QuestWorld.api.contract;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.QuestWorld.api.MissionType;
import me.mrCookieSlime.QuestWorld.api.annotation.NoImpl;
import me.mrCookieSlime.QuestWorld.util.BitFlag.BitString;

@NoImpl
public interface IMissionState extends IMission {
	void setAmount(int amount);
	void setCustomString(String customString);
	void setCustomInt(int customInt);
	void setDeathReset(boolean deathReset);
	void setDescription(String description);
	void setDialogue(List<String> dialogue);
	void setDisplayName(String displayName);
	void setEntity(EntityType entity);
	void setItem(ItemStack item);
	void setLocation(Location loc);
	void setSpawnerSupport(boolean acceptsSpawners);
	void setTimeframe(int timeframe);
	void setType(MissionType type);

	boolean  apply();
	boolean  discard();
	boolean  hasChange(Member field);
	IMission getSource();
	
	public enum Member implements BitString {
		QUEST,
		AMOUNT,
		CUSTOM_STRING,
		CUSTOM_INT,
		DEATH_RESET,
		DESCRIPTION,
		DIALOGUE,
		DISPLAY_NAME,
		ENTITY,
		ITEM,
		INDEX,
		LOCATION,
		SPAWNER_SUPPORT,
		TYPE,
		TIMEFRAME,
	}
}
