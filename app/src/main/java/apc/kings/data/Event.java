package apc.kings.data;

import apc.kings.data.hero.Hero;

public class Event implements Comparable<Event> {

    public Hero hero;
    public String action;
    public String target;
    public int time;
    public int bonus;
    public int intervals;
    public double physicalFactor;
    public double magicFactor;
    public boolean regen;
    public boolean canCritical;

    public Event(Hero hero, String action, int time) {
        this.hero = hero;
        this.action = action;
        this.time = time;
    }

    public Event(Hero hero, String action, String target, int time) {
        this.hero = hero;
        this.action = action;
        this.target = target;
        this.time = time;
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Event another) {
        return time - another.time;
    }
}
