package bkhn.et.storemanage.di.module;

import android.content.Context;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.login.ILoginContract.ILoginPresenter;
import bkhn.et.storemanage.ui.login.ILoginContract.ILoginView;
import bkhn.et.storemanage.ui.login.presenter.LoginPresenter;
import bkhn.et.storemanage.ui.main.IMainContract;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerPresenter;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerView;
import bkhn.et.storemanage.ui.main.IMainContract.IMainStaffView;
import bkhn.et.storemanage.ui.main.presenter.MainManagerPresenter;
import bkhn.et.storemanage.ui.main.presenter.MainStaffPresenter;
import bkhn.et.storemanage.ui.splash.ISplashContract.ISplashPresenter;
import bkhn.et.storemanage.ui.splash.ISplashContract.ISplashView;
import bkhn.et.storemanage.ui.splash.presenter.SplashPresenter;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by PL_itto on 2/12/2018.
 */
@Module
public class FragmentModule {
    BaseFragment mFragment;

    public FragmentModule(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @ActivityContext
    public Context provideActivityContext() {
        return mFragment.getContext();
    }

    @Provides
    BaseActivity provideActivity() {
        return (BaseActivity) mFragment.getActivity();
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    ISplashPresenter<ISplashView> provideSplashPresenter(SplashPresenter<ISplashView> presenter) {
        return presenter;
    }

    @Provides
    ILoginPresenter<ILoginView> provideLoginPresenter(LoginPresenter<ILoginView> presenter) {
        return presenter;
    }

    @Provides
    IMainContract.IMainStaffPresenter<IMainStaffView> provideMainStaffPresenter(MainStaffPresenter<IMainStaffView> presenter) {
        return presenter;
    }

    @Provides
    IMainManagerPresenter<IMainManagerView> provideMainManagerPresenter(MainManagerPresenter<IMainManagerView> presenter) {
        return presenter;
    }
}
