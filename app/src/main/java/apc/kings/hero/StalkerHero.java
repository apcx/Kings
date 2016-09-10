package apc.kings.hero;

import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class StalkerHero extends Hero {

    private int quickArrows;

    StalkerHero(HeroType heroType) {
        super(heroType);
        skills = new Skill[]{
                new Skill("炙热之风", 10, 0.1),
                new Skill("燎原箭雨", 4, 0.4, 1.16, 760, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("惩戒射击", 30, 0),
        };
        nextAttackTime = 0.101;
    }

    @Override
    void autoChooseAction() {
        if (time >= nextAttackTime) {
            attack();
        } else if (time >= skills[0].nextCastTime) {
            cast1();
        } else if (time >= skills[1].nextCastTime) {
            cast2();
        } else {
            attack();
        }
    }

    @Override
    Event attack() {
        Event event = super.attack();
        if (quickArrows > 0) {
            quickArrows--;
            event.action = "箭风";

            event = hit();
            event.action = "箭风";

            event = hit();
            event.action = "箭风";

            if (quickArrows <= 0) {
                attackFactor = 1;
                attackBonus = 0;
            }
        }
        return event;
    }

    @Override
    void cast1() {
        super.cast1();
        quickArrows = 3;
        attackFactor = 0.4;
        attackBonus = 140;
    }
}
