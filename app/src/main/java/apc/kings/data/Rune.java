package apc.kings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import apc.kings.R;

public class Rune {

    public static Rune[] ALL_RUNES = new Rune[] {
            new Rune("无双", R.id.rune_red,   0, 0,   0, 0, 0, 0,   0, 0, 0,   0.7, 3.6, 0),
            new Rune("祸源", R.id.rune_red,   0, 0,   0, 0, 0, 0,   0, 0, 0,   1.6, 0,   0),
            new Rune("红月", R.id.rune_red,   0, 0,   0, 0, 0, 0,   0, 0, 1.6, 0.5, 0,   0),
            new Rune("异变", R.id.rune_red,   0, 2,   0, 0, 0, 3.6, 0, 0, 0,   0,   0,   0),
            new Rune("传承", R.id.rune_red,   0, 3.2, 0, 0, 0, 0,   0, 0, 0,   0,   0,   0),
            new Rune("纷争", R.id.rune_red,   0, 2.5, 0, 0, 0, 0,   0, 0, 0,   0,   0,   0),
            new Rune("隐匿", R.id.rune_blue,  0, 1.6, 0, 0, 0, 0,   0, 0, 0,   0,   0,   0),
            new Rune("狩猎", R.id.rune_blue,  0, 0,   0, 0, 0, 0,   0, 0, 1,   0,   0,   0),
            new Rune("兽痕", R.id.rune_blue, 60, 0,   0, 0, 0, 0,   0, 0, 1,   0.5, 0,   0),
            new Rune("夺萃", R.id.rune_blue,  0, 0,   0, 0, 0, 0,   0, 0, 0,   0,   0,   0),
            new Rune("鹰眼", R.id.rune_green, 0, 0.9, 0, 0, 0, 6.4, 0, 0, 0,   0,   0,   0),
    };

    public String name;
    public int category;
    public int hp;
    public double attack;
    public double magic;
    public double defense;
    public double magicDefense;
    public double penetrate;
    public double magicPenetrate;
    public double regen;
    public double attackSpeed;
    public double critical;
    public double criticalDamage;
    public double cdr;

    public Rune(String name, int category, int hp, double attack, double magic, double defense, double magicDefense, double penetrate, double magicPenetrate, double regen, double attackSpeed, double critical, double criticalDamage, double cdr) {
        this.name = name;
        this.category = category;
        this.hp = hp;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.magicDefense = magicDefense;
        this.penetrate = penetrate;
        this.magicPenetrate = magicPenetrate;
        this.regen = regen;
        this.attackSpeed = attackSpeed;
        this.critical = critical;
        this.criticalDamage = criticalDamage;
        this.cdr = cdr;
    }

    @Nullable
    public static Rune findRune(@NonNull String name) {
        for (Rune item : ALL_RUNES) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return null;
    }
}
