package bkhn.et.storemanage.util;

import android.support.annotation.NonNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class AppUtils {

    public static <V> boolean isNotNull(V item) {
        return item != null;
    }

    public static boolean isValidProductCode(@NonNull String data){
        return data.matches("(.*)\\|(.*)");
    }
}
