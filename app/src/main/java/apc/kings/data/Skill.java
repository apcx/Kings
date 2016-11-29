package apc.kings.data;

public class Skill {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_PHYSICAL = 1;
    public static final int TYPE_MAGIC = 2;
    public static final int TYPE_REAL = 3;

    public String name;
    public int cd;
    public int factorType;
    public int damageType;
    public double swing;
    public double damageFactor;
    public double damageBonus;

    public Skill(String name, int cd, double swing) {
        this.name = name;
        this.cd = cd;
        this.swing = swing;
    }

    public Skill(String name, int cd, double swing, double damageFactor, double damageBonus, int factorType, int damageType) {
        this.name = name;
        this.cd = cd;
        this.swing = swing;
        this.damageFactor = damageFactor;
        this.damageBonus = damageBonus;
        this.factorType = factorType;
        this.damageType = damageType;
    }
}
