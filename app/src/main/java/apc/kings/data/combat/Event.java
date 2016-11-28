package apc.kings.data.combat;

import android.support.annotation.NonNull;

import apc.kings.data.hero.Hero;

public class Event implements Comparable<Event> {

    public CContext context;
    public Hero hero;
    public String target;
    public String action;
    public int time;
    public int bonus;
    public int intervals;
    public double physicalFactor;
    public double magicFactor;
    public boolean regen;
    public boolean canCritical;

    public Event(CContext context, Hero hero, String action, int time) {
        this.context = context;
        this.hero = hero;
        this.action = action;
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull Event another) {
        return time - another.time;
    }
}
