package apc.kings.data.hero;

import apc.kings.data.HeroType;

@SuppressWarnings("unused")
class MonkeyHero extends Hero {

    MonkeyHero(HeroType heroType) {
        super(heroType);
    }

    @Override
    void updateAttackCanCritical() {
        attackCanCritical = (int) (attack * attackFactor) + attackBonus;
    }
}
