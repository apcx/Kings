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
    public int atAttack;
    public int atMagic;
    public int atDefense;
    public int atMagicDefense;
    public int atPenetrate;
    public int atMagicPenetrate;
    public int atRegen;
    public double atAttackSpeed;
    public double atCritical;
    public double atCriticalDamage = 2;
    public double cdFactor;
    public int flags;
    public Skill[] skills;

    public boolean hasCut;
    public boolean hasPenetrate;
    public boolean hasMagicPenetrateRate;
    public boolean hasMagicPenetrateBoots;
    public boolean hasMagicPenetrateMask;
    public boolean hasCorrupt;
    public boolean hasAccurate;
    public boolean hasLightning;
    public boolean hasEnchant;
    public boolean hasExecute;
    public boolean hasStorm;

    public Hero target;
    double time;
    public int attackCd = 1000;
    double attackFactor = 1;
    public double normalFactor;
    int attackBonus;
    int attackCanCritical;
    int attackCannotCritical;
    boolean enchanted;

    int cutCount = 5;
    int hitCount;
    int criticalCount;
    int lightingCount = 5;

    private boolean inStorm;
    private boolean inLighting;

    protected Hero(CContext context, HeroType heroType) {
        this.context = context;
        this.heroType = heroType;
        mhp = heroType.hp;
        atAttack = heroType.attack;
        atDefense = heroType.defense;
        atMagicDefense = 169;
        atRegen = heroType.regen;
        atAttackSpeed = heroType.attackSpeedPerLevel * 14;

        if (heroType.items != null) {
            for (Item item : heroType.items) {
                if (item != null) {
                    price += item.price;
                    mhp += item.hp;
                    atAttack += item.attack;
                    atMagic += item.magic;
                    atDefense += item.defense;
                    atMagicDefense += item.magicDefense;
                    atRegen += item.regen;
                    atAttackSpeed += item.attackSpeed;
                    atCritical += item.critical;
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

                    atAttackSpeed += rune.attackSpeed * n;
                    mhp += rune.hp * n;
                    attack += rune.attack * n;
                    magic += rune.magic * n;
                    defense += rune.defense * n;
                    magicDefense += rune.magicDefense * n;
                    penetrate += rune.penetrate * n;
                    magicPenetrate += rune.magicPenetrate * n;
                    atRegen += rune.regen * n;
                    atCritical += rune.critical * n / 100;
                    atCriticalDamage += rune.criticalDamage * n / 100;
                    cdFactor += rune.cdReduction * n;
                }
                this.atAttack += attack;
                this.atMagic += magic;
                this.atDefense += defense;
                this.atMagicDefense += magicDefense;
                this.atPenetrate = (int) penetrate;
                this.atMagicPenetrate = (int) magicPenetrate;
            }

            if ((flags & Item.FLAG_CRITICAL) != 0) {
                atCriticalDamage += 0.5;
            }
            cdFactor = 1 - cdFactor;
            hasCut = (flags & Item.FLAG_CUT) != 0;
            hasPenetrate = (flags & Item.FLAG_PENETRATE) != 0;
            hasMagicPenetrateRate = (flags & Item.FLAG_MAGIC_PENETRATE_RATE) != 0;
            hasMagicPenetrateBoots = (flags & Item.FLAG_MAGIC_PENETRATE_BOOTS) != 0;
            hasMagicPenetrateMask = (flags & Item.FLAG_MAGIC_PENETRATE_MASK) != 0;
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
                    log.regen = Math.min(atRegen, damaged);
                    hp += log.regen;
                    event.time += 5000;
                    context.logs.add(log);
                }
                break;
            case "失效":
            case "冷却":
                context.events.remove(event);
                print(event.action, event.target);
                switch (event.target) {
                    case "暴风":
                        inStorm = false;
                        atAttackSpeed -= 50;
                        break;
                    case "电弧":
                        inLighting = false;
                        break;
                }
                break;
            case "攻击":
                log.target = target.heroType.name;
                event.time = context.time + Math.max(100, doAttack(log));
                break;
        }
    }

    protected int doAttack(CLog log) {
        int cd = (int) (attackCd * 100 / (100 + Math.min(200, atAttackSpeed))); // todo: check negative formula
        hit(log, true);   // actually hit later, call hit() immediately for simplicity
        return cd;
    }

    void print(String action, String target) {
        context.logs.add(new CLog(heroType.name, action, target, context.time));
    }

    void updateAttackCanCritical() {
        double criticalFactor = Math.min(attackFactor, 1);
        attackCanCritical = (int) (atAttack * criticalFactor);
        attackCannotCritical = (int) (atAttack * (attackFactor - criticalFactor)) + attackBonus;
    }

    void hit(CLog log, boolean canCritical) {
        updateAttackCanCritical();
        double damage = attackCanCritical;
        if (canCritical && checkCritical()) {
            damage *= atCriticalDamage;
            log.critical = true;
        }
        log.damage = (int) ((damage + attackCannotCritical) * getDamageRate() * normalFactor);
        target.hp -= log.damage;

        if (target.hp > 0) {
            damage = 0;
            if (hasCorrupt) {
                damage = target.hp * 0.08;
            }
            if (hasAccurate) {
                damage += 60;
            }
            if (damage > 0) {
                log.extraDamage = (int) (damage * getDamageRate());
                target.hp -= log.extraDamage;
            }

//            if (enchanted && target.hp > 0) {
//                enchanted = false;
//                log.enchantDamage = (int) (atAttack * getDamageRate());
//                target.hp -= log.enchantDamage;
//            }
        }
        context.logs.add(log);
        appendStormEvent(log);

        if (hasLightning && target.hp > 0) {
            lightingCount--;
            if (!inLighting && lightingCount <= 0) {
                inLighting = true;
                lightingCount = 5;
                damage = 100;
                log = new CLog(heroType.name, "电弧", target.heroType.name, context.time);
                if (checkCritical()) {
                    damage *= atCriticalDamage;
                    log.critical = true;
                }
                log.magicDamage = (int) (damage * getMagicDamageRate());
                target.hp -= log.magicDamage;
                context.logs.add(log);
                context.addEvent(this, "冷却", "电弧", 500);
                appendStormEvent(log);
            }
        }
    }

    private boolean checkCritical() {
        hitCount++;
        if (atCritical * hitCount >= criticalCount + 1) {
            criticalCount++;
            if (hasStorm) {
                if (!inStorm) {
                    inStorm = true;
                    atAttackSpeed += 50;
                }
            }
            return true;
        }
        return false;
    }

    private void appendStormEvent(CLog log) {
        if (hasStorm && log.critical) {
            context.updateBuff(this, "失效", "暴风", 2000);
            print("强化", "暴风");
        }
    }

    double getDamageRate() {
        int defense = target.atDefense;
        if (hasPenetrate) {
            // unknown: 是否需要取整
            defense -= (int)(defense * 0.45);
        }
        defense -= atPenetrate;
        double rate = 600.0 / (600 + defense);
        if (hasExecute && target.hp * 2 < target.mhp) {
            rate *= 1.3;
        }

        if (hasCut && target.cutCount > 0) {
            target.cutCount--;
            target.atDefense -= 40;
        }
        return rate;
    }

    double getMagicDamageRate() {
        int defense = target.atMagicDefense;
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
        defense -= atMagicPenetrate;
        if (defense < 0) {
            defense = 0;
        }
        double rate = 600.0 / (600 + defense);
        if (hasExecute && target.hp * 2 < target.mhp) {
            rate *= 1.3;
        }

        if (hasCut && target.cutCount > 0) {
            target.cutCount--;
            target.atDefense -= 40;
        }
        return rate;
    }

    void doCast1() {
        onCast(0);
    }

    void doCast2() {
        onCast(1);
    }

    void doCast3() {
        onCast(2);
    }

    void onCast(int index) {
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
                    double damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? atAttack : atMagic) * skill.damageFactor) + skill.damageBonus;
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
                            log.realDamage = (int) damage;
                            target.hp -= log.realDamage;
                            break;
                    }
                }
                skill.nextCastTime = time + skill.cd * cdFactor;
            }
        }
    }
}
