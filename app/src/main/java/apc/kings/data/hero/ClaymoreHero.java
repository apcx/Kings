package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class ClaymoreHero extends Hero {

    private Event event_shield;
    private int shield;

    protected ClaymoreHero(CContext context, HeroType heroType) {
        super(context, heroType);
        attr_critical += 0.2;
        attr_critical_damage -= 0.5;
        skills = new Skill[]{
                new Skill("豪气斩", 7000, 400, 1, 490, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("龙卷闪", 7000, 400),
                new Skill("不羁之刃", 15000, 400, 1.18, 550, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        if (attacked) {
            actions_cast[1].time = --context.time_start;
            actions_active.add(actions_cast[1]);
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "护盾消失":
                shield = 0;
                break;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        shield = attr_mhp / 10;
        event_shield = context.addEvent(this, "护盾消失", skills[1].name, 5000);
    }

    @Override
    protected void onDamaged(int damage) {
        if (shield > 0) {
            int damage_shield = Math.min(shield, damage);
            shield -= damage_shield;
            damage -= damage_shield;
            if (shield <= 0) {
                context.events.remove(event_shield);
                print("护盾击破", event_shield.target);
            }
        }
        if (damage > 0) {
            hp -= damage;
        }
    }
}
