package bkhn.et.storemanage.ui.staffinfo.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.staffinfo.IStaffInfoContract;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanage.util.UiUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class StaffInfoFragment extends BaseFragment implements IStaffInfoContract.IStaffInfoView {
    private static final String TAG = TAGG + StaffInfoFragment.class.getSimpleName();
    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.staff_info_avatar)
    ImageView mAvatar;
    @BindView(R.id.staff_info_position)
    TextView mPosition;
    @BindView(R.id.staff_info_name)
    TextView mName;
    @BindView(R.id.staff_info_email)
    TextView mEmail;
    @BindView(R.id.staff_info_action_mail)
    TextView nActionMail;
    @BindView(R.id.staff_info_session)
    TextView mSession;
    @BindView(R.id.staff_info_phone)
    TextView mPhone;

    /* Parameters */
    @Inject
    BaseActivity mActivity;
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    IStaffInfoContract.IStaffInfoPresenter<IStaffInfoContract.IStaffInfoView> mPresenter;

    private String mUserId = null;
    private UserDetailModel mUserDetailModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
    }

    private void setup() {
        setHasOptionsMenu(true);
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
        mUserId = mActivity.getIntent().getStringExtra(AppConstant.StaffInfo.EXTRA_USER_ID);
        if (!isNotNull(mUserId)) {
            showMessage(R.string.staff_user_not_valid);
            mActivity.finish();
        }
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mActivity.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadStaffDetail(mUserId);
    }

    @Override
    public void updateStaffDetail(UserDetailModel model) {
        mUserDetailModel=model;
        mPosition.setText(model.getPosition());
        mName.setText(model.getName());
        mEmail.setText(model.getEmail());
        mPhone.setText(model.getPhone());
        UiUtils.loadImageLink(mContext, model.getProfileImage(), mAvatar, R.drawable.ic_avatar_default, true);
        mSession.setText(getString(R.string.staff_session_time, AppUtils.getTimeString(model.getStartTime()), AppUtils.getTimeString(model.getEndTime())));
    }

    @OnClick(R.id.staff_info_action_call)
    @Override
    public void requestCall() {
        if(!isNotNull(mUserDetailModel))
            return;
        String phoneNumber=mUserDetailModel.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @OnClick(R.id.staff_info_action_sms)
    @Override
    public void requestSms() {
        if(!isNotNull(mUserDetailModel))
            return;
        String phoneNumber=mUserDetailModel.getPhone();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + phoneNumber)));
    }

    @OnClick(R.id.staff_info_action_mail)
    @Override
    public void requestEmail() {
        if(!isNotNull(mUserDetailModel))
            return;
        String email=mUserDetailModel.getEmail();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.setType("message/rfc822");
        startActivity(intent);
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_staff_info;
    }

    @Override
    protected void setupView() {
        setupActionBar();
    }

    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setTitle(R.string.staff_info_title);
    }
}
