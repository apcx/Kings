package apc.kings.data.hero;

import apc.kings.data.combat.CContext;
import apc.kings.data.combat.CLog;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;
import apc.kings.data.combat.Event;

@SuppressWarnings("unused")
public class BomberHero extends Hero {

    private int marks = 100;

    protected BomberHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("谍影重重", 8000, 400),
                new Skill("刃遁", 8000, 100),
                new Skill("无间刃风", 24000, 0),
        };
    }

    @Override
    public void initActionMode(Hero target, boolean attacked, boolean specific) {
        super.initActionMode(target, attacked, specific);
        actions_cast[0].time = 4599;
        action_attack.time = 4999;
        actions_active.add(actions_cast[0]);
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        if ("失效".equals(event.action) && "谍影加速".equals(event.target)) {
            attr_attack_speed -= 50;
        }
    }

    @Override
    protected void doCast(int index) {
        super.doCast(index);
        switch (index) {
            case 0:
                marks = 0;
                attr_attack_speed += 50;
                context.updateBuff(this, "失效", "谍影加速", 4000);
                break;
        }
    }

    @Override
    protected void doAttack(CLog log) {
        super.doAttack(log);
        if (++marks == 4) {
            log = new CLog(name, target.name, "引爆", context.time);
            log.damage = (int) (((int) (attr_attack * 0.48) + 220) * 5 * getDamageRate());
            target.hp -= log.damage;
            onDamage(log);
            afterHit();
        }
    }
}
