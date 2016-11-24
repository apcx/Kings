package apc.kings.data.combat;

import android.annotation.SuppressLint;

// Combat Log
public class CLog {

    public String primaryHero;
    public String secondaryHero;
    public String name;
    public double time;
    public double damage;
    public double extraDamage;
    public double magicDamage;
    public double realDamage;
    public double enchantDamage;
    public double totalDamage;

    public CLog(String primaryHero, String secondaryHero, String name, double time) {
        this.primaryHero = primaryHero;
        this.secondaryHero = secondaryHero;
        this.name = name;
        this.time = time;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return totalDamage > 0 ?
                String.format("%.3f %s %s %s: %.0f %.0f %.0f %.0f %.0f", time, primaryHero, name, secondaryHero, damage, extraDamage, magicDamage, realDamage, enchantDamage)
                : String.format("%.3f %s %s", time, primaryHero, name);
    }

    public CLog sum() {
        totalDamage = damage + extraDamage + magicDamage + realDamage + enchantDamage;
        return this;
    }
}
