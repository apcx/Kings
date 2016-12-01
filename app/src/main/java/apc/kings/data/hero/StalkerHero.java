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
                new Skill("炙热之风", 10000, 100),
                new Skill("燎原箭雨", 4000, 400, 1.16, 760, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("惩戒射击", 35000, 0),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        attackEvent.time = 4999;
        castEvents[0].time = 4499;
        castEvents[1].time = 4599;
        actions.add(castEvents[0]);
        actions.add(castEvents[1]);
    }

    @Override
    protected void doAttack() {
        CLog log = new CLog(heroType.name, "攻击", target.heroType.name, context.time);
        int cd = (int) (attr_attack_cd * 100 / (100 + Math.min(200, attr_attack_speed)));
        if (quickArrows > 0) {
            quickArrows--;
            log.action = "箭风";
            hit(log.clone(), true);
            hit(log.clone(), true);
            hit(log, true);
            if (quickArrows <= 0) {
                attackFactor = 1;
                attackBonus = 0;
            }
        } else {
            hit(log, true);
        }
        attackEvent.time = context.time + cd;
        delayActions(100);
    }

    @Override
    void doCast1() {
        super.doCast1();
        quickArrows = 3;
        attackFactor = 0.4;
        attackBonus = 140;
    }
}
