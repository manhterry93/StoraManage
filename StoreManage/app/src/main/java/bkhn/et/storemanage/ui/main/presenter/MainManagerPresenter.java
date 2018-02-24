package bkhn.et.storemanage.ui.main.presenter;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerPresenter;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerView;

/**
 * Created by PL_itto on 2/17/2018.
 */

public class MainManagerPresenter<V extends IMainManagerView> extends RxPresenter<V>
        implements IMainManagerPresenter<V> {
    IDataManager mDataManager;

    @Inject
    public MainManagerPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void logout() {
        mDataManager.logout();
    }
}
