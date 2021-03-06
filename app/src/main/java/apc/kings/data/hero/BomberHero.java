package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

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
        action_attack.time = 400;
        if (target != null) {
            actions_active.add(actions_cast[0]);
        }
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "失效":
                switch (event.target) {
                    case "谍影加速":
                        panel_attack_speed -= 500;
                        break;
                }
                break;
        }
    }

    @Override
    protected void doSmartCast(int index) {
        doCast(index);
    }

    @Override
    protected void onAttack(CLog log) {
        super.onAttack(log);
        if (++marks == 4) {
            log = new CLog(name, "引爆", target.name, context.time);
            context.logs.add(log);
            log.damage = target.onDamaged(260 * 5 + panel_attack * 77 / 100 + (panel_attack * 48 / 100) * 4, Skill.TYPE_PHYSICAL);
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 0:
                marks = 0;
                panel_attack_speed += 500;
                context.addEvent(this, "失效", "谍影加速", 4000);
                log.target = target.name;
                break;
        }
    }
}
