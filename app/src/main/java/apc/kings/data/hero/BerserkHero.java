package apc.kings.data.hero;

import apc.kings.data.CContext;
import apc.kings.data.CLog;
import apc.kings.data.Event;
import apc.kings.data.HeroType;
import apc.kings.data.Skill;

@SuppressWarnings("unused")
public class BerserkHero extends Hero {

    protected BerserkHero(CContext context, HeroType heroType) {
        super(context, heroType);
        skills = new Skill[]{
                new Skill("爆裂双斧", 12000, 400, 0.5, 295, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("激热回旋", 5000, 400, 0.6, 250, Skill.TYPE_PHYSICAL, Skill.TYPE_PHYSICAL),
                new Skill("正义潜能", 30000, 400),
        };
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        switch (event.action) {
            case "回血":
                switch (event.target) {
                    case "正义潜能":
                        recover(event);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onCast(int index, CLog log) {
        super.onCast(index, log);
        switch (index) {
            case 2:
                hp -= hp * 16 / 100;
                recover(context.updateBuff(this, "回血", "正义潜能", 9, 500));
                break;
        }
    }

    @Override
    protected int onDamaged(double damage_raw, int type) {
        int damage = super.onDamaged(damage_raw, type);
        if (hp * 100 < panel_hp * 30 && !actions_active.contains(actions_cast[2])) {
            actions_cast[2].time = context.time + 1;
            actions_active.add(actions_cast[2]);
        }
        return damage;
    }

    private void recover(Event event) {
        CLog log = new CLog(name, event.action, event.target, context.time);
        int regen = panel_hp * 4;
        if (event.intervals >= 4) {
            regen += panel_hp - hp;
        }
        onRegen(log, Math.round(regen * 0.01f * getHealRate()));
    }
}
