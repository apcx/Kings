package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class MonkeyHero extends Hero {

    protected MonkeyHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("七十二变", 8400, 400),
                new Skill("斗战冲锋", 5000, 400),
                new Skill("如意金箍", 24000, 400, 0.5, 230, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
        panel_critical += 200;
        panel_critical_damage -= 500;
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        action_attack.time = 400;
        actions_cast[2].time = 600;
        actions_cast[1].time = 601;
        if (target != null) {
            actions_active.add(actions_cast[0]);
            actions_active.add(actions_cast[1]);
            actions_active.add(actions_cast[2]);
        }
    }

    @Override
    protected void onAttack(CLog log) {
        if (bonus_damage > 0) {
            log.action = "神威";
            super.onAttack(log);
            action_attack.time = 0;
            factor_attack = 1;
            bonus_damage = 0;
        } else {
            super.onAttack(log);
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        action_attack.time = 0;
        factor_attack = 1.4;
        bonus_damage = 420;
    }

    @Override
    protected void onUpdateAttackCanCritical() {
        damage_can_critical = (int) (panel_attack * factor_attack) + bonus_damage;
    }
}
