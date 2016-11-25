package apc.kings.data.combat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import apc.kings.data.HeroType;
import apc.kings.data.hero.Hero;

public class Combat {

    public Hero attacker;
    public Hero defender;
    public long beginTime;
    public long time;
    public boolean defensive;
    public boolean specific;
    public List<Event> events = new ArrayList<>();

    // temp
    public int damage;
    public double totalTime;
    public double dps;
    public double costRatio;

    public Combat(@NonNull HeroType attackerType, @NonNull HeroType defenderType, boolean defensive, boolean specific) {
        this.attacker = Hero.create(attackerType);
        this.defender = Hero.create(defenderType);
        this.defensive = defensive;
        this.specific = specific;

        if (!defensive) {
            beginTime = attacker.getBeginTime(specific);
        }
        events.add(new Event(defender, null, "regen", 5000));

        List<CLog> logs = attacker.fight(defender);
        for (CLog log : logs) {
            Log.d("CombatLog", log.toString());
        }

        // temp
        damage = defender.mhp;
        totalTime = logs.get(logs.size() - 1).time - beginTime;
        dps = damage / totalTime;
        costRatio = 100 * dps / (1 + attacker.price);
    }
}
