package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class HunterHero extends Hero {

    private int cnt_eagle;
    private int bonus_arrows = 5;
    private int recharge_time;

    private boolean in_wood = true;
    private boolean in_recharge;

    protected HunterHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("鹰眼", 5000, 0),
                new Skill("百兽陷阱", 2000, 0),
                new Skill("可汗狂猎", 2000, 400, 1.15, 360, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        action_attack.time = 4999;
        actions_cast[2].time = 5099;
        actions_active.add(actions_cast[2]);
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "冷却":
                switch (event.target) {
                    case "追猎":
                        in_wood = true;
                        break;
                }
                break;
            case "准备":
                print(event.action, event.target + ++bonus_arrows);
                break;
        }
    }

    @Override
    protected void onAttack(CLog log) {
        bonus_damage = 0;
        if (in_wood) {
            in_wood = false;
            factor_attack = 0.7;
            log.action = "追猎";
            onHit(log.clone());
            onHit(log);
            context.addEvent(this, "冷却", "追猎", 4000);
        } else {
            factor_attack = 1;
            onHit(log);
        }
    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 2:
                if (bonus_arrows <= 0) {
                    Event action = actions_cast[index];
                    if (action.time < recharge_time) {
                        action.time = recharge_time;
                    }
                }
                break;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        switch (index) {
            case 2:
                hit_normal = false;
                hit_can_critical = true;

                Skill skill = skills[index];
                factor_attack = skill.damageFactor;
                bonus_damage = skill.damageBonus;
                onHit(log);

                bonus_arrows--;
                if (!in_recharge) {
                    in_recharge = true;
                    recharge_time = context.addEvent(this, "准备", "箭矢", 4000).time + 1;
                }
                break;
        }
    }

    @Override
    protected void onHit(CLog log) {
        super.onHit(log);
        if (++cnt_eagle >= 3 && target.hp > 0) {
            cnt_eagle = 0;
            log = new CLog(name, "鹰眼", target.name, context.time);
            log.damage = (int) (((int) (attr_attack * 0.4) + 480) * getDefenseFactor() * getDamageFactor(false));
            target.hp -= log.damage;
            context.logs.add(log);
        }
    }
}
