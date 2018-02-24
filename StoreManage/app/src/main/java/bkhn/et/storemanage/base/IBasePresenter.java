package bkhn.et.storemanage.base;

/**
 * Created by PL_itto on 11/21/2017.
 */

public interface IBasePresenter<V extends IBaseView> {
    void onAttach(V view);

    void onDetach();
}
