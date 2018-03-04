package bkhn.et.storemanage.ui.main.presenter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.base.UserCallback;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerPresenter;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerView;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/17/2018.
 */

public class MainManagerPresenter<V extends IMainManagerView> extends RxPresenter<V>
        implements IMainManagerPresenter<V> {
    private static final String TAG = TAGG + MainManagerPresenter.class.getSimpleName();

    IDataManager mDataManager;

    @Inject
    public MainManagerPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadData(String userId) {
        loadUserDetail(userId);
        loadCurrentStaffs();
    }

    @Override
    public void loadCurrentStaffs() {
        mDataManager.getCurrentStaffs(new CurrentStaffCallback());
    }

    @Override
    public void loadUserDetail(String userId) {
        mDataManager.getUserDetail(userId, new UserDetailListener(userId));
    }

    @Override
    public void logout() {
        mDataManager.logout();
    }

    class UserDetailListener implements ValueEventListener {
        String userId;

        public UserDetailListener(String userId) {
            this.userId = userId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            UserDetailModel model = dataSnapshot.getValue(UserDetailModel.class);
            if (isNotNull(model) && isNotNull(mView)) {
//                model.setUserId(userId);
                mView.updateUserDetail(model);
                Log.d(TAG, "onDataChange: " + model.toString());
            } else
                Log.e(TAG, "Data null");

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class CurrentStaffCallback implements UserCallback {

        @Override
        public void onResult(List<UserDetailModel> data) {
            if (isNotNull(mView)) {
                mView.updateListCurrentStaffs(data);
            }
        }
    }

}
