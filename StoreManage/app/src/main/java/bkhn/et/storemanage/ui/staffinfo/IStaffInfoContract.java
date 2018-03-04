package bkhn.et.storemanage.ui.staffinfo;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.UserDetailModel;

/**
 * Created by PL_itto on 3/4/2018.
 */

public interface IStaffInfoContract {
    interface IStaffInfoView extends IBaseView {
        void updateStaffDetail(UserDetailModel model);

        void requestCall();

        void requestSms();

        void requestEmail();
    }

    interface IStaffInfoPresenter<V extends IStaffInfoView> extends IBasePresenter<V> {
        void loadStaffDetail(String userId);
    }
}
