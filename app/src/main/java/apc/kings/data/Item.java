package apc.kings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import apc.kings.R;

public class Item {

    public static final int FLAG_CUT                   = 0x0001;
    public static final int FLAG_PENETRATE             = 0x0002;
    public static final int FLAG_MAGIC_PENETRATE_RATE  = 0x0004;
    public static final int FLAG_MAGIC_PENETRATE_BOOTS = 0x0008;
    public static final int FLAG_MAGIC_PENETRATE_MASK  = 0x0010;
    public static final int FLAG_CRITICAL              = 0x0020;
    public static final int FLAG_CORRUPT               = 0x0040;
    public static final int FLAG_ACCURATE              = 0x0080;
    public static final int FLAG_LIGHTNING             = 0x0100;
    public static final int FLAG_ENCHANT               = 0x0200;
    public static final int FLAG_BOOTS_DEFENSE         = 0x0400;
    public static final int FLAG_EXECUTE               = 0x0800;
    public static final int FLAG_STORM                 = 0x1000;

    public static Item[] ALL_ITEMS = new Item[] {
            new Item("破军",       R.drawable.weapon_200,       R.id.item_weapon, 2950,    0, 200,  0,   0,   0,   0, 0,      0, 0,    FLAG_EXECUTE),
            new Item("三圣之力",   R.drawable.weapon_trinity,   R.id.item_weapon, 2510,  300,  60, 60,   0,   0,   0, 0.2, 0.15, 0,    FLAG_ENCHANT),
            new Item("纯净苍穹",   R.drawable.weapon_dispel,    R.id.item_weapon, 2230,    0,   0,  0,   0,   0,   0, 0.4,  0.2, 0,    FLAG_ACCURATE),
            new Item("破灭君主",   R.drawable.weapon_corrupt,   R.id.item_weapon, 2160,    0,  60,  0,   0,   0,   0, 0.3,    0, 0,    FLAG_CORRUPT),
            new Item("无尽战刃",   R.drawable.weapon_critical,  R.id.item_weapon, 2140,    0, 100,  0,   0,   0,   0, 0,    0.2, 0,    FLAG_CRITICAL),
            new Item("暗影战斧",   R.drawable.weapon_axe,       R.id.item_weapon, 2090,    0,  85,  0,   0,   0,   0, 0,      0, 0.15, FLAG_CUT),
            new Item("影刃",       R.drawable.weapon_storm,     R.id.item_weapon, 2070,    0,   0,  0,   0,   0,   0, 0.4 , 0.1, 0,    FLAG_STORM),
            new Item("冰霜长矛",   R.drawable.weapon_frost,     R.id.item_weapon, 1970,  500,  80,  0,   0,   0,   0, 0.3,  0.2, 0,    0),
            new Item("闪电匕首",   R.drawable.weapon_lightning, R.id.item_weapon, 1840,    0,   0,  0,   0,   0,   0, 0.3,  0.2, 0,    FLAG_LIGHTNING),
            new Item("泣血之刃",   R.drawable.weapon_drain,     R.id.item_weapon, 1740,    0, 100,  0,   0,   0,   0, 0,      0, 0,    0),
            new Item("破甲弓",     R.drawable.weapon_penetrate, R.id.item_weapon, 1390,    0,  80,  0,   0,   0,   0, 0,      0, 0,    FLAG_PENETRATE),
            new Item("霸者重装",   R.drawable.armor_2000,       R.id.item_armor, 2370, 2000,   0,  0,   0,   0, 100, 0,      0, 0,    0),
            new Item("不祥征兆",   R.drawable.armor_iron,       R.id.item_armor, 2180, 1200,   0,  0, 270,   0,   0, 0,      0, 0,    0),
            new Item("反伤刺甲",   R.drawable.armor_thorns,     R.id.item_armor, 2140,    0,  80,  0, 360,   0,   0, 0,      0, 0,    0),
            new Item("冰封之心",   R.drawable.armor_freeze,     R.id.item_armor, 2100,    0,   0,  0, 360,   0,   0, 0,      0, 0.2,  0),
            new Item("振兴之铠",   R.drawable.armor_heal,       R.id.item_armor, 2100, 1100,   0,  0,   0, 180,  45, 0,      0, 0.1,  0),
            new Item("贤者的庇护", R.drawable.armor_revive,     R.id.item_armor, 2080,    0,   0,  0, 140, 140,   0, 0,      0, 0,    0),
            new Item("冰脉护手",   R.drawable.armor_guanlets,   R.id.item_armor, 2070,    0,   0, 60, 360,   0,   0, 0,      0, 0.1,  0),
            new Item("军团荣耀",   R.drawable.armor_shield,     R.id.item_armor, 1970, 1000,   0,  0, 120, 120,  60, 0,      0, 0,    0),
            new Item("魔女斗篷",   R.drawable.armor_mist,       R.id.item_armor, 1870, 1100,   0,  0,   0, 280,   0, 0,      0, 0,    0),
            new Item("红莲斗篷",   R.drawable.armor_fire,       R.id.item_armor, 1830, 1200,   0,  0, 240,   0,   0, 0,      0, 0,    0),
            new Item("秘法之靴",   R.drawable.boots_mage,       R.id.item_boots,  790,    0,   0,  0,   0,   0,   0, 0,      0, 0,    FLAG_MAGIC_PENETRATE_BOOTS),
            new Item("急速战靴",   R.drawable.boots_attack,     R.id.item_boots,  710,    0,   0,  0,   0,   0,   0, 0.15,   0, 0,    0),
            new Item("影忍之足",   R.drawable.boots_defense,    R.id.item_boots,  690,    0,   0,  0, 110,   0,   0, 0,      0, 0,    FLAG_BOOTS_DEFENSE),
    };

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
    public double attackSpeed;
    public double critical;
    public double cdReduction;
    public int flags;

    private Item(String name, int imageRes, int category, int price, int hp, int attack, int magic, int defense, int magicDefense, int regen, double attackSpeed, double critical, double cdReduction, int flags) {
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
        for (Item item : ALL_ITEMS) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return null;
    }
}
