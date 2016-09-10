package apc.kings.hero;

import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class CannonHero extends Hero {

    private boolean snipe;

    CannonHero(HeroType heroType) {
        super(heroType);
        skills = new Skill[]{
                new Skill("翻滚突袭", 5, 0.6),
                new Skill("红莲爆弹", 6, 0.1, 0.5, 200, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("究极弩炮", 20, 0.8, 1.5, 600, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    void autoChooseAction() {
        if (skills[0].nextCastTime <= nextAttackTime) {
            cast1();
        } else {
            attack();
        }
    }

    @Override
    Event attack() {
        Event event = super.attack();
        if (attackBonus > 0) {
            event.action = "强射";
            attackFactor = 1;
            attackBonus = 0;
            attackCd = 1;
            snipe = true;
        } else {
            if (snipe) {
                event.action = "远射";
            }
            snipe = false;
        }

        skills[0].nextCastTime -= 0.5;
        if (skills[0].nextCastTime < time) {
            skills[0].nextCastTime = time;
        }
        return event;
    }

    @Override
    void cast1() {
        super.cast1();
        attackFactor = 1.15;
        attackBonus = 480;
        nextAttackTime = time;
        attackCd = 0;
    }
}
