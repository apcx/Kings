package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class HunterHero extends Hero {

    protected HunterHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("鹰眼", 5000, 0),
                new Skill("百兽陷阱", 2000, 0),
                new Skill("可汗狂猎", 2000, 0, 1.15, 360, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }
}
