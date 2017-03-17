package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class SaviorHero extends Hero {

    protected SaviorHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("损人利己", 9000, 400),
                new Skill("双重恐吓", 10000, 400),
                new Skill("统御战场", 50000, 2200),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        if (attacked) {
            actions_active.add(actions_cast[0]);
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "护盾消失":
                switch (event.target) {
                    case "损人利己":
                        shield_hero = 0;
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                shield_hero = 720 + panel_magic * 40 / 100 + panel_hp * 9 / 100;
                event_shield = context.addEvent(this, "护盾消失", skills[index].name, 5000);
                break;
        }
    }
}
