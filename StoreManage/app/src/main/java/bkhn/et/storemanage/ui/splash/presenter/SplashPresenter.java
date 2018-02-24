package bkhn.et.storemanage.ui.splash.presenter;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.ui.splash.ISplashContract;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class SplashPresenter<V extends ISplashContract.ISplashView> extends RxPresenter<V>
        implements ISplashContract.ISplashPresenter<V> {
    private static final String TAG = TAGG + SplashPresenter.class.getSimpleName();
    IDataManager mDataManager;

    @Inject
    public SplashPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void checkLoginState() {
        FirebaseUser user = mDataManager.getRemoteCurrentUser();
        if (isNotNull(user)) {
            String uid = user.getUid();
            mDataManager.getUserPosition(uid, new UserDetailListener(uid));
        } else {
            mView.openLoginActivity();
        }
    }

    class UserDetailListener implements ValueEventListener {
        String userId;

        public UserDetailListener(String userId) {
            this.userId = userId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long mode = (long) dataSnapshot.getValue();
            mView.openMainActivity(userId, (int) mode);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


}
