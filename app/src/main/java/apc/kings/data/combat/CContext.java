package apc.kings.data.combat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apc.kings.data.HeroType;
import apc.kings.data.Item;
import apc.kings.data.hero.Hero;

// Combat context
public class CContext {

    public Hero attacker;
    public Hero defender;
    public int beginTime;
    public int time;
    public boolean defensive;
    public boolean specific;
    public List<Event> events = new ArrayList<>();
    public List<CLog> logs = new ArrayList<>();

    // temp
    public int damage;
    public double totalTime;
    public double dps;
    public double costRatio;

    public CContext(@NonNull HeroType attackerType, @NonNull HeroType defenderType, boolean defensive, boolean specific) {
        attacker = Hero.create(this, attackerType);
        defender = Hero.create(this, defenderType);
        this.defensive = defensive;
        this.specific = specific;

        if (!defensive) {
            beginTime = attacker.getBeginTime(specific);
        }
        events.add(new Event(this, defender,"回血", 5000));
        attacker.events.add(new Event(this, attacker,"攻击", beginTime));
        attacker.target = defender;
        attacker.normalFactor = (defender.flags & Item.FLAG_BOOTS_DEFENSE) == 0 ? 1 : 0.85;
        runAttack();

        // temp
        for (CLog log : logs) {
            log.sum();
            Log.d("CombatLog", log.toString());
        }
        damage = defender.mhp;
        totalTime = (logs.get(logs.size() - 1).time - beginTime) / 1000.0;
        dps = damage / totalTime;
        costRatio = 100 * dps / (1 + attacker.price);
    }

    public void addBuff(@NonNull Hero hero, @NonNull String action, @NonNull String buff, int duration) {
        int eventTime = time + duration;
        for (int i = 0, n = events.size(); i < n; ++i) {
            Event event = events.get(i);
            if (hero == event.hero && action.equals(event.action) && buff.equals(event.buff)) {
                event.time = eventTime;
                return;
            }
        }
        Event event = new Event(this, hero, action, eventTime);
        event.buff = buff;
        events.add(event);
    }

    private void runAttack() {
        do {
            Collections.sort(events);
            Collections.sort(attacker.events);
            Event combatEvent = events.get(0);
            Event attackerEvent = attacker.events.get(0);
            if (combatEvent.time <= attackerEvent.time) {
                combatEvent.hero.onEvent(combatEvent);
            } else {
                attackerEvent.hero.onEvent(attackerEvent);
            }
        } while (defender.hp > 0);
    }
}
