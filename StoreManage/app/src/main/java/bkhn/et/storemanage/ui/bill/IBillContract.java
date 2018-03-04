package bkhn.et.storemanage.ui.bill;

import java.util.LinkedHashMap;
import java.util.List;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.SimpleProductModel;

/**
 * Created by PL_itto on 3/2/2018.
 */

public interface IBillContract {
    interface IBillView extends IBaseView {
        void requestCompleteBill();

        void requestModelInfo(String id);

        void openScanner();

        void updateModelInfo(ProductModel.ModelInfo info);

        void onRequestResult(boolean success);
    }

    interface IBillPresenter<V extends IBillView> extends IBasePresenter<V> {
        void getProductInfo(String modelId);

        void processPayBill(List<SimpleProductModel> productList, LinkedHashMap<String, ProductModel.ModelInfo> modelInfo,String userId,String userName);

        void processImportBill(List<SimpleProductModel> productList, LinkedHashMap<String, ProductModel.ModelInfo> modelInfo,String userId,String userName);
    }

}
