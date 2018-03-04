package bkhn.et.storemanage.ui.staffinfo.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.ui.staffinfo.IStaffInfoContract;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class StaffInfoPresenter<V extends IStaffInfoContract.IStaffInfoView> extends RxPresenter<V> implements IStaffInfoContract.IStaffInfoPresenter<V> {
    private static final String TAG = TAGG + StaffInfoPresenter.class.getSimpleName();
    IDataManager mDataManager;

    @Inject
    public StaffInfoPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadStaffDetail(String userId) {
        mDataManager.getUserDetail(userId, new USerDetailListener());
    }

    class USerDetailListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isNotNull(dataSnapshot) && isNotNull(mView)) {
                mView.updateStaffDetail(dataSnapshot.getValue(UserDetailModel.class));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
