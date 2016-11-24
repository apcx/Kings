package apc.kings.data.combat;

import apc.kings.data.hero.Hero;

public class Event {

    public Hero primaryHero;
    public Hero secondaryHero;
    public String name;
    public long time;
    public int bonus;
    public int intervals;
    public double physicalFactor;
    public double magicFactor;
    public boolean regen;
    public boolean canCritical;

    public Event(Hero primaryHero, Hero secondaryHero, String name, long time) {
        this.primaryHero = primaryHero;
        this.secondaryHero = secondaryHero;
        this.name = name;
        this.time = time;
    }

    public CLog trigger() {
        return new CLog(primaryHero.heroType.name, null == secondaryHero ? null : secondaryHero.heroType.name, name, time);
    }
}
