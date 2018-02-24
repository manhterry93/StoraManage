package bkhn.et.storemanage.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;

import bkhn.et.storemanage.data.remote.IRemoteProvider;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */
@Singleton
public class DataManager implements IDataManager {
    private static final String TAG = TAGG + DataManager.class.getSimpleName();

    IRemoteProvider mRemoteProvider;

    @Inject
    public DataManager(IRemoteProvider IRemoteProvider) {
        mRemoteProvider = IRemoteProvider;
    }

    @Override
    public FirebaseUser getRemoteCurrentUser() {
        if (isNotNull(mRemoteProvider)) {
            return mRemoteProvider.getCurrentUser();
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
        return null;
    }

    @Override
    public void loginToServer(@NonNull String email, @NonNull String password, OnCompleteListener listener) {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.loginToServer(email, password, listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void getUserPosition(String userId, ValueEventListener listener) {
        if (isNotNull(mRemoteProvider)) {
           mRemoteProvider.getUserPosition(userId,listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void getUserDetail(String userId, ValueEventListener listener) {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.getUserDetail(userId,listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void getCategory(ValueEventListener listener) {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.getCategory(listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void getProductType(ValueEventListener listener) {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.getProductType(listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void getProductList(long type, ValueEventListener listener) {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.getProductList(type,listener);
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

    @Override
    public void logout() {
        if (isNotNull(mRemoteProvider)) {
            mRemoteProvider.logout();
        } else {
            Log.e(TAG, "getRemoteCurrentUser failed:\n" + "RemoteProvider is not initialized");
        }
    }

}
