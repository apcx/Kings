package apc.kings.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apc.kings.R;
import apc.kings.data.hero.Hero;

// Combat context
public class CContext {

    public List<Event> events = new ArrayList<>();
    public List<CLog> logs = new ArrayList<>();
    public boolean far;
    public int time;

    public int summary_damage;
    public int summary_time;
    public double summary_dps;
    public double[] summary_marks = new double[2];
    public double[] summary_cost_ratios = new double[2];

    private Hero attacker;
    private Hero defender;
    private boolean option_combo;
    private boolean option_hunt;
    private boolean option_siege;
    private boolean option_frenzy;
    private boolean exit;

    public CContext(Context context, HeroType attackerType, HeroType defenderType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        option_combo = preferences.getBoolean(context.getString(R.string.combat_combo), false);
        option_frenzy = preferences.getBoolean(context.getString(R.string.combat_frenzy), false);
        option_hunt = preferences.getBoolean(context.getString(R.string.combat_hunt), false);
        option_siege = preferences.getBoolean(context.getString(R.string.combat_siege), false);

        attacker = Hero.create(this, attackerType);
        defender = Hero.create(this, defenderType);
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
                case "炮击":
                case "警戒地雷":
                    damage *= 1.2;
                    break;
                case "强射":
                case "红莲爆弹":
                    damage *= 1.1;
                    break;
            }
            summary_damage += damage;
        }
        summary_time = logs.get(logs.size() - 1).time;
        summary_dps = summary_damage * 1000.0 / summary_time;

        int base_move = 400;
        double weight = 2;
        switch (attacker.name) {
            case "黄忠":
                base_move = 453;
                if (option_siege) {
                    weight = 1.5;
                }
                break;
        }
        summary_marks[0] = summary_dps * Math.pow(attacker.getAverageMove() / base_move, weight);
        summary_marks[1] = summary_time / 5.0 * Math.pow(defender.getAverageMove() / 430, 0.5);
        summary_cost_ratios[0] = summary_marks[0] * 10 / (1 + attacker.attr_price);
        summary_cost_ratios[1] = summary_marks[1] * 10 / (1 + defender.attr_price);
    }

    public boolean isCombo() {
        return option_combo;
    }

    public boolean isHunt() {
        return option_hunt;
    }

    public boolean isSiege() {
        return option_siege;
    }

    public boolean isFrenzy() {
        return option_frenzy;
    }

    public Event addEvent(Hero hero, String action, int intervals, int period) {
        Event event = new Event(hero, action, null, period, time + period);
        event.intervals = intervals;
        events.add(event);
        return event;
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
        if (option_combo) {
            exit = true;
        }
    }

    private void runAttack() {
        attacker.initActionMode(defender, false, option_combo);
        defender.initActionMode(null, true, option_combo);
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
        } while (defender.hp > 0 && time < 90000 && !exit);

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
