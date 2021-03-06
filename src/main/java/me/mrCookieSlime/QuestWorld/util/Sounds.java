package me.mrCookieSlime.QuestWorld.util;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Sounds {
	public final SoundList QUEST_CLICK;
	public final SoundList MISSION_SUBMIT;
	public final SoundList MISSION_REJECT;
	public final SoundList QUEST_REWARD;

	public final SoundList EDITOR_CLICK;
	public final SoundList DIALOG_ADD;
	public final SoundList DESTRUCTIVE_WARN;
	public final SoundList DESTRUCTIVE_CLICK;
	
	public final SoundList PARTY_CLICK;
	
	private final ConfigurationSection config;
	public Sounds(ConfigurationSection config) {
		this.config = config;
		
		QUEST_CLICK    = new SoundList("sounds.quest.click", 1.0f, 0.2f);
		MISSION_SUBMIT = new SoundList("sounds.quest.mission-submit", 0.3f, 0.7f);
		MISSION_REJECT = new SoundList("sounds.quest.mission-reject");
		QUEST_REWARD   = new SoundList("sounds.quest.reward");

		EDITOR_CLICK      = new SoundList("sounds.editor.click", 1.0f, 0.2f);
		DIALOG_ADD        = new SoundList("sounds.editor.dialog-add");
		DESTRUCTIVE_WARN  = new SoundList("sounds.editor.destructive-action-warning");
		DESTRUCTIVE_CLICK = new SoundList("sounds.editor.destructive-action-click", 0.5f, 0.5f);
		
		PARTY_CLICK = new SoundList("sounds.party.click", 1.0f, 0.2f);
	}

	public class SoundList {
		private Sound sound = null;
		private float volume;
		private float pitch;
		
		public SoundList(String path, float volume, float pitch) {
			this.volume = volume;
			this.pitch = pitch;
			
			String soundStr = config.getString(path + ".sound", null);
			
			if(soundStr == null) {
				List<String> sounds = config.getStringList(path + ".list");
				if(!sounds.isEmpty()) {
					soundStr = sounds.get(0);
					Log.warning("Sound path \"" + path + "\" uses an old format, using first sound in list ("+soundStr+")");
					Log.warning("  Please change this from \"list: [sound[, sound, ...]]\" to \"sound: [sound]");
				}
				else {
					Log.severe("No sound found for path \"" + path + "\" in sounds.yml");
					return;
				}
			}
			
			try {
				sound = Sound.valueOf(soundStr.toUpperCase());
			}
			catch(Exception e) {
				Log.severe("The sound \"" + soundStr + "\" is not a valid sound (path = \""+path+"\")");
				return;
			}
			
			if(config.contains(path + ".volume"))
				this.volume = (float) config.getDouble(path + ".volume");

			if(config.contains(path + ".pitch"))
				this.pitch = (float) config.getDouble(path + ".pitch");
		}
		
		public SoundList(String path) {
			this(path, 1.0f, 1.0f);
		}
		
		public void playTo(Player p) {
			p.playSound(p.getLocation(), sound, volume, pitch);
		}
	}
}
