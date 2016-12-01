package apc.kings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import apc.kings.R;

public class Item {

    public static final int SLOTS = 6;

    public static final int FLAG_STORM                 = 0x0001;
    public static final int FLAG_COLD_IRON             = 0x0002;
    public static final int FLAG_FROZEN_HEART          = 0x0004;
    public static final int FLAG_FROST                 = 0x0008;

    public static final int FLAG_PENETRATE             = 0x0010;
    public static final int FLAG_MAGIC_PENETRATE_RATE  = 0x0020;
    public static final int FLAG_MAGIC_PENETRATE_BOOTS = 0x0040;
    public static final int FLAG_MAGIC_PENETRATE_MASK  = 0x0080;

    public static final int FLAG_CUT                   = 0x0100;
    public static final int FLAG_CRITICAL              = 0x0200;
    public static final int FLAG_BOOTS_DEFENSE         = 0x0400;
    public static final int FLAG_EXECUTE               = 0x0800;

    public static final int FLAG_CORRUPT               = 0x1000;
    public static final int FLAG_ACCURATE              = 0x2000;
    public static final int FLAG_LIGHTNING             = 0x4000;
    public static final int FLAG_ENCHANT               = 0x8000;

    public static final Item[] ALL_ITEMS = new Item[] {
            new Item("破灭君主",  R.drawable.weapon_corrupt,    R.id.item_weapon, 2160,    0,  60,   0,   0,   0,   0, 30, 0,    0,    FLAG_CORRUPT),
            new Item("泣血之刃",  R.drawable.weapon_drain,      R.id.item_weapon, 1740,    0, 100,   0,   0,   0,   0,  0, 0,    0,    0),
            new Item("制裁之刃",  R.drawable.weapon_wound,      R.id.item_weapon, 1600,    0, 100,   0,   0,   0,   0,  0, 0,    0,    0),
            new Item("破甲弓",    R.drawable.weapon_penetrate,  R.id.item_weapon, 2100,    0,  80,   0,   0,   0,   0,  0, 0,    0.1,  FLAG_PENETRATE),
            new Item("暗影战斧",  R.drawable.weapon_axe,        R.id.item_weapon, 2090,    0,  85,   0,   0,   0,   0,  0, 0,    0.15, FLAG_CUT),
            new Item("三圣之力",  R.drawable.weapon_trinity,    R.id.item_weapon, 2510,  300,  60,  60,   0,   0,   0, 20, 0.15, 0,    FLAG_ENCHANT),
            new Item("影刃",      R.drawable.weapon_storm,      R.id.item_weapon, 2070,    0,   0,   0,   0,   0,   0, 40, 0.1,  0,    FLAG_STORM),
            new Item("纯净苍穹",  R.drawable.weapon_dispel,     R.id.item_weapon, 2230,    0,   0,   0,   0,   0,   0, 40, 0.2,  0,    FLAG_ACCURATE),
            new Item("闪电匕首",  R.drawable.weapon_lightning,  R.id.item_weapon, 1840,    0,   0,   0,   0,   0,   0, 30, 0.2,  0,    FLAG_LIGHTNING),
            new Item("无尽战刃",  R.drawable.weapon_critical,   R.id.item_weapon, 2140,    0, 100,   0,   0,   0,   0,  0, 0.2,  0,    FLAG_CRITICAL),
            new Item("冰霜长矛",  R.drawable.weapon_frost,      R.id.item_weapon, 1970,  500,  80,   0,   0,   0,   0,  0, 0,    0,    FLAG_FROST),
            new Item("名刀·司命",R.drawable.weapon_curtain,    R.id.item_weapon, 1760,    0,  60,   0,   0,   0,   0,  0, 0,    0.05, 0),
            new Item("破军",      R.drawable.weapon_200,        R.id.item_weapon, 2950,    0, 200,   0,   0,   0,   0,  0, 0,    0,    FLAG_EXECUTE),
            new Item("红莲斗篷",  R.drawable.armor_fire,        R.id.item_armor,  1830, 1200,   0,   0, 240,   0,   0,  0, 0,    0,    0),
            new Item("不祥征兆",  R.drawable.armor_cold_iron,   R.id.item_armor,  2180, 1200,   0,   0, 270,   0,   0,  0, 0,    0,    FLAG_COLD_IRON),
            new Item("霸者重装",  R.drawable.armor_2000,        R.id.item_armor,  2370, 2000,   0,   0,   0,   0, 100,  0, 0,    0,    0),
            new Item("魔女斗篷",  R.drawable.armor_mist,        R.id.item_armor,  2120, 1200,   0,   0,   0, 360,   0,  0, 0,    0,    0),
            new Item("振兴之铠",  R.drawable.armor_heal,        R.id.item_armor,  2100, 1100,   0,   0,   0, 180,  45,  0, 0,    0.1,  0),
            new Item("军团荣耀",  R.drawable.armor_legion,      R.id.item_armor,  1970, 1000,   0,   0, 120, 120,  60,  0, 0,    0,    0),
            new Item("反伤刺甲",  R.drawable.armor_thorns,      R.id.item_armor,  2140,    0,  80,   0, 360,   0,   0,  0, 0,    0,    0),
            new Item("冰封之心",  R.drawable.armor_frozen_heart,R.id.item_armor,  2100,    0,   0,   0, 360,   0,   0,  0, 0,    0.2,  FLAG_FROZEN_HEART),
            new Item("冰脉护手",  R.drawable.armor_guanlets,    R.id.item_armor,  2070,    0,   0,  60, 360,   0,   0,  0, 0,    0.1,  0),
            new Item("暴烈之甲",  R.drawable.armor_berserk,     R.id.item_armor,  1820, 1000,  60,   0,   0,   0,   0,  0, 0,    0,    0),
            new Item("血魔之怒",  R.drawable.armor_fury,        R.id.item_armor,  2120, 1000,  20,   0,   0,   0,   0,  0, 0,    0,    0),
            new Item("奔狼纹章",  R.drawable.armor_horn,        R.id.item_armor,  1530,  400,   0,   0, 100,   0,   0,  0, 0,    0,    0),
            new Item("贤者的庇护",R.drawable.armor_revive,      R.id.item_armor,  2080,    0,   0,   0, 140, 140,   0,  0, 0,    0,    0),
            new Item("急速战靴",  R.drawable.boots_attack,      R.id.item_boots,   710,    0,   0,   0,   0,   0,   0, 15, 0,    0,    0),
            new Item("秘法之靴",  R.drawable.boots_mage,        R.id.item_boots,   790,    0,   0,   0,   0,   0,   0,  0, 0,    0,    FLAG_MAGIC_PENETRATE_BOOTS),
            new Item("冷静之靴",  R.drawable.boots_cd,          R.id.item_boots,   710,    0,   0,   0,   0,   0,   0,  0, 0,    0.1,  0),
            new Item("影忍之足",  R.drawable.boots_defense,     R.id.item_boots,   690,    0,   0,   0, 110,   0,   0,  0, 0,    0,    FLAG_BOOTS_DEFENSE),
            new Item("抵抗之靴",  R.drawable.boots_resist,      R.id.item_boots,   690,    0,   0,   0,   0, 110,   0,  0, 0,    0,    0),
            new Item("疾步之靴",  R.drawable.boots_fly,         R.id.item_boots,   630,    0,   0,   0,   0,   0,   0,  0, 0,    0,    0),
            new Item("贪婪之噬",  R.drawable.mob_attack,        R.id.item_mob,    1460,    0,  45,   0,   0,   0,   0, 12, 0,    0,    0),
            new Item("符文大剑",  R.drawable.mob_magic,         R.id.item_mob,    1490,    0,   0, 100,   0,   0,   0,  0, 0,    0,    0),
            new Item("巨人之握",  R.drawable.mob_hp,            R.id.item_mob,    1500,  800,   0,   0,   0,   0,   0,  0, 0,    0,    0),
    };

    private static final Map<String, Item> map = new ArrayMap<>();
    static {
        for (Item item : ALL_ITEMS) {
            map.put(item.name, item);
        }
    }

    public String name;
    public int imageRes;
    public int category;
    public int price;
    public int hp;
    public int attack;
    public int magic;
    public int defense;
    public int magicDefense;
    public int regen;
    public int attackSpeed;
    public double critical;
    public double cdReduction;
    public int flags;

    private Item(String name, int imageRes, int category, int price, int hp, int attack, int magic, int defense, int magicDefense, int regen, int attackSpeed, double critical, double cdReduction, int flags) {
        this.name = name;
        this.imageRes = imageRes;
        this.category = category;
        this.price = price;
        this.hp = hp;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.magicDefense = magicDefense;
        this.regen = regen;
        this.attackSpeed = attackSpeed;
        this.critical = critical;
        this.cdReduction = cdReduction;
        this.flags = flags;
    }

    @Nullable
    public static Item findItem(@NonNull String name) {
        return map.get(name);
    }
}
