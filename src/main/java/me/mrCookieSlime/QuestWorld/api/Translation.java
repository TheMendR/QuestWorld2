package me.mrCookieSlime.QuestWorld.api;

public enum Translation implements Translator {
	DEFAULT_CATEGORY("defaults.category.name"),
	DEFAULT_QUEST   ("defaults.quest.name"),
	DEFAULT_MISSION ("defaults.mission.name"),
	DEFAULT_PREFIX  ("defaults.prefix"),
	
	GUIDE_BOOK("guide-book"),
	
	NAV_ITEM(   "navigation.item", "page", "pages", "pre-next", "pre-last"),
	NAV_NEXT(   "navigation.prefix-next"),
	NAV_PREV(   "navigation.prefix-last"),
	NAV_NEXTBAD("navigation.prefix-next-inactive"),
	NAV_PREVBAD("navigation.prefix-last-inactive"),
	
	CATEGORY_CREATED  ("editor.category.created",     "name"),
	CATEGORY_DELETED  ("editor.category.deleted",     "name"),
	CATEGORY_NAME_EDIT("editor.category.name-change", "name"),
	CATEGORY_NAME_SET ("editor.category.name-set",    "name", "name_old"),
	CATEGORY_PERM_EDIT("editor.category.perm-change", "name", "perm"),
	CATEGORY_PERM_SET ("editor.category.perm-set",    "name", "perm", "perm_old"),
	CATEGORY_DESC     ("editor.category.description", "total", "completed", "available", "cooldown", "reward", "progress"),
	
	QUEST_CREATED  ("editor.quest.created",     "name"),
	QUEST_DELETED  ("editor.quest.deleted",     "name"),
	QUEST_NAME_EDIT("editor.quest.name-change", "name"),
	QUEST_NAME_SET ("editor.quest.name-set",    "name", "name_old"),
	QUEST_PERM_EDIT("editor.quest.perm-change", "name", "perm"),
	QUEST_PERM_SET ("editor.quest.perm-set",    "name", "perm", "perm_old"),

	// TODO Description is only used by QW-Citizens, potentially export to CitizenTranslation
	MISSION_DESC_EDIT("editor.misssion-description"),
	MISSION_DESC_SET("editor.mission.desc-set", "name", "desc"),
	MISSION_DIALOG_ADD("editor.add-dialogue"),
	MISSION_DIALOG_SET("editor.set-dialogue", "path"),
	MISSION_NAME_EDIT("editor.await-mission-name"),
	MISSION_NAME_SET ("editor.edit-mission-name"),
	
	KILLMISSION_NAME_EDIT("editor.rename-kill-mission"),
	KILLMISSION_NAME_SET ("editor.renamed-kill-type"),
	LOCMISSION_NAME_EDIT("editor.rename-location"),
	LOCMISSION_NAME_SET ("editor.renamed-location"),

	PARTY_ERROR_FULL  ("party.full",       "max"),
	PARTY_ERROR_ABSENT("party.not-online", "name"),
	PARTY_ERROR_MEMBER("party.already",    "name"),
	PARTY_PLAYER_PICK ("party.invite"),
	PARTY_PLAYER_ADD  ("party.invited",    "name"),
	PARTY_PLAYER_JOIN ("party.join",       "name"),
	PARTY_PLAYER_KICK ("party.kicked",     "name"),
	PARTY_GROUP_INVITE("party.invitation", "name"),
	PARTY_GROUP_JOIN  ("party.joined",     "name"),

	NOTIFY_COMPLETED ("notifications.task-completed",         "quest"),
	NOTIFY_TIME_FAIL ("notifications.task-failed-timeframe",  "quest"),
	NOTIFY_TIME_START("notifications.task-timeframe-started", "task", "time"),
	
	// TODO This is hacky, look again when less tired
	gui_title,
	gui_party,
	button_open,
	button_back_party,
	button_back_quests,
	button_back_general,
	quests_locked,
	quests_locked_in_world("quests.locked-in-world"),
	quests_tasks_completed("quests.tasks_completed"),
	quests_state_cooldown,
	quests_state_completed,
	quests_state_reward_claimable("quests.state.reward_claimable"),
	quests_state_reward_claim("quests.state.reward_claim"),
	quests_display_cooldown,
	quests_display_monetary,
	quests_display_exp,
	quests_display_rewards,
	task_locked,
	;
	
	private String path;
	private String[] placeholders;
	Translation(String path, String... placeholders) {
		this.path = path;
		this.placeholders = wrap(placeholders);
	}
	
	Translation() {
		path = name().replace('_', '.');
		placeholders = new String[0];
	}
	
	@Override
	public String path() {
		return path;
	}
	
	@Override
	public String[] placeholders() {
		return placeholders;
	}
	
	@Override
	public String toString() {
		String result = path;
		if(placeholders.length > 0) {
			result += "[";
			for(String s : placeholders)
				result += s + ", ";
			result = result.substring(0, result.length()-2) + "]";
		}
		
		return result;
	}
}
