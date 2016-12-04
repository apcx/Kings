package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class CannonHero extends Hero {

    private boolean snipe;

    protected CannonHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("翻滚突袭", 5000, 600),
                new Skill("红莲爆弹", 6000, 100, 0.5, 200, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("究极弩炮", 20000, 800, 1.5, 600, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        actions_cast[0].time = 4399;
        action_attack.time = 4999;
        actions_active.add(actions_cast[0]);
    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 0:
                attackFactor = 1.15;
                attackBonus = 480;
                action_attack.time = context.time + skills[index].swing;
                break;
        }
    }

    @Override
    protected void onAttack(CLog log) {
        super.onAttack(log);
        if (attackBonus > 0) {
            log.action = "强射";
            attackFactor = 1;
            attackBonus = 0;
            action_attack.time = context.time + 100;
            snipe = true;
        } else if (snipe) {
            snipe = false;
            log.action = "远射";
        }
        actions_cast[0].time -= 500;
    }
}
