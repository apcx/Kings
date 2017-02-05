package apc.kings.data;

import apc.kings.data.hero.Hero;

public class Event implements Comparable<Event> {

    public Hero hero;
    public String action;
    public String target;
    public int period;
    public int time;
    public int intervals;

    public Event(Hero hero, String action, int time) {
        this.hero = hero;
        this.action = action;
        this.time = time;
        period = time;
    }

    public Event(Hero hero, String action, String target, int period, int time) {
        this.hero = hero;
        this.action = action;
        this.target = target;
        this.period = period;
        this.time = time;
    }

    public void delayTo(int time) {
        if (this.time < time) {
            this.time = time;
        }
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Event another) {
        return time - another.time;
    }
}
