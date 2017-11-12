package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class ClaymoreHero extends Hero {

    protected ClaymoreHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("豪气斩", 7000, 400, 1, 490, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("龙卷闪", 7000, 400),
                new Skill("不羁之刃", 15000, 400, 1.18, 550, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        if (attacked) {
            actions_active.add(actions_cast[1]);
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "护盾消失":
                switch (event.target) {
                    case "龙卷闪":
                        shield_hero = 0;
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        shield_hero = panel_hp * 15 / 100;
        event_shield = context.addEvent(this, "护盾消失", skills[1].name, 5000);
    }
}
