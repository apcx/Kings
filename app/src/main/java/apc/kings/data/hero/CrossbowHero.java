package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class CrossbowHero extends Hero {

    private boolean snipe;

    protected CrossbowHero(CContext context, HeroType heroType) {
        super(context, heroType);
        attack_speed_model = 2;
        skills = new Skill[]{
                new Skill("翻滚突袭", 5000, 600),
                new Skill("红莲爆弹", 6000, 100, 0.5, 200, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("究极弩炮", 20000, 800, 1.5, 600, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        action_attack.time = 600;
        actions_cast[2].time = 800;
        if (target != null) {
            actions_active.add(actions_cast[0]);
            actions_active.add(actions_cast[2]);
        }
    }

    @Override
    protected void doSmartCast(int index) {
        if (snipe || bonus_damage > 0) {
            doSkip(index);
        } else if (0 == index) {
            doCast(index);
        } else {
            super.doSmartCast(index);
        }
    }

    @Override
    protected void onAttack(CLog log) {
        super.onAttack(log);
        if (bonus_damage > 0) {
            log.action = "强射";
            factor_attack = 1;
            bonus_damage = 0;
            action_attack.time = 0;
            snipe = true;
        } else if (snipe) {
            snipe = false;
            log.action = "远射";
            context.checkExit();
        }
        actions_cast[0].time -= 500;
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                factor_attack = 1.15;
                bonus_damage = 480;
                action_attack.time = 0;
                break;
        }
    }
}