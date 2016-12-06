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

    private Event energyEvent;
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
        actions_cast[0].time = 4999;
        action_attack.time = 5000;
        actions_cast[2].time = 5001;
        actions_cast[1].time = 5002;
        actions_active.add(actions_cast[0]);
        actions_active.add(actions_cast[1]);
        actions_active.add(actions_cast[2]);
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
                break;
            case "狂热弹幕":
                onHit(new CLog(name, event.action, target.name, context.time));
                if (event.intervals <= 0) {
                    in_barrage = false;
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
            actions_cast[index].time = energyEvent.time + 1;
        } else if (1 == index) {
            if (in_barrage) {
                doCast(index);
            } else {
                actions_cast[index].time = Integer.MAX_VALUE;
            }
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
        log.action = "小弹";
        factor_attack = 0.4;
        onHit(log);
    }

    @Override
    protected void onCast(int index, CLog log) {
        energy -= ENERGY_COST;
        if (!in_energy_restoring) {
            in_energy_restoring = true;
            energyEvent = context.addEvent(this, "回能量", null, 1000);
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
            log.magicDamage = (int) (((int) (attr_attack * 0.11) + 65) * getMagicDefenseFactor() * getDamageFactor(false));
            target.hp -= log.magicDamage;
        }
    }

    private void restoreEnergy() {
        energy += 10;
        if (energy >= ENERGY_MAX) {
            in_energy_restoring = false;
            context.events.remove(energyEvent);
        }
        print("回能量", Integer.toString(energy));
    }

    private void startShooting(int index, CLog log) {
        hit_normal = false;

        Skill skill = skills[index];
        factor_attack = skill.damageFactor;
        bonus_damage = skill.damageBonus;
        onHit(log);

        int intervals = 4 + 2 * (int) (attr_attack_speed / 75);
        context.addEvent(this, skill.name, intervals, SHOOTING_TIMES[index] / intervals);

        int time = context.time + SHOOTING_TIMES[index] + 1;
        action_attack.time = time++;
        Event another = actions_cast[2 - index];
        if (another.time < time) {
            another.time = time;
        }
    }
}
