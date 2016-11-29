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
                new Skill("炙热之风", 10000, 0.1),
                new Skill("燎原箭雨", 4000, 0.4, 1.16, 760, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("惩戒射击", 35000, 0),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean defensive, boolean specific) {
        super.initActionMode(target, attacked, defensive, specific);
        attackEvent.time = 4100;
        castEvents[0].time = 4000;
        activeEvents.add(castEvents[0]);
    }

    @Override
    protected void doAttack() {
        CLog log = new CLog(heroType.name, "攻击", target.heroType.name, context.time);
        int cd = (int) (atAttackCd * 100 / (100 + Math.min(200, atAttackSpeed))); // todo: check negative formula
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
        } else {
            hit(log, true);
        }
        attackEvent.time = context.time + Math.max(100, cd);
    }

    @Override
    void doCast1() {
        super.doCast1();
        quickArrows = 3;
        attackFactor = 0.4;
        attackBonus = 140;
    }
}
