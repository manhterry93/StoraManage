package bkhn.et.storemanage.ui.login;

import android.support.annotation.NonNull;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;

/**
 * Created by PL_itto on 2/12/2018.
 */

public interface ILoginContract {
    interface ILoginView extends IBaseView {
        void requestLogin();

        void handleLoginResult(boolean success);

        void openMainActivity(String userId,int userMode);
    }

    interface ILoginPresenter<V extends ILoginView> extends IBasePresenter<V> {
        void loginToServer(@NonNull String email, @NonNull String password);

        void checkUserState();
    }
}
