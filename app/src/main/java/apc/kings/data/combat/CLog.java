package apc.kings.data.combat;

import android.annotation.SuppressLint;

// Combat Log
public class CLog {

    public String primaryHero;
    public String secondaryHero;
    public String name;
    public int time;
    public double damage;
    public double extraDamage;
    public double magicDamage;
    public double realDamage;
    public double enchantDamage;
    public double totalDamage;

    public CLog(String primaryHero, String secondaryHero, String name, int time) {
        this.primaryHero = primaryHero;
        this.secondaryHero = secondaryHero;
        this.name = name;
        this.time = time;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return totalDamage > 0 ?
                String.format("%d %s %s %s: %.0f %.0f %.0f %.0f %.0f", time, primaryHero, name, secondaryHero, damage, extraDamage, magicDamage, realDamage, enchantDamage)
                : String.format("%d %s %s", time, primaryHero, name);
    }

    public void sum() {
        totalDamage = damage + extraDamage + magicDamage + realDamage + enchantDamage;
    }
}
