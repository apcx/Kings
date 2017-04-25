package apc.kings.data.hero;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import apc.kings.R;
import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Item;
import apc.kings.data.Rune;
import apc.kings.data.Skill;

public class Hero {

    public HeroType heroType;
    public CContext context;
    public String name;

    public List<Event> actions_active = new ArrayList<>();
    public Event[] actions_cast = new Event[3];
    Event action_attack = new Event(this, "攻击", 0);
    Event event_shield;
    public Skill[] skills;

    int base_attack;
    int bonus_attack;
    private int base_magic;
    int base_magic_defense;
    int base_move;
    int base_critical;

    int panel_level = 15;
    public int panel_hp;
    int panel_attack;
    private int panel_magic;
    int panel_defense;
    int panel_magic_defense;
    private int panel_penetrate;
    private int panel_penetrate_percent;
    private int panel_magic_penetrate;
    private int panel_magic_penetrate_percent;
    int panel_move_speed;
    int panel_attack_speed;
    int panel_critical;
    int panel_critical_damage = 2000;
    int panel_cdr;
    private int panel_regen;
    private int panel_flags;

    private int attr_flags;
    private int attr_price;
    int attr_attack_cd = 1000;
    private int attr_enchants;

    boolean has_anti_magic;
    private boolean has_hat;
    private boolean has_storm;
    private boolean has_cold_iron;
    private boolean has_frozen_heart;
    private boolean has_defense_boots;
    private boolean has_corrupt;
    private boolean has_corrupt_2;
    private boolean has_judgement;
    private boolean has_accurate;
    private boolean has_lightning;
    private boolean has_execute;
    private boolean has_heal;
    private boolean has_wound;
    private boolean has_shield_bottom;
    private boolean has_thorns;
    private boolean has_berserk;
    private boolean has_echo;

    boolean hit_normal;
    boolean hit_can_critical;
    int factor_damage = 100;
    double factor_damage_attack = 1;
    double factor_attack = 1;
    int bonus_damage;

    public Hero target;
    private Hero attacker;
    public int hp;
    int damage_can_critical;
    private int damage_cannot_critical;

    private boolean cnt_corrupt;
    private int cnt_lightning = 5;
    private int cnt_berserk;
    int shield_hero;
    private int shield_magic;
    private int shield_bottom;
    private SparseArray<int[]> critical_histories = new SparseArray<>();

    boolean in_alert_mine;
    private boolean in_red_power;
    private boolean in_storm;
    private boolean in_cold_iron;
    private boolean in_enchant;
    private boolean in_wound;
    private boolean in_cd_enchant;
    private boolean in_cd_judgement;
    private boolean in_cd_lighting;

    protected Hero(CContext context, HeroType heroType) {
        this.context = context;
        this.heroType = heroType;
        name = heroType.name;
        panel_hp = heroType.hp;
        base_attack = heroType.attack;
        panel_defense = heroType.defense;
        base_magic_defense = 169;
        base_move = heroType.move;
        panel_attack_speed = heroType.level_up_attack_speed * (panel_level - 1);
        panel_regen = heroType.regen;

        actions_cast[0] = new Event(this, "cast1", 0);
        actions_cast[1] = new Event(this, "cast2", 0);
        actions_cast[2] = new Event(this, "cast3", 0);
    }

    public static Hero create(CContext context, HeroType heroType) {
        Hero hero;
        try {
            Class clazz = Class.forName(Hero.class.getPackage().getName() + "." + (char) (heroType.resName.charAt(0) - 32) + heroType.resName.substring(1) + "Hero");
            //noinspection unchecked
            hero = (Hero) clazz.getDeclaredConstructor(CContext.class, HeroType.class).newInstance(context, heroType);
        } catch (Exception e) {
            hero = new Hero(context, heroType);
        }
        hero.updatePanel();
        return hero;
    }

    private void updatePanel() {
        initItems();
        initRunes();
        panel_attack = base_attack + bonus_attack;
        panel_magic = has_hat ? base_magic * 135 / 100 : base_magic;
        panel_magic_defense = base_magic_defense;
        if (has_anti_magic) {
            panel_magic_defense += Math.min(panel_attack * 40 / 100,300);
        }
        panel_critical = base_critical;
        panel_cdr = Math.min(panel_cdr, 400);
        hp = panel_hp;
    }

    private void initItems() {
        if (heroType.items != null) {
            for (Item item : heroType.items) {
                if (item != null) {
                    panel_flags |= item.panel_flags;
                    attr_price += item.price;
                    panel_hp += item.hp;
                    base_attack += item.attack;
                    base_magic += item.magic;
                    panel_defense += item.defense;
                    base_magic_defense += item.magic_defense;
                    panel_move_speed += item.move_speed;
                    panel_attack_speed += item.attack_speed;
                    base_critical += item.critical;
                    panel_cdr += item.cdr;
                    panel_regen += item.regen;
                    attr_flags |= item.flags;
                }
            }
            has_hat = (panel_flags & Item.FP_HAT) != 0;
            has_anti_magic = (panel_flags & Item.FP_ANTI_MAGIC) != 0;
            has_defense_boots = (attr_flags & Item.FLAG_DEFENSE_BOOTS) != 0;
            has_corrupt = (attr_flags & Item.FLAG_CORRUPT) != 0;
            has_corrupt_2 = (attr_flags & Item.FLAG_CORRUPT_2) != 0;
            has_judgement = (attr_flags & Item.FLAG_JUDGEMENT) != 0 && R.id.cat_archer == heroType.category;
            has_accurate = (attr_flags & Item.FLAG_ACCURATE) != 0;
            has_lightning = (attr_flags & Item.FLAG_LIGHTNING) != 0;
            has_execute = (attr_flags & Item.FLAG_EXECUTE) != 0;
            has_storm = (attr_flags & Item.FLAG_STORM) != 0;
            has_cold_iron = (attr_flags & Item.FLAG_COLD_IRON) != 0;
            has_frozen_heart = (attr_flags & Item.FLAG_FROZEN_HEART) != 0;
            has_heal = (attr_flags & Item.FLAG_HEAL) != 0;
            has_wound = (attr_flags & Item.FLAG_WOUND) != 0;
            has_shield_bottom = (attr_flags & Item.FLAG_SHIELD_BOTTOM) != 0;
            has_echo = (attr_flags & Item.FLAG_ECHO) != 0;
            has_thorns = (attr_flags & Item.FLAG_THORNS) != 0;
            has_berserk = (attr_flags & Item.FLAG_BERSERK) != 0;
            attr_enchants = attr_flags & Item.ENCHANT_VOODOO;

            if ((panel_flags & Item.FP_BOOTS) != 0) {
                base_move += 60;
            }
            if ((panel_flags & Item.FP_MPN_BOOTS) == Item.FP_MPN_BOOTS) {
                panel_magic_penetrate += 75;
            }
            if ((panel_flags & Item.FP_MPN_MASK) != 0) {
                panel_magic_penetrate += 75;
            }
            if ((panel_flags & Item.FP_MPN_VOID) != 0) {
                panel_magic_penetrate_percent += 45;
            }
            if ((panel_flags & Item.FP_PN) != 0) {
                panel_penetrate += 50 + panel_level * 10;
            }
            if ((panel_flags & Item.FP_PNP) != 0) {
                panel_penetrate_percent += 45;
            }
            if ((panel_flags & Item.FP_SENTINEL) != 0) {
                base_attack += 30 + panel_level * 2;
                base_magic += 60 + panel_level * 4;
            }
            if ((panel_flags & Item.FP_CRITICAL) != 0) {
                panel_critical_damage += 500;
            }
            if ((panel_flags & Item.FP_MARK) != 0) {
                panel_hp += 1400;
            }
            if ((panel_flags & Item.MOB_ATTACK) != 0) {
                panel_attack_speed += 30;
            }
            if ((panel_flags & Item.MOB_MAGIC) != 0) {
                base_magic += 120;
            }
            if ((panel_flags & Item.MOB_HP) != 0) {
                panel_hp += 1050;
            }
            if ((attr_flags & Item.FLAG_SHIELD_MAGIC) != 0) {
                shield_magic = 200 + panel_level * 120;
            }
        }
        panel_move_speed *= 10;
        panel_attack_speed *= 10;
        base_critical *= 10;
        panel_cdr *= 10;
    }

    private void initRunes() {
        if (heroType.runes != null) {
            int hp = 0;
            int attack = 0;
            int magic = 0;
            int defense = 0;
            int magic_defense = 0;
            int penetrate = 0;
            int magic_penetrate = 0;
            int regen = 0;
            for (Map.Entry<Rune, Integer> entry : heroType.runes.entrySet()) {
                Rune rune = entry.getKey();
                int n = entry.getValue();

                hp += rune.hp * n;
                attack += rune.attack * n;
                magic += rune.magic * n;
                defense += rune.defense * n;
                magic_defense += rune.magic_defense * n;
                penetrate += rune.penetrate * n;
                magic_penetrate += rune.magic_penetrate * n;
                regen += rune.regen * n;
                panel_move_speed += rune.move_speed * n;
                panel_attack_speed += rune.attack_speed * n;
                base_critical += rune.critical * n;
                panel_critical_damage += rune.critical_damage * n;
                panel_cdr += rune.cdr * n;
            }
            panel_hp += hp / 10;
            bonus_attack += attack / 10;
            base_magic += magic / 10;
            panel_defense += defense / 10;
            base_magic_defense += magic_defense / 10;
            panel_penetrate += penetrate / 10;
            panel_magic_penetrate += magic_penetrate / 10;
            panel_regen += regen / 10;
        }
    }

    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        if (target != null) {
            this.target = target;
            target.attacker = this;
            actions_active.add(action_attack);
            if (context.hasRedPower()) {
                in_red_power = true;
                context.addEvent(this, "失效", "绯红之力", 70000);
            }
        }
        if (attacked) {
            context.events.add(new Event(this, "回血", "每5秒", 5000, 5000));
        }
    }

    public void onEvent(Event event) {
        if (event.intervals > 0) {
            if (--event.intervals <= 0) {
                context.events.remove(event);
            }
        }
        if (event.period > 0) {
            event.time += event.period;
        }
        CLog log = new CLog(name, event.action, event.target, context.time);
        switch (event.action) {
            case "回血":
                switch (event.target) {
                    case "每5秒":
                        onRegen(log, panel_regen);
                        break;
                }
                break;
            case "持续伤害":
                context.events.remove(event);
                onRedPower();
                break;
            case "失效":
            case "复原":
            case "护盾消失":
                context.events.remove(event);
                print(event.action, event.target);
                switch (event.target) {
                    case "暴风":
                        in_storm = false;
                        panel_attack_speed -= 500;
                        break;
                    case "血怒":
                        base_attack -= 40;
                        shield_bottom = 0;
                        break;
                    case "绯红之力":
                        in_red_power = false;
                        break;
                    case "地雷破甲":
                        in_alert_mine = false;
                        break;
                }
                break;
            case "冷却":
                context.events.remove(event);
                switch (event.target) {
                    case "强击":
                        in_cd_enchant = false;
                        break;
                    case "审判":
                        in_cd_judgement = false;
                        break;
                    case "电弧":
                        in_cd_lighting = false;
                        break;
                }
                break;
        }
    }

    public void onAction(Event event) {
        switch (event.action) {
            case "攻击":
                doAttack();
                break;
            default:
                doSmartCast(Integer.parseInt(event.action.substring(4)) - 1);
        }
    }

    protected void doAttack() {
        action_attack.time = context.time + attr_attack_cd * 1000 / (1000 + Math.min(panel_attack_speed, 2000));
        hit_normal = true;
        onAttack(new CLog(name, "攻击", target.name, context.time));
        delayActions(100);
    }

    protected void doSmartCast(int index) {
        if (actions_active.contains(action_attack)) {
            Event action = actions_cast[index];
            int castEndTime = action.time + skills[index].swing;
            int attackEndTime = action_attack.time + 100;
            if (castEndTime - action_attack.time > attackEndTime - action.time) {
                doSkip(index);
            } else {
                doCast(index);
            }
        } else {
            doCast(index);
        }
    }

    void doSkip(int index) {  // delay the cast after the next attack
        actions_cast[index].time = action_attack.time + 101;
    }

    void doCast(int index) {
        if (attr_enchants != 0 && !in_cd_enchant) {
            in_cd_enchant = true;
            in_enchant = true;
            context.addEvent(this, "冷却", "强击", Item.ENCHANT_ICE == attr_enchants ? 3000 : 2000);
        }
        Skill skill = skills[index];
        actions_cast[index].time = context.time + skill.cd * (1000 - panel_cdr) / 1000;
        CLog log = new CLog(name, skill.name, Skill.TYPE_NONE == skill.damageType ? null : target.name, context.time);
        onCast(index, log);
        delayActions(Math.max(1, skill.swing));
    }

    protected void onAttack(CLog log) {
        onHit(log);   // It actually takes time for missiles to fly to the target. Call hit() immediately here, just for simplicity.
    }

    protected void onCast(int index, CLog log) {
        Skill skill = skills[index];
        if (skill.damageType != Skill.TYPE_NONE) {
            context.logs.add(log);
            int damage = target.onDamaged((int)((Skill.TYPE_MAGIC == skill.factorType ? panel_magic : panel_attack) * skill.damageFactor) + skill.damageBonus, skill.damageType);
            switch (skill.damageType) {
                case Skill.TYPE_PHYSICAL:
                    log.damage = damage;
                    break;
                case Skill.TYPE_MAGIC:
                    log.magic_damage = damage;
                    break;
                case Skill.TYPE_REAL:
                    log.real_damage = damage;
                    break;
            }
            onImpact();
        } else {
            context.logs.add(log);
        }
    }

    protected void onHit(CLog log) {
        onUpdateAttackCanCritical();
        double damage = panel_attack;
        if ((hit_normal || hit_can_critical)) {
            damage = getCriticalDamage(log);
        }
        context.logs.add(log);
        log.damage = target.onDamaged(damage * factor_damage_attack, Skill.TYPE_NORMAL);

        if (target.hp > 0) {
            int extra_damage = 0;
            if (has_corrupt) {
                extra_damage += target.hp * 8 / 100;
            }
            if (has_corrupt_2) {
                if (cnt_corrupt) {
                    extra_damage += 200 + panel_level * 20;
                }
                cnt_corrupt = !cnt_corrupt;
            }
            if (has_accurate) {
                extra_damage += 60;
            }
            if (extra_damage > 0) {
                log.extra_damage = target.onDamaged(extra_damage, Skill.TYPE_PHYSICAL);
            }
        }
        if (target.hp > 0) {
            onHitMagic(log);
        }
        checkStorm(log);

        if (in_enchant && target.hp > 0) {
            in_enchant = false;
            log = new CLog(name, "强击", target.name, context.time);
            context.logs.add(log);
            switch (attr_enchants) {
                case Item.ENCHANT_VOODOO:
                    log.magic_damage = target.onDamaged(panel_attack * 30 / 100 + panel_magic * 65 / 100, Skill.TYPE_MAGIC);
                    break;
                case Item.ENCHANT_MASTER:
                    log.damage = target.onDamaged(panel_attack, Skill.TYPE_PHYSICAL);
                    break;
                case Item.ENCHANT_ICE:
                    log.damage = target.onDamaged(150 + panel_level * 20, Skill.TYPE_PHYSICAL);
                    break;
                case Item.ENCHANT_MOB:
                    log.magic_damage = target.onDamaged(panel_magic * 30 / 100, Skill.TYPE_MAGIC);
                    break;
            }
        }

        if (has_judgement && target.hp > 0 && !in_cd_judgement) {
            in_cd_judgement = true;
            log = new CLog(name, "审判", target.name, context.time);
            context.logs.add(log);
            log.magic_damage = target.onDamaged(panel_hp * 8 / 100, Skill.TYPE_MAGIC);
            context.addEvent(this, "冷却", "审判", 1000);
        }

        if (has_lightning && target.hp > 0 && !in_cd_lighting) {
            if (--cnt_lightning <= 0) {
                in_cd_lighting = true;
                cnt_lightning = 5;
                damage_can_critical = 100;
                damage_cannot_critical = 0;
                log = new CLog(name, "电弧", target.name, context.time);
                context.logs.add(log);
                log.magic_damage = target.onDamaged(getCriticalDamage(log), Skill.TYPE_MAGIC);
                context.addEvent(this, "冷却", "电弧", 500);
                checkStorm(log);
            }
        }
        onImpact();

        if (in_red_power && target.hp > 0) {
            target.onRedPower();
            context.updateBuff(target, "持续伤害", "绯红之力", 500);
        }
    }

    protected void onUpdateAttackCanCritical() {
        double criticalFactor = Math.min(factor_attack, 1);
        damage_can_critical = (int) (panel_attack * criticalFactor);
        damage_cannot_critical = (int) (panel_attack * (factor_attack - criticalFactor)) + bonus_damage;
    }

    protected void onHitMagic(CLog log) {

    }

    private void onRedPower() {
        CLog log = new CLog(name, "持续伤害", "绯红之力", context.time);
        context.logs.add(log);
        log.real_damage = onDamaged(17 + attacker.panel_level * 2, Skill.TYPE_REAL);
    }

    protected int onDamaged(double damage_raw, int type) {
        damage_raw *= getDamageFactor(Skill.TYPE_NORMAL == type);
        if (has_thorns && (Skill.TYPE_NORMAL == type || Skill.TYPE_PHYSICAL == type)) {
            CLog log = new CLog(name, "荆棘", attacker.name, context.time);
            context.logs.add(log);
            log.magic_damage = attacker.onDamaged(damage_raw * 0.15, Skill.TYPE_MAGIC);
        }
        int damage_log = (int) damage_raw;
        switch (type) {
            case Skill.TYPE_NORMAL:
            case Skill.TYPE_PHYSICAL:
                damage_log = (int) (damage_raw * getDefenseFactor());
                break;
            case Skill.TYPE_MAGIC:
                damage_log = (int) (damage_raw * getMagicDefenseFactor());
                break;
        }

        int damage = damage_log;
        int damage_shield;
        if (Skill.TYPE_MAGIC == type && shield_magic > 0) {
            damage_shield = Math.min(damage, shield_magic);
            shield_magic -= damage_shield;
            damage -= damage_shield;
            if (shield_magic <= 0) {
                print("护盾击破", "魔女斗篷");
            }
        }
        if (type != Skill.TYPE_REAL) {
            if (shield_bottom > 0) {
                damage_shield = Math.min(damage, shield_bottom);
                shield_bottom -= damage_shield;
                damage -= damage_shield;
                if (shield_bottom <= 0) {
                    print("护盾击破", "血怒护盾");
                }
            }
            if (shield_hero > 0) {
                damage_shield = Math.min(damage, shield_hero);
                shield_hero -= damage_shield;
                damage -= damage_shield;
                if (shield_hero <= 0) {
                    context.events.remove(event_shield);
                    print("护盾击破", event_shield.target);
                }
            }
        }
        if (damage > 0) {
            hp -= damage;
            if (has_berserk && cnt_berserk < 5) {
                factor_damage += 2;
                print("强化", "无畏" + ++cnt_berserk);
            }
            if (hp < panel_hp * 30 / 100) {
                if (has_shield_bottom) {
                    has_shield_bottom = false;
                    base_attack += 40;
                    shield_bottom = panel_hp * 30 / 100;
                    print("强化", "血怒");
                    context.addEvent(this, "失效", "血怒", 8000);
                }
            }
        }
        return damage_log;
    }

    private void onImpact() {
        if (target.has_cold_iron && !in_cold_iron) {
            in_cold_iron = true;
            panel_attack_speed -= 300;
            print("弱化", "寒铁");
        }
        if (has_wound && !target.in_wound) {
            target.in_wound = true;
            target.print("弱化", "重伤");
        }
    }

    void onRegen(CLog log, int regen) {
        int damaged = panel_hp - hp;
        if (damaged > 0) {
            log.regen = Math.min(damaged, regen);
            hp += log.regen;
            context.logs.add(log);
        }
    }

    private double getCriticalDamage(CLog log) {
        int key = "黄忠".equals(name) && !"电弧".equals(log.action) ? base_attack : damage_can_critical;
        int[] history = critical_histories.get(key);
        if (null == history) {
            history = new int[2];
            // history[0]: theoretical critical count (*1000)
            // history[1]: actual critical count
            critical_histories.put(key, history);
        }
        int critical = Math.min(panel_critical, 1000);
        history[0] += critical;
        if (history[0] >= 1000 * (history[1] + 1)) {
            ++history[1];
            log.critical = true;
        }
        double damage = damage_can_critical;
        if (context.isCombo()) {
            damage *= (panel_critical_damage * critical + 1000 * (1000 - critical)) / 1000000.0;
        } else if (log.critical) {
            damage *= panel_critical_damage / 1000.0;
        }
        return damage + damage_cannot_critical;
    }

    private void checkStorm(CLog log) {
        if (has_storm && log.critical) {
            context.updateBuff(this, "失效", "暴风", 2000);
            if (!in_storm) {
                in_storm = true;
                panel_attack_speed += 500;
                print("强化", "暴风");
            }
        }
    }

    private double getDefenseFactor() {
        int defense = panel_defense;
        if (in_alert_mine) {
            defense = defense * 70 / 100;   // Round down. Confirmed!
        }
        Hero attacker = this.attacker != null ? this.attacker : target;
        return 600.0 / (600 + Math.max(0, defense - attacker.panel_penetrate) * (100 - attacker.panel_penetrate_percent) / 100);
    }

    private double getMagicDefenseFactor() {
        Hero attacker = this.attacker != null ? this.attacker : target;
        return 600.0 / (600 + Math.max(0, panel_magic_defense - attacker.panel_magic_penetrate) * (100 - attacker.panel_magic_penetrate_percent) / 100);
    }

    private double getDamageFactor(boolean normal) {
        Hero attacker = this.attacker != null ? this.attacker : target;
        int factor = attacker.factor_damage;
        if (normal && has_defense_boots) {
            factor -= 15;
        }
        if (attacker.has_execute && hp * 2 < panel_hp) {
            factor += 30;
        }
        return factor / 100.0;
    }

    float getHealRate() {
        int rate = 100;
        if (has_heal) {
            rate += hp * 100 < panel_hp * 50 ? 30 : 10;
        }
        if (in_wound) {
            rate -= 50;
        }
        return rate / 100.0f;
    }

    public double getAverageMove() {
        return getPanelMove(base_move * (1000 + panel_move_speed) / 1000);
    }

    int getPanelMove(int move) {
        if (move > 575) {
            move = 560 + (move - 575) * 50 / 100;
        } else if (move > 500) {
            move = 500 + (move - 500) * 75 / 100;
        }
        return move;
    }

    void print(String action, String target) {
        context.logs.add(new CLog(name, action, target, context.time));
    }

    private void delayActions(int swing) {
        swing += context.time;
        for (int i = 0, n = actions_active.size(); i < n; ++i) {
            actions_active.get(i).delayTo(swing);
        }
    }

    public int getPrice() {
        int price = attr_price;
        if (context.mobPurchased()) {
            boolean empty_slot = false;
            boolean mob_used = false;
            for (Item item : heroType.items) {
                if (null == item) {
                    empty_slot = true;
                } else {
                    switch (item.name) {
                        case "贪婪之噬":
                        case "符文大剑":
                        case "巨人之握":
                            mob_used = true;
                            break;
                    }
                    if (mob_used) {
                        break;
                    }
                }
            }
            if (!mob_used) {
                price += empty_slot ? 250 : 100;
            }
        }
        return Math.max(1, price);
    }
}
