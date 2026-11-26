package dev.travellerscompanions;

public enum CharacterKind {
    VARYS("Varys","The Farwatch",24,0x83C9AD,"Once a scout of the vanished Green Road, Varys learned to read danger in a broken twig. He follows travellers who leave a path worth remembering.",
            new String[]{"Keep moving. I have the high ground.","One arrow, one less worry.","You chose the wrong trail."},
            new String[]{"That came closer than I liked.","Still standing. Keep your eyes forward."}),
    RAESHEL("Raeshel","The Unbroken",28,0xDBAB80,"Raeshel walked away from a mercenary company rather than burn a village. Her sword is for hire; her conscience never was.",
            new String[]{"Stay behind my blade!","Come on, then. Try me.","No one touches my companion."},
            new String[]{"I've survived worse.","You'll have to do better than that."}),
    GIDEON("Gideon","The Last Bulwark",36,0x89AFD8,"The last defender of a fallen watchtower, Gideon carries its oath on his shield: no traveller abandoned, no road left unguarded.",
            new String[]{"By my oath, you will not pass!","Rally to my shield!","Stand firm. We face this together."},
            new String[]{"My shield holds. So do I.","A little dent is no defeat."}),
    CASTELAN("Castelan","The Roadwarden",32,0xC7A5DE,"Castelan once guarded a royal road that no longer appears on any map. He still walks its forgotten milestones, spear in hand, searching for a worthy destination.",
            new String[]{"At spear's length, if you please.","This road is under my protection.","Hold the line with me!"},
            new String[]{"A scratch on a long journey.","Distance. I need a little distance."}),
    MARA("Mara","The Surebolt",26,0xD5B075,"Mara once escorted supply caravans through the mountain passes. When the roads fell to monsters, she stayed to bring the stranded travellers home, one careful crossbow shot at a time.",
            new String[]{"Hold steady. I've got the shot.","One bolt will do.","Keep clear of my sights."},
            new String[]{"Still here. Still aiming.","I'll catch my breath after this."}),
    LEONARDO("Leonardo","The Wayward Scholar",24,0xA9A1EA,"Leonardo left a locked academy with an empty spellbook and a promise to learn from the world. Each recovered scroll is another way to keep a fellow traveller safe.",
            new String[]{"A practical demonstration, then!","Stand clear of the spell!","Let's put that theory to work."},
            new String[]{"That was not in my calculations.","A moment. I can mend this."}),
    YURIKO("Yuriko","The Still Blade",30,0xE4A5BC,"Yuriko guarded a mountain shrine until its last pilgrim reached safety. Her katana follows a quiet discipline: read the blow, turn it aside, and answer only when the opening is true.",
            new String[]{"Watch the blade, not my eyes.","Your opening is mine.","Stay close. I'll turn their strikes."},
            new String[]{"My guard will be tighter next time.","One breath. Then we continue."}),
    YUSHIRO("Yushiro","The Mountain's Edge",34,0xB9CC92,"Yushiro carries a blade once used to defend a remote mountain pass. Its weight rewards patience: a measured guard, a committed cut, and a path cleared for those behind him.",
            new String[]{"Give me room to draw.","The mountain does not yield.","One cut to clear the road."},
            new String[]{"I'm not finished.","A heavy blade needs steady feet."});
    // Append new characters: these indices are stored in existing worlds.
    public static final int COUNT=values().length;
    public final String display,title,lore;public final int health,legacyHealth,color;public final String[] combat,hurt;
    CharacterKind(String display,String title,int health,int color,String lore,String[] combat,String[] hurt){this.display=display;this.title=title;this.legacyHealth=health;this.health=Math.max(100,health);this.color=color;this.lore=lore;this.combat=combat;this.hurt=hurt;}
    public String key(){return name().toLowerCase(java.util.Locale.ROOT);}
    public static CharacterKind of(int i){return values()[Math.max(0,Math.min(COUNT-1,i))];}
    public float maxHealth(int level){return health+2*(level-1);}
    /** Upgrade old saves once, preserving missing health instead of leaving healthy travellers injured. */
    public float restoredHealth(net.minecraft.nbt.CompoundTag snapshot,int level){
        float oldMax=snapshot.contains("MaxHealth")?snapshot.getFloat("MaxHealth"):legacyHealth+2*(level-1);
        return Math.max(1,Math.min(maxHealth(level),snapshot.getFloat("Health")+Math.max(0,maxHealth(level)-oldMax)));
    }
    public static int trainingCost(int level){return 4+2*(level-1);}
    public String dialogue(int level,int variant){
        String[][] lines={
            {"Listen to the woods before you trust the road.","I used to count my arrows. These days, I count the people who make it home.","The Green Road wasn't lost. Someone erased it. Someday, we'll find out why.","Wherever this trail ends, you won't walk its last mile alone."},
            {"A sharp blade is useful. A clear purpose is better.","My old captain called mercy a weakness. Funny how afraid he was of it.","I kept one coin from that company. A reminder of what I won't sell again.","I used to fight for a purse. Now I fight for us. It's a better bargain."},
            {"Check your straps. Courage cannot mend a loose buckle.","There were twelve of us at the tower. I still remember every name.","An oath survives a fortress. That's what the others trusted me to learn.","You gave my oath a future. You have my shield, to the very end."},
            {"Every road was someone's brave idea once.","The milestones are older than the kingdom. I wonder what they remember.","I was ordered to guard an empty road. Then I learned it led to people, not palaces.","I think I've found my destination. It keeps moving, right beside you."},
            {"Breathe out before the shot. Rushing only wastes a bolt.","The mountain caravans taught me patience. And how to sleep with one eye open.","I went back for the last wagon. Everyone said the pass was lost. The people in it weren't.","Whatever waits beyond the next ridge, I'll watch your back. That's a promise."},
            {"A scroll is only the beginning of understanding.","The academy forbade fieldwork. I took that as an invitation.","Bring me the spells you find. Knowledge deserves to travel.","Whatever we discover, I will use it to bring us both home."},
            {"Stillness gives you time to see the next strike.","At the shrine, we practised turning rain with wooden blades.","My teacher called defence a promise to someone standing behind you.","You have my promise. No road will find you unguarded."},
            {"Leave enough room for the draw.","This blade is older than the bridge we just crossed.","I once held the pass alone. I prefer travelling beside someone.","Where you plant your feet, I will stand with you."}
        };
        int unlocked=level>=15?4:level>=8?3:level>=3?2:1;
        return lines[ordinal()][Math.floorMod(variant,unlocked)];
    }
}
