package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class RobotHero extends Hero {

    private int quick_bullets;
    private int normal_bullets;
    private boolean missile_launched;

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
        action_attack.time = 500;
        actions_cast[1].time = 501;
        if (target != null) {
            actions_active.add(actions_cast[0]);
            actions_active.add(actions_cast[1]);
        }
    }

    @Override
    protected void doAttack() {
        // Grenades miss, until getting close enough.
        Skill skill = skills[0];
        if (skill.damageBonus <= 0 && quick_bullets <= 0) {
            skill.damageFactor = 0.54;
            skill.damageBonus = 245;
            skill.factorType = Skill.TYPE_PHYSICAL;
            skill.damageType = Skill.TYPE_PHYSICAL;
        }
        super.doAttack();
    }

    @Override
    protected void doSmartCast(int index) {
        if (quick_bullets > 0) {
            doSkip(index);
        } else if (normal_bullets < 3) {
            doCast(index);
        } else {
            doSkip(index);
        }
    }

    @Override
    protected void onAttack(CLog log) {
        if (quick_bullets > 0) {
            log.action = "扫射";
            super.onAttack(log);
            if (--quick_bullets <= 0) {
                normal_bullets = 0;
                quick_bullets = 0;
                factor_attack = 1;
                attr_attack_cd = 800;
                if (missile_launched) {
                    context.checkExit();
                }
            }
        } else {
            super.onAttack(log);
            if (++normal_bullets >= 4) {
                toStrafe();
            }
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                toStrafe();
                action_attack.time = 0;
                break;
            case 1:
                toStrafe();
                action_attack.time = 0;
                if (target.hp > 0) {
                    log.magic_damage = (int) ((target.panel_hp - target.hp) / 10 * getMagicDefenseFactor() * getDamageFactor());
                    target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
                }
                missile_launched = true;
                actions_active.remove(actions_cast[1]);
                break;
        }
    }

    private void toStrafe() {
        normal_bullets = 0;
        quick_bullets = 4;
        factor_attack = 0.7;
        attr_attack_cd = 425;
    }
}
