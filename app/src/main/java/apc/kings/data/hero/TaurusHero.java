package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class TaurusHero extends Hero {

    protected TaurusHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("咆哮之斧", 6000, 400),
                new Skill("横行霸道", 8000, 1000),
                new Skill("山崩地裂", 30000, 1000),
        };
        int defense = 200 + 7 * (panel_level - 1);
        panel_defense += defense;
        base_magic_defense += defense;
    }
}
