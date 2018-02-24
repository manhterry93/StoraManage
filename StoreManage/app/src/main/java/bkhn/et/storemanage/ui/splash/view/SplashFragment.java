package bkhn.et.storemanage.ui.splash.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.login.view.LoginActivity;
import bkhn.et.storemanage.ui.main.view.MainActivity;
import bkhn.et.storemanage.ui.splash.ISplashContract;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanege.R;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class SplashFragment extends BaseFragment implements ISplashContract.ISplashView {
    private static final String TAG = TAGG + SplashFragment.class.getSimpleName();

    /*Views*/


    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    ISplashContract.ISplashPresenter<ISplashContract.ISplashView> mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_splash;
    }

    @Override
    protected void setupView() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.checkLoginState();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void openMainActivity(String userId, int userMode) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(AppConstant.Main.EXTRA_USER_ID, userId);
        intent.putExtra(AppConstant.Main.EXTRA_USER_MODE, userMode);
        startActivity(intent);
    }

}
