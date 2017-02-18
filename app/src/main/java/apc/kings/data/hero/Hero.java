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
    public Event action_attack = new Event(this, "攻击", 0);
    public Skill[] skills;

    public int attr_price;
    int attr_mhp;
    int attr_attack_base;
    int attr_attack_panel;
    int attr_magic;
    int attr_defense;
    int attr_magic_defense;
    private int attr_penetrate;
    private int attr_magic_penetrate;
    private int attr_regen;
    int attr_attack_cd = 1000;
    int attr_attack_speed;
    int attr_critical;
    int attr_critical_damage = 2000;
    private int attr_cdr;
    int attr_heal = 10;
    private int attr_flags;
    private int attr_enchants;

    private boolean has_storm;
    private boolean has_cold_iron;
    private boolean has_frozen_heart;
    private boolean has_penetrate;
    private boolean has_magic_penetrate;
    private boolean has_defense_boots;
    private boolean has_corrupt;
    private boolean has_accurate;
    private boolean has_lightning;
    private boolean has_execute;
    private boolean has_heal;
    private boolean has_recover;
    private boolean has_wound;

    boolean hit_normal;
    boolean hit_can_critical;
    int factor_damage = 100;
    double factor_damage_attack = 1;
    double factor_attack = 1;
    int bonus_damage;

    public Hero target;
    public int hp;
    int damage_can_critical;
    private int damage_cannot_critical;

    private int cnt_lightning = 5;
    private int shield_magic;
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
        attr_mhp = heroType.hp;
        attr_attack_base = heroType.attack;
        attr_defense = heroType.defense;
        attr_magic_defense = 169;
        attr_regen = heroType.regen;
        attr_attack_speed = heroType.attack_speed_per_level * 14;
        initItems();
        initRunes();
        attr_cdr = Math.min(attr_cdr, 400);
        hp = attr_mhp;
        attr_attack_panel = attr_attack_base;

        actions_cast[0] = new Event(this, "cast1", 0);
        actions_cast[1] = new Event(this, "cast2", 0);
        actions_cast[2] = new Event(this, "cast3", 0);
    }

    private void initItems() {
        if (heroType.items != null) {
            for (Item item : heroType.items) {
                if (item != null) {
                    attr_price += item.price;
                    attr_mhp += item.hp;
                    attr_attack_base += item.attack;
                    attr_magic += item.magic;
                    attr_defense += item.defense;
                    attr_magic_defense += item.magic_defense;
                    attr_regen += item.regen;
                    attr_attack_speed += item.attack_speed;
                    attr_critical += item.critical;
                    attr_cdr += item.cdr;
                    attr_flags |= item.flags;
                }
            }
            has_penetrate = (attr_flags & Item.FLAG_PENETRATE) != 0;
            has_magic_penetrate = (attr_flags & Item.FLAG_MAGIC_PENETRATE_RATE) != 0;
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
            attr_enchants = attr_flags & Item.ENCHANT_VOODOO;

            if (has_heal) {
                attr_heal += 2;
            }
            if ((attr_flags & Item.FLAG_CUT) != 0) {
                attr_penetrate += 250;
            }
            if ((attr_flags & Item.FLAG_CRITICAL) != 0) {
                attr_critical_damage += 500;
            }
            if ((attr_flags & Item.FLAG_MAGIC_PENETRATE_MASK) != 0) {
                attr_magic_penetrate += 75;
            }
            if ((attr_flags & Item.FLAG_MAGIC_PENETRATE_BOOTS) != 0) {
                attr_magic_penetrate += 75;
            }
            if ((attr_flags & Item.FLAG_SENTINEL) != 0) {
                attr_attack_base += 60;
                attr_magic += 120;
            }
            if ((attr_flags & Item.FLAG_MAGIC_SHIELD) != 0) {
                shield_magic = 2000;
            }
            if ((attr_flags & Item.MOB_ATTACK) != 0) {
                attr_attack_speed += 30;
            }
            if ((attr_flags & Item.MOB_MAGIC) != 0) {
                attr_magic += 120;
            }
            if ((attr_flags & Item.MOB_HP) != 0) {
                attr_mhp += 1050;
            }
        }
        attr_attack_speed *= 10;
        attr_critical *= 10;
        attr_cdr *= 10;
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
                attr_attack_speed += rune.attack_speed * n;
                attr_critical += rune.critical * n;
                attr_critical_damage += rune.critical_damage * n;
                attr_cdr += rune.cdr * n;
            }
            attr_mhp += hp / 10;
            attr_attack_base += attack / 10;
            attr_magic += magic / 10;
            attr_defense += defense / 10;
            attr_magic_defense += magic_defense / 10;
            attr_penetrate += penetrate / 10;
            attr_magic_penetrate += magic_penetrate / 10;
            attr_regen += regen / 10;
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
                onRegen(log, attr_regen);
                break;
            case "振兴回复":
                onRegen(log, Math.round(attr_mhp * attr_heal * 0.001f));
                break;
            case "血铠":
                onRegen(log, Math.round(attr_mhp * attr_heal * 0.003f));
                break;
            case "失效":
            case "复原":
            case "护盾消失":
                context.events.remove(event);
                print(event.action, event.target);
                switch (event.target) {
                    case "暴风":
                        in_storm = false;
                        attr_attack_speed -= 500;
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
        action_attack.time = context.time + attr_attack_cd * 1000 / (1000 + Math.min(attr_attack_speed, 2000));
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
        actions_cast[index].time = context.time + skill.cd * (1000 - attr_cdr) / 1000;
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
            int damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? attr_attack_panel : attr_magic) * skill.damageFactor) + skill.damageBonus;
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
                    log.magic_damage = (int) (((int) (attr_attack_panel * 0.3) + (int) (attr_magic * 0.65)) * getMagicDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
                    break;
                case Item.ENCHANT_MASTER:
                    log.damage = (int) (attr_attack_panel * getDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.damage, Skill.TYPE_PHYSICAL);
                    break;
                case Item.ENCHANT_ICE:
                    log.damage = (int) (450 * getDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.damage, Skill.TYPE_PHYSICAL);
                    break;
                case Item.MOB_MAGIC:
                    log.magic_damage = (int) ((int) (attr_magic * 0.3) * getMagicDefenseFactor() * getDamageFactor());
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
        damage_can_critical = (int) (attr_attack_panel * criticalFactor);
        damage_cannot_critical = (int) (attr_attack_panel * (factor_attack - criticalFactor)) + bonus_damage;
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
            if (has_recover && !in_cd_recover && hp < attr_mhp * 0.3) {
                in_cd_recover = true;
                context.addEvent(this, "血铠", 5, 1000);
                context.addEvent(this, "冷却", "血铠", 20000);
            }
        }
    }

    protected int onSpecificShield(int damage) {
        return damage;
    }

    private void onCrash() {
        if (target.has_cold_iron && !in_cold_iron) {
            in_cold_iron = true;
            attr_attack_speed -= 300;
            print("弱化", "寒铁");
        }
        if (has_wound && !target.in_wound) {
            target.in_wound = true;
            target.attr_heal -= 5;
            target.print("弱化", "重伤");
        }
    }

    private void onRegen(CLog log, int regen) {
        int damaged = attr_mhp - hp;
        if (damaged > 0) {
            log.regen = Math.min(damaged, regen);
            hp += log.regen;
            context.logs.add(log);
        }
    }

    void checkFrozenHeart() {
        context.far = false;
        if (target.has_frozen_heart) {
            attr_attack_speed -= 300;
            print("弱化", "冰心");
        }
    }

    private double getCriticalDamage(CLog log) {
        int key = "黄忠".equals(name) && !"电弧".equals(log.action) ? attr_attack_base : damage_can_critical;
        int[] history = critical_histories.get(key);
        if (null == history) {
            history = new int[2];
            // history[0]: theoretical critical count (*1000)
            // history[1]: actual critical count
            critical_histories.put(key, history);
        }
        int critical = Math.min(attr_critical, 1000);
        history[0] += critical;
        if (history[0] >= 1000 * (history[1] + 1)) {
            ++history[1];
            log.critical = true;
        }
        double damage = damage_can_critical;
        if (context.isCombo()) {
            damage *= (attr_critical_damage * critical + 1000 * (1000 - critical)) / 1000000.0;
        } else if (log.critical) {
            damage *= attr_critical_damage / 1000.0;
        }
        return damage;
    }

    private void checkStorm(CLog log) {
        if (has_storm && log.critical) {
            context.updateBuff(this, "失效", "暴风", 2000);
            if (!in_storm) {
                in_storm = true;
                attr_attack_speed += 500;
                print("强化", "暴风");
            }
        }
    }

    double getDefenseFactor() {
        int defense = target.attr_defense;
        if (target.in_alert_mine) {
            defense -= defense * 30 / 100;
        }
        defense = Math.max(0, defense - attr_penetrate);
        if (has_penetrate) {
            defense -= defense * 45 / 100;
        }
        return 600.0 / (600 + defense);
    }

    double getMagicDefenseFactor() {
        int defense = Math.max(0, target.attr_magic_defense - attr_magic_penetrate);
        if (has_magic_penetrate) {
            defense -= defense * 45 / 100;
        }
        return 600.0 / (600 + defense);
    }

    double getDamageFactor() {
        return getDamageFactor(false);
    }

    private double getDamageFactor(boolean normal) {
        int factor = factor_damage;
        if (normal && target.has_defense_boots) {
            factor -= 15;
        }
        if (has_execute && target.hp * 2 < target.attr_mhp) {
            factor += 30;
        }
        return factor / 100.0;
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
