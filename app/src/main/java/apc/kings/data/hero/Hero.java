package apc.kings.data.hero;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Skill[] skills;

    int base_attack;
    int base_magic;
    int base_move;

    private int panel_flags;
    int panel_hp;
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
    private int panel_cdr;
    private int panel_regen;

    private int attr_flags;
    public int attr_price;
    int attr_attack_cd = 1000;
    private int attr_heal = 10;
    private int attr_enchants;

    private boolean has_storm;
    private boolean has_cold_iron;
    private boolean has_frozen_heart;
    private boolean has_defense_boots;
    private boolean has_corrupt;
    private boolean has_accurate;
    private boolean has_lightning;
    private boolean has_execute;
    private boolean has_heal;
    private boolean has_recover;
    private boolean has_wound;
    private boolean has_shield_bottom;

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

    private int cnt_lightning = 5;
    private int shield_magic;
    private int shield_bottom;
    private SparseArray<int[]> critical_histories = new SparseArray<>();

    boolean in_alert_mine;
    private boolean in_storm;
    private boolean in_cold_iron;
    private boolean in_enchant;
    private boolean in_wound;
    private boolean in_cd_enchant;
    private boolean in_cd_lighting;
    private boolean in_cd_heal;
    private boolean in_cd_recover;

    protected Hero(CContext context, HeroType heroType) {
        this.context = context;
        this.heroType = heroType;
        name = heroType.name;
        panel_hp = heroType.hp;
        base_attack = heroType.attack;
        panel_defense = heroType.defense;
        panel_magic_defense = 169;
        base_move = heroType.move;
        panel_attack_speed = heroType.level_up_attack_speed * 14;
        panel_regen = heroType.regen;
        initItems();
        initRunes();
        panel_attack = base_attack;
        panel_magic = base_magic;
        panel_cdr = Math.min(panel_cdr, 400);
        hp = panel_hp;

        actions_cast[0] = new Event(this, "cast1", 0);
        actions_cast[1] = new Event(this, "cast2", 0);
        actions_cast[2] = new Event(this, "cast3", 0);
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
                    panel_magic_defense += item.magic_defense;
                    panel_move_speed += item.move_speed;
                    panel_attack_speed += item.attack_speed;
                    panel_critical += item.critical;
                    panel_cdr += item.cdr;
                    panel_regen += item.regen;
                    attr_flags |= item.flags;
                }
            }
            has_defense_boots = (attr_flags & Item.FLAG_DEFENSE_BOOTS) != 0;
            has_corrupt = (attr_flags & Item.FLAG_CORRUPT) != 0;
            has_accurate = (attr_flags & Item.FLAG_ACCURATE) != 0;
            has_lightning = (attr_flags & Item.FLAG_LIGHTNING) != 0;
            has_execute = (attr_flags & Item.FLAG_EXECUTE) != 0;
            has_storm = (attr_flags & Item.FLAG_STORM) != 0;
            has_cold_iron = (attr_flags & Item.FLAG_COLD_IRON) != 0;
            has_frozen_heart = (attr_flags & Item.FLAG_FROZEN_HEART) != 0;
            has_heal = (attr_flags & Item.FLAG_HEAL) != 0;
            has_recover = (attr_flags & Item.FLAG_RECOVER) != 0;
            has_wound = (attr_flags & Item.FLAG_WOUND) != 0;
            has_shield_bottom = (attr_flags & Item.FLAG_SHIELD_BOTTOM) != 0;
            attr_enchants = attr_flags & Item.ENCHANT_VOODOO;

            if ((panel_flags & Item.FP_BOOTS) != 0) {
                base_move += 60;
            }
            if ((panel_flags & Item.FP_MPN_BOOTS) == Item.FP_MPN_BOOTS) {
                panel_magic_penetrate += 75;
            }
            if ((panel_flags & Item.FP_PN) != 0) {
                panel_penetrate += 250;
            }
            if ((panel_flags & Item.FP_PNP) != 0) {
                panel_penetrate_percent += 45;
            }
            if ((panel_flags & Item.FP_SENTINEL) != 0) {
                base_attack += 60;
                base_magic += 120;
            }
            if ((panel_flags & Item.FP_CRITICAL) != 0) {
                panel_critical_damage += 500;
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

            if (has_heal) {
                attr_heal += 2;
            }
            if ((attr_flags & Item.FLAG_SHIELD_MAGIC) != 0) {
                shield_magic = 2000;
            }
        }
        panel_move_speed *= 10;
        panel_attack_speed *= 10;
        panel_critical *= 10;
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
                panel_critical += rune.critical * n;
                panel_critical_damage += rune.critical_damage * n;
                panel_cdr += rune.cdr * n;
            }
            panel_hp += hp / 10;
            base_attack += attack / 10;
            base_magic += magic / 10;
            panel_defense += defense / 10;
            panel_magic_defense += magic_defense / 10;
            panel_penetrate += penetrate / 10;
            panel_magic_penetrate += magic_penetrate / 10;
            panel_regen += regen / 10;
        }
    }

    public static Hero create(CContext context, HeroType heroType) {
        try {
            Class clazz = Class.forName(Hero.class.getPackage().getName() + "." + (char) (heroType.resName.charAt(0) - 32) + heroType.resName.substring(1) + "Hero");
            //noinspection unchecked
            return (Hero) clazz.getDeclaredConstructor(CContext.class, HeroType.class).newInstance(context, heroType);
        } catch (Exception e) {
            return new Hero(context, heroType);
        }
    }

    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        if (target != null) {
            this.target = target;
            target.attacker = this;
            actions_active.add(action_attack);
            if (!context.far) {
                checkFrozenHeart();
            }
        }
        if (attacked) {
            context.events.add(new Event(this, "回血", 5000));
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
        CLog log = new CLog(name, event.action, null, context.time);
        switch (event.action) {
            case "回血":
                onRegen(log, panel_regen);
                break;
            case "振兴回复":
                onRegen(log, Math.round(panel_hp * attr_heal * 0.001f));
                break;
            case "血铠":
                onRegen(log, Math.round(panel_hp * attr_heal * 0.003f));
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
                    case "地雷破甲":
                        in_alert_mine = false;
                        break;
                    case "血怒":
                        base_attack -= 40;
                        shield_bottom = 0;
                        break;
                }
                break;
            case "冷却":
                context.events.remove(event);
                switch (event.target) {
                    case "强击":
                        in_cd_enchant = false;
                        break;
                    case "电弧":
                        in_cd_lighting = false;
                        break;
                    case "振兴回复":
                        in_cd_heal = false;
                        break;
                    case "血铠":
                        in_cd_recover = false;
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
            int damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? panel_attack : panel_magic) * skill.damageFactor) + skill.damageBonus;
            switch (skill.damageType) {
                case Skill.TYPE_PHYSICAL:
                    damage = log.damage = (int) (damage * getDefenseFactor() * getDamageFactor());
                    break;
                case Skill.TYPE_MAGIC:
                    damage = log.magic_damage = (int) (damage * getMagicDefenseFactor() * getDamageFactor());
                    break;
                case Skill.TYPE_REAL:
                    damage = log.real_damage = (int) (damage * getDamageFactor());
                    break;
            }
            target.onDamaged(damage, skill.damageType);
            onCrash();
        } else {
            context.logs.add(log);
        }
    }

    protected void onHit(CLog log) {
        onUpdateAttackCanCritical();
        double damage = damage_can_critical;
        if ((hit_normal || hit_can_critical)) {
            damage = getCriticalDamage(log);
        }
        log.damage = (int) ((damage + damage_cannot_critical) * getDefenseFactor() * getDamageFactor(hit_normal) * factor_damage_attack);
        context.logs.add(log);
        target.onDamaged(log.damage, Skill.TYPE_PHYSICAL);

        if (target.hp > 0) {
            damage = 0;
            if (has_corrupt) {
                damage = target.hp * 0.08;
            }
            if (has_accurate) {
                damage += 60;
            }
            if (damage > 0) {
                log.extra_damage = (int) (damage * getDefenseFactor() * getDamageFactor());
                target.onDamaged(log.extra_damage, Skill.TYPE_PHYSICAL);
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
                    log.magic_damage = (int) (((int) (panel_attack * 0.3) + (int) (panel_magic * 0.65)) * getMagicDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
                    break;
                case Item.ENCHANT_MASTER:
                    log.damage = (int) (panel_attack * getDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.damage, Skill.TYPE_PHYSICAL);
                    break;
                case Item.ENCHANT_ICE:
                    log.damage = (int) (450 * getDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.damage, Skill.TYPE_PHYSICAL);
                    break;
                case Item.ENCHANT_MOB:
                    log.magic_damage = (int) ((int) (panel_magic * 0.3) * getMagicDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
                    break;
            }
        }

        if (has_lightning && target.hp > 0 && !in_cd_lighting) {
            if (--cnt_lightning <= 0) {
                in_cd_lighting = true;
                cnt_lightning = 5;
                damage_can_critical = 100;
                log = new CLog(name, "电弧", target.name, context.time);
                log.magic_damage = (int) (getCriticalDamage(log) * getMagicDefenseFactor() * getDamageFactor());
                context.logs.add(log);
                target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
                context.addEvent(this, "冷却", "电弧", 500);
                checkStorm(log);
            }
        }

        onCrash();
    }

    protected void onUpdateAttackCanCritical() {
        double criticalFactor = Math.min(factor_attack, 1);
        damage_can_critical = (int) (panel_attack * criticalFactor);
        damage_cannot_critical = (int) (panel_attack * (factor_attack - criticalFactor)) + bonus_damage;
    }

    protected void onHitMagic(CLog log) {

    }

    void onDamaged(int damage, int type) {
        if (Skill.TYPE_MAGIC == type && shield_magic > 0) {
            int damage_shield = Math.min(damage, shield_magic);
            shield_magic -= damage_shield;
            damage -= damage_shield;
            if (shield_magic <= 0) {
                print("护盾击破", "魔女斗篷");
            }
        }
        if (shield_bottom > 0) {
            int damage_shield = Math.min(damage, shield_bottom);
            shield_bottom -= damage_shield;
            damage -= damage_shield;
            if (shield_bottom <= 0) {
                print("护盾击破", "血怒护盾");
            }
        }
        if (type != Skill.TYPE_REAL) {
            damage = onSpecificShield(damage);
        }
        if (damage > 0) {
            hp -= damage;
            if (has_heal && !in_cd_heal) {
                in_cd_heal = true;
                context.addEvent(this, "振兴回复", 4, 500);
                context.addEvent(this, "冷却", "振兴回复", 10000);
            }
            if (attacker.has_wound && !in_wound) {
                in_wound = true;
                attr_heal -= 5;
                print("弱化", "重伤");
            }
            if (hp < panel_hp * 30 / 100) {
                if (has_shield_bottom) {
                    has_shield_bottom = false;
                    base_attack += 40;
                    shield_bottom = panel_hp * 30 / 100;
                    print("强化", "血怒");
                    context.addEvent(this, "失效", "血怒", 8000);
                }
                if (has_recover && !in_cd_recover) {
                    in_cd_recover = true;
                    context.addEvent(this, "血铠", 5, 1000);
                    context.addEvent(this, "冷却", "血铠", 20000);
                }
            }
        }
    }

    protected int onSpecificShield(int damage) {
        return damage;
    }

    private void onCrash() {
        if (target.has_cold_iron && !in_cold_iron) {
            in_cold_iron = true;
            panel_attack_speed -= 300;
            print("弱化", "寒铁");
        }
    }

    private void onRegen(CLog log, int regen) {
        int damaged = panel_hp - hp;
        if (damaged > 0) {
            log.regen = Math.min(damaged, regen);
            hp += log.regen;
            context.logs.add(log);
        }
    }

    void checkFrozenHeart() {
        context.far = false;
        if (target.has_frozen_heart) {
            panel_attack_speed -= 300;
            print("弱化", "冰心");
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
        return damage;
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

    double getDefenseFactor() {
        int defense = target.panel_defense;
        if (target.in_alert_mine) {
            defense = defense * 70 / 100;   // Round down. Confirmed!
        }
        return 600.0 / (600 + Math.max(0, defense - panel_penetrate) * (100 - panel_penetrate_percent) / 100);
    }

    double getMagicDefenseFactor() {
        return 600.0 / (600 + Math.max(0, target.panel_magic_defense - panel_magic_penetrate) * (100 - panel_magic_penetrate_percent) / 100);
    }

    double getDamageFactor() {
        return getDamageFactor(false);
    }

    private double getDamageFactor(boolean normal) {
        int factor = factor_damage;
        if (normal && target.has_defense_boots) {
            factor -= 15;
        }
        if (has_execute && target.hp * 2 < target.panel_hp) {
            factor += 30;
        }
        return factor / 100.0;
    }

    protected double getAverageMove() {
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
}
