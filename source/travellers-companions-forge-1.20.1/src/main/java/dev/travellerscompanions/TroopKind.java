package dev.travellerscompanions;

/** Separate from CharacterKind: saved named-character indexes must never change. */
public enum TroopKind {
    FOOTSQUIRE("Footsquire", "Footsquires", CharacterKind.RAESHEL, "Four sword-bearing companions for the road."),
    FOOTPALADIN("Footpaladin", "Footpaladins", CharacterKind.CASTELAN, "Four spear-and-shield companions. They raise their shields between attacks to block frontal blows."),
    FOOTARCHER("Footarcher", "Footarchers", CharacterKind.VARYS, "Four bow-bearing companions. Each starts with 64 arrows; replace spent ammunition in their backpacks.");

    public static final int SIZE = 4;
    public static final int HEALTH = 20;
    public final String display, plural, description;
    public final CharacterKind combatKind;

    TroopKind(String display, String plural, CharacterKind combatKind, String description) {
        this.display = display;
        this.plural = plural;
        this.combatKind = combatKind;
        this.description = description;
    }

    public static boolean valid(int id) { return id >= 0 && id < values().length; }
    public static TroopKind of(int id) { return values()[Math.max(0, Math.min(values().length - 1, id))]; }
}
