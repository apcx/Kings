package apc.kings.common;

import android.support.annotation.Nullable;

import java.util.Map;

public class Utils {

    public static boolean isEmpty(@Nullable Map map) {
        return null == map || map.isEmpty();
    }
}
