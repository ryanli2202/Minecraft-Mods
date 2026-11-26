package dev.deepnight;
public enum Weapon {
    DESTINED_BLADE(0,-2.4f,"sword"), JIAN(0,-2.4f,"sword"),
    BERSERKER(1,-3f,"heavy_axe"), PEACEKEEPER(1,-2.6f,"claymore"),
    VANQUISHER(2,-2.8f,"claymore"), GILGAMESH(2,-2.8f,"claymore"),
    GUANDAO(3,-2.8f,"glaive"), TIDEOFAUTUMN(3,-2.4f,"katana"),
    AWAKENEDJINGUBANG(4,-2.4f,"staff"), DRAGONFISTS(4,-1.8f,"fist"),
    INFERNICUS(5,-2.8f,"claymore"), AZAZEL(5,-2.8f,"claymore"), DIVINEJUDGEMENT(5,-2.8f,"claymore"),
    HEAVENLYSKYFALL(6,-2.8f,"scythe"), GANYMEDE(6,-2.6f,"hammer"), STARLIGHTCRESCENDO(6,-2.6f,"rapier"),
    BLOODHOUND(7,-2.6f,"claymore"), BLOODMOON(7,-2.6f,"scythe"), MURAMASA(7,-2.4f,"katana"),
    PRIMORDIS(8,-1.8f,"fist"), TATSUKANOTSURUGI(8,-2.6f,"claymore"), SILENTDANCE(8,-2.4f,"sword"),
    NIGHTFALL(9,-2.8f,"claymore"), TOKOYAMI(9,-2.4f,"katana"), VOIDOCULUS(9,-2.6f,"claymore");
    public final int rank;
    public final float speed;
    public final String preset;
    Weapon(int rank,float speed,String preset){this.rank=rank;this.speed=speed;this.preset=preset;}
    public String id(){return name().toLowerCase(java.util.Locale.ROOT);}
}

