package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class GunnerHero extends Hero {

    protected GunnerHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("华丽左轮", 5000, 0),
                new Skill("漫游之枪", 4000, 0),
                new Skill("狂热弹幕", 30000, 0, 0.3, 300, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }
}
