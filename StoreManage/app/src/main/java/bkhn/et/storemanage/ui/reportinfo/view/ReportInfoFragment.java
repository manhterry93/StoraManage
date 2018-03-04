package bkhn.et.storemanage.ui.reportinfo.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.ReportModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.reportinfo.IReportInfoContract;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanage.util.UiUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/5/2018.
 */

public class ReportInfoFragment extends BaseFragment implements IReportInfoContract.IReportInfoView {
    private static final String TAG = TAGG + ReportInfoFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.overview_time)
    TextView mOverViewTime;
    @BindView(R.id.overview_user)
    TextView mOverViewUser;
    @BindView(R.id.overview_type)
    TextView mOverViewType;
    @BindView(R.id.overview_cost)
    TextView mTotalCost;

    @BindView(R.id.report_list_view)
    RecyclerView mReportList;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    BaseActivity mActivity;
    @Inject
    IReportInfoContract.IReportInfoPresenter<IReportInfoContract.IReportInfoView> mPresenter;
    ReportListAdapter mAdapter;

    private String mReportId = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setup();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadReportInfo(mReportId);
    }

    private void setup() {
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
        mAdapter = new ReportListAdapter();
        mReportId = mActivity.getIntent().getStringExtra(AppConstant.ReportInfo.EXTRA_REPORT_ID);
        if (!isNotNull(mReportId)) {
            showMessage(R.string.report_invalid);
            mActivity.finish();
        }
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_report_info;
    }

    @Override
    protected void setupView() {
        setupActionBar();
        mReportList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mReportList.setAdapter(mAdapter);
    }

    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void updateReportInfo(ReportModel model) {
        updateOverView(model);
        mAdapter.replaceData(model.getModelList());
    }

    private void updateOverView(ReportModel model) {
        mOverViewUser.setText(model.getUserName());
        mOverViewTime.setText(AppUtils.longToTime(model.getTime()));
        long time = model.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mToolbar.setTitle(getString(R.string.report_info_label, dateFormat.format(new Date(time))));
        switch ((int) model.getReportType()) {
            case AppConstant.Bill.BILL_IMPORT:
                mOverViewType.setText(R.string.report_overview_import_bill);
                break;
            case AppConstant.Bill.BILL_PAY:
                mOverViewType.setText(R.string.report_overview_pay_bill);
                break;
        }
    }

    void updateTotalCost(double cost) {
        mTotalCost.setText(String.valueOf(cost));
    }

    class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportHolder> {
        List<ProductModel> mReportList;
        LayoutInflater mInflater;

        public ReportListAdapter() {
            mReportList = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
        }

        public void replaceData(List<ProductModel> modelList) {
            Log.d(TAG, "replaceData: " + modelList.size());
            mReportList.clear();
            mReportList.addAll(modelList);
            notifyDataSetChanged();
            updateTotalCost(getTotalCost());
        }

        double getTotalCost() {
            double result = 0;
            for (ProductModel model : mReportList) {
                result += model.getProductCost() * model.getProductCount();
            }
            return result;
        }

        @Override
        public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.bill_product_item, parent, false);
            return new ReportHolder(view);
        }

        ProductModel getItemAt(int pos) {
            return mReportList.get(pos);
        }

        @Override
        public void onBindViewHolder(ReportHolder holder, int position) {
            holder.bindItem(getItemAt(position));
        }

        @Override
        public int getItemCount() {
            return mReportList.size();
        }

        class ReportHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.product_icon)
            ImageView mIcon;
            @BindView(R.id.product_title)
            TextView mTitle;
            @BindView(R.id.product_cost)
            TextView mCost;
            @BindView(R.id.product_count)
            TextView mCount;

            public ReportHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(ProductModel model) {
                UiUtils.loadImageLink(mContext, model.getProductImage(), mIcon, R.drawable.ic_product_default, false);
                mTitle.setText(model.getProductTitle());
                mCost.setText(String.valueOf(model.getProductCost()));
                mCount.setText(String.valueOf(model.getProductCount()));
            }
        }
    }
}
