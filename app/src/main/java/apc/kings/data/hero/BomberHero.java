package apc.kings.data.hero;

import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class BomberHero extends Hero {

    private double bombEndTime;
    private int marks;

    BomberHero(HeroType heroType) {
        super(heroType);
        skills = new Skill[]{
                new Skill("谍影重重", 8, 0.4),
                new Skill("刃遁", 8, 0.1),
                new Skill("无间刃风", 24, 0),
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
        if (target.hp > 0 && time < bombEndTime) {
            double speed = attackSpeed;
            if (time < stormEndTime) {
                speed += 0.5;
            }
            if (time < bombEndTime) {
                speed += 0.5;
            }
            nextAttackTime = time + attackCd / (1 + Math.min(2, speed));

            marks++;
            if (4 == marks) {
                log = new CLog(heroType.name, target.heroType.name, "引爆", time);
                log.damage = ((int) (attack * 0.48) + 220) * 5 * getDamageRate();
                target.hp -= log.damage;
                logs.add(log.sum());
            }
        }
        return log;
    }

    @Override
    void cast1() {
        super.cast1();
        bombEndTime = time + 4;
        marks = 0;
    }
}
