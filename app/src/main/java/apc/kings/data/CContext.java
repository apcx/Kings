package apc.kings.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apc.kings.common.App;
import apc.kings.data.hero.Hero;

// Combat context
public class CContext {

    public List<Event> events = new ArrayList<>();
    public List<CLog> logs = new ArrayList<>();
    public boolean option_frenzy;
    public boolean far;
    public int time;

    public int summary_damage;
    public int summary_time;
    public double summary_dps;
    public double summary_cost_ratio;

    private Hero attacker;
    private Hero defender;
    private boolean option_specific;
    private boolean exit;

    public CContext(HeroType attackerType, HeroType defenderType) {
        attacker = Hero.create(this, attackerType);
        defender = Hero.create(this, defenderType);
        option_specific = App.preferences().getBoolean("combat_specific", false);
        option_frenzy = App.preferences().getBoolean("combat_frenzy", false);
        if (attackerType == defenderType) {
            attacker.name += "A";
            defender.name += "B";
        }

        runAttack();

        for (CLog log : logs) {
            int damage = log.sum();

            // Some kinds of damages can have multiple targets.
            switch (log.action) {
                case "狂热弹幕":
                    damage *= 1.5;
                    break;
                case "电弧":
                    damage *= 1.3;
                    break;
                case "扫射":
                case "引爆":
                    damage *= 1.2;
                    break;
                case "强射":
                    damage *= 1.1;
                    break;
            }
            summary_damage += damage;
        }
        summary_time = logs.get(logs.size() - 1).time;
        summary_dps = summary_damage * 1000.0 / summary_time;
        summary_cost_ratio = summary_dps * 10 / (1 + attacker.attr_price);
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

    public void checkExit() {
        if (option_specific) {
            exit = true;
        }
    }

    private void runAttack() {
        attacker.initActionMode(defender, false, option_specific);
        defender.initActionMode(null, true, option_specific);
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
        } while (defender.hp > 0 && time <= 99000 && !exit);

        if (defender.hp <= 0) {
            logs.add(new CLog(attacker.name, "击败", defender.name, time));
        }
    }

    private void updateTime(Event event) {
        if (time < event.time) {
            time = event.time;
        }
    }
}
