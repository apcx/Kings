package apc.kings.data;

public class Skill {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_PHYSICAL = 2;
    public static final int TYPE_MAGIC = 3;
    public static final int TYPE_REAL = 4;

    public String name;
    public int cd;
    public int factorType;
    public int damageType;
    public int swing;
    public double damageFactor;
    public int damageBonus;

    public Skill(String name, int cd, int swing) {
        this.name = name;
        this.cd = cd;
        this.swing = swing;
    }

    public Skill(String name, int cd, int swing, double damageFactor, int damageBonus, int factorType, int damageType) {
        this.name = name;
        this.cd = cd;
        this.swing = swing;
        this.damageFactor = damageFactor;
        this.damageBonus = damageBonus;
        this.factorType = factorType;
        this.damageType = damageType;
    }
}
