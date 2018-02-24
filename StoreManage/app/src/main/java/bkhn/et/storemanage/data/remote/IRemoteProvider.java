package bkhn.et.storemanage.data.remote;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by PL_itto on 2/12/2018.
 */

public interface IRemoteProvider {
    FirebaseUser getCurrentUser();

    void loginToServer(@NonNull String email, @NonNull String password, OnCompleteListener listener);

    void getUserPosition(String userId, ValueEventListener listener);

    void getUserDetail(String userId, ValueEventListener listener);

    void getCategory(ValueEventListener listener);

    void getProductType(ValueEventListener listener);

    void getProductList(long type,ValueEventListener listener);

    void logout();
}
