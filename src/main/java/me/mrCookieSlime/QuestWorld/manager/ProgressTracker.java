package me.mrCookieSlime.QuestWorld.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import me.mrCookieSlime.QuestWorld.QuestWorldPlugin;
import me.mrCookieSlime.QuestWorld.api.QuestStatus;
import me.mrCookieSlime.QuestWorld.api.contract.IMission;
import me.mrCookieSlime.QuestWorld.api.contract.IMissionState;
import me.mrCookieSlime.QuestWorld.api.contract.IQuest;

public class ProgressTracker {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	private final File configFile;
	private final YamlConfiguration config;
	
	private static File fileFor(UUID uuid) {
		return new File(QuestWorldPlugin.getPath("data.player"), uuid.toString() + ".yml");
	}
	
	public static boolean exists(UUID uuid) {
		return fileFor(uuid).exists();
	}
	
	public ProgressTracker(UUID uuid) {
		configFile = fileFor(uuid);
		config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static UUID tryUUID(String uuid) {
		if(uuid != null)
			try {
				return UUID.fromString(uuid);
			}
			catch(IllegalArgumentException e) {
			}
		
		return null;
	}
	
	//// PARTY
	public UUID getPartyLeader() {
		return tryUUID(config.getString("party.associated", null));
	}
	
	public void setPartyLeader(UUID uuid) {
		config.set("party.associated", uuid.toString());
	}
	
	public List<UUID> getPartyMembers() {
		return Lists.transform(config.getStringList("party.members"), s -> tryUUID(s));
	}
	
	public void setPartyMembers(List<UUID> members) {
		config.set("party.members", Lists.transform(members, uuid -> uuid.toString()));
	}
	
	public List<UUID> getPartyPending() {
		return Lists.transform(config.getStringList("party.pending-requests"), s -> tryUUID(s));
	}
	
	public void setPartyPending(List<UUID> pending) {
		config.set("party.pending-requests", Lists.transform(pending, uuid -> uuid.toString()));
	}
	
	//// QUEST
	private static String path(IQuest quest) {
		return quest.getCategory().getID() + "." + quest.getID();
	}
	
	public long getQuestRefresh(IQuest quest) {
		long result = -1;
		String end = config.getString(path(quest) + ".cooldown", null);
		if(end != null)
			try {
				result = dateFormat.parse(end).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		return result;
	}
	
	public void setQuestRefresh(IQuest quest, long until) {
		config.set(path(quest) + ".cooldown", dateFormat.format(until));
	}
	
	public QuestStatus getQuestStatus(IQuest quest) {
		QuestStatus result = QuestStatus.AVAILABLE;
		String status = config.getString(path(quest) + ".status", null);
		if(status != null)
			try {
				result = QuestStatus.valueOf(status.toUpperCase());
			}
			catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
		
		return result;
	}
	
	public void setQuestStatus(IQuest quest, QuestStatus state) {
		config.set(path(quest) + ".status", state.toString());
	}
	
	public boolean isQuestFinished(IQuest quest) {
		return config.getBoolean(path(quest) + ".finished", false);
	}
	
	public void setQuestFinished(IQuest quest, boolean state) {
		config.set(path(quest) + ".finished", state);
	}
	
	public void clearQuest(IQuest quest) {
		config.set(path(quest), null);
	}
	
	
	//// MISSION
	private static String path(IMission mission) {
		return path(mission.getQuest()) + ".mission." + mission.getIndex();
	}
	
	public static File dialogueFile(IMission mission) {
		return new File(QuestWorldPlugin.getPath("data.dialogue"), mission.getQuest().getCategory().getID()
				+ "+" + mission.getQuest().getID() + "+" + mission.getIndex() + ".txt");
	}
	
	public static void saveDialogue(IMission mission) {
		File file = dialogueFile(mission);
		if(file.exists())
			file.delete();
		
		try {
			// The only downside to this is system-specific newlines
			Files.write(file.toPath(), mission.getDialogue(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadDialogue(IMissionState mission) {
		File file = dialogueFile(mission);
		if (file.exists()) {
			try {
				mission.setDialogue(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public int getMissionProgress(IMission mission) {
		return config.getInt(path(mission) + ".progress", 0);
	}
	
	public void setMissionProgress(IMission mission, int progress) {
		config.set(path(mission) + ".progress", progress);
	}
	
	public long getMissionCompleted(IMission mission) {
		return config.getLong(path(mission) + ".complete-until", 0);
	}
	
	public void setMissionCompleted(IMission mission, Long time) {
		config.set(path(mission) + ".complete-until", time);
	}
}
