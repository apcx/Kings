package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class CannonHero extends Hero {

    private int rage;
    private int siege_power;
    private boolean siege;
    private Event event_siege_power;
    private Event event_siege_end;

    protected CannonHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("追击潜能", 8000, 0),
                new Skill("警戒地雷", 6000, 100, 0.6, 400, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("重装炮台", 4000, 600),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        actions_cast[1].time = 100;
        if (target != null) {
            actions_active.add(actions_cast[1]);
            if (context.isSiege()) {
                toSiegeMode(0);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "完成架起":
                toSiegeMode(0);
                break;
            case "炮台加强":
                print(event.action, 20 * ++siege_power + "%");
                break;
            case "结束架起":
                toNormalMode();
                break;
        }
    }

    @Override
    protected void onAttack(CLog log) {
        if (siege) {
            factor_damage_attack = 1 + siege_power / 5.0;
            log.action = "炮击";
        } else {
            factor_damage_attack = 1;
        }
        super.onAttack(log);
        if (rage < 5) {
            int critical, attack;
            if (siege) {
                critical = 30;
                attack = 2;
            } else {
                critical = 15;
                attack = 1;
            }
            panel_critical = base_critical + critical * ++rage;
            panel_attack = base_attack * (100 + attack * rage) / 100 + bonus_attack;
            if (has_anti_magic) {
                panel_magic_defense += Math.min(panel_attack * 40 / 100,300);
            }
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
                target.in_alert_mine = true;
                context.updateBuff(target, "复原", "地雷破甲", 2000);
                break;
            case 2:
                if (siege) {
                    toNormalMode();
                    log.action = "取消架起";
                } else {
                    context.addEvent(this, "完成架起", 1, skills[2].swing);
                }
                break;
        }
    }

    @Override
    public double getAverageMove() {
        int cd = skills[0].cd * (1000 - panel_cdr) / 1000;
        return (getPanelMove(base_move * (1700 + panel_move_speed) / 1000) * 2000 + super.getAverageMove() * (cd - 2000)) / cd;
    }

    private void toSiegeMode(int seconds) {
        if (!siege) {
            siege = true;
            rage = 0;
            panel_critical = base_critical;
            panel_attack = base_attack + bonus_attack;
            if (has_anti_magic) {
                panel_magic_defense += Math.min(panel_attack * 40 / 100,300);
            }
        }
        factor_attack = 0.8;
        bonus_damage = 120;
        panel_defense += 50;
        base_magic_defense += 50;
        panel_magic_defense += 50;

        siege_power = Math.min(seconds, 5);
        if (siege_power < 5) {
            event_siege_power = context.addEvent(this, "炮台加强", 5 - siege_power, 1000);
        }
        event_siege_end = context.addEvent(this, "结束架起", 1, 1000 * (15 - seconds));
        actions_active.remove(actions_cast[2]);
    }

    private void toNormalMode() {
        if (siege) {
            siege = false;
            rage = 0;
            panel_critical = base_critical;
            panel_attack = base_attack + bonus_attack;
            if (has_anti_magic) {
                panel_magic_defense += Math.min(panel_attack * 40 / 100,300);
            }
        }
        factor_attack = 1;
        bonus_damage = 0;
        panel_defense -= 50;
        base_magic_defense -= 50;
        panel_magic_defense -= 50;

        siege_power = 0;
        context.events.remove(event_siege_power);
        context.events.remove(event_siege_end);
        if (!actions_active.contains(actions_cast[2])) {
            actions_active.add(actions_cast[2]);
        }
    }
}
