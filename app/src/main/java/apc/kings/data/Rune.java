package apc.kings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import apc.kings.R;

public class Rune {

    public static Rune[] ALL_RUNES = new Rune[] {
            new Rune("无双", R.id.rune_red,     0,  0,  0,  0, 0,  0, 0,  0,  0,  7, 36, 0,  0),
            new Rune("祸源", R.id.rune_red,     0,  0,  0,  0, 0,  0, 0,  0,  0, 16,  0, 0,  0),
            new Rune("红月", R.id.rune_red,     0,  0,  0,  0, 0,  0, 0,  0, 16,  5,  0, 0,  0),
            new Rune("异变", R.id.rune_red,     0, 20,  0,  0, 0, 36, 0,  0,  0,  0,  0, 0,  0),
            new Rune("传承", R.id.rune_red,     0, 32,  0,  0, 0,  0, 0,  0,  0,  0,  0, 0,  0),
            new Rune("纷争", R.id.rune_red,     0, 25,  0,  0, 0,  0, 0,  0,  0,  0,  0, 0,  0),
            new Rune("圣人", R.id.rune_red,     0,  0, 53,  0, 0,  0, 0,  0,  0,  0,  0, 0,  0),
            new Rune("宿命", R.id.rune_red,   337,  0,  0, 23, 0,  0, 0,  0, 10,  0,  0, 0,  0),
            new Rune("隐匿", R.id.rune_blue,    0, 16,  0,  0, 0,  0, 0, 10,  0,  0,  0, 0,  0),
            new Rune("狩猎", R.id.rune_blue,    0,  0,  0,  0, 0,  0, 0, 10, 10,  0,  0, 0,  0),
            new Rune("兽痕", R.id.rune_blue,  600,  0,  0,  0, 0,  0, 0,  0,  0,  5,  0, 0,  0),
            new Rune("夺萃", R.id.rune_blue,    0,  0,  0,  0, 0,  0, 0,  0,  0,  0,  0, 0,  0),
            new Rune("调和", R.id.rune_blue,  450,  0,  0,  0, 0,  0, 0,  0,  0,  0,  0, 0, 52),
            new Rune("鹰眼", R.id.rune_green,   0,  9,  0,  0, 0, 64, 0,  0,  0,  0,  0, 0,  0),
            new Rune("虚空", R.id.rune_green, 375,  0,  0,  0, 0,  0, 0,  0,  0,  0,  0, 6,  0),
            new Rune("霸者", R.id.rune_green,   0,  0,  0, 90, 0,  0, 0,  0,  0,  0,  0, 6,  0),
    };

    private static final Map<String, Rune> map = new ArrayMap<>();
    static {
        for (Rune rune : ALL_RUNES) {
            map.put(rune.name, rune);
        }
    }

    public String name;
    public int category;
    public int hp;
    public int attack;
    public int magic;
    public int defense;
    public int magic_defense;
    public int penetrate;
    public int magic_penetrate;
    public int move_speed;
    public int attack_speed;
    public int critical;
    public int critical_damage;
    public int cdr;
    public int regen;

    public Rune(String name, int category, int hp, int attack, int magic, int defense, int magic_defense, int penetrate, int magic_penetrate, int move_speed, int attack_speed, int critical, int critical_damage, int cdr, int regen) {
        this.name = name;
        this.category = category;
        this.hp = hp;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.magic_defense = magic_defense;
        this.penetrate = penetrate;
        this.magic_penetrate = magic_penetrate;
        this.move_speed = move_speed;
        this.attack_speed = attack_speed;
        this.critical = critical;
        this.critical_damage = critical_damage;
        this.cdr = cdr;
        this.regen = regen;
    }

    @Nullable
    public static Rune findRune(@NonNull String name) {
        return map.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
