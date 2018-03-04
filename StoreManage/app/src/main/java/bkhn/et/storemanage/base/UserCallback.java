package bkhn.et.storemanage.base;

import android.support.annotation.NonNull;

import java.util.List;

import bkhn.et.storemanage.data.model.UserDetailModel;

/**
 * Created by PL_itto on 3/4/2018.
 */

public interface UserCallback {
    void onResult(@NonNull List<UserDetailModel> user);
}
