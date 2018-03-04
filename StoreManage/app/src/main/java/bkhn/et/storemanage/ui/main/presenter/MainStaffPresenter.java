package bkhn.et.storemanage.ui.main.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.CategoryModel;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.ProductType;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.ui.main.IMainContract.IMainStaffPresenter;
import bkhn.et.storemanage.ui.main.IMainContract.IMainStaffView;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/14/2018.
 */

public class MainStaffPresenter<V extends IMainStaffView> extends RxPresenter<V> implements IMainStaffPresenter<V> {
    private static final String TAG = TAGG + MainStaffPresenter.class.getSimpleName();

    IDataManager mDataManager;

    @Inject
    public MainStaffPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }


    @Override
    public void loadData(String userId) {
        loadUserDetail(userId);
        loadCategory();
        loadProductType();
    }


    @Override
    public void loadUserDetail(String userId) {
        mDataManager.getUserDetail(userId, new UserDetailListener(userId));
    }

    @Override
    public void loadCategory() {
        mDataManager.getCategory(new CategoryListener());
    }

    @Override
    public void loadProductType() {
        mDataManager.getProductType(new ProductTypeListener());
    }

    @Override
    public void loadProductList(long type) {
        Log.d(TAG, "loadProductList: " + type);
        mDataManager.getProductList(type, new ProductListListener());
    }

    @Override
    public void logout() {
        mDataManager.logout();
    }

    class UserDetailListener implements ValueEventListener {
        String userId;

        public UserDetailListener(String userId) {
            this.userId = userId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            UserDetailModel model = dataSnapshot.getValue(UserDetailModel.class);
            if (isNotNull(model) && isNotNull(mView)) {
                model.setUserId(userId);
                mView.updateUserDetail(model);
                Log.d(TAG, "onDataChange: " + model.toString());
            } else
                Log.e(TAG, "Data null");

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class CategoryListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!isNotNull(dataSnapshot)) return;
            List<CategoryModel> list = new ArrayList<>();
            int count = 0;
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                list.add(new CategoryModel(count++, postSnapshot.getValue(String.class)));
            }
            mView.updateCategoryList(list);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class ProductTypeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (!isNotNull(dataSnapshot)) return;
            List<ProductType> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                list.add(postSnapshot.getValue(ProductType.class));
            }

            mView.updateProductTypeList(list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    class ProductListListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
            if (isNotNull(dataSnapshot)) {
                Log.d(TAG, "data size: " + dataSnapshot.getChildrenCount());
                List<ProductModel> result = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    result.add(convertProductData(child));
                }
                if (result.size() > 0)
                    mView.updateProductList(result);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "onCancelled: " + databaseError.toString());
        }
    }

    /**
     * Convert from DataSnapshot to ProductModel
     * DataSnapshot will be at first child of Product Table in remoteDatabase
     *
     * @param dataSnapshot
     * @return
     */
    private static final ProductModel convertProductData(@NonNull DataSnapshot dataSnapshot) {
        DataSnapshot infoSnapShot = dataSnapshot.child("Info");
        ProductModel.ModelInfo modelInfo = infoSnapShot.getValue(ProductModel.ModelInfo.class);
        modelInfo.setId(infoSnapShot.getKey());
        if (isNotNull(modelInfo)) {
            ProductModel model = new ProductModel(modelInfo);
            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
            };
            List<String> productList = new ArrayList<>();
            for (DataSnapshot data : dataSnapshot.child("List").getChildren())
//                dataSnapshot.child("List").getValue(t);
                productList.add((String) data.getValue());


            if (isNotNull(productList))
                model.setProductList(productList);
            return model;
        }
        return null;
    }
}
