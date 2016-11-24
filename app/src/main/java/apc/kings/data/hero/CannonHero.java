package apc.kings.data.hero;

import apc.kings.data.combat.CLog;
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
    CLog attack() {
        CLog log = super.attack();
        if (attackBonus > 0) {
            log.name = "强射";
            attackFactor = 1;
            attackBonus = 0;
            attackCd = 1;
            snipe = true;
        } else {
            if (snipe) {
                log.name = "远射";
            }
            snipe = false;
        }

        skills[0].nextCastTime -= 0.5;
        if (skills[0].nextCastTime < time) {
            skills[0].nextCastTime = time;
        }
        return log;
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
