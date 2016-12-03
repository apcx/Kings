package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;
import apc.kings.data.combat.CLog;

@SuppressWarnings("unused")
class RobotHero extends Hero {

    private int quick_bullets;
    private int normal_bullets;

    protected RobotHero(CContext context, HeroType heroType) {
        super(context, heroType);
        attr_attack_cd = 800;
        skills = new Skill[]{
                new Skill("河豚手雷", 7000, 500),
                new Skill("无敌鲨嘴炮", 12000, 800, 1, 435, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("空中支援", 20000, 0),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        actions_cast[0].time = 4499;
        action_attack.time = 4999;
        actions_cast[1].time = 5000;
        actions_active.add(actions_cast[0]);
        actions_active.add(actions_cast[1]);
    }

    @Override
    protected void doSmartCast(int index) {
        if (quick_bullets > 0 || normal_bullets >= 3) {
            doSkip(index);
        } else {
            super.doSmartCast(index);
        }
    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 0:
            case 1:
                toStrafe();
                action_attack.time = context.time + skills[index].swing;
                break;
        }
    }

    @Override
    protected void doAttack(CLog log) {
        if (quick_bullets > 0) {
            log.action = "扫射";
            super.doAttack(log);
            if (--quick_bullets <= 0) {
                toNormal();
            }
        } else {
            super.doAttack(log);
            if (++normal_bullets >= 4) {
                toStrafe();
            }
        }
    }

    private void toStrafe() {
        normal_bullets = 0;
        quick_bullets = 4;
        attackFactor = 0.7;
        attr_attack_cd = 425;
    }

    private void toNormal() {
        normal_bullets = 0;
        quick_bullets = 0;
        attackFactor = 1;
        attr_attack_cd = 800;
    }
}
