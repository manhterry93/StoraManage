package bkhn.et.storemanage.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.login.view.LoginActivity;
import bkhn.et.storemanage.ui.main.IMainContract;
import bkhn.et.storemanage.ui.main.IMainContract.IMainManagerView;
import bkhn.et.storemanage.ui.reportmanage.view.ReportActivity;
import bkhn.et.storemanage.ui.staffmanage.view.StaffManageActivity;
import bkhn.et.storemanage.util.UiUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 2/14/2018.
 */

public class MainManagerFragment extends MainBaseFragment implements IMainManagerView {
    private static final String TAG = TAGG + MainManagerFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.main_staff_search_layout)
    LinearLayout mProductSearchLayout;
    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;

    private StaffListAdapter mAdapter;

    @Inject
    IMainContract.IMainManagerPresenter<IMainManagerView> mPresenter;

    @Override
    protected void setupView() {
        super.setupView();
        mContentHeaderTitle.setText(R.string.main_header_manager_content);
        mProductSearchLayout.setVisibility(View.GONE);
        mMainListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMainListView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadData(mUserId);
    }

    @Override
    protected void setup() {
        super.setup();
        mAdapter = new StaffListAdapter();
    }

    @Override
    protected void initInjection() {
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
    protected int getNavigationLayoutId() {
        return R.menu.main_menu_manager;
    }


    @Override
    protected void setupNavigationView() {
        super.setupNavigationView();
        mMainNavigation.setNavigationItemSelectedListener(mNavItemListener);
    }

    NavigationView.OnNavigationItemSelectedListener mNavItemListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            switch (item.getItemId()) {
                case R.id.nav_logout:
                    requestLogout();
                    return true;
                case R.id.nav_staff_manage:
                    openStaffManageActivity();
                    return true;
                case R.id.nav_statistic:
                    openStatisticActivity();
                    return true;

            }
            return false;
        }
    };

    @Override
    public void updateUserDetail(@NonNull UserDetailModel model) {
        mHeaderUserName.setText(model.getName());
        UiUtils.loadImageLink(mContext, model.getProfileImage(), mHeaderUserAvatar, R.drawable.ic_avatar_default, true);
        mHeaderUserPosition.setText(model.getPosition());

        //Content
        mUserName = model.getName();
        mUserNameTxt.setText(model.getName());
        mUserPosition.setText(model.getPosition());
        UiUtils.loadImageLink(mContext, model.getProfileImage(), mUserAvatar, R.drawable.ic_avatar_default, true);
        mUserPhone.setText(model.getPhone());
    }

    @Override
    public void openStaffManageActivity() {
        Intent intent = new Intent(mActivity, StaffManageActivity.class);
        startActivity(intent);
    }

    @Override
    public void openStatisticActivity() {
        Intent intent=new Intent(mActivity, ReportActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestLogout() {
        mPresenter.logout();
        openLoginActivity();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void callStaff(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void sendSmsStaff(String number) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + number)));
    }

    @Override
    public void updateListCurrentStaffs(List<UserDetailModel> list) {
        mAdapter.replaceData(list);
    }

    class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffHolder> {
        List<UserDetailModel> mStaffList;
        LayoutInflater mInflater;

        public StaffListAdapter() {
            mStaffList = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public StaffHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.user_list_item, parent, false);
            return new StaffHolder(view);
        }

        void replaceData(List<UserDetailModel> list) {
            mStaffList.clear();
            mStaffList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(StaffHolder holder, int position) {
            UserDetailModel model = getItemAt(position);
            holder.bindIem(model);
        }

        @Override
        public int getItemCount() {
            return mStaffList.size();
        }

        UserDetailModel getItemAt(int pos) {
            return mStaffList.get(pos);
        }

        class StaffHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.staff_avatar)
            ImageView mAvatar;
            @BindView(R.id.staff_name)
            TextView mName;
            @BindView(R.id.staff_tel)
            TextView mNumber;
            @BindView(R.id.staff_action_call)
            TextView mCallAction;
            @BindView(R.id.staff_action_sms)
            TextView mSmsAction;

            public StaffHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.staff_action_call)
            void call() {
                UserDetailModel model = getItemAt(getAdapterPosition());
                callStaff(model.getPhone());
            }

            @OnClick(R.id.staff_action_sms)
            void sms() {
                UserDetailModel model = getItemAt(getAdapterPosition());
                sendSmsStaff(model.getPhone());
            }

            void bindIem(UserDetailModel model) {
                UiUtils.loadImageLink(mContext, model.getProfileImage(), mAvatar, R.drawable.ic_avatar_default, true);
                mName.setText(model.getName());
                mNumber.setText(model.getPhone());
            }
        }
    }
}
