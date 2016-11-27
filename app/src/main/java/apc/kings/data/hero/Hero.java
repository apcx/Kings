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

    public List<Event> events = new ArrayList<>();

    public int price;
    public int mhp;
    public int hp;
    public int attack;
    public int magic;
    public int defense;
    public int magicDefense;
    public int penetrate;
    public int magicPenetrate;
    public int regen;
    public double attackSpeed;
    public double critical;
    public double criticalDamage;
    public double cdFactor;
    public int flags;
    public Skill[] skills;

    public boolean hasCut;
    public boolean hasPenetrate;
    public boolean hasMagicPenetrateRate;
    public boolean hasMagicPenetrateBoots;
    public boolean hasMagicPenetrateMask;
    public boolean hasCritical;
    public boolean hasCorrupt;
    public boolean hasAccurate;
    public boolean hasLightning;
    public boolean hasEnchant;
    public boolean hasExecute;
    public boolean hasStorm;

    public Hero target;
    public List<CLog> logs = new ArrayList<>();
    double beginTime;
    double time;
    double nextAttackTime;
    double stormEndTime;
    public int attackCd = 1000;
    double attackFactor = 1;
    public double normalFactor;
    int attackBonus;
    int attackCanCritical;
    int attackCannotCritical;
    boolean enchanted;

    int cutCount;
    int hitCount;
    int criticalCount;

    private boolean inStorm;

    Hero(CContext context, HeroType heroType) {
        this.context = context;
        this.heroType = heroType;
        mhp = heroType.hp;
        attack = heroType.attack;
        defense = heroType.defense;
        magicDefense = 169;
        regen = heroType.regen;
        attackSpeed = heroType.attackSpeed;

        if (heroType.items != null) {
            for (Item item : heroType.items) {
                if (item != null) {
                    price += item.price;
                    mhp += item.hp;
                    attack += item.attack;
                    magic += item.magic;
                    defense += item.defense;
                    magicDefense += item.magicDefense;
                    regen += item.regen;
                    attackSpeed += item.attackSpeed;
                    critical += item.critical;
                    cdFactor += item.cdReduction;
                    flags |= item.flags;
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

                    mhp += rune.hp * n;
                    attack += rune.attack * n;
                    magic += rune.magic * n;
                    defense += rune.defense * n;
                    magicDefense += rune.magicDefense * n;
                    penetrate += rune.penetrate * n;
                    magicPenetrate += rune.magicPenetrate * n;
                    regen += rune.regen * n;
                    attackSpeed += rune.attackSpeed * n / 100;
                    critical += rune.critical * n / 100;
                    criticalDamage += rune.criticalDamage * n / 100;
                    cdFactor += rune.cdReduction * n;
                }
                this.attack += attack;
                this.magic += magic;
                this.defense += defense;
                this.magicDefense += magicDefense;
                this.penetrate = (int) penetrate;
                this.magicPenetrate = (int) magicPenetrate;
            }

            cdFactor = 1 - cdFactor;
            hasCut = (flags & Item.FLAG_CUT) != 0;
            hasPenetrate = (flags & Item.FLAG_PENETRATE) != 0;
            hasMagicPenetrateRate = (flags & Item.FLAG_MAGIC_PENETRATE_RATE) != 0;
            hasMagicPenetrateBoots = (flags & Item.FLAG_MAGIC_PENETRATE_BOOTS) != 0;
            hasMagicPenetrateMask = (flags & Item.FLAG_MAGIC_PENETRATE_MASK) != 0;
            hasCritical = (flags & Item.FLAG_CRITICAL) != 0;
            hasCorrupt = (flags & Item.FLAG_CORRUPT) != 0;
            hasAccurate = (flags & Item.FLAG_ACCURATE) != 0;
            hasLightning = (flags & Item.FLAG_LIGHTNING) != 0;
            hasEnchant = (flags & Item.FLAG_ENCHANT) != 0;
            hasExecute = (flags & Item.FLAG_EXECUTE) != 0;
            hasStorm = (flags & Item.FLAG_STORM) != 0;
        }
        hp = mhp;
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

    public int getBeginTime(boolean specific) {
        return 0;
    }

    public void autoChooseAction() {

    }

    public void onEvent(Event event) {
        if (event.time > context.time) {
            context.time = event.time;
        }
        CLog log = new CLog(heroType.name, event.action, null, context.time);
        switch (event.action) {
            case "回血":
                int damaged = mhp - hp;
                if (damaged > 0) {
                    log.damage = Math.min(regen, damaged);
                    hp += log.damage;
                    event.time += 5000;
                    context.logs.add(log);
                }
                break;
            case "失效":
                inStorm = false;
                context.events.remove(event);
                print(event.action, event.buff);
                break;
            case "攻击":
                log.target = target.heroType.name;
                event.time = context.time + Math.max(100, doAttack(log));
                break;
        }
    }

    int doAttack(CLog log) {
        double speed = attackSpeed;
        if (inStorm) {
            speed += 0.5;
        }
        int cd = (int) (attackCd / (1 + Math.min(2, speed)));
        hit(log);
        return cd;
    }

    void print(String action, String target) {
        context.logs.add(new CLog(heroType.name, action, target, context.time));
    }

    private void hit(CLog log) {
        hitCount++;
        updateAttackCanCritical();
        double damage = attackCanCritical;
        double criticalDamageRate = 1;
        if (critical * hitCount >= criticalCount + 1) {
            criticalCount++;
            criticalDamageRate = (hasCritical ? 2.5 : 2) + criticalDamage;
            damage *= criticalDamageRate;
        }

        log.damage = (damage + attackCannotCritical) * getDamageRate() * normalFactor;
        target.hp -= log.damage;

        damage = 0;
        if (target.hp > 0) {
            if (hasCorrupt) {
                damage = target.hp * 0.08;
            }
            if (hasAccurate) {
                damage += 60;
            }
            log.extraDamage = damage * getDamageRate();
            target.hp -= log.extraDamage;

            if (enchanted && target.hp > 0) {
                enchanted = false;
                log.enchantDamage = attack * getDamageRate();
                target.hp -= log.enchantDamage;
            }

            if (hasLightning && target.hp > 0 && hitCount % 5 == 0) {
                log.magicDamage = 100 * criticalDamageRate * getMagicDamageRate();
                target.hp -= log.magicDamage;
            }
        }

        context.logs.add(log);
        if (hasStorm && criticalDamageRate > 1) {
//            stormEndTime = time + 2;
            if (!inStorm) {
                print("强化", "暴风");
            }
            inStorm = true;
            context.addBuff(this, "失效", "暴风", 2000);
        }
    }

    void updateAttackCanCritical() {
        double criticalFactor = Math.max(1, attackFactor);
        attackCanCritical = (int) (attack * criticalFactor);
        attackCannotCritical = (int) (attack * (attackFactor - criticalFactor)) + attackBonus;
    }

    double getDamageRate() {
        int defense = target.defense;
        if (hasCut) {
            defense -= 40 * cutCount;
            if (cutCount < 5) {
                cutCount++;
            }
        }
        if (hasPenetrate) {
            // unknown: 是否需要取整
            defense -= (int)(defense * 0.45);
        }
        defense -= penetrate;
        double rate = 600.0 / (600 + defense);
        if (hasExecute && target.hp * 2 < target.mhp) {
            rate *= 1.3;
        }
        return rate;
    }

    double getMagicDamageRate() {
        int defense = target.magicDefense;
        if (hasMagicPenetrateRate) {
            // unknown: 是否需要取整
            defense -= (int)(defense * 0.45);
        }
        if (hasMagicPenetrateBoots) {
            defense -= 75;
        }
        if (hasMagicPenetrateMask) {
            defense -= 75;
        }
        defense -= magicPenetrate;
        if (defense < 0) {
            defense = 0;
        }
        double rate = 600.0 / (600 + defense);
        if (hasExecute && target.hp * 2 < target.mhp) {
            rate *= 1.3;
        }
        return rate;
    }

    void cast1() {
        cast(0);
    }

    void cast2() {
        cast(1);
    }

    void cast3() {
        cast(2);
    }

    void cast(int index) {
        if (skills != null && index < skills.length) {
            Skill skill = skills[index];
            if (skill != null) {
                enchanted = hasEnchant;
                if (skill.nextCastTime > time) {
                    time = skill.nextCastTime;
                }
                time += skill.swing;
                CLog log = new CLog(heroType.name, skill.name, target.heroType.name, (int) time);
                if (skill.damageType != Skill.TYPE_NONE) {
                    double damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? attack : magic) * skill.damageFactor) + skill.damageBonus;
                    switch (skill.damageType) {
                        case Skill.TYPE_PHYSICAL:
                            log.damage = damage * getDamageRate();
                            target.hp -= log.damage;
                            break;
                        case Skill.TYPE_MAGIC:
                            log.magicDamage = damage * getMagicDamageRate();
                            target.hp -= log.magicDamage;
                            break;
                        case Skill.TYPE_REAL:
                            log.realDamage = damage;
                            target.hp -= log.realDamage;
                            break;
                    }
                }
                skill.nextCastTime = time + skill.cd * cdFactor;
            }
        }
    }
}
