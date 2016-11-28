package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class StalkerHero extends Hero {

    private int quickArrows;

    protected StalkerHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("炙热之风", 10, 0.1),
                new Skill("燎原箭雨", 4, 0.4, 1.16, 760, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("惩戒射击", 35, 0),
        };
    }

//    @Override
//    public void autoChooseAction() {
//        if (time >= nextAttackTime) {
////            attack();
//        } else if (time >= skills[0].nextCastTime) {
//            cast1();
//        } else if (time >= skills[1].nextCastTime) {
//            cast2();
//        } else {
////            attack();
//        }
//    }
//
    @Override
    protected int doAttack(CLog log) {
        int cd = (int) (attackCd * 100 / (100 + Math.min(200, atAttackSpeed))); // todo: check negative formula
        if (quickArrows > 0) {
            quickArrows--;
            log.action = "箭风";
            hit(log.clone(), true);
            hit(log.clone(), true);
            hit(log.clone(), true);
            if (quickArrows <= 0) {
                attackFactor = 1;
                attackBonus = 0;
            }
        }
        return cd;
    }

    @Override
    void doCast1() {
        super.doCast1();
        quickArrows = 3;
        attackFactor = 0.4;
        attackBonus = 140;
    }
}
