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

    public void addEvent(Hero hero, String action, String target, int duration) {
        Event event = new Event(hero, action, time + duration);
        event.target = target;
        events.add(event);
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
        Event event = new Event(hero, action, eventTime);
        event.target = buff;
        events.add(event);
    }

    private void runAttack() {
        events.add(new Event(defender,"回血", 5000));
        attacker.target = defender;
        attacker.normalFactor = (defender.flags & Item.FLAG_BOOTS_DEFENSE) == 0 ? 1 : 0.85;
        attacker.attackEvent.time = beginTime;
        attacker.events.add(attacker.attackEvent);
        do {
            Collections.sort(events);
            Collections.sort(attacker.events);
            Event contextEvent = events.get(0);
            Event attackerEvent = attacker.events.get(0);
            if (contextEvent.time <= attackerEvent.time) {
                if (contextEvent.time > time) {
                    time = contextEvent.time;
                }
                contextEvent.hero.onEvent(contextEvent);
            } else {
                attackerEvent.hero.onAI(attackerEvent);
            }
        } while (defender.hp > 0);
    }
}
