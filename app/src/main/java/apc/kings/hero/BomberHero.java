package apc.kings.hero;

import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class BomberHero extends Hero {

    private double bombEndTime;
    private int marks;

    BomberHero(HeroType heroType) {
        super(heroType);
        skills = new Skill[]{
                new Skill("谍影重重", 9.5, 0.4),
                new Skill("刃遁", 9.5, 0.1),
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
    Event attack() {
        Event event = super.attack();
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
                event = new Event(heroType.name, target.heroType.name, "引爆", time);
                event.damage = ((int) (attack * 0.48) + 220) * 5 * getDamageRate();
                target.hp -= event.damage;
                events.add(event.sum());
            }
        }
        return event;
    }

    @Override
    void cast1() {
        super.cast1();
        bombEndTime = time + 4;
        marks = 0;
    }
}
