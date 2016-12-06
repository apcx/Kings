package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.HeroType;

@SuppressWarnings("unused")
public class MonkeyHero extends Hero {

    protected MonkeyHero(CContext context, HeroType heroType) {
        super(context, heroType);
        attr_critical += 0.2;
        attr_critical_damage -= 0.5;
    }

    @Override
    void updateAttackCanCritical() {
        attackCanCritical = (int) (attr_attack * factor_attack) + attackBonus;
    }
}
