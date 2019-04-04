package apc.kings.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import apc.kings.R;

public class Item {

    public static final int SLOTS = 6;

    // unique flags on panel
    public static final int FP_BOOTS      = 0x0001;
    public static final int FP_MPN_BOOTS  = 0x0003;
    public static final int FP_MPN_MASK   = 0x0004;
    public static final int FP_MPN_VOID   = 0x0008;
    public static final int FP_PN         = 0x0010;
    public static final int FP_PNP        = 0x0020;
    public static final int FP_ANTI_MAGIC = 0x0040;
    public static final int FP_HAT        = 0x0100;
    public static final int FP_MARK       = 0x0200;
    public static final int FP_SENTINEL   = 0x0400;
    public static final int FP_CRITICAL   = 0x0800;
    public static final int MOB_ATTACK    = 0x1000;
    public static final int MOB_MAGIC     = 0x2000;
    public static final int MOB_HP        = 0x4000;

    // unique flags on special effects
    public static final int FLAG_STORM         = 0x0001;
    public static final int FLAG_COLD_IRON     = 0x0002;
    public static final int FLAG_FROZEN_HEART  = 0x0004;
    public static final int FLAG_JUDGEMENT     = 0x0008;

    public static final int FLAG_DEFENSE_BOOTS = 0x0010;
    public static final int FLAG_EXECUTE       = 0x0020;
    public static final int FLAG_SHIELD_MAGIC  = 0x0040;
    public static final int FLAG_SHIELD_BOTTOM = 0x0080;

    public static final int FLAG_CORRUPT       = 0x0100;
    public static final int FLAG_LIGHTNING     = 0x0400;
    public static final int FLAG_CORRUPT_2     = 0x0800;

    public static final int FLAG_HEAL          = 0x1000;
    public static final int FLAG_THORNS        = 0x2000;
    public static final int FLAG_WOUND         = 0x4000;
    public static final int FLAG_ECHO          = 0x8000;

    public static final int FLAG_BERSERK       = 0x00010000;
    public static final int FLAG_BONUS_70      = 0x00020000;
    public static final int FLAG_BONUS_100     = 0x00040000;

    public static final int ENCHANT_VOODOO     = 0x0f000000;
    public static final int ENCHANT_MASTER     = 0x07000000;
    public static final int ENCHANT_ICE        = 0x03000000;
    public static final int ENCHANT_MOB        = 0x01000000;

    public static final Item[] ALL_ITEMS = new Item[] {
            new Item("末世",      R.drawable.weapon_corrupt,    R.id.item_weapon, 2160, 0,    60,  0,   0,   0,   0,  30, 0,  0,  0, 0, FLAG_CORRUPT),
            new Item("制裁之刃",  R.drawable.weapon_wound,      R.id.item_weapon, 1800, 0,    100, 0,   0,   0,   0,  0,  0,  0,  0, 0, FLAG_WOUND),
            new Item("泣血之刃",  R.drawable.weapon_drain,      R.id.item_weapon, 1740, 0,    100, 0,   0,   0,   0,  0,  0,  0,  0, 0, 0),
            new Item("影刃",      R.drawable.weapon_storm,      R.id.item_weapon, 2070, 0,    0,   0,   0,   0,   5,  40, 20, 0,  0, 0, FLAG_STORM),
            new Item("纯净苍穹",  R.drawable.weapon_dispel,     R.id.item_weapon, 2230, 0,    0,   0,   0,   0,   0,  40, 20, 0,  0, 0, FLAG_BONUS_70),
            new Item("闪电匕首",  R.drawable.weapon_lightning,  R.id.item_weapon, 1840, 0,    0,   0,   0,   0,   8,  30, 20, 0,  0, 0, FLAG_LIGHTNING),
            new Item("碎星锤",    R.drawable.weapon_penetrate,  R.id.item_weapon, 2100, 0,    80,  0,   0,   0,   0,  0,  0,  10, 0, FP_PNP,        0),
            new Item("暗影战斧",  R.drawable.weapon_axe,        R.id.item_weapon, 2090, 500,  85,  0,   0,   0,   0,  0,  0,  15, 0, FP_PN,         0),
            new Item("宗师之力",  R.drawable.weapon_master,     R.id.item_weapon, 2100, 400,  60,  0,   0,   0,   0,  0,  20, 0,  0, 0, ENCHANT_MASTER),
            new Item("无尽战刃",  R.drawable.weapon_critical,   R.id.item_weapon, 2140, 0,    120, 0,   0,   0,   0,  0,  20, 0,  0, FP_CRITICAL,   0),
            new Item("冰霜长矛",  R.drawable.weapon_frost,      R.id.item_weapon, 1980, 600,  80,  0,   0,   0,   0,  0,  0,  0,  0, 0, 0),
            new Item("名刀·司命",R.drawable.weapon_curtain,    R.id.item_weapon, 1760, 0,    60,  0,   0,   0,   0,  0,  0,  5,  0, 0, 0),
            new Item("破军",      R.drawable.weapon_200,        R.id.item_weapon, 2950, 0,    200, 0,   0,   0,   0,  0,  0,  0,  0, 0, FLAG_EXECUTE),
            new Item("破魔刀",    R.drawable.weapon_anti_magic, R.id.item_weapon, 2000, 0,    100, 0,   0,   50,  0,  0,  0,  0,  0, FP_ANTI_MAGIC, 0),
            new Item("逐日之弓",  R.drawable.weapon_stride,     R.id.item_weapon, 2100, 0,    0,   0,   0,   0,   0,  25, 15, 0,  0, 0, FLAG_BONUS_70),
            new Item("破晓",      R.drawable.weapon_dawn,       R.id.item_weapon, 3400, 0,    50,  0,   0,   0,   5,  35, 15, 0,  0, FP_PNP,         FLAG_BONUS_100),
//            new Item("金色圣剑",  R.drawable.weapon_judgement,  R.id.item_weapon, 2030,    0,   0,   0,   0,   0, 0, 50,  0,  0,   0, 0,            FLAG_JUDGEMENT),
            new Item("圣杯",      R.drawable.magic_mana,        R.id.item_magic,  1930, 0,    0,   180, 0,   0,   0,  0,  0,  15, 0,   0, 0),
            new Item("虚无法杖",  R.drawable.magic_void,        R.id.item_magic,  2110, 500,  0,   180, 0,   0,   0,  0,  0,  0,  0,   FP_MPN_VOID,  0),
            new Item("博学者之怒",R.drawable.magic_hat,         R.id.item_magic,  2300, 0,    0,   240, 0,   0,   0,  0,  0,  0,  0,   FP_HAT,       0),
            new Item("回响之杖",  R.drawable.magic_echo,        R.id.item_magic,  2100, 0,    0,   240, 0,   0,   7,  0,  0,  0,  0,   0, FLAG_ECHO),
            new Item("冰霜法杖",  R.drawable.magic_frost,       R.id.item_magic,  2100, 1050, 0,   150, 0,   0,   0,  0,  0,  0,  0,   0,0),
            new Item("痛苦面具",  R.drawable.magic_mask,        R.id.item_magic,  2040, 500,  0,   140, 0,   0,   0,  0,  0,  5,  0,   FP_MPN_MASK,  0),
            new Item("巫术法杖",  R.drawable.magic_voodoo,      R.id.item_magic,  2120, 400,  0,   140, 0,   0,   8,  0,  0,  0,  0,   0, ENCHANT_VOODOO),
            new Item("时之预言",  R.drawable.magic_prophecy,    R.id.item_magic,  2090, 800,  0,   160, 0,   0,   0,  0,  0,  0,  0,   0, 0),
            new Item("辉月",      R.drawable.magic_moon,        R.id.item_magic,  1990, 0,    0,   160, 0,   0,   0,  0,  0,  10, 0,   0, 0),
            new Item("噬神之书",  R.drawable.magic_drain,       R.id.item_magic,  2090, 800,  0,   180, 0,   0,   0,  0,  0,  10, 0,   0, 0),
            new Item("炽热支配者",R.drawable.magic_shield,      R.id.item_magic,  1950, 0,    0,   180, 0,   0,   0,  0,  0,  0,  0,   0, 0),
            new Item("梦魇之牙",  R.drawable.magic_wound,       R.id.item_magic,  2050, 0,    0,   240, 0,   0,   5,  0,  0,  0,  0,   0, 0),
            new Item("贤者之书",  R.drawable.magic_400,         R.id.item_magic,  2990, 0,    0,   400, 0,   0,   0,  0,  0,  0,  0,   FP_MARK,       0),
            new Item("红莲斗篷",  R.drawable.armor_fire,        R.id.item_armor,  1830, 1200, 0,   0,   240, 0,   0,  0,  0,  0,  0,   0, 0),
            new Item("不祥征兆",  R.drawable.armor_cold_iron,   R.id.item_armor,  2180, 1200, 0,   0,   270, 0,   0,  0,  0,  0,  0,   0, FLAG_COLD_IRON),
            new Item("冰痕之握",  R.drawable.armor_gauntlets,   R.id.item_armor,  2020, 800,  0,   0,   200, 0,   0,  0,  0,  10, 0,   0, ENCHANT_ICE),
            new Item("魔女斗篷",  R.drawable.armor_magic_shield,R.id.item_armor,  2120, 1000, 0,   0,   0,   360, 0,  0,  0,  0,  0,   0, FLAG_SHIELD_MAGIC),
            new Item("不死鸟之眼",R.drawable.armor_heal,        R.id.item_armor,  2100, 1200, 0,   0,   0,   240, 0,  0,  0,  0,  100, 0, FLAG_HEAL),
            new Item("霸者重装",  R.drawable.armor_2000,        R.id.item_armor,  2070, 2000, 0,   0,   0,   0,   0,  0,  0,  0,  100, 0, 0),
            new Item("极寒风暴",  R.drawable.armor_frozen_heart,R.id.item_armor,  2100, 0,    0,   0,   360, 0,   0,  0,  0,  20, 0,   0, 0),
            new Item("反伤刺甲",  R.drawable.armor_thorns,      R.id.item_armor,  1840, 0,    40,  0,   420, 0,   0,  0,  0,  0,  0,   0, FLAG_THORNS),
            new Item("血魔之怒",  R.drawable.armor_fury,        R.id.item_armor,  2120, 1000, 20,  0,   0,   0,   0,  0,  0,  0,  0,   0, FLAG_SHIELD_BOTTOM),
            new Item("暴烈之甲",  R.drawable.armor_berserk,     R.id.item_armor,  1820, 1000, 0,   0,   220, 0,   0,  0,  0,  0,  0,   0, FLAG_BERSERK),
            new Item("贤者的庇护",R.drawable.armor_revive,      R.id.item_armor,  2080, 0,    0,   0,   140, 140, 0,  0,  0,  0,  0,   0, 0),
            new Item("急速战靴",  R.drawable.boots_attack,      R.id.item_boots,  710,  0,    0,   0,   0,   0,   0,  30, 0,  0,  0,   FP_BOOTS,      0),
            new Item("秘法之靴",  R.drawable.boots_mage,        R.id.item_boots,  710,  0,    0,   0,   0,   0,   0,  0,  0,  0,  0,   FP_MPN_BOOTS,  0),
            new Item("冷静之靴",  R.drawable.boots_cd,          R.id.item_boots,  710,  0,    0,   0,   0,   0,   0,  0,  0,  15, 0,   FP_BOOTS,      0),
            new Item("影忍之足",  R.drawable.boots_defense,     R.id.item_boots,  710,  0,    0,   0,   110, 0,   0,  0,  0,  0,  0,   FP_BOOTS,       FLAG_DEFENSE_BOOTS),
            new Item("抵抗之靴",  R.drawable.boots_resist,      R.id.item_boots,  710,  0,    0,   0,   0,   110, 0,  0,  0,  0,  0,   FP_BOOTS,      0),
            new Item("疾步之靴",  R.drawable.boots_fly,         R.id.item_boots,  530,  0,    0,   0,   0,   0,   0,  0,  0,  0,  0,   FP_BOOTS,      0),
            new Item("贪婪之噬",  R.drawable.mob_attack,        R.id.item_mob,    1460, 0,    45,  0,   0,   0,   0,  12, 0,  0,  0,   MOB_ATTACK,    0),
            new Item("符文大剑",  R.drawable.mob_magic,         R.id.item_mob,    1490, 0,    0,   100, 0,   0,   0,  0,  0,  0,  0,   MOB_MAGIC,      ENCHANT_MOB),
            new Item("巨人之握",  R.drawable.mob_hp,            R.id.item_mob,    1500, 800,  0,   0,   0,   0,   0,  0,  0,  0,  0,   MOB_HP,        0),
            new Item("奔狼纹章",  R.drawable.armor_horn,        R.id.item_support,2090, 1000, 0,   0,   0,   0,   10, 0,  0,  0,  0,   0, 0),
            new Item("近卫荣耀",  R.drawable.armor_sentinel,    R.id.item_support,2010, 1000, 0,   0,   0,   0,   10, 0,  0,  0,  0,   FP_SENTINEL,   0),
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
    public int magic_defense;
    public int move_speed;
    public int attack_speed;
    public int critical;
    public int cdr;
    public int regen;
    public int panel_flags;
    public int flags;
    public boolean experimental;

    private Item(String name, int imageRes, int category, int price, int hp, int attack, int magic, int defense, int magic_defense, int move_speed, int attack_speed, int critical, int cdr, int regen, int panel_flags, int flags) {
        this.name = name;
        this.imageRes = imageRes;
        this.category = category;
        this.price = price;
        this.hp = hp;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.magic_defense = magic_defense;
        this.move_speed = move_speed;
        this.attack_speed = attack_speed;
        this.critical = critical;
        this.cdr = cdr;
        this.regen = regen;
        this.panel_flags = panel_flags;
        this.flags = flags;
    }

    @Nullable
    public static Item findItem(@NonNull String name) {
        return map.get(name);
    }

    @SuppressWarnings("unused")
    private static void markExperimental(String[] names) {
        for (String name : names) {
            Item item = findItem(name);
            if (item != null) {
                item.experimental = true;
            }
        }
    }
}