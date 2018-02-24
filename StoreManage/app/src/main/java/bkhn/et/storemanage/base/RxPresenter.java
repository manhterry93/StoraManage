package bkhn.et.storemanage.base;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class RxPresenter<V extends IBaseView> implements IBasePresenter<V> {
    protected V mView;
    @Inject
    CompositeDisposable mCompositeDisposable;

    private void subscribe(Disposable disposable) {
        if (!isNotNull(mCompositeDisposable))
            mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(disposable);
    }

    private void unSubscribe() {
        if (isNotNull(mCompositeDisposable))
            mCompositeDisposable.dispose();
    }

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
        unSubscribe();
    }
}
