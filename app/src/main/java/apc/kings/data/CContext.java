package apc.kings.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apc.kings.data.hero.Hero;

// Combat context
public class CContext {

    public Hero attacker;
    public Hero defender;
    public List<Event> events = new ArrayList<>();
    public List<CLog> logs = new ArrayList<>();
    public boolean specific;
    public boolean far;
    public int time;
    public int time_start;

    public int summary_damage;
    public int summary_time;
    public double summary_dps;
    public double summary_cost_ratio;

    public CContext(HeroType attackerType, HeroType defenderType) {
        attacker = Hero.create(this, attackerType);
        defender = Hero.create(this, defenderType);
        if (attackerType == defenderType) {
            attacker.name += "A";
            defender.name += "B";
        }

        runAttack();

        for (CLog log : logs) {
            summary_damage += log.sum();
        }
        summary_time = logs.get(logs.size() - 1).time - time_start;
        summary_dps = summary_damage * 1000.0 / summary_time;
        summary_cost_ratio = 1000000000.0 / summary_time / (1 + attacker.attr_price);
    }

    public void addEvent(Hero hero, String action, int intervals, int period) {
        Event event = new Event(hero, action, null, period, time + period);
        event.intervals = intervals;
        events.add(event);
    }

    public Event addEvent(Hero hero, String action, String target, int duration) {
        Event event = new Event(hero, action, target, duration, time + duration);
        events.add(event);
        return event;
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
        events.add(new Event(hero, action, buff, 0, eventTime));
    }

    private void runAttack() {
        attacker.initActionMode(defender, false, specific);
        Collections.sort(attacker.actions_active);
        time_start = attacker.actions_active.get(0).time;
        defender.initActionMode(null, true, specific);
        do {
            Collections.sort(events);
            Collections.sort(attacker.actions_active);
            Event event = events.get(0);
            Event action = attacker.actions_active.get(0);
            if (!defender.actions_active.isEmpty()) {
                Event defenderAction = defender.actions_active.get(0);
                if (defenderAction.time <= action.time) {
                    action = defenderAction;
                }
            }
            if (event.time <= action.time) {
                updateTime(event);
                event.hero.onEvent(event);
            } else {
                updateTime(action);
                action.hero.onAction(action);
            }
        } while (defender.hp > 0 && time < 99000);
    }

    private void updateTime(Event event) {
        if (time < event.time) {
            time = event.time;
        }
    }
}
