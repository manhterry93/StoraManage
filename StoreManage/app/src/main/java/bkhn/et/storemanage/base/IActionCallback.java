package bkhn.et.storemanage.base;

import android.support.annotation.Nullable;

/**
 * Created by PL_itto on 11/23/2017.
 */

public interface IActionCallback<V extends Object> {
    void onSuccess(@Nullable V result);

    void onFailed(@Nullable V result);

}
