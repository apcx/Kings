package apc.kings.data;

import android.renderscript.Double2;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import apc.kings.hero.Hero;

public class Summary {

    public int damage;
    public double time;
    public double dps;
    public double costRatio;

    public Summary(@NonNull Hero attacker, @NonNull Hero defender) {
        damage = defender.mhp;
        List<Event> events = attacker.fight(defender);
        time = events.get(events.size() - 1).time;
        dps = damage / time;
        costRatio = 100 * dps / (1 + attacker.price);
    }

    @NonNull
    public static Summary build(@NonNull HeroType attackerType, @NonNull HeroType defenderType) {
        return new Summary(Hero.create(attackerType), Hero.create(defenderType));
    }
}
