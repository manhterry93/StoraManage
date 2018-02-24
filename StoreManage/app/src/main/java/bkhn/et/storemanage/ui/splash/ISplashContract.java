package bkhn.et.storemanage.ui.splash;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;

/**
 * Created by PL_itto on 2/12/2018.
 */

public interface ISplashContract {
    interface ISplashView extends IBaseView {
        void openLoginActivity();

        void openMainActivity(@NonNull String userId, @NonNull int userMode);
    }

    interface ISplashPresenter<V extends ISplashView> extends IBasePresenter<V> {
        void checkLoginState();
    }
}
