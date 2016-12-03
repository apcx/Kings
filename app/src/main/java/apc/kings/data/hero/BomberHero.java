package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class BomberHero extends Hero {

    private double bombEndTime;
    private int marks;

    protected BomberHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("谍影重重", 8000, 400),
                new Skill("刃遁", 8000, 100),
                new Skill("无间刃风", 24000, 0),
        };
    }

//    @Override
//    public void onAI() {
//        if (skills[0].nextCastTime <= nextAttackTime) {
//            cast1();
//        } else {
////            attack();
//        }
//    }
//
//    @Override
//    public CLog attack(int time) {
//        CLog log = super.attack(time);
//        if (target.hp > 0 && time < bombEndTime) {
//            double speed = attackSpeed;
//            if (time < stormEndTime) {
//                speed += 0.5;
//            }
//            if (time < bombEndTime) {
//                speed += 0.5;
//            }
//            nextAttackTime = time + attr_attack_cd / (1 + Math.min(2, speed));
//
//            marks++;
//            if (4 == marks) {
//                log = new CLog(heroType.name, target.heroType.name, "引爆", time);
//                log.damage = ((int) (attack * 0.48) + 220) * 5 * getDamageRate();
//                target.hp -= log.damage;
//            }
//        }
//        return log;
//    }
//
//    @Override
//    void cast1() {
//        super.cast1();
//        bombEndTime = time + 4;
//        marks = 0;
//    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 0:
                break;
        }
    }
}
