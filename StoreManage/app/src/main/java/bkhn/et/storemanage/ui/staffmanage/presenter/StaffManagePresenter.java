package bkhn.et.storemanage.ui.staffmanage.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.base.UserCallback;
import bkhn.et.storemanage.data.DataManager;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.ui.staffmanage.IStaffManageContract;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class StaffManagePresenter<V extends IStaffManageContract.IStaffManageView> extends RxPresenter<V> implements IStaffManageContract.IStaffManagePresenter<V> {
    private static final String TAG = TAGG + StaffManagePresenter.class.getSimpleName();

    IDataManager mDataManager;

    @Inject
    public StaffManagePresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadAllStaffs() {
        mDataManager.getAllStaffs(new StaffCallback());
    }

    class StaffCallback implements UserCallback {

        @Override
        public void onResult(@NonNull List<UserDetailModel> user) {
            if (isNotNull(mView)) {
                mView.updateListStaff(user);
            }
        }
    }
}
