package dev.thedeepnightnpcs;

import java.util.List;

public final class NpcRoster {
    public record Companion(String id,String title,int coat,int trim) {}
    public static final List<Companion> COMPANIONS=List.of(
        new Companion("rowan","Keeper of Dawnstead",0x49604c,0xc8ab6d),
        new Companion("sera","Keeper of the Uncensored Library",0x454979,0xc5b786),
        new Companion("mara","Hearthwright of Dawnstead",0x884d31,0xc69a55),
        new Companion("elian","Captain of the Free Watch",0x586771,0xcbb57c),
        new Companion("iona","Scout of the Far Lanterns",0x365d54,0xb79c62),
        new Companion("tamsin","Healer of the Living Garden",0xd6d0b2,0x557761),
        new Companion("fen","Boatkeeper of the Common Harbor",0x36516b,0xd2bc89),
        new Companion("ysolde","Warden of the Covenant of Wings",0x694757,0xc3a36e)
    );
    public static String name(String kind) { return kind.equals("spire_guard") ? "Spire Guard" : Character.toUpperCase(kind.charAt(0))+kind.substring(1); }
    public static String title(String kind) {
        return switch(kind) {
            case "eldara" -> "The Eternal Sage"; case "caelen" -> "The Monster Hunter";
            case "jorge" -> "The Armskeeper"; case "spire_guard" -> "Spire Watch";
            default -> COMPANIONS.stream().filter(c -> c.id().equals(kind)).map(Companion::title).findFirst().orElse("The Covenant");
        };
    }
}
