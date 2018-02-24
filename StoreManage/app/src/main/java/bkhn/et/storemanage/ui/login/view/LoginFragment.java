package bkhn.et.storemanage.ui.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.login.ILoginContract;
import bkhn.et.storemanage.ui.main.view.MainActivity;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class LoginFragment extends BaseFragment implements ILoginContract.ILoginView {
    private static final String TAG = TAGG + LoginFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.login_email_edit)
    EditText mEmailField;
    @BindView(R.id.login_password_edit)
    EditText mPasswordField;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_progress)
    ProgressBar mProgressBar;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    ILoginContract.ILoginPresenter<ILoginContract.ILoginView> mPresenter;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_login;
    }

    @Override
    protected void setupView() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.login_btn)
    @Override
    public void requestLogin() {
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showMessage(R.string.login_email_null);
            return;
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showMessage(R.string.login_password_null);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.loginToServer(email, password);
    }

    @Override
    public void handleLoginResult(boolean success) {
        if (!success) {
            mProgressBar.setVisibility(View.INVISIBLE);
            showMessage(R.string.login_failed);
        }
    }

    @Override
    public void openMainActivity(String userId, int userMode) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(AppConstant.Main.EXTRA_USER_ID, userId);
        intent.putExtra(AppConstant.Main.EXTRA_USER_MODE, userMode);
        startActivity(intent);
    }

}
