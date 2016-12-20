package apc.kings.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.util.Map;

import apc.kings.R;
import apc.kings.common.App;

public class HeroType {

    public static final String TYPE_HERO = "hero";
    public static final String TYPE_POSTER = "poster";

    public static HeroType[] ALL_HEROES = new HeroType[] {
            new HeroType("成吉思汗", "hunter",   R.id.cat_archer,  0,                 5799, 404, 329, 66, 3),
            new HeroType("马可波罗", "gunner",   R.id.cat_archer,  0,                 5584, 372, 344, 75, 2),
            new HeroType("后羿",     "stalker",  R.id.cat_archer,  0,                 6127, 406, 346, 71, 3),
            new HeroType("李元芳",   "bomber",   R.id.cat_archer,  0,                 5725, 406, 340, 66, 2),
            new HeroType("鲁班七号", "robot",    R.id.cat_archer,  0,                 5989, 410, 323, 69, 3),
            new HeroType("孙尚香",   "cannon",   R.id.cat_archer,  0,                 6014, 421, 346, 69, 3),
            new HeroType("夏侯惇",   "claymore", R.id.cat_warrior, R.id.cat_tank,     7350, 331, 397, 98, 2),
            new HeroType("孙悟空",   "monkey",   R.id.cat_warrior, R.id.cat_assassin, 7017, 359, 400, 92, 1),
//            new HeroType("虞姬",    "hime",     R.id.cat_archer,  0,                 5669, 417, 329,  63,  0.42),
//            new HeroType("狄仁杰",   "judge",   R.id.cat_archer,  0,                 5710, 386, 338,  66,  0.56),
//            new HeroType("刘邦",   "savior",   R.id.cat_tank,    R.id.cat_support,  8193, 302, 504, 117,  0.42),
//            new HeroType("吕布",     "stark",    R.id.cat_warrior, R.id.cat_tank,     7344, 353, 390,  97,  0.14),
    };

    private static final Map<String, HeroType> map = new ArrayMap<>();
    static {
        for (HeroType heroType : ALL_HEROES) {
            map.put(heroType.name, heroType);
        }

        buildDefaultItems("成吉思汗", new String[]{"无尽战刃", "急速战靴", "三圣之力", "泣血之刃", "冰霜长矛", "破甲弓"});
        buildDefaultItems("马可波罗", new String[]{"破灭君主", "急速战靴", "无尽战刃", "泣血之刃", "破甲弓", "影刃"});
        buildDefaultItems("后羿", new String[]{"破灭君主", "闪电匕首", "急速战靴", "无尽战刃", "破甲弓", "影刃"});
        buildDefaultItems("李元芳", new String[]{"破灭君主", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("鲁班七号", new String[]{"破灭君主", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("孙尚香", new String[]{"破灭君主", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("夏侯惇", new String[]{"红莲斗篷", "抵抗之靴", "振兴之铠", "暗影战斧", "不祥征兆", "霸者重装"});
        buildDefaultItems("孙悟空", new String[]{"暗影战斧", "抵抗之靴", "三圣之力", "冰封之心", "破军", "破甲弓"});

        String[] archerItemNames = {"影忍之足", "破灭君主", "泣血之刃", "破甲弓", "无尽战刃", "贤者的庇护"};
        String[] attackItemNames = {"影忍之足", "泣血之刃", "破甲弓", "三圣之力", "无尽战刃", "贤者的庇护"};
        buildRecommendedItems("后羿", archerItemNames);
        buildRecommendedItems("李元芳", archerItemNames);
        buildRecommendedItems("鲁班七号", archerItemNames);
        buildRecommendedItems("孙尚香", attackItemNames);
        buildRecommendedItems("虞姬", attackItemNames);
        buildRecommendedItems("孙悟空", attackItemNames);
        buildRecommendedItems("成吉思汗", new String[]{"影忍之足", "泣血之刃", "三圣之力", "破甲弓", "无尽战刃", "贤者的庇护"});
        buildRecommendedItems("马可波罗", new String[]{"影忍之足", "破灭君主", "纯净苍穹", "破甲弓", "暴烈之甲", "贤者的庇护"});
        buildRecommendedItems("夏侯惇", new String[]{"影忍之足", "红莲斗篷", "不祥征兆", "魔女斗篷", "军团荣耀", "霸者重装"});

        buildDefaultRunes("成吉思汗", new String[]{"传承", "隐匿", "鹰眼"});
        buildDefaultRunes("马可波罗", new String[]{"红月", "狩猎", "鹰眼"});
        buildDefaultRunes("后羿", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("李元芳", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("鲁班七号", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("孙尚香", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("夏侯惇", new String[]{"宿命", "调和", "鹰眼"});
        buildDefaultRunes("孙悟空", new String[]{"无双", "兽痕", "鹰眼"});

        SharedPreferences preferences = App.preferences();
        for (HeroType heroType : ALL_HEROES) {
            boolean loaded = false;
            String value = preferences.getString("item_" + heroType.name, null);
            if (!TextUtils.isEmpty(value)) {
                String[] itemNames = value.split(",");
                if (itemNames.length >= Item.SLOTS) {
                    heroType.items = new Item[Item.SLOTS];
                    for (int i = 0; i < Item.SLOTS; ++i) {
                        heroType.items[i] = Item.findItem(itemNames[i]);
                    }
                    loaded = true;
                }
            }
            if (!loaded) {
                heroType.items = heroType.recommendedItems != null ? heroType.recommendedItems : heroType.defaultItems;
            }

            loaded = false;
            value = preferences.getString("rune_" + heroType.name, null);
            if (!TextUtils.isEmpty(value)) {
                String[] runeNames = value.split(",");
                if (runeNames.length >= 3) {
                    for (int i = 0; i < 3; ++i) {
                        heroType.runes.put(Rune.findRune(runeNames[i]), 10);
                    }
                    loaded = true;
                }
            }
            if (!loaded) {
                Rune[] runes = heroType.recommendedRunes != null ? heroType.recommendedRunes : heroType.defaultRunes;
                for (Rune rune : runes) {
                    heroType.runes.put(rune, 10);
                }
            }
        }
    }

    public String name;
    public String resName;
    public int category;
    public int subCategory;
    public int hp;
    public int attack;
    public int defense;
    public int regen;
    public int attackSpeedPerLevel;
    public Item[] defaultItems = new Item[Item.SLOTS];
    public Item[] recommendedItems = new Item[Item.SLOTS];
    public Item[] items;
    public Rune[] defaultRunes = new Rune[3];
    public Rune[] recommendedRunes;
    public Map<Rune, Integer> runes = new ArrayMap<>();

    private HeroType(String name, String resName, int category, int subCategory, int hp, int attack, int defense, int regen, int attackSpeedPerLevel) {
        this.name = name;
        this.resName = resName;
        this.category = category;
        this.subCategory = subCategory;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.regen = regen;
        this.attackSpeedPerLevel = attackSpeedPerLevel;
    }

    @Nullable
    public static HeroType findHero(@NonNull String name) {
        return map.get(name);
    }
    
    @NonNull
    public Uri getImageUri(Context context, String type) {
        return Uri.parse("res:///" + context.getResources().getIdentifier(type + '_' + resName, "drawable", context.getPackageName()));
    }
    
    public void setItems(@NonNull Item[] items) {
        this.items = items;
        String value = "";
        for (int i = 0; i < Item.SLOTS; ++i) {
            String name = null == items[i] ? "empty" : items[i].name;
            value += 0 == i ? name : "," + name;
        }
        App.preferences().edit().putString("item_" + name, value).apply();
    }

    private static void buildDefaultItems(String name, String[] itemNames) {
        HeroType heroType = findHero(name);
        if (heroType != null) {
            for (int i = 0; i < Item.SLOTS; ++i) {
                heroType.defaultItems[i] = Item.findItem(itemNames[i]);
            }
        }
    }

    private static void buildRecommendedItems(String name, String[] itemNames) {
        HeroType heroType = findHero(name);
        if (heroType != null) {
            for (int i = 0; i < Item.SLOTS; ++i) {
                heroType.recommendedItems[i] = Item.findItem(itemNames[i]);
            }
        }
    }

    private static void buildDefaultRunes(String name, String[] runeNames) {
        HeroType heroType = findHero(name);
        if (heroType != null) {
            for (int i = 0; i < 3; ++i) {
                heroType.defaultRunes[i] = Rune.findRune(runeNames[i]);
            }
        }
    }

    private static void buildRecommendedRunes(String name, String[] runeNames) {
        HeroType heroType = findHero(name);
        if (heroType != null) {
            for (int i = 0; i < 3; ++i) {
                heroType.recommendedRunes[i] = Rune.findRune(runeNames[i]);
            }
        }
    }
}
