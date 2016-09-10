package apc.kings.data;

import android.annotation.SuppressLint;

public class Event {

    public String primaryHero;
    public String secondaryHero;
    public String action;
    public double time;
    public double damage;
    public double extraDamage;
    public double magicDamage;
    public double realDamage;
    public double enchantDamage;
    public double totalDamage;

    public Event(String primaryHero, String secondaryHero, String action, double time) {
        this.primaryHero = primaryHero;
        this.secondaryHero = secondaryHero;
        this.action = action;
        this.time = time;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return totalDamage > 0 ?
                String.format("%.3f %s %s %s: %.0f %.0f %.0f %.0f %.0f", time, primaryHero, action, secondaryHero, damage, extraDamage, magicDamage, realDamage, enchantDamage)
                : String.format("%.3f %s %s", time, primaryHero, action);
    }

    public Event sum() {
        totalDamage = damage + extraDamage + magicDamage + realDamage + enchantDamage;
        return this;
    }
}
