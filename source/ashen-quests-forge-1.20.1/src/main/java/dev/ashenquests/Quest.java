package dev.ashenquests;

import java.util.List;

public record Quest(String id, String title, String chapter, String section, String story, String aftermath,
                    String icon, List<String> requires, List<Objective> objectives, List<Reward> rewards,
                    int emeralds, int xp, String faction, int standing, int cooldown, List<Choice> choices, String giver) {
    public record Objective(String type, String target, int count, String label, String dialogue) {
        public Objective(String type,String target,int count,String label) { this(type,target,count,label,null); }
    }
    public record Reward(String item, int count) {}
    public record Choice(String title, String result) {}
    public boolean repeatable() { return cooldown > 0; }
    public String giverId() { return giver == null || giver.isBlank() ? "eldara" : giver; }
    public String giverName() { return Character.toUpperCase(giverId().charAt(0)) + giverId().substring(1); }
}
