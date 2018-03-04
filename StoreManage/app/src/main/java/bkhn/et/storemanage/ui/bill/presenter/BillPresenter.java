package bkhn.et.storemanage.ui.bill.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.SimpleProductModel;
import bkhn.et.storemanage.ui.bill.IBillContract;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/3/2018.
 */

public class BillPresenter<V extends IBillContract.IBillView> extends RxPresenter<V>
        implements IBillContract.IBillPresenter<V> {
    private static final String TAG = TAGG + BillPresenter.class.getSimpleName();
    IDataManager mDataManager;

    @Inject
    public BillPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void getProductInfo(String modelId) {
        mDataManager.getModelInfo(modelId, new ModelInfoListener(modelId));
    }


    @Override
    public void processPayBill(List<SimpleProductModel> productList, LinkedHashMap<String, ProductModel.ModelInfo> modelInfo, String userId, String userName) {
        final List<ProductModel> data = prepareData(productList, modelInfo);
        mDataManager.createPayBill(data, userId, userName, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mView.onRequestResult(true);
                mDataManager.deleteProducts(data, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                    }
                });
            }
        });

    }

    @Override
    public void processImportBill(List<SimpleProductModel> productList, LinkedHashMap<String, ProductModel.ModelInfo> modelInfo, String userId, String userName) {
        final List<ProductModel> data = prepareData(productList, modelInfo);
        mDataManager.createImportBill(data, userId, userName, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mView.onRequestResult(true);
                mDataManager.addProducts(data, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                    }
                });
            }
        });
    }

    class ModelInfoListener implements ValueEventListener {
        String mModelId;

        public ModelInfoListener(String modelId) {
            this.mModelId = modelId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isNotNull(dataSnapshot)) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                ProductModel.ModelInfo info = dataSnapshot.getValue(ProductModel.ModelInfo.class);
                if (isNotNull(info)) {
                    info.setId(mModelId);
                    mView.updateModelInfo(info);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private List<ProductModel> prepareData(List<SimpleProductModel> productList, LinkedHashMap<String, ProductModel.ModelInfo> modelInfo) {
        List<ProductModel> modelList = new ArrayList<>();
        for (SimpleProductModel simpleProductModel : productList) {
            String modelId = simpleProductModel.getModelId();
            ProductModel.ModelInfo info = modelInfo.get(modelId);
            ProductModel model = new ProductModel(info);
            model.setProductList(simpleProductModel.getProductList());
            modelList.add(model);
        }
        return modelList;
    }
}
