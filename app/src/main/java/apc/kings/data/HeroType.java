package apc.kings.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Map;

import apc.kings.R;
import apc.kings.common.App;
import apc.kings.common.Utils;

public class HeroType {

    public static final String TYPE_HERO = "hero";
    public static final String TYPE_POSTER = "poster";
    public static final HeroType[] ALL_HEROES = new HeroType[] {
            new HeroType("成吉思汗", "hunter",   R.id.cat_archer,  0,                 5799, 404, 329,  66, 3, 370),
            new HeroType("黄忠",     "cannon",   R.id.cat_archer,  0,                 5898, 513, 319,  68, 3, 340),
            new HeroType("马可波罗", "gunner",   R.id.cat_archer,  0,                 5584, 372, 344,  75, 2, 350),
//            new HeroType("虞姬",    "hime",     R.id.cat_archer,  0,                 5669, 417, 329,  63,  3),
            new HeroType("李元芳",   "bomber",   R.id.cat_archer,  0,                 5725, 406, 340,  66, 2, 340),
//            new HeroType("刘备",
//            new HeroType("狄仁杰",   "judge",   R.id.cat_archer,  0,                 5710, 386, 338,  66,  4),
            new HeroType("孙尚香",   "sniper",   R.id.cat_archer,  0,                 6014, 421, 346,  69, 3, 340),
            new HeroType("鲁班七号", "robot",    R.id.cat_archer,  0,                 5989, 410, 323,  69, 3, 360),
            new HeroType("后羿",     "stalker",  R.id.cat_archer,  0,                 5986, 406, 336,  71, 3, 340),
            new HeroType("孙悟空",   "monkey",   R.id.cat_warrior, R.id.cat_assassin, 7017, 359, 400,  92, 1, 380),
            new HeroType("刘邦",     "savior",   R.id.cat_tank,    R.id.cat_support,  8193, 302, 504, 117, 3, 380),
            new HeroType("程咬金",   "berserk",  R.id.cat_tank,    R.id.cat_warrior,  8731, 316, 504, 119, 2, 380),
            new HeroType("夏侯惇",   "claymore", R.id.cat_warrior, R.id.cat_tank,     7350, 331, 397,  98, 2, 380),
//            new HeroType("吕布",     "stark",    R.id.cat_warrior, R.id.cat_tank,     7344, 353, 390,  97,  1),
    };

    private static final String SAVE_RUNE = "rune_";
    private static final Map<String, HeroType> map = new ArrayMap<>();
    private static final Gson gson = new Gson();
    static {
        for (HeroType heroType : ALL_HEROES) {
            map.put(heroType.name, heroType);
        }

        buildDefaultItems("成吉思汗", new String[]{"无尽战刃", "急速战靴", "宗师之力", "泣血之刃", "冰霜长矛", "破甲弓"});
        buildDefaultItems("黄忠", new String[]{"末世", "急速战靴", "无尽战刃", "泣血之刃", "破甲弓", "影刃"});
        buildDefaultItems("马可波罗", new String[]{"末世", "急速战靴", "无尽战刃", "泣血之刃", "破甲弓", "影刃"});
        buildDefaultItems("李元芳", new String[]{"末世", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("孙尚香", new String[]{"末世", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("鲁班七号", new String[]{"末世", "急速战靴", "无尽战刃", "破甲弓", "泣血之刃", "破军"});
        buildDefaultItems("后羿", new String[]{"末世", "闪电匕首", "急速战靴", "无尽战刃", "破甲弓", "影刃"});
        buildDefaultItems("孙悟空", new String[]{"暗影战斧", "抵抗之靴", "宗师之力", "极寒风暴", "破军", "破甲弓"});
        buildDefaultItems("刘邦", new String[]{"红莲斗篷", "近卫荣耀", "冷静之靴", "极寒风暴", "霸者重装", "闪电匕首"});
        buildDefaultItems("程咬金", new String[]{"红莲斗篷", "影忍之足", "不死鸟之眼", "血魔之怒", "不祥征兆", "暴烈之甲"});
        buildDefaultItems("夏侯惇", new String[]{"红莲斗篷", "抵抗之靴", "不死鸟之眼", "暗影战斧", "不祥征兆", "霸者重装"});

        String[] archer_items = {"影忍之足", "末世", "影刃", "破甲弓", "纯净苍穹", "破军"};
        buildRecommendedItems("成吉思汗", new String[]{"影忍之足", "末世", "破甲弓", "影刃", "纯净苍穹", "破军"});
        buildRecommendedItems("黄忠", new String[]{"影忍之足", "泣血之刃", "纯净苍穹", "影刃", "破甲弓", "无尽战刃"});
        buildRecommendedItems("马可波罗", new String[]{"影忍之足", "末世", "纯净苍穹", "破甲弓", "末世", "贤者的庇护"});
        buildRecommendedItems("李元芳", archer_items);
        buildRecommendedItems("孙尚香", new String[]{"影忍之足", "闪电匕首", "无尽战刃", "破甲弓", "宗师之力", "破军"});
        buildRecommendedItems("鲁班七号", archer_items);
        buildRecommendedItems("后羿", new String[]{"影忍之足", "末世", "破甲弓", "影刃", "纯净苍穹", "破军"});
        buildRecommendedItems("刘邦", new String[]{"影忍之足", "红莲斗篷", "魔女斗篷", "血魔之怒", "不祥征兆", "霸者重装"});
        buildRecommendedItems("程咬金", new String[]{"影忍之足", "红莲斗篷", "不死鸟之眼", "反伤刺甲", "不祥征兆", "血魔之怒"});
        buildRecommendedItems("夏侯惇", new String[]{"影忍之足", "红莲斗篷", "魔女斗篷", "不祥征兆", "极寒风暴", "血魔之怒"});

        buildDefaultRunes("成吉思汗", new String[]{"传承", "隐匿", "鹰眼"});
        buildDefaultRunes("黄忠", new String[]{"无双", "兽痕", "鹰眼"});
        buildDefaultRunes("马可波罗", new String[]{"红月", "狩猎", "鹰眼"});
        buildDefaultRunes("李元芳", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("孙尚香", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("鲁班七号", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("后羿", new String[]{"红月", "隐匿", "鹰眼"});
        buildDefaultRunes("孙悟空", new String[]{"无双", "兽痕", "鹰眼"});
        buildDefaultRunes("刘邦", new String[]{"凶兆", "长生", "虚空"});
        buildDefaultRunes("程咬金", new String[]{"宿命", "隐匿", "虚空"});
        buildDefaultRunes("夏侯惇", new String[]{"宿命", "调和", "鹰眼"});

        String[] tank_runes = {"宿命", "调和", "虚空"};
        Map<String, Integer> mixed_runes = new ArrayMap<>();
        mixed_runes.put("祸源", 10);
        mixed_runes.put("鹰眼", 10);
        mixed_runes.put("夺萃", 5);
        mixed_runes.put("狩猎", 5);
        buildRecommendedRunes("成吉思汗", mixed_runes);
        buildRecommendedRunes("黄忠", new String[]{"无双", "狩猎", "鹰眼"});
        buildRecommendedRunes("李元芳", mixed_runes);
        buildRecommendedRunes("孙尚香", new String[]{"无双", "夺萃", "鹰眼"});
        buildRecommendedRunes("鲁班七号", mixed_runes);
        buildRecommendedRunes("后羿", mixed_runes);
        buildRecommendedRunes("刘邦", tank_runes);
        buildRecommendedRunes("夏侯惇", tank_runes);

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
                heroType.items = heroType.recommended_items != null ? heroType.recommended_items : heroType.default_items;
            }
            heroType.loadRunes();
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
    public int level_up_attack_speed;
    public int move;
    public Item[] default_items = new Item[Item.SLOTS];
    public Item[] recommended_items;
    public Item[] items;
    public Rune[] default_runes = new Rune[3];
    public Map<Rune, Integer> recommended_runes;
    public Map<Rune, Integer> runes = new ArrayMap<>();

    private HeroType(String name, String resName, int category, int subCategory, int hp, int attack, int defense, int regen, int level_up_attack_speed, int move) {
        this.name = name;
        this.resName = resName;
        this.category = category;
        this.subCategory = subCategory;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.regen = regen;
        this.level_up_attack_speed = level_up_attack_speed;
        this.move = move;
    }

    private static void buildDefaultItems(String hero, String[] itemNames) {
        HeroType heroType = findHero(hero);
        if (heroType != null) {
            for (int i = 0; i < Item.SLOTS; ++i) {
                heroType.default_items[i] = Item.findItem(itemNames[i]);
            }
        }
    }

    private static void buildRecommendedItems(String hero, String[] itemNames) {
        HeroType heroType = findHero(hero);
        if (heroType != null) {
            heroType.recommended_items = new Item[Item.SLOTS];
            for (int i = 0; i < Item.SLOTS; ++i) {
                heroType.recommended_items[i] = Item.findItem(itemNames[i]);
            }
        }
    }

    private static void buildDefaultRunes(String hero, String[] runeNames) {
        HeroType heroType = findHero(hero);
        if (heroType != null) {
            for (int i = 0; i < 3; ++i) {
                heroType.default_runes[i] = Rune.findRune(runeNames[i]);
            }
        }
    }

    private static void buildRecommendedRunes(String hero, String[] runeNames) {
        HeroType heroType = findHero(hero);
        if (heroType != null) {
            heroType.recommended_runes = new ArrayMap<>();
            for (String runeName : runeNames) {
                Rune rune = Rune.findRune(runeName);
                if (rune != null) {
                    heroType.recommended_runes.put(rune, 10);
                }
            }
        }
    }

    private static void buildRecommendedRunes(String hero, Map<String, Integer> runes) {
        HeroType heroType = findHero(hero);
        if (heroType != null) {
            heroType.recommended_runes = new ArrayMap<>();
            for (Map.Entry<String, Integer> entry : runes.entrySet()) {
                Rune rune = Rune.findRune(entry.getKey());
                if (rune != null) {
                    heroType.recommended_runes.put(rune, entry.getValue());
                }
            }
        }
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

        SharedPreferences.Editor editor = App.preferences().edit();
        String key = "item_" + name;
        boolean reset = false;
        if (recommended_items != null) {
            if (Arrays.equals(items, recommended_items)) {
                editor.remove(key);
                reset = true;
            }
        } else if (Arrays.equals(items, default_items)) {
            editor.remove(key);
            reset = true;
        }

        if (!reset) {
            String value = "";
            for (int i = 0; i < Item.SLOTS; ++i) {
                String name = null == items[i] ? "empty" : items[i].name;
                value += 0 == i ? name : "," + name;
            }
            editor.putString(key, value);
        }
        editor.apply();
    }

    private void loadRunes() {
        boolean loaded = false;
        String json = App.preferences().getString(SAVE_RUNE + name, null);
        if (!TextUtils.isEmpty(json)) {
            try {
                Map<String, Integer> map = gson.fromJson(json, new TypeToken<ArrayMap<String, Integer>>(){}.getType());
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    Rune rune = Rune.findRune(entry.getKey());
                    if (rune != null) {
                        runes.put(rune, entry.getValue());
                    }
                }
                loaded = true;
            } catch (JsonSyntaxException e) {
                // ignore
            }
        }
        if (!loaded) {
            if (!Utils.isEmpty(recommended_runes)) {
                runes.putAll(recommended_runes);
            } else {
                runes.put(default_runes[0], 10);
                runes.put(default_runes[1], 10);
                runes.put(default_runes[2], 10);
            }
        }
    }

    public void resetRecommendedRunes() {
        runes.clear();
        runes.putAll(recommended_runes);
        App.preferences().edit().remove(SAVE_RUNE + name).apply();
    }

    public void resetDefaultRunes() {
        runes.clear();
        runes.put(default_runes[0], 10);
        runes.put(default_runes[1], 10);
        runes.put(default_runes[2], 10);
        if (Utils.isEmpty(recommended_runes)) {
            App.preferences().edit().remove(SAVE_RUNE + name).apply();
        }
    }

    public void saveRunes() {
        boolean reset = false;
        if (!Utils.isEmpty(recommended_runes)) {
            reset = runes.equals(recommended_runes);
        } else {
            if (runes.size() == 3) {
                reset = true;
                for (int i = 0; i < 3; ++i) {
                    Integer quantity = runes.get(default_runes[i]);
                    if (null == quantity || quantity != 10) {
                        reset = false;
                        break;
                    }
                }
            }
        }

        SharedPreferences.Editor editor = App.preferences().edit();
        String key = SAVE_RUNE + name;
        if (reset) {
            editor.remove(key);
        } else {
            editor.putString(key, runes.toString());
        }
        editor.apply();
    }
}
