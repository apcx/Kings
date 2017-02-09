package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class CannonHero extends Hero {

    private boolean snipe;
    private int rage;

    protected CannonHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("追击潜能", 8000, 0),
                new Skill("警戒地雷", 6000, 100, 0.6, 400, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("重装炮台", 2000, 800),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        context.far = true;
        super.initActionMode(target, attacked, specific);
        action_attack.time = 0;
        actions_cast[2].time = 800;
        if (target != null) {
//            actions_active.add(actions_cast[0]);
//            actions_active.add(actions_cast[2]);
        }
    }

    @Override
    protected void onAttack(CLog log) {
        super.onAttack(log);
        if (rage < 5) {
            attr_critical += 30;
            attr_attack_panel = attr_attack_base * (100 + 2 * ++rage) / 100;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                break;
        }
    }
}
