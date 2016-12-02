package apc.kings.data.combat;

import android.annotation.SuppressLint;

// Combat Log
public class CLog implements Cloneable {

    public String hero;
    public String target;
    public String action;
    public int time;
    public int regen;
    public int damage;
    public int extraDamage;
    public int magicDamage;
    public int realDamage;
    public int enchantDamage;
    public int totalDamage;
    public boolean critical;
    public boolean cut;

    public CLog(String hero, String action, String target, int time) {
        this.hero = hero;
        this.action = action;
        this.target = target;
        this.time = time;
    }

    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    public CLog clone() {
        CLog log = null;
        try {
            log = (CLog) super.clone();
        } catch (CloneNotSupportedException e) {
            // ignore
        }
        return log;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        double time = this.time / 1000.0;
        if (regen > 0) {
            return String.format("%.3f %s %s %d", time, hero, action, regen);
        } else if (totalDamage > 0) {
            return String.format("%.3f %s %s %s: %d %d %d %d %d", time, hero, action, target, damage, extraDamage, magicDamage, realDamage, enchantDamage);
        } else {
            return String.format("%.3f %s %s %s", time, hero, action, target);
        }
    }

    public int sum() {
        totalDamage = damage + extraDamage + magicDamage + realDamage + enchantDamage;
        return totalDamage;
    }
}
