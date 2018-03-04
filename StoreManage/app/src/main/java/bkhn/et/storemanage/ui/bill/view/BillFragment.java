package bkhn.et.storemanage.ui.bill.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.BaseActivity;
import bkhn.et.storemanage.base.BaseFragment;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.SimpleProductModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.bill.IBillContract;
import bkhn.et.storemanage.ui.scan.view.ScannerActivity;
import bkhn.et.storemanage.util.AppConstant;
import bkhn.et.storemanage.util.AppUtils;
import bkhn.et.storemanage.util.UiUtils;
import bkhn.et.storemanege.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/2/2018.
 */

public class BillFragment extends BaseFragment implements IBillContract.IBillView {
    private static final String TAG = TAGG + BillFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bill_scan_img)
    ImageView mScanImg;
    @BindView(R.id.bill_product_list)
    RecyclerView mProductListView;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    IBillContract.IBillPresenter<IBillContract.IBillView> mPresenter;
    @Inject
    BaseActivity mActivity;

    private int mMode = -1;
    private ProductAdapter mProductAdapter;
    private String mUserId = null;
    private String mUserName = null;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_bill;
    }

    @Override
    protected void setupView() {
        setupActionBar();
        mProductListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mProductListView.setAdapter(mProductAdapter);
    }

    private void setupActionBar() {
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        switch (mMode) {
            case AppConstant.Bill.BILL_IMPORT:
                mActivity.getSupportActionBar().setTitle(R.string.bill_import);
                break;
            case AppConstant.Bill.BILL_PAY:
                mActivity.getSupportActionBar().setTitle(R.string.bill_pay);
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
        mPresenter.onAttach(this);
        setup();
    }

    private void setup() {
        mMode = mActivity.getIntent().getIntExtra(AppConstant.Bill.EXTRA_BILL_MODE, -1);
        mUserId = mActivity.getIntent().getStringExtra(AppConstant.Bill.EXTRA_USER_ID);
        mUserName = mActivity.getIntent().getStringExtra(AppConstant.Bill.EXTRA_USER_NAME);

        if (mMode == -1) {
            showMessage("Bill Mode is not defined!");
            mActivity.finish();
        }
        setHasOptionsMenu(true);
        mProductAdapter = new ProductAdapter();

    }

    @Override
    public void requestCompleteBill() {
        switch (mMode) {
            case AppConstant.Bill.BILL_PAY:
                if (mProductAdapter.getItemCount() > 0) {
                    Log.d(TAG, "requestCompleteBill: ");
                    mPresenter.processPayBill(mProductAdapter.getProductModels(), mProductAdapter.getModelList(), mUserId, mUserName);
                }
                return;
                case AppConstant.Bill.BILL_IMPORT:
                    if (mProductAdapter.getItemCount() > 0) {
                        Log.d(TAG, "requestCompleteBill: ");
                        mPresenter.processImportBill(mProductAdapter.getProductModels(), mProductAdapter.getModelList(), mUserId, mUserName);
                    }
                    return;
        }
    }

    @Override
    public void requestModelInfo(String modelId) {
        mPresenter.getProductInfo(modelId);
    }

    @OnClick(R.id.bill_scan_img)
    @Override
    public void openScanner() {
        Intent intent = new Intent(mActivity, ScannerActivity.class);
        startActivityForResult(intent, AppConstant.Bill.REQUEST_QR_CODE);
    }

    @Override
    public void updateModelInfo(ProductModel.ModelInfo info) {
        mProductAdapter.updateModelList(info);
    }

    @Override
    public void onRequestResult(boolean success) {
        if (!success) return;
        switch (mMode) {
            case AppConstant.Bill.BILL_PAY:
                showMessage(R.string.bill_pay_done);
                mActivity.finish();
                return;
            case AppConstant.Bill.BILL_IMPORT:
                showMessage(R.string.bill_import_done);
                mActivity.finish();
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppConstant.Bill.REQUEST_QR_CODE) {
            if (isNotNull(data)) {
                String content = data.getStringExtra(AppConstant.Scanner.CODE_RESULT);
                showMessage(content);
                if (AppUtils.isValidProductCode(content)) {
                    Log.i(TAG, "IsValidCode");
                    String[] temp = content.split("\\|");
                    mProductAdapter.updateProduct(temp[0], temp[1]);
                } else {
                    Log.e(TAG, "Is not a valid product Code");
                }
            }
        }
    }

    class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
        List<SimpleProductModel> mProductModels;
        LayoutInflater mInflater;
        LinkedHashMap<String, ProductModel.ModelInfo> mModelList;

        public ProductAdapter() {
            mProductModels = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
            mModelList = new LinkedHashMap<>();
        }

        public List<SimpleProductModel> getProductModels() {
            return mProductModels;
        }

        public LinkedHashMap<String, ProductModel.ModelInfo> getModelList() {
            return mModelList;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.bill_product_item, parent, false);
            return new ProductHolder(view);
        }

        public boolean hasModelInfo(String id) {
            return mModelList.containsKey(id);
        }

        public void updateModelList(ProductModel.ModelInfo info) {
            String id = info.getId();
            if (!mModelList.containsKey(id)) {
                mModelList.put(id, info);
                notifyDataSetChanged();
            }

        }

        public void updateProduct(String modelId, String productId) {
            Log.d(TAG, "updateProduct: " + modelId + " " + productId);
            if (!hasModelInfo(modelId)) {
                requestModelInfo(modelId);
            }
            for (SimpleProductModel model : mProductModels) {
                if (model.getModelId().equals(modelId)) {
                    model.addProduct(productId);
                    notifyDataSetChanged();
                    return;
                }
            }
            mProductModels.add(new SimpleProductModel(modelId, productId));
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(ProductHolder holder, int position) {
            holder.bindItem(getItemAt(position));
        }

        private SimpleProductModel getItemAt(int pos) {
            return mProductModels.get(pos);
        }

        @Override
        public int  getItemCount() {
            return mProductModels.size();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.product_icon)
            ImageView mIcon;
            @BindView(R.id.product_title)
            TextView mTitle;
            @BindView(R.id.product_cost)
            TextView mCost;
            @BindView(R.id.product_count)
            TextView mCount;

            public ProductHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(SimpleProductModel model) {
                mCount.setText(String.valueOf(model.getProductCount()));
                String modelId = model.getModelId();
                if (mModelList.containsKey(modelId)) {
                    ProductModel.ModelInfo modelInfo = mModelList.get(model.getModelId());
                    UiUtils.loadImageLink(mContext, modelInfo.getImage(), mIcon, R.drawable.ic_product_default, false);
                    mTitle.setText(modelInfo.getName());
                    mCost.setText(String.valueOf(modelInfo.getCost()));
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bill_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
            case R.id.bill_action_done:
                requestCompleteBill();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }
}
