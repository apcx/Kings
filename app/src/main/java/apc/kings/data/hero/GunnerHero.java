package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class GunnerHero extends Hero {

    private static final int[] SHOOTING_TIMES = {1800, 0, 3000};
    private static final int ENERGY_MAX = 150;
    private static final int ENERGY_COST = 70;

    private Event event_energy;
    private int energy = ENERGY_MAX;
    private boolean in_energy_restoring;
    private boolean in_silver_bullets;
    private boolean in_barrage;

    protected GunnerHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("华丽左轮", 5000, 0, 0.12, 210, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("漫游之枪", 4000, 0),
                new Skill("狂热弹幕", 30000, 0, 0.3, 300, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
        factor_damage += 20;
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        context.far = true;
        super.initActionMode(target, attacked, specific);
        action_attack.time = 601;
        actions_cast[2].time = 602;
        actions_cast[1].time = context.isMultiple() ? 603 : 100;
        if (target != null) {
            actions_active.add(actions_cast[0]);
            actions_active.add(actions_cast[1]);
            if (context.isMultiple()) {
                actions_active.add(actions_cast[2]);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "失效":
                switch (event.target) {
                    case "漫游加攻":
                        in_silver_bullets = false;
                        break;
                }
                break;
            case "回能量":
                restoreEnergy();
                break;
            case "华丽左轮":
                onHit(new CLog(name, event.action, target.name, context.time));
                restoreEnergy();
                if (!context.isMultiple() && event.intervals <= 0) {
                    context.checkExit();
                }
                break;
            case "狂热弹幕":
                onHit(new CLog(name, event.action, target.name, context.time));
                if (event.intervals <= 0) {
                    in_barrage = false;
                    context.checkExit();
                }
                break;
        }
    }

    @Override
    protected void doAttack() {
        if (context.far) {
            checkFrozenHeart();
        }
        super.doAttack();
    }

    @Override
    protected void doSmartCast(int index) {
        if (energy < ENERGY_COST) {
            actions_cast[index].time = event_energy.time + 1;
        } else {
            super.doSmartCast(index);
        }
    }

    @Override
    protected void onAttack(CLog log) {
        bonus_damage = 0;
        factor_attack = 0.7;
        log.action = "大弹";
        onHit(log.clone());
        factor_attack = 0.4;
        log.action = "小弹";
        onHit(log);
    }

    @Override
    protected void onCast(int index, CLog log) {
        energy -= ENERGY_COST;
        if (!in_energy_restoring) {
            in_energy_restoring = true;
            event_energy = context.addEvent(this, "回能量", null, 1000);
        }
        switch (index) {
            case 0:
                startShooting(index, log);
                restoreEnergy();
                break;
            case 2:
                startShooting(index, log);
                in_barrage = true;
                actions_cast[1].time = context.time + 374;
                break;
            case 1:
                super.onCast(index, log);
                in_silver_bullets = true;
                context.addEvent(this, "失效", "漫游加攻", 3000);
                break;
        }
    }

    @Override
    protected void onHitMagic(CLog log) {
        if (in_silver_bullets) {
            log.magic_damage = (int) (((int) (panel_attack * 0.11) + 65) * getMagicDefenseFactor() * getDamageFactor());
            target.onDamaged(log.magic_damage, Skill.TYPE_MAGIC);
        }
    }

    private void restoreEnergy() {
        if (energy < ENERGY_MAX) {
            energy += 10;
            if (energy >= ENERGY_MAX) {
                in_energy_restoring = false;
                context.events.remove(event_energy);
            }
            print("回能量", Integer.toString(energy));
        }
    }

    private void startShooting(int index, CLog log) {
        hit_normal = false;

        Skill skill = skills[index];
        factor_attack = skill.damageFactor;
        bonus_damage = skill.damageBonus;
        onHit(log);

        int speed = panel_attack_speed;
        if (context.isFrenzy() && 2 == index) {
            speed += 600;
        }
        int intervals = 4 + 2 * (Math.min(speed, 1500) / 750);
        context.addEvent(this, skill.name, intervals, SHOOTING_TIMES[index] / intervals);
        action_attack.time = context.time + SHOOTING_TIMES[index] + 1;
        actions_cast[2 - index].delayTo(action_attack.time + 1);
    }
}
