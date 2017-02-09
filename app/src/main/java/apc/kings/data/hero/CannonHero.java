package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class CannonHero extends Hero {

    private int rage;
    private int siege_rate;
    private boolean siege;
    private Event event_siege_power;
    private Event event_siege_end;

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
        action_attack.time = 800;
        actions_cast[1].time = 900;
        if (target != null) {
//            actions_active.add(actions_cast[1]);
            actions_active.add(actions_cast[2]);
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "完成架起":
                event_siege_power = context.addEvent(this, "炮台加强", 5, 1000);
                event_siege_end = context.addEvent(this, "结束架起", 1, 15000);
                actions_active.remove(actions_cast[2]);
                break;
            case "炮台加强":
                siege_rate += 20;
                factor_damage_specific = 1 + siege_rate / 100.0;
                print(event.action, siege_rate + "%");
                break;
            case "结束架起":
                toNormalMode();
                break;
        }
    }

    @Override
    protected void onAttack(CLog log) {
        if (siege) {
            log.action = "炮击";
        }
        super.onAttack(log);
        if (rage < 5) {
            attr_critical += 30;
            attr_attack_panel = attr_attack_base * (100 + 2 * ++rage) / 100;
            print("炮手燃魂", Integer.toString(rage));
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                if (siege) {
                    toNormalMode();
                }
                break;
            case 1:
                // todo
                break;
            case 2:
                if (siege) {
                    toNormalMode();
                    log.action = "取消架起";
                } else {
                    toSiegeMode();
                }
                break;
        }
    }

    private void toSiegeMode() {
        siege = true;
        factor_attack = 0.8;
        bonus_damage = 120;
        attr_defense += 50;
        attr_magic_defense += 50;

        factor_damage_specific = 1;
        siege_rate = 0;
        context.addEvent(this, "完成架起", 1, 800);
    }

    private void toNormalMode() {
        siege = false;
        factor_attack = 1;
        bonus_damage = 0;
        attr_defense -= 50;
        attr_magic_defense -= 50;

        factor_damage_specific = 1;
        siege_rate = 0;
        context.events.remove(event_siege_power);
        context.events.remove(event_siege_end);
        if (!actions_active.contains(actions_cast[2])) {
            actions_active.add(actions_cast[2]);
        }
    }
}
