package me.mrCookieSlime.QuestWorld.quest;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.QuestWorld.api.MissionType;
import me.mrCookieSlime.QuestWorld.event.CancellableEvent;
import me.mrCookieSlime.QuestWorld.event.MissionChangeEvent;
import me.mrCookieSlime.QuestWorld.manager.ProgressTracker;
import me.mrCookieSlime.QuestWorld.util.BitFlag;
import me.mrCookieSlime.QuestWorld.util.BitFlag.BitString;

class MissionState extends Mission {
	private long changeBits = 0;
	private Mission origin;
	
	public MissionState(Mission source) {
		super(source);
		origin = source;
	}
	
	@Override
	public MissionState getState() {
		return this;
	}
	
	//// IMissionWrite
	@Override
	public void setAmount(int amount) {
		super.setAmount(amount);
		changeBits |= BitFlag.getBits(Member.AMOUNT);
	}

	@Override
	public void setCustomInt(int val) {
		super.setCustomInt(val);
		changeBits |= BitFlag.getBits(Member.CUSTOM_INT);
	}

	@Override
	public void setCustomString(String customString) {
		super.setCustomString(customString);
		changeBits |= BitFlag.getBits(Member.CUSTOM_STRING);
	}

	@Override
	public void setDeathReset(boolean deathReset) {
		super.setDeathReset(deathReset);
		changeBits |= BitFlag.getBits(Member.DEATH_RESET);
	}

	@Override
	public void setDescription(String description) {
		super.setDescription(description);
		changeBits |= BitFlag.getBits(Member.DESCRIPTION);
	}
	
	@Override
	public void setDialogue(List<String> dialogue) {
		super.setDialogue(dialogue);
		changeBits |= BitFlag.getBits(Member.DIALOGUE);
	}
	
	@Override
	public void setDisplayName(String name) {
		super.setDisplayName(name);
		changeBits |= BitFlag.getBits(Member.CUSTOM_STRING);
	}

	@Override
	public void setEntity(EntityType entity) {
		super.setEntity(entity);
		changeBits |= BitFlag.getBits(Member.ENTITY);
	}
	
	@Override
	public void setItem(ItemStack item) {
		super.setItem(item);
		changeBits |= BitFlag.getBits(Member.ITEM);
	}
	
	@Override
	public void setLocation(Location loc) {
		super.setLocation(loc);
		changeBits |= BitFlag.getBits(Member.LOCATION);
	}

	@Override
	public void setSpawnerSupport(boolean acceptsSpawners) {
		super.setSpawnerSupport(acceptsSpawners);
		changeBits |= BitFlag.getBits(Member.SPAWNER_SUPPORT);
	}

	@Override
	public void setTimeframe(int timeframe) {
		super.setTimeframe(timeframe);
		changeBits |= BitFlag.getBits(Member.TIMEFRAME);
	}

	@Override
	public void setType(MissionType type) {
		// TODO RIP migrateFrom
		if(true) {
			loadDefaults();
			changeBits |= BitString.ALL;
		}
		super.setType(type);
		changeBits |= BitFlag.getBits(Member.TYPE);
	}
	
	@Override
	public boolean apply() {
		if(sendEvent()) {
			copyTo(origin);
			origin.updateLastModified();
			ProgressTracker.saveDialogue(origin);
			changeBits = 0;
			return true;
		}
		return false;
	}

	@Override
	public boolean discard() {
		if(changeBits != 0) {
			copy(origin);
			changeBits = 0;
			return true;
		}
		return false;
	}
	
	@Override
	public Mission getSource() {
		return origin;
	}
	
	@Override
	public boolean hasChange(Member field) {
		return (changeBits & BitFlag.getBits(field)) != 0;
	}
	
	private boolean sendEvent() {
		return changeBits != 0 && CancellableEvent.send(new MissionChangeEvent(this));
	}
}
