package apc.kings.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import apc.kings.App;
import apc.kings.R;

public class HeroType {

    public static HeroType[] ALL_HEROES = new HeroType[] {
            new HeroType("孙尚香",  "突进/收割",     "cannon", R.id.cat_archer,  0,                 6014, 421, 346,  69, 340, 0.42),
            new HeroType("后羿",    "团控/先手",     "stalker",  R.id.cat_archer,  0,                 6299, 421, 346,  73, 340, 0.42),
            new HeroType("鲁班七号","远程消耗",      "robot",    R.id.cat_archer,  0,                 5989, 410, 323,  69, 340, 0.42),
            new HeroType("李元芳",  "突进/收割",     "bomber", R.id.cat_archer,  0,                 5725, 406, 340,  66, 340, 0.28),
//            new HeroType("虞姬",    "突进/远程消耗", "hime",     R.id.cat_archer,  0,                 5669, 417, 329,  63, 340, 0.42),
//            new HeroType("狄仁杰",  "",              "judge",   R.id.cat_archer,  0,                 5710, 386, 338,  66, 350, 0.56),
//            new HeroType("刘邦",    "突进/团控",     "savior",   R.id.cat_tank,    R.id.cat_support,  8193, 302, 504, 117, 380, 0.42),
            new HeroType("夏侯惇",    "突进/团控",    "pirate",   R.id.cat_warrior,  R.id.cat_tank,  7350, 331, 397, 98, 380, 0.28),
//            new HeroType("吕布",    "收割",          "stark",    R.id.cat_warrior, R.id.cat_tank,     7344, 353, 390,  97, 370, 0.14),
            new HeroType("孙悟空",  "突进/团控",     "monkey",   R.id.cat_warrior, R.id.cat_assassin, 7017, 359, 400,  92, 380, 0.14),
    };

    public String name;
    public String description;
    public String resName;
    public int category;
    public int subCategory;
    public int hp;
    public int attack;
    public int defense;
    public int regen;
    public int move;
    public double attackSpeed;
    public Item[] items;
    public Map<Rune, Integer> runes;

    private HeroType(String name, String description, String resName, int category, int subCategory, int hp, int attack, int defense, int regen, int move, double attackSpeed) {
        this.name = name;
        this.description = description;
        this.resName = resName;
        this.category = category;
        this.subCategory = subCategory;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.regen = regen;
        this.move = move;
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

    public Uri getImageUri(String type) {
        Context context = App.context();
        return Uri.parse("res://drawable/" + context.getResources().getIdentifier(type + '_' + resName, "drawable", context.getPackageName()));
    }
}
