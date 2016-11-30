//package apc.kings.data.hero;
//
//import apc.kings.data.combat.CLog;
//import apc.kings.data.HeroType;
//import apc.kings.data.Skill;
//
//@SuppressWarnings("unused")
//class RobotHero extends Hero {
//
//    private int quickBullets;
//    private int normalBullets;
//
//    RobotHero(HeroType heroType) {
//        super(heroType);
////        attr_attack_cd = 0.8;
//        skills = new Skill[]{
//                new Skill("河豚手雷", 7, 0.5),
//                new Skill("无敌鲨嘴炮", 12, 0.8, 1, 435, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
//                new Skill("空中支援", 20, 0),
//        };
//        nextAttackTime = 0.001;
//    }
//
//    @Override
//    public void autoChooseAction() {
//        if (quickBullets > 0) {
////            attack();
//        } else if (time >= nextAttackTime) {
////            attack();
//        } else if (time >= skills[0].nextCastTime) {
////            cast1();
//        } else if (time >= skills[1].nextCastTime) {
//            cast2();
//        } else {
////            attack();
//        }
//    }
//
//    @Override
//    public CLog attack(int time) {
//        CLog log = super.attack(time);
//        if (quickBullets > 0) {
//            log.name = "扫射";
//            quickBullets--;
//            if (quickBullets <= 0) {
//                toNormal();
//            }
//        } else {
//            normalBullets++;
//            if (normalBullets >= 4) {
//                toStrafe();
//            }
//        }
//        return log;
//    }
//
//    @Override
//    void cast1() {
//        super.cast1();
//        toStrafe();
//        nextAttackTime = time;
//    }
//
//    @Override
//    void cast2() {
//        super.cast2();
//        toStrafe();
//        nextAttackTime = time;
//    }
//
//    private void toStrafe() {
//        normalBullets = 0;
//        quickBullets = 4;
//        attackFactor = 0.7;
////        attr_attack_cd = 0.425;
//    }
//
//    private void toNormal() {
//        normalBullets = 0;
//        quickBullets = 0;
//        attackFactor = 1;
////        attr_attack_cd = 0.8;
//    }
//}
