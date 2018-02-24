package bkhn.et.storemanage.ui.main;

import android.support.annotation.NonNull;

import java.util.List;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.CategoryModel;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.ProductType;
import bkhn.et.storemanage.data.model.UserDetailModel;

/**
 * Created by PL_itto on 2/14/2018.
 */

public interface IMainContract {
    interface IMainStaffView extends IBaseView {
        void openPayBillActivity();

        void openImportBillActivity();

        void updateUserDetail(@NonNull UserDetailModel model);

        void requestLogout();

        void updateCategoryList(List<CategoryModel> list);

        void updateProductTypeList(List<ProductType> list);

        void updateProductList(List<ProductModel> data);

        void openLoginActivity();
    }

    interface IMainManagerView extends IBaseView {
        void openStaffManageActivity();

        void openStatisticActivity();

        void requestLogout();

        void openLoginActivity();

    }

    interface IMainStaffPresenter<V extends IMainStaffView> extends IBasePresenter<V> {
        void loadData(String userId);

        void loadUserDetail(String userId);

        void loadCategory();

        void loadProductType();

        void loadProductList(long type);

        void logout();
    }

    interface IMainManagerPresenter<V extends IMainManagerView> extends IBasePresenter<V> {
        void loadData();

        void logout();
    }
}
