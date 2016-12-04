package apc.kings.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apc.kings.data.hero.Hero;

// Combat context
public class CContext {

    public Hero attacker;
    public Hero defender;
    public int beginTime;
    public int time;
    public boolean specific;
    public boolean far;
    public List<Event> events = new ArrayList<>();
    public List<CLog> logs = new ArrayList<>();

    // temp
    public int damage;
    public double totalTime;
    public double dps;
    public double costRatio;

    public CContext(HeroType attackerType, HeroType defenderType) {
        attacker = Hero.create(this, attackerType);
        defender = Hero.create(this, defenderType);
        if (attackerType == defenderType) {
            attacker.name += "A";
            defender.name += "B";
        }

        runAttack();

        // temp
        for (CLog log : logs) {
            log.sum();
            Log.d("CombatLog", log.toString());
        }
        damage = defender.attr_mhp;
        totalTime = (logs.get(logs.size() - 1).time - beginTime) / 1000.0;
        dps = damage / totalTime;
        costRatio = 100 * dps / (1 + attacker.attr_price);
    }

    public void addEvent(Hero hero, String action, int intervals, int duration) {
        Event event = new Event(hero, action, null, time + duration);
        event.intervals = intervals;
        events.add(event);
    }

    public void addEvent(Hero hero, String action, String target, int duration) {
        events.add(new Event(hero, action, target, time + duration));
    }

    public void updateBuff(Hero hero, String action, String buff, int duration) {
        int eventTime = time + duration;
        for (int i = 0, n = events.size(); i < n; ++i) {
            Event event = events.get(i);
            if (hero == event.hero && action.equals(event.action) && buff.equals(event.target)) {
                event.time = eventTime;
                return;
            }
        }
        events.add(new Event(hero, action, buff, eventTime));
    }

    private void runAttack() {
        attacker.initActionMode(defender, false, specific);
        defender.initActionMode(null, true, specific);
        Collections.sort(attacker.actions_active);
        beginTime = attacker.actions_active.get(0).time;
        do {
            Collections.sort(events);
            Collections.sort(attacker.actions_active);
            Event contextEvent = events.get(0);
            Event attackerEvent = attacker.actions_active.get(0);
            if (contextEvent.time <= attackerEvent.time) {
                if (time < contextEvent.time) {
                    time = contextEvent.time;
                }
                contextEvent.hero.onEvent(contextEvent);
            } else {
                attackerEvent.hero.onAction(attackerEvent);
            }
        } while (defender.hp > 0);
    }
}
