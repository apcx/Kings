package apc.kings.data;

import android.annotation.SuppressLint;

// Combat Log
public class CLog implements Cloneable {

    public String hero;
    public String target;
    public String action;
    public int time;
    public int regen;
    public int damage;
    public int extra_damage;
    public int magic_damage;
    public int real_damage;
    public int total_damage;
    public boolean critical;

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
        } else if (total_damage > 0) {
            return String.format("%.3f %s %s %s : %d %d %d", time, hero, action, target, damage, extra_damage, magic_damage);
        } else {
            return String.format("%.3f %s %s %s", time, hero, action, null == target ? "" : target);
        }
    }

    public int sum() {
        total_damage = damage + extra_damage + magic_damage + real_damage;
        return total_damage;
    }
}
