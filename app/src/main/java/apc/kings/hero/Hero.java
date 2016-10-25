package apc.kings.hero;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Item;
import apc.kings.data.Rune;
import apc.kings.data.Skill;

public class Hero {

    static final String TAG = "HeroFight";

    public HeroType heroType;

    public int price;
    public int mhp;
    public double hp;
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

    Hero target;
    List<Event> events = new ArrayList<>();
    double time;
    double nextAttackTime;
    double stormEndTime;
    double attackCd = 1;
    double attackFactor = 1;
    double normalFactor;
    int attackBonus;
    int attackCanCritical;
    int attackCannotCritical;
    boolean enchanted;

    int cutCount;
    int hitCount;
    int criticalCount;

    Hero(HeroType heroType) {
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
    public static Hero create(@NonNull HeroType heroType) {
        try {
            Class clazz = Class.forName(Hero.class.getPackage().getName() + "." + (char) (heroType.resName.charAt(0) - 32) + heroType.resName.substring(1) + "Hero");
            //noinspection unchecked
            return (Hero) clazz.getDeclaredConstructor(HeroType.class).newInstance(heroType);
        } catch (Exception e) {
            return new Hero(heroType);
        }
    }

    public List<Event> fight(@NonNull Hero target) {
        this.target = target;
        normalFactor = (target.flags & Item.FLAG_BOOTS_DEFENSE) == 0 ? 1 : 0.85;
        events.clear();
        do {
            autoChooseAction();
        } while (target.hp > 0);

        for (Event event : events) {
            Log.d(TAG, event.toString());
        }
        return events;
    }

    void autoChooseAction() {
        attack();
    }

    Event attack() {
        if (nextAttackTime > time) {
            time = nextAttackTime;
        }
        Event event = hit();

        double speed = attackSpeed;
        if (time < stormEndTime) {
            speed += 0.5;
        }
        nextAttackTime = time + attackCd / (1 + Math.min(2, speed));
        return event;
    }

    Event hit() {
        hitCount++;
        updateAttackCanCritical();
        double damage = attackCanCritical;
        double criticalDamageRate = 1;
        if (critical * hitCount >= criticalCount + 1) {
            criticalCount++;
            criticalDamageRate = (hasCritical ? 2.5 : 2) + criticalDamage;
            damage *= criticalDamageRate;
        }
        Event event = new Event(heroType.name, target.heroType.name, "攻击", time);
        event.damage = (damage + attackCannotCritical) * getDamageRate() * normalFactor;
        target.hp -= event.damage;

        damage = 0;
        if (target.hp > 0) {
            if (hasCorrupt) {
                damage = target.hp * 0.08;
            }
            if (hasAccurate) {
                damage += 60;
            }
            event.extraDamage = damage * getDamageRate();
            target.hp -= event.extraDamage;

            if (enchanted && target.hp > 0) {
                enchanted = false;
                event.enchantDamage = attack * getDamageRate();
                target.hp -= event.enchantDamage;
            }

            if (hasLightning && target.hp > 0 && hitCount % 5 == 0) {
                event.magicDamage = 100 * criticalDamageRate * getMagicDamageRate();
                target.hp -= event.magicDamage;
            }
        }
        events.add(event.sum());
        if (hasStorm && criticalDamageRate > 1) {
            stormEndTime = time + 2;
        }
        return event;
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
                Event event = new Event(heroType.name, target.heroType.name, skill.name, time);
                if (skill.damageType != Skill.TYPE_NONE) {
                    double damage = (int)((Skill.TYPE_PHYSICAL == skill.factorType ? attack : magic) * skill.damageFactor) + skill.damageBonus;
                    switch (skill.damageType) {
                        case Skill.TYPE_PHYSICAL:
                            event.damage = damage * getDamageRate();
                            target.hp -= event.damage;
                            break;
                        case Skill.TYPE_MAGIC:
                            event.magicDamage = damage * getMagicDamageRate();
                            target.hp -= event.magicDamage;
                            break;
                        case Skill.TYPE_REAL:
                            event.realDamage = damage;
                            target.hp -= event.realDamage;
                            break;
                    }
                }
                events.add(event.sum());
                skill.nextCastTime = time + skill.cd * cdFactor;
            }
        }
    }
}
