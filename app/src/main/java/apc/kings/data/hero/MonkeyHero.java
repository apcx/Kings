package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class MonkeyHero extends Hero {

    protected MonkeyHero(CContext context, HeroType heroType) {
        super(context, heroType);
        attr_critical += 0.2;
        attr_critical_damage -= 0.5;
        skills = new Skill[]{
                new Skill("七十二变", 8400, 100),
                new Skill("斗战冲锋", 5000, 100),
                new Skill("如意金箍", 24000, 100, 0.5, 230, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        action_attack.time = 4999;
        actions_cast[0].time = 4899;
        actions_cast[2].time = 5099;
        actions_cast[1].time = 5299;
        actions_active.add(actions_cast[0]);
        actions_active.add(actions_cast[1]);
        actions_active.add(actions_cast[2]);
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        factor_attack = 1.4;
        bonus_damage = 210;
    }

    @Override
    protected void onHit(CLog log) {
        super.onHit(log);
        factor_attack = 1;
        bonus_damage = 0;
    }

    @Override
    void updateAttackCanCritical() {
        attackCanCritical = (int) (attr_attack * factor_attack) + bonus_damage;
    }
}
