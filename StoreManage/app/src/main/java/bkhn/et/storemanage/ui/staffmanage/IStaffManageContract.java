package bkhn.et.storemanage.ui.staffmanage;

import java.util.List;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.UserDetailModel;

/**
 * Created by PL_itto on 3/4/2018.
 */

public interface IStaffManageContract {
    interface IStaffManageView extends IBaseView {
        void updateListStaff(List<UserDetailModel> data);

        void openStaffInfo(String userId);

        void requestCall(String phoneNumber);

        void requestSms(String phoneNumber);

        void searchStaff(String key);
    }

    interface IStaffManagePresenter<V extends IBaseView> extends IBasePresenter<V> {
        void loadAllStaffs();
    }
}
