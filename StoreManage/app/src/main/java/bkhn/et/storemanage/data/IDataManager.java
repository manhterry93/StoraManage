package bkhn.et.storemanage.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import bkhn.et.storemanage.base.UserCallback;
import bkhn.et.storemanage.data.model.ProductModel;

/**
 * Created by PL_itto on 2/12/2018.
 */

public interface IDataManager {
    FirebaseUser getRemoteCurrentUser();

    void loginToServer(@NonNull String email, @NonNull String password, OnCompleteListener listener);

    void getUserPosition(String userId, ValueEventListener listener);

    void getUserDetail(String userId, ValueEventListener listener);

    void getCategory(ValueEventListener listener);

    void getProductType(ValueEventListener listener);

    void getProductList(long type, ValueEventListener listener);

    void logout();

    void getModelInfo(String model, ValueEventListener listener);

    void createPayBill(List<ProductModel> dataList, String userID, String userName,OnCompleteListener listener);

    void createImportBill(List<ProductModel> dataList, String userID, String userName,OnCompleteListener listener);

    void deleteProducts(List<ProductModel> dataList, OnCompleteListener listener);

    void addProducts(List<ProductModel> dataList,OnCompleteListener listener);

    void getCurrentStaffs(UserCallback callback);

    void getAllStaffs(UserCallback callback);

    void getAllReports(ValueEventListener listener);

    void getReportInfo(String reportId,ValueEventListener listener);
}
