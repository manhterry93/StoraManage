package bkhn.et.storemanage.ui.login.presenter;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.ui.login.ILoginContract.ILoginPresenter;
import bkhn.et.storemanage.ui.login.ILoginContract.ILoginView;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/13/2018.
 */

public class LoginPresenter<V extends ILoginView> extends RxPresenter<V> implements ILoginPresenter<V> {
    private static final String TAG = TAGG + LoginPresenter.class.getSimpleName();

    IDataManager mDataManager;

    @Inject
    public LoginPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loginToServer(@NonNull String email, @NonNull String password) {
        mDataManager.loginToServer(email, password, new LoginListener());
    }

    @Override
    public void checkUserState() {
        FirebaseUser user = mDataManager.getRemoteCurrentUser();
        if (isNotNull(user)) {
            String uid = user.getUid();
            mDataManager.getUserPosition(uid, new UserDetailListener(uid));
        }
    }

    class LoginListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                checkUserState();
            }
            mView.handleLoginResult(task.isSuccessful());
        }
    }

    class UserDetailListener implements ValueEventListener {
        String userId;

        public UserDetailListener(String userId) {
            this.userId = userId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long mode = (long) dataSnapshot.getValue();
            mView.openMainActivity(userId, (int) mode);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
