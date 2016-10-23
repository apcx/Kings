package apc.kings.hero;

import apc.kings.data.HeroType;

@SuppressWarnings("unused")
class MonkeyHero extends Hero {

    MonkeyHero(HeroType heroType) {
        super(heroType);
    }

    @Override
    void updateDamageCanCritical() {
        damageCanCritical = (int) (attack * attackFactor) + attackBonus;
    }
}
