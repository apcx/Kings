package apc.kings.data.combat;

import android.annotation.SuppressLint;

// Combat Log
public class CLog {

    public String hero;
    public String target;
    public String action;
    public int time;
    public double damage;
    public double extraDamage;
    public double magicDamage;
    public double realDamage;
    public double enchantDamage;
    public double totalDamage;

    public CLog(String hero, String action, String target, int time) {
        this.hero = hero;
        this.action = action;
        this.target = target;
        this.time = time;
    }

    public CLog(Event event) {
        hero = event.hero.heroType.name;
        action = event.action;
        target = event.buff;
        time = event.context.time;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return totalDamage > 0 ?
                String.format("%d %s %s %s: %.0f %.0f %.0f %.0f %.0f", time, hero, action, target, damage, extraDamage, magicDamage, realDamage, enchantDamage)
                : String.format("%d %s %s %s", time, hero, action, target);
    }

    public void sum() {
        totalDamage = damage + extraDamage + magicDamage + realDamage + enchantDamage;
    }
}
