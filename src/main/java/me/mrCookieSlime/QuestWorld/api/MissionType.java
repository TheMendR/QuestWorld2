package me.mrCookieSlime.QuestWorld.api;

import me.mrCookieSlime.QuestWorld.api.contract.IMission;
import me.mrCookieSlime.QuestWorld.api.contract.IMissionState;
import me.mrCookieSlime.QuestWorld.api.menu.Menu;
import me.mrCookieSlime.QuestWorld.api.menu.MenuData;
import me.mrCookieSlime.QuestWorld.api.menu.MissionButton;
import me.mrCookieSlime.QuestWorld.util.Log;
import me.mrCookieSlime.QuestWorld.util.Text;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public abstract class MissionType {
	private String name;
	private ItemStack selectorItem;
	private boolean supportsTimeframes;
	private Map<Integer, MenuData> menuData;
	
	public MissionType(String name, boolean supportsTimeframes, ItemStack item) {
		Log.fine("MissionType - Creating: " + name);
		this.name = name;
		this.selectorItem = item;
		this.supportsTimeframes = supportsTimeframes;
		menuData = new HashMap<>();
	}
	
	public final String userDescription(IMission instance) {
		return userInstanceDescription(instance) + formatTimeframe(instance) + formatDeathReset(instance);
	}
	
	protected abstract String userInstanceDescription(IMission instance);
	
	public abstract ItemStack userDisplayItem(IMission instance);
	
	private String formatTimeframe(IMission instance) {
		if(instance.getTimeframe() == 0 || !supportsTimeframes)
			return "";
		long duration = instance.getTimeframe();
		
		return " &7within " + Text.timeFromNum(duration);
	}
	
	private String formatDeathReset(IMission instance) {
		if(!instance.getDeathReset() || !supportsDeathReset())
			return "";
		
		return " &7without dying";
	}

	public ItemStack getSelectorItem() {
		return selectorItem;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean supportsTimeframes() {
		return supportsTimeframes;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public boolean supportsDeathReset() {
		return this instanceof Decaying;
	}

	protected void setName(String newName) {
		name = newName;
	}
	
	public boolean attemptUpgrade(IMissionState instance) {
		return false;
	}
	
	protected void setSelectorItem(ItemStack material) {
		selectorItem = material.clone();
	}
	
	public String progressString(int current, int total) {
		float percent = current / (float)total;
		return Math.round(percent * 100) + "% (" + current + "/" + total + ")";
	}
	
	public final MenuData getButton(int index) {
		return menuData.get(index);
	}
	
	public final MenuData putButton(int index, MenuData data) {
		return menuData.put(index, data);
	}
	
	public final MenuData removeButton(int index) {
		return menuData.remove(index);
	}
	
	public final void buildMenu(IMissionState changes, Menu menu) {
		layoutMenu(changes);
		for(Map.Entry<Integer, MenuData> entry : menuData.entrySet())
			menu.put(entry.getKey(), entry.getValue().getItem(), entry.getValue().getHandler());
	}
	
	protected void layoutMenu(IMissionState changes) {
		if(supportsDeathReset()) putButton(5, MissionButton.deathReset(changes));
		if(supportsTimeframes()) putButton(6, MissionButton.timeframe(changes));
		putButton(7, MissionButton.missionName(changes));
		putButton(8, MissionButton.dialogue(changes));
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
