package bkhn.et.storemanage.ui.staffmanage.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.staffinfo.view.StaffInfoActivity;
import bkhn.et.storemanage.ui.staffmanage.IStaffManageContract.IStaffManagePresenter;
import bkhn.et.storemanage.ui.staffmanage.IStaffManageContract.IStaffManageView;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.UiUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class StaffManageFragment extends BaseFragment implements IStaffManageView {
    private static final String TAG = TAGG + StaffManageFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.staff_search)
    EditText mStaffSearch;
    @BindView(R.id.staff_list)
    RecyclerView mStaffListView;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    IStaffManagePresenter<IStaffManageView> mPresenter;
    @Inject
    BaseActivity mActivity;

    private StaffListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
    }

    private void setup() {
        setHasOptionsMenu(true);
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
        mAdapter = new StaffListAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadAllStaffs();
    }

    @Override
    public void updateListStaff(List<UserDetailModel> data) {
        mAdapter.replaceData(data);
    }

    @Override
    public void openStaffInfo(String userId) {
        Intent intent = new Intent(mActivity, StaffInfoActivity.class);
        intent.putExtra(AppConstant.StaffInfo.EXTRA_USER_ID,userId);
        startActivity(intent);
    }

    @Override
    public void requestCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void requestSms(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + phoneNumber)));
    }

    @Override
    public void searchStaff(String key) {
        mAdapter.filter(key);
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_staff_manage;
    }

    @Override
    protected void setupView() {
        setupActionBar();
        mStaffListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mStaffListView.setAdapter(mAdapter);
        setupSearch();
    }

    private void setupSearch() {
        mStaffSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNotNull(mAdapter))
                    searchStaff(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mActivity.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffHolder> {
        List<UserDetailModel> mFilteredStaffList;
        List<UserDetailModel> mStaffList;
        LayoutInflater mInflater;

        public StaffListAdapter() {
            mFilteredStaffList = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
            mStaffList = new ArrayList<>();
        }

        @Override
        public StaffHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.user_list_item, parent, false);
            return new StaffHolder(view);
        }

        void replaceData(List<UserDetailModel> list) {
            mFilteredStaffList.clear();
            mFilteredStaffList.addAll(list);
            mStaffList.clear();
            mStaffList.addAll(list);
            notifyDataSetChanged();
        }

        void filter(String key) {
            key = key.toLowerCase();
            mFilteredStaffList.clear();
            for (UserDetailModel model : mStaffList) {
                String modelId = model.getUserId().toLowerCase();
                String name = model.getName().toLowerCase();
                String email = model.getEmail().toLowerCase();
                String tel = model.getPhone().toLowerCase();

                if (modelId.contains(key) || name.contains(key)
                        || email.contains(key) || tel.contains(key)) {
                    mFilteredStaffList.add(model);
                }

            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(StaffHolder holder, int position) {
            UserDetailModel model = getItemAt(position);
            holder.bindIem(model);
        }

        @Override
        public int getItemCount() {
            return mFilteredStaffList.size();
        }

        UserDetailModel getItemAt(int pos) {
            return mFilteredStaffList.get(pos);
        }

        class StaffHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.staff_layout)
            RelativeLayout mRootView;
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
                mRootView.setClickable(true);
            }

            @OnClick(R.id.staff_layout)
            void onItemClick() {
                UserDetailModel model = getItemAt(getAdapterPosition());
                openStaffInfo(model.getUserId());
            }

            @OnClick(R.id.staff_action_call)
            void call() {
                UserDetailModel model = getItemAt(getAdapterPosition());
                requestCall(model.getPhone());
            }

            @OnClick(R.id.staff_action_sms)
            void sms() {
                UserDetailModel model = getItemAt(getAdapterPosition());
                requestSms(model.getPhone());
            }


            void bindIem(UserDetailModel model) {
                UiUtils.loadImageLink(mContext, model.getProfileImage(), mAvatar, R.drawable.ic_avatar_default, true);
                mName.setText(model.getName());
                mNumber.setText(model.getPhone());
            }
        }
    }
}
