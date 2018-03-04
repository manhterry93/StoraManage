package bkhn.et.storemanage.ui.reportmanage.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.data.model.SimpleReportModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.reportinfo.view.ReportInfoActivity;
import bkhn.et.storemanage.ui.reportmanage.IReportContract;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class ReportFragment extends BaseFragment implements IReportContract.IReportView {
    private static final String TAG = TAGG + ReportFragment.class.getSimpleName();
    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.report_list_view)
    RecyclerView mReportListView;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    BaseActivity mActivity;
    @Inject
    IReportContract.IReportPresenter<IReportContract.IReportView> mPresenter;

    ReportListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
    }

    private void setup() {
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
        mAdapter = new ReportListAdapter();
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_report_manage;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadAllReport();
    }

    @Override
    protected void setupView() {
        setupActionBar();
        mReportListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mReportListView.setAdapter(mAdapter);
    }

    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateReportList(List<SimpleReportModel> list) {
        mAdapter.replaceData(list);
    }

    @Override
    public void openReportInfo(String reportId) {
        Intent intent = new Intent(mActivity, ReportInfoActivity.class);
        intent.putExtra(AppConstant.ReportInfo.EXTRA_REPORT_ID, reportId);
        startActivity(intent);
    }

    class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportHolder> {
        List<SimpleReportModel> mReportList;
        LayoutInflater mInflater;

        public ReportListAdapter() {
            mReportList = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
        }

        void replaceData(List<SimpleReportModel> data) {
            mReportList.clear();
            mReportList.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.report_list_item, parent, false);
            return new ReportHolder(view);
        }

        @Override
        public void onBindViewHolder(ReportHolder holder, int position) {
            holder.bindItem(getItemAt(position));
        }

        public SimpleReportModel getItemAt(int pos) {
            return mReportList.get(pos);
        }

        @Override
        public int getItemCount() {
            return mReportList.size();
        }

        class ReportHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.report_type_icon)
            ImageView mTypeIcon;
            @BindView(R.id.report_time)
            TextView mReportTime;
            @BindView(R.id.report_staff)
            TextView mStaff;

            public ReportHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
            @OnClick(R.id.report_item_root)
            void openReport(){
                String reportId=getItemAt(getAdapterPosition()).getReportId();
                openReportInfo(reportId);
            }

            void bindItem(SimpleReportModel model) {
                switch ((int) model.getReportType()) {
                    case AppConstant.Bill.BILL_IMPORT:
                        mTypeIcon.setImageResource(R.drawable.ic_import);
                        break;
                    case AppConstant.Bill.BILL_PAY:
                        mTypeIcon.setImageResource(R.drawable.ic_pay);
                        break;
                }
                mReportTime.setText(AppUtils.longToTime(model.getCreateTime()));
                mStaff.setText(model.getStaffName());

            }

        }
    }

}
