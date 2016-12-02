package apc.kings.data.hero;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Item;
import apc.kings.data.Rune;
import apc.kings.data.Skill;
import apc.kings.data.combat.Event;

public class Hero {

    public HeroType heroType;
    public CContext context;
    public String name;

    public List<Event> actions = new ArrayList<>();
    public Event attackEvent = new Event(this, "攻击", 0);
    public Event[] castEvents = new Event[3];
    public Skill[] skills;

    public int attr_mhp;
    public int attr_price;
    protected int attr_attack;
    protected int attr_magic;
    protected int attr_defense;
    protected int attr_magic_defense;
    protected int attr_penetrate;
    protected int attr_magic_penetrate;
    protected int attr_regen;
    protected int attr_attack_cd = 1000;
    protected int attr_flags;
    protected int attr_enchants;
    protected double attr_attack_speed;
    protected double attr_critical;
    protected double attr_critical_damage = 2;
    protected double attr_cd_factor;

    public double tgNormalFactor;

    public boolean has_storm;
    public boolean has_cold_iron;
    public boolean has_cut;
    public boolean has_penetrate;
    public boolean has_magic_penetrate_rate;
    public boolean has_magic_penetrate_boots;
    public boolean has_magic_penetrate_mask;
    public boolean has_corrupt;
    public boolean has_accurate;
    public boolean has_lightning;
    public boolean has_execute;

    public Hero target;
    public int hp;
    double attackFactor = 1;
    int attackBonus;
    int attackCanCritical;
    int attackCannotCritical;

    int cutCount;
    int hitCount;
    int criticalCount;
    int lightingCount = 5;

    protected boolean in_storm;
    protected boolean in_cold_iron;
    protected boolean in_enchant;
    protected boolean in_cd_enchant;
    protected boolean in_cd_lighting;

    protected Hero(CContext context, HeroType heroType) {
        this.context = context;
        this.heroType = heroType;
        name = heroType.name;
        attr_mhp = heroType.hp;
        attr_attack = heroType.attack;
        attr_defense = heroType.defense;
        attr_magic_defense = 169;
        attr_regen = heroType.regen;
        attr_attack_speed = heroType.attackSpeedPerLevel * 14;

        if (heroType.items != null) {
            for (Item item : heroType.items) {
                if (item != null) {
                    attr_price += item.price;
                    attr_mhp += item.hp;
                    attr_attack += item.attack;
                    attr_magic += item.magic;
                    attr_defense += item.defense;
                    attr_magic_defense += item.magicDefense;
                    attr_regen += item.regen;
                    attr_attack_speed += item.attackSpeed;
                    attr_critical += item.critical;
                    attr_cd_factor += item.cdReduction;
                    attr_flags |= item.flags;
                }
            }

            double attack = 0;
            double magic = 0;
            double defense = 0;
            double magicDefense = 0;
            double penetrate = 0;
            double magicPenetrate = 0;
            if (heroType.runes != null) {
                for (Map.Entry<Rune, Integer> entry : heroType.runes.entrySet()) {
                    Rune rune = entry.getKey();
                    int n = entry.getValue();

                    attr_attack_speed += rune.attackSpeed * n;
                    attr_mhp += rune.hp * n;
                    attack += rune.attack * n;
                    magic += rune.magic * n;
                    defense += rune.defense * n;
                    magicDefense += rune.magicDefense * n;
                    penetrate += rune.penetrate * n;
                    magicPenetrate += rune.magicPenetrate * n;
                    attr_regen += rune.regen * n;
                    attr_critical += rune.critical * n / 100;
                    attr_critical_damage += rune.criticalDamage * n / 100;
                    attr_cd_factor += rune.cdReduction * n;
                }
                this.attr_attack += attack;
                this.attr_magic += magic;
                this.attr_defense += defense;
                this.attr_magic_defense += magicDefense;
                this.attr_penetrate = (int) penetrate;
                this.attr_magic_penetrate = (int) magicPenetrate;
            }

            if ((attr_flags & Item.FLAG_CRITICAL) != 0) {
                attr_critical_damage += 0.5;
            }
            attr_cd_factor = 1 - attr_cd_factor;
            has_cut = (attr_flags & Item.FLAG_CUT) != 0;
            has_penetrate = (attr_flags & Item.FLAG_PENETRATE) != 0;
            has_magic_penetrate_rate = (attr_flags & Item.FLAG_MAGIC_PENETRATE_RATE) != 0;
            has_magic_penetrate_boots = (attr_flags & Item.FLAG_MAGIC_PENETRATE_BOOTS) != 0;
            has_magic_penetrate_mask = (attr_flags & Item.FLAG_MAGIC_PENETRATE_MASK) != 0;
            has_corrupt = (attr_flags & Item.FLAG_CORRUPT) != 0;
            has_accurate = (attr_flags & Item.FLAG_ACCURATE) != 0;
            has_lightning = (attr_flags & Item.FLAG_LIGHTNING) != 0;
            has_execute = (attr_flags & Item.FLAG_EXECUTE) != 0;
            has_storm = (attr_flags & Item.FLAG_STORM) != 0;
            has_cold_iron = (attr_flags & Item.FLAG_COLD_IRON) != 0;
            attr_enchants = attr_flags & Item.ENCHANT_TRINITY;
        }
        hp = attr_mhp;

        castEvents[0] = new Event(this, "cast1", 0);
        castEvents[1] = new Event(this, "cast2", 0);
        castEvents[2] = new Event(this, "cast3", 0);
    }

    @NonNull
    public static Hero create(@NonNull CContext context, @NonNull HeroType heroType) {
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
            tgNormalFactor = (target.attr_flags & Item.FLAG_BOOTS_DEFENSE) == 0 ? 1 : 0.85;
            if ((target.attr_flags & Item.FLAG_FROZEN_HEART) != 0) {
                attr_attack_speed -= 30;
                print("弱化", "冰心");
            }
            actions.add(attackEvent);
        }
        if (attacked) {
            context.events.add(new Event(this, "回血", 5000));
        }
    }

    public void onEvent(Event event) {
        CLog log = new CLog(name, event.action, null, context.time);
        switch (event.action) {
            case "回血":
                int damaged = attr_mhp - hp;
                if (damaged > 0) {
                    log.regen = Math.min(attr_regen, damaged);
                    hp += log.regen;
                    event.time += 5000;
                    context.logs.add(log);
                }
                break;
            case "失效":
                context.events.remove(event);
                print(event.action, event.target);
                switch (event.target) {
                    case "暴风":
                        in_storm = false;
                        attr_attack_speed -= 50;
                        break;
                }
                break;
            case "冷却":
                context.events.remove(event);
                switch (event.target) {
                    case "咒刃":
                        in_cd_enchant = false;
                        break;
                    case "电弧":
                        in_cd_lighting = false;
                        break;
                }
                break;
        }
    }

    public void onAI(Event event) {
        switch (event.action) {
            case "攻击":
                if (event.time > context.time) {
                    context.time = event.time;
                }
                doAttack();
                break;
            case "cast1":
                if (event.time > context.time) {
                    context.time = event.time;
                }
                doCast(0);
                break;
            case "cast2":
                if (event.time > context.time) {
                    context.time = event.time;
                }
                doCast(1);
                break;
            case "cast3":
                if (event.time > context.time) {
                    context.time = event.time;
                }
                doCast(2);
                break;
        }
    }

    protected void doSkip() {
        int timeDelayed = actions.get(1).time + 1;
        actions.get(0).time = timeDelayed;
        for (int i = 2, n = actions.size(); i < n; ++i) {
            Event event = actions.get(i);
            if (event.time < timeDelayed) {
                event.time = timeDelayed;
            }
        }
    }

    protected void doAttack() {
        CLog log = new CLog(name, "攻击", target.name, context.time);
        int cd = (int) (attr_attack_cd * 100 / (100 + Math.min(200, attr_attack_speed)));
        onHit(log, true);   // actually hit later, call hit() immediately for simplicity
        attackEvent.time = context.time + cd;
        delayActions(100);
    }

    void print(String action, String target) {
        context.logs.add(new CLog(name, action, target, context.time));
    }

    void updateAttackCanCritical() {
        double criticalFactor = Math.min(attackFactor, 1);
        attackCanCritical = (int) (attr_attack * criticalFactor);
        attackCannotCritical = (int) (attr_attack * (attackFactor - criticalFactor)) + attackBonus;
    }

    protected void onHit(CLog log, boolean canCritical) {
        updateAttackCanCritical();
        double damage = attackCanCritical;
        if (canCritical && checkCritical()) {
            damage *= attr_critical_damage;
            log.critical = true;
        }
        log.damage = (int) ((damage + attackCannotCritical) * getDamageRate(log) * tgNormalFactor);
        target.hp -= log.damage;

        if (target.hp > 0) {
            damage = 0;
            if (has_corrupt) {
                damage = target.hp * 0.08;
            }
            if (has_accurate) {
                damage += 60;
            }
            if (damage > 0) {
                log.extraDamage = (int) (damage * getDamageRate(log));
                target.hp -= log.extraDamage;
            }
        }
        onDamage(log);

        if (in_enchant && target.hp > 0) {
            in_enchant = false;
            CLog enchantLog = new CLog(name, "咒刃", target.name, context.time);
            switch (attr_enchants) {
                case Item.ENCHANT_TRINITY:
                    enchantLog.damage = (int) (attr_attack * getDamageRate(enchantLog));
                    target.hp -= enchantLog.damage;
                    break;
                case Item.ENCHANT_VOODOO:
                    enchantLog.magicDamage = (int) (((int) (attr_attack * 0.3) + (int) (attr_magic * 0.65)) * getMagicDamageRate(enchantLog));
                    target.hp -= enchantLog.magicDamage;
                    break;
                case Item.ENCHANT_ICE:
                    enchantLog.damage = (int) (430 * getDamageRate(enchantLog));
                    target.hp -= enchantLog.damage;
                    break;
            }
            onDamage(enchantLog);
        }

        if (has_lightning && target.hp > 0) {
            lightingCount--;
            if (!in_cd_lighting && lightingCount <= 0) {
                in_cd_lighting = true;
                lightingCount = 5;
                damage = 100;
                log = new CLog(name, "电弧", target.name, context.time);
                if (checkCritical()) {
                    damage *= attr_critical_damage;
                    log.critical = true;
                }
                log.magicDamage = (int) (damage * getMagicDamageRate(log));
                target.hp -= log.magicDamage;
                context.addEvent(this, "冷却", "电弧", 500);
                onDamage(log);
            }
        }
    }

    private boolean checkCritical() {
        hitCount++;
        if (attr_critical * hitCount >= criticalCount + 1) {
            criticalCount++;
            return true;
        }
        return false;
    }

    private void onDamage(CLog log) {
        context.logs.add(log);
        if (log.sum() > 0) {
            if (has_storm && log.critical) {
                context.updateBuff(this, "失效", "暴风", 2000);
                if (!in_storm) {
                    in_storm = true;
                    attr_attack_speed += 50;
                    print("强化", "暴风");
                }
            }

            if (target.has_cold_iron && !in_cold_iron) {
                in_cold_iron = true;
                attr_attack_speed -= 30;
                print("弱化", "寒铁");
            }

            if (log.cut) {
                target.print("弱化", "切割" + target.cutCount);
            }
        }
    }

    double getDamageRate(CLog log) {
        int defense = target.attr_defense;
        if (has_penetrate) {
            defense -= (int)(defense * 0.45);
        }
        defense -= attr_penetrate;
        double rate = 600.0 / (600 + defense);
        if (has_execute && target.hp * 2 < target.attr_mhp) {
            rate *= 1.3;
        }

        if (has_cut && target.cutCount < 5) {
            target.cutCount++;
            target.attr_defense -= 40;
            log.cut = true;
        }
        return rate;
    }

    double getMagicDamageRate(CLog log) {
        int defense = target.attr_magic_defense;
        if (has_magic_penetrate_rate) {
            defense -= (int)(defense * 0.45);
        }
        if (has_magic_penetrate_boots) {
            defense -= 75;
        }
        if (has_magic_penetrate_mask) {
            defense -= 75;
        }
        defense -= attr_magic_penetrate;
        if (defense < 0) {
            defense = 0;
        }
        double rate = 600.0 / (600 + defense);
        if (has_execute && target.hp * 2 < target.attr_mhp) {
            rate *= 1.3;
        }

        if (has_cut && target.cutCount < 5) {
            target.cutCount++;
            target.attr_defense -= 40;
            log.cut = true;
        }
        return rate;
    }

    protected void doCast(int index) {
        if (attr_enchants != 0 && !in_cd_enchant) {
            in_cd_enchant = true;
            in_enchant = true;
            context.addEvent(this, "冷却", "咒刃", Item.ENCHANT_ICE == attr_enchants ? 3000 : 2000);
        }
        if (skills != null) {
            Skill skill = skills[index];
            CLog log = new CLog(name, skill.name, null, context.time);
            if (skill.damageType != Skill.TYPE_NONE) {
                log.target = target.name;
                int damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? attr_attack : attr_magic) * skill.damageFactor) + skill.damageBonus;
                switch (skill.damageType) {
                    case Skill.TYPE_PHYSICAL:
                        log.damage = (int) (damage * getDamageRate(log));
                        target.hp -= log.damage;
                        break;
                    case Skill.TYPE_MAGIC:
                        log.magicDamage = (int) (damage * getMagicDamageRate(log));
                        target.hp -= log.magicDamage;
                        break;
                    case Skill.TYPE_REAL:
                        log.realDamage = damage;
                        target.hp -= log.realDamage;
                        break;
                }
            }
            onDamage(log);
            castEvents[index].time = context.time + skill.cd;
            delayActions(skill.swing);
        }
    }

    protected void delayActions(int swing) {
        swing += context.time;
        for (int i = 0, n = actions.size(); i < n; ++i) {
            Event action = actions.get(i);
            if (swing > action.time) {
                action.time = swing;
            }
        }
    }
}
