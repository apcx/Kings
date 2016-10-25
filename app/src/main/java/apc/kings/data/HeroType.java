package apc.kings.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import apc.kings.R;

public class HeroType {

    public static final String TYPE_HERO = "hero";
    public static final String TYPE_POSTER = "poster";

    public static HeroType[] ALL_HEROES = new HeroType[] {
            new HeroType("后羿",    "stalker",  R.id.cat_archer,  0,                 6127, 406, 346,  71,  0.42),
            new HeroType("孙尚香",   "cannon", R.id.cat_archer,  0,                 6014, 421, 346,  69, 0.42),
            new HeroType("鲁班七号", "robot",    R.id.cat_archer,  0,                 5989, 410, 323,  69, 0.42),
            new HeroType("李元芳",   "bomber", R.id.cat_archer,  0,                 5725, 406, 340,  66,  0.28),
//            new HeroType("虞姬",    "hime",     R.id.cat_archer,  0,                 5669, 417, 329,  63,  0.42),
//            new HeroType("狄仁杰",   "judge",   R.id.cat_archer,  0,                 5710, 386, 338,  66,  0.56),
//            new HeroType("刘邦",   "savior",   R.id.cat_tank,    R.id.cat_support,  8193, 302, 504, 117,  0.42),
            new HeroType("夏侯惇",  "claymore",   R.id.cat_warrior,  R.id.cat_tank,  7350, 331, 397, 98,  0.28),
//            new HeroType("吕布",     "stark",    R.id.cat_warrior, R.id.cat_tank,     7344, 353, 390,  97,  0.14),
            new HeroType("孙悟空",  "monkey",   R.id.cat_warrior, R.id.cat_assassin, 7017, 359, 400,  92, 0.14),
    };

    public String name;
    public String resName;
    public int category;
    public int subCategory;
    public int hp;
    public int attack;
    public int defense;
    public int regen;
    public double attackSpeed;
    public Item[] defaultItems;
    public Item[] recommendedItems;
    public Item[] items;
    public Map<Rune, Integer> runes;

    private HeroType(String name, String resName, int category, int subCategory, int hp, int attack, int defense, int regen, double attackSpeed) {
        this.name = name;
        this.resName = resName;
        this.category = category;
        this.subCategory = subCategory;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.regen = regen;
        this.attackSpeed = attackSpeed;

        runes = new HashMap<>();
        runes.put(Rune.findRune("无双"), 10);
        runes.put(Rune.findRune("鹰眼"), 10);
        runes.put(Rune.findRune("隐匿"), 10);
    }

    @Nullable
    public static HeroType findHero(@NonNull String name) {
        for (HeroType heroType : ALL_HEROES) {
            if (heroType.name.equals(name)) {
                return heroType;
            }
        }
        return null;
    }

    public Item[] buildDefaultItems() {
        items = new Item[6];
        switch (category) {
            case R.id.cat_archer:
                switch (name) {
                    case "孙尚香":
                        items[0] = Item.findItem("破甲弓");
                        items[1] = Item.findItem("影忍之足");
                        items[2] = Item.findItem("三圣之力");
                        items[3] = Item.findItem("无尽战刃");
                        items[4] = Item.findItem("泣血之刃");
                        items[5] = Item.findItem("贤者的庇护");
                        break;
                    default:
                        items[0] = Item.findItem("破灭君主");
                        items[1] = Item.findItem("急速战靴");
                        items[2] = Item.findItem("破甲弓");
                        items[3] = Item.findItem("影刃");
                        items[4] = Item.findItem("无尽战刃");
                        items[5] = Item.findItem("贤者的庇护");
                }
                break;
            default:
                switch (name) {
                    case "孙悟空":
                        items[0] = Item.findItem("破甲弓");
                        items[1] = Item.findItem("影忍之足");
                        items[2] = Item.findItem("三圣之力");
                        items[3] = Item.findItem("无尽战刃");
                        items[4] = Item.findItem("泣血之刃");
                        items[5] = Item.findItem("贤者的庇护");
                        break;
                    default:
                        items[0] = Item.findItem("红莲斗篷");
                        items[1] = Item.findItem("影忍之足");
                        items[2] = Item.findItem("不祥征兆");
                        items[3] = Item.findItem("振兴之铠");
                        items[4] = Item.findItem("军团荣耀");
                        items[5] = Item.findItem("冰封之心");
                }
        }
        return items;
    }

    @NonNull
    public Uri getImageUri(Context context, String type) {
        return Uri.parse("res:///" + context.getResources().getIdentifier(type + '_' + resName, "drawable", context.getPackageName()));
    }
}
