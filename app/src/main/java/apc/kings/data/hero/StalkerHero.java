package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
class StalkerHero extends Hero {

    private int quick_arrows;

    protected StalkerHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("炙热之风", 10000, 100),
                new Skill("燎原箭雨", 4000, 400, 1.16, 760, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("惩戒射击", 35000, 0),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        actions_cast[0].time = 4499;
        actions_cast[1].time = 4599;
        action_attack.time = 4999;
        actions_active.add(actions_cast[0]);
        actions_active.add(actions_cast[1]);
    }

    @Override
    protected void doAttack(CLog log) {
        if (quick_arrows > 0) {
            quick_arrows--;
            log.action = "箭风";
            onHit(log.clone(), true);
            onHit(log.clone(), true);
            onHit(log, true);
            if (quick_arrows <= 0) {
                attackFactor = 1;
                attackBonus = 0;
            }
        } else {
            super.doAttack(log);
        }
    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 0:
                quick_arrows = 3;
                attackFactor = 0.4;
                attackBonus = 140;
                break;
        }
    }
}
