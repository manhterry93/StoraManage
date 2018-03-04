package bkhn.et.storemanage.di.component;

import bkhn.et.storemanage.di.PerActivity;
import bkhn.et.storemanage.di.module.FragmentModule;
import bkhn.et.storemanage.ui.bill.view.BillFragment;
import bkhn.et.storemanage.ui.login.view.LoginFragment;
import bkhn.et.storemanage.ui.main.view.MainManagerFragment;
import bkhn.et.storemanage.ui.main.view.MainStaffFragment;
import bkhn.et.storemanage.ui.scan.view.ScannerFragment;
import bkhn.et.storemanage.ui.splash.view.SplashFragment;
import dagger.Component;

/**
 * Created by PL_itto on 2/12/2018.
 */
@PerActivity
@Component(modules = FragmentModule.class, dependencies = ApplicationComponent.class)
public interface FragmentComponent {
    void inject(SplashFragment fragment);

    void inject(LoginFragment fragment);

    void inject(MainStaffFragment fragment);

    void inject(MainManagerFragment fragment);

    void inject(BillFragment fragment);

    void inject(ScannerFragment fragment);
}
