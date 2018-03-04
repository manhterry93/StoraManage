package bkhn.et.storemanage.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import bkhn.et.storemanage.base.UserCallback;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.di.ApplicationContext;
import bkhn.et.storemanage.ui.main.presenter.MainManagerPresenter;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */
@Singleton
public class RemoteProvider implements IRemoteProvider {
    private static final String TAG = TAGG + RemoteProvider.class.getSimpleName();

    @ApplicationContext
    Context mContext;

    FirebaseAuth mFirebaseAuth;

    @Inject
    public RemoteProvider(@ApplicationContext Context context) {
        mContext = context;
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void loginToServer(@NonNull String email, @NonNull String password, OnCompleteListener listener) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    @Override
    public void getUserPosition(String userId, ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + userId + "/positionId");
        myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void getUserDetail(String userId, ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + userId);
        myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void getCategory(ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Storage/Category");
        myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void getProductType(ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Storage/ProductId");
        myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void getProductList(long type, ValueEventListener listener) {
        Log.i(TAG, "getProductList: " + type);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Storage/Product");
        if (type != -1)
            myRef.orderByChild("Info/type").equalTo(type).addListenerForSingleValueEvent(listener);
        else
            myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void logout() {
        mFirebaseAuth.signOut();
    }

    @Override
    public void getModelInfo(String model, ValueEventListener listener) {
        Log.d(TAG, "getModelInfo: " + model);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Storage/Product/" + model + "/Info");
        myRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void createPayBill(List<ProductModel> dataList, String userId, String userName, OnCompleteListener listener) {
        Log.d(TAG, "createPayBill: ");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String reportLabel = database.getReference("Report/").push().getKey();
        DatabaseReference reportRef = database.getReference("Report/" + reportLabel);

        DatabaseReference productRef = reportRef.child("Product");
        for (ProductModel model : dataList) {
            String modelId = model.getModelId();
            DatabaseReference infoRef = productRef.child(modelId + "/Info");
            infoRef.setValue(model.getModelInfo());
            DatabaseReference listRef = productRef.child(modelId + "/List");
            listRef.setValue(model.getProductList());
        }

        reportRef.child("Time").setValue(Calendar.getInstance().getTime().getTime());
        reportRef.child("Type").setValue(AppConstant.Bill.BILL_PAY);
        DatabaseReference userRef = reportRef.child("Staff");
        userRef.child("id").setValue(userId);
        userRef.child("name").setValue(userName).addOnCompleteListener(listener);
    }

    @Override
    public void createImportBill(List<ProductModel> dataList, String userID, String userName, OnCompleteListener listener) {
        Log.d(TAG, "createImportBill: "+userID+" "+userName);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String reportLabel = database.getReference("Report/").push().getKey();
        DatabaseReference reportRef = database.getReference("Report/" + reportLabel);

        DatabaseReference productRef = reportRef.child("Product");
        for (ProductModel model : dataList) {
            String modelId = model.getModelId();
            DatabaseReference infoRef = productRef.child(modelId + "/Info");
            infoRef.setValue(model.getModelInfo());
            DatabaseReference listRef = productRef.child(modelId + "/List");
            listRef.setValue(model.getProductList());
        }

        reportRef.child("Time").setValue(Calendar.getInstance().getTime().getTime());
        reportRef.child("Type").setValue(AppConstant.Bill.BILL_IMPORT);
        DatabaseReference userRef = reportRef.child("Staff");
        userRef.child("id").setValue(userID);
        userRef.child("name").setValue(userName).addOnCompleteListener(listener);
    }

    @Override
    public void deleteProducts(List<ProductModel> dataList, OnCompleteListener listener) {
        Log.d(TAG, "deleteProducts: ");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference product = database.getReference("Storage/Product");
        for (final ProductModel model : dataList) {
            Log.d(TAG, "deleteProducts: " + model.getProductTitle());
            String modelId = model.getModelId();
            final DatabaseReference modelRef = product.child(modelId);
            for (String productId : model.getProductList()) {
                modelRef.child("List").orderByValue().equalTo(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.w(TAG, "onDataChange: " + data.getRef().toString());
                            data.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    @Override
    public void addProducts(List<ProductModel> dataList, OnCompleteListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference product = database.getReference("Storage/Product");
        for (ProductModel model : dataList) {
            String modelId = model.getModelId();
            DatabaseReference modelRef = product.child(modelId);
            for (String productId : model.getProductList())
                modelRef.child("List").push().setValue(productId);
        }
    }

    @Override
    public void getCurrentStaffs(@NonNull final UserCallback callback) {
        Log.d(TAG, "getCurrentStaffs: ");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Date currentTime = Calendar.getInstance().getTime();
        DatabaseReference userRef = database.getReference("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isNotNull(dataSnapshot))
                    return;
                Log.d(TAG, "data size: " + dataSnapshot.getChildrenCount());
                List<UserDetailModel> models = new ArrayList<>();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    UserDetailModel userModel = user.getValue(UserDetailModel.class);
                    Log.i(TAG, "UserPos: " + userModel.getPositionId());
                    if (userModel.getPositionId() != AppConstant.UserMode.USER_MANAGER) {
                        double startTime = userModel.getStartTime();
                        double endTime = userModel.getEndTime();
                        if (AppUtils.isValidSession(startTime, endTime, currentTime)) {
                            models.add(userModel);
                        }
                    }
                }
                if (models.size() > 0)
                    callback.onResult(models);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getAllStaffs(final UserCallback callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isNotNull(dataSnapshot))
                    return;
                Log.d(TAG, "data size: " + dataSnapshot.getChildrenCount());
                List<UserDetailModel> models = new ArrayList<>();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    UserDetailModel userModel = user.getValue(UserDetailModel.class);
                    Log.i(TAG, "UserPos: " + userModel.getPositionId());
                    if (userModel.getPositionId() != AppConstant.UserMode.USER_MANAGER) {
                        models.add(userModel);
                    }
                }
                if (models.size() > 0)
                    callback.onResult(models);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void getAllReports(ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportRef = database.getReference("Report");
        reportRef.addListenerForSingleValueEvent(listener);

    }

    @Override
    public void getReportInfo(String reportId, ValueEventListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reportRef = database.getReference("Report/"+reportId);
        reportRef.addListenerForSingleValueEvent(listener);
    }

}
