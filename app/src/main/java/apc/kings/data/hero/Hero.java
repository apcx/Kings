package apc.kings.data.hero;

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

    public List<Event> actions_active = new ArrayList<>();
    public Event[] actions_cast = new Event[3];
    public Event action_attack = new Event(this, "攻击", 0);
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

    int cnt_cut;
    int cnt_hit;
    int cnt_critical;
    int cnt_lightning = 5;

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

        actions_cast[0] = new Event(this, "cast1", 0);
        actions_cast[1] = new Event(this, "cast2", 0);
        actions_cast[2] = new Event(this, "cast3", 0);
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
            tgNormalFactor = (target.attr_flags & Item.FLAG_BOOTS_DEFENSE) == 0 ? 1 : 0.85;
            if ((target.attr_flags & Item.FLAG_FROZEN_HEART) != 0) {
                attr_attack_speed -= 30;
                print("弱化", "冰心");
            }
            actions_active.add(action_attack);
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
        if (context.time < action_attack.time) {
            context.time = action_attack.time;
        }
        action_attack.time = context.time + (int) (attr_attack_cd * 100 / (100 + Math.min(200, attr_attack_speed)));
        onAttack(new CLog(name, "攻击", target.name, context.time));
        delayActions(100);
    }

    protected void doSmartCast(int index) {
        Event action = actions_cast[index];
        int castEndTime = action.time + skills[index].swing;
        int attackEndTime = action_attack.time + 100;
        if (castEndTime - action_attack.time > attackEndTime - action.time) {
            doSkip(index);
        } else {
            doCast(index);
        }
    }

    protected void doSkip(int index) {  // delay the cast after the next attack
        actions_cast[index].time = action_attack.time + 101;
    }

    protected void doCast(int index) {
        Event action = actions_cast[index];
        if (context.time < action.time) {
            context.time = action.time;
        }
        if (attr_enchants != 0 && !in_cd_enchant) {
            in_cd_enchant = true;
            in_enchant = true;
            context.addEvent(this, "冷却", "咒刃", Item.ENCHANT_ICE == attr_enchants ? 3000 : 2000);
        }

        action.time = context.time + skills[index].cd;
        onCast(index);
        delayActions(skills[index].swing);
    }

    protected void onAttack(CLog log) {
        onHit(log, true);   // It actually takes time for missiles to fly to the target. Call hit() immediately here, just for simplicity.
    }

    protected void onCast(int index) {

        Skill skill = skills[index];
        CLog log = new CLog(name, skill.name, null, context.time);
        if (skill.damageType != Skill.TYPE_NONE) {
            log.target = target.name;
            int damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? attr_attack : attr_magic) * skill.damageFactor) + skill.damageBonus;
            switch (skill.damageType) {
                case Skill.TYPE_PHYSICAL:
                    log.damage = (int) (damage * getDamageRate());
                    target.hp -= log.damage;
                    break;
                case Skill.TYPE_MAGIC:
                    log.magicDamage = (int) (damage * getMagicDamageRate());
                    target.hp -= log.magicDamage;
                    break;
                case Skill.TYPE_REAL:
                    log.realDamage = damage;
                    target.hp -= log.realDamage;
                    break;
            }
            context.logs.add(log);
            onHitDone();
        } else {
            context.logs.add(log);
        }

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
        log.damage = (int) ((damage + attackCannotCritical) * getDamageRate() * tgNormalFactor);
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
                log.extraDamage = (int) (damage * getDamageRate());
                target.hp -= log.extraDamage;
            }
        }
        checkStorm(log);

        if (in_enchant && target.hp > 0) {
            in_enchant = false;
            log = new CLog(name, "咒刃", target.name, context.time);
            switch (attr_enchants) {
                case Item.ENCHANT_TRINITY:
                    log.damage = (int) (attr_attack * getDamageRate());
                    target.hp -= log.damage;
                    break;
                case Item.ENCHANT_VOODOO:
                    log.magicDamage = (int) (((int) (attr_attack * 0.3) + (int) (attr_magic * 0.65)) * getMagicDamageRate());
                    target.hp -= log.magicDamage;
                    break;
                case Item.ENCHANT_ICE:
                    log.damage = (int) (430 * getDamageRate());
                    target.hp -= log.damage;
                    break;
            }
            context.logs.add(log);
        }

        if (has_lightning && target.hp > 0) {
            if (--cnt_lightning <= 0 && !in_cd_lighting) {
                in_cd_lighting = true;
                cnt_lightning = 5;
                damage = 100;
                log = new CLog(name, "电弧", target.name, context.time);
                if (checkCritical()) {
                    damage *= attr_critical_damage;
                    log.critical = true;
                }
                log.magicDamage = (int) (damage * getMagicDamageRate());
                target.hp -= log.magicDamage;
                context.addEvent(this, "冷却", "电弧", 500);
                checkStorm(log);
            }
        }

        onHitDone();
    }

    private boolean checkCritical() {
        if (attr_critical * ++cnt_hit >= cnt_critical + 1) {
            cnt_critical++;
            return true;
        }
        return false;
    }

    private void checkStorm(CLog log) {
        context.logs.add(log);
        if (has_storm && log.critical) {
            context.updateBuff(this, "失效", "暴风", 2000);
            if (!in_storm) {
                in_storm = true;
                attr_attack_speed += 50;
                print("强化", "暴风");
            }
        }
    }

    protected void onHitDone() {
        if (target.has_cold_iron && !in_cold_iron) {
            in_cold_iron = true;
            attr_attack_speed -= 30;
            print("弱化", "寒铁");
        }

        if (has_cut && target.cnt_cut < 5) {
            target.attr_defense -= 40;
            target.print("弱化", "切割" + ++target.cnt_cut);
        }
    }

    double getDamageRate() {
        int defense = target.attr_defense;
        if (has_penetrate) {
            defense -= (int)(defense * 0.45);
        }
        defense -= attr_penetrate;
        double rate = 600.0 / (600 + defense);
        if (has_execute && target.hp * 2 < target.attr_mhp) {
            rate *= 1.3;
        }
        return rate;
    }

    double getMagicDamageRate() {
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
        return rate;
    }

    protected void delayActions(int swing) {
        swing += context.time;
        for (int i = 0, n = actions_active.size(); i < n; ++i) {
            Event action = actions_active.get(i);
            if (action.time < swing) {
                action.time = swing;
            }
        }
    }
}
