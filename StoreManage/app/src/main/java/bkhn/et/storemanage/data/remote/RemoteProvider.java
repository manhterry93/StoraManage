package bkhn.et.storemanage.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;
import javax.inject.Singleton;

import bkhn.et.storemanage.di.ApplicationContext;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

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
}
