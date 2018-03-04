package bkhn.et.storemanage.ui.main.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.data.model.CategoryModel;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.ProductType;
import bkhn.et.storemanage.data.model.UserDetailModel;
import bkhn.et.storemanage.di.ActivityContext;
import bkhn.et.storemanage.ui.bill.view.BillActivity;
import bkhn.et.storemanage.ui.login.view.LoginActivity;
import bkhn.et.storemanage.ui.main.IMainContract.IMainStaffPresenter;
import bkhn.et.storemanage.ui.main.IMainContract.IMainStaffView;
import bkhn.et.storemanage.util.AppConstant;
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

public class MainStaffFragment extends MainBaseFragment implements IMainStaffView {
    private static final String TAG = TAGG + MainStaffFragment.class.getSimpleName();

    /* Views */
    @BindView(R.id.main_staff_type_spin)
    Spinner mTypeSpin;
    @BindView(R.id.main_staff_category_spin)
    Spinner mCategorySpin;
    @BindView(R.id.main_staff_product_btn)
    Button mProductSerachBtn;

    /* Parameters */
    @Inject
    @ActivityContext
    Context mContext;
    @Inject
    IMainStaffPresenter<IMainStaffView> mPresenter;

    CategoryAdapter mCategoryAdapter;
    ProductTypeAdapter mTypeAdapter;
    List<ProductType> mProductTypes;
    boolean mCategoryLoaded = false;
    boolean mTypeLoaded = false;
    ContentAdapter mContentAdapter;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    protected void initInjection() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        if (isNotNull(mPresenter))
            mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setup() {
        super.setup();
        mPresenter.onAttach(this);
        mProgressDialog = new ProgressDialog(mContext) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
            }
        };
        mProductTypes = new ArrayList<>();
        mProgressDialog.setCancelable(true);
        mContentAdapter = new ContentAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mProgressDialog.show(mContext, null, getString(R.string.main_load_title), true);
        mPresenter.loadData(mUserId);
    }

    @Override
    protected int getNavigationLayoutId() {
        return R.menu.main_menu_staff;
    }


    @Override
    public void openPayBillActivity() {
        if (isNotNull(mUserId) && isNotNull(mUserName)) {
            Intent intent = new Intent(mActivity, BillActivity.class);
            intent.putExtra(AppConstant.Bill.EXTRA_BILL_MODE, AppConstant.Bill.BILL_PAY);
            intent.putExtra(AppConstant.Bill.EXTRA_USER_ID, mUserId);
            intent.putExtra(AppConstant.Bill.EXTRA_USER_NAME, mUserName);
            startActivity(intent);
        }

    }

    @Override
    protected void setupView() {
        super.setupView();
        setupSpinner();
        mMainListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMainListView.setAdapter(mContentAdapter);
        mUserLoginTime.setText(Calendar.getInstance().getTime().toString());
        mContentHeaderTitle.setText(R.string.main_header_staff_content);
    }

    private void setupSpinner() {
        mCategoryAdapter = new CategoryAdapter(mContext, R.layout.category_item);
        mTypeAdapter = new ProductTypeAdapter(mContext, R.layout.category_item);
        mCategorySpin.setAdapter(mCategoryAdapter);
        mTypeSpin.setAdapter(mTypeAdapter);
        mTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mTypeLoaded) {
                    filterProductType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void openImportBillActivity() {
        Intent intent = new Intent(mActivity, BillActivity.class);
        intent.putExtra(AppConstant.Bill.EXTRA_BILL_MODE, AppConstant.Bill.BILL_IMPORT);
        startActivity(intent);
    }

    @Override
    public void updateUserDetail(@NonNull UserDetailModel model) {
        mUserLoad = true;
        if (isAllLoaded())
            mProgressDialog.hide();
        //Header
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
    public void requestLogout() {
        mPresenter.logout();
        openLoginActivity();
    }

    @Override
    public void updateCategoryList(List<CategoryModel> list) {
        mCategoryAdapter.replaceData(list);
        mCategoryLoaded = true;
    }

    @Override
    public void updateProductTypeList(List<ProductType> list) {
        mProductTypes.clear();
        mProductTypes.addAll(list);
        if (mCategoryLoaded)
            filterProductType();
        mTypeLoaded = true;
    }

    @Override
    public void updateProductList(List<ProductModel> data) {
        mContentAdapter.replaceData(data);
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    @Override
    protected void setupNavigationView() {
        super.setupNavigationView();
        mMainNavigation.setNavigationItemSelectedListener(mNavItemListener);
    }

    NavigationView.OnNavigationItemSelectedListener mNavItemListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d(TAG, "onNavigationItemSelected: " + item.getItemId());
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            switch (item.getItemId()) {
                case R.id.nav_paybill:
                    openPayBillActivity();
                    return true;
                case R.id.nav_import:
                    openImportBillActivity();
                    return true;
                case R.id.nav_logout:
                    Log.d(TAG, "onNavigationItemSelected: " + "Logout");
                    requestLogout();
                    return true;
            }
            return false;
        }
    };

    class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {
        List<ProductModel> mDataList;
        LayoutInflater mInflater;

        public ContentAdapter() {
            mDataList = new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
        }

        public void replaceData(List<ProductModel> data) {
            mDataList.clear();
            mDataList.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData() {
            mDataList.clear();
            notifyDataSetChanged();
        }

        @Override
        public ContentAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.product_list_item, parent, false);
            return new ContentHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentAdapter.ContentHolder holder, int position) {
            holder.bindItem(getItem(position));
        }


        ProductModel getItem(int pos) {
            return mDataList.get(pos);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }


        class ContentHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.product_title)
            TextView mProductTitle;
            @BindView(R.id.product_cost)
            TextView mProductCost;
            @BindView(R.id.product_amount)
            TextView mProductAmount;
            @BindView(R.id.product_icon)
            ImageView mProductIcon;

            public ContentHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(ProductModel model) {
                UiUtils.loadImageLink(mContext, model.getProductImage(), mProductIcon, R.drawable.ic_product_default, false);
                mProductTitle.setText(model.getProductTitle());
                mProductCost.setText(getString(R.string.main_staff_cost, model.getProductCost()));
                mProductAmount.setText(getString(R.string.main_staff_amount, model.getProductCount()));
            }
        }
    }

    class CategoryAdapter extends ArrayAdapter<CategoryModel> {
        private List<CategoryModel> mDataList;
        private LayoutInflater mInflater;
        private Context mContext;

        public CategoryAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mDataList = new ArrayList<>();
        }

        @Nullable
        @Override
        public CategoryModel getItem(int position) {
            return mDataList.get(position);
        }

        private void addFirstItem() {
            mDataList.add(new CategoryModel(-1, getString(R.string.all)));
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getRowView(convertView, position);
        }

        public void replaceData(List<CategoryModel> list) {
            mDataList.clear();
            addFirstItem();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getRowView(convertView, position);
        }

        private View getRowView(View convertView, int position) {
            CategoryModel model = getItem(position);
            Holder holder;
            View rowView = convertView;
            if (rowView == null) {
                holder = new Holder();
                rowView = mInflater.inflate(R.layout.category_item, null);
                holder.mName = rowView.findViewById(R.id.spin_title);
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }
            holder.mName.setText(model.getName());

            return rowView;
        }

        private class Holder {
            TextView mName;
        }
    }

    class ProductTypeAdapter extends ArrayAdapter<ProductType> {
        private List<ProductType> mDataList;
        private LayoutInflater mInflater;
        private Context mContext;

        public ProductTypeAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mDataList = new ArrayList<>();
        }

        private void addFirstItem() {
            mDataList.add(new ProductType(-1, -1, getString(R.string.all)));
        }

        @Nullable
        @Override
        public ProductType getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getRowView(convertView, position);
        }

        public void replaceData(List<ProductType> list) {
            mDataList.clear();
            addFirstItem();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getRowView(convertView, position);
        }

        private View getRowView(View convertView, int position) {
            ProductType model = getItem(position);
            Holder holder;
            View rowView = convertView;
            if (rowView == null) {
                holder = new Holder();
                rowView = mInflater.inflate(R.layout.category_item, null);
                holder.mName = rowView.findViewById(R.id.spin_title);
                holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }
            holder.mName.setText(model.getTypeName());

            return rowView;
        }

        private class Holder {
            TextView mName;
        }

    }

    private void filterProductType() {
        Log.d(TAG, "filterProductType: " + mCategorySpin.getCount());
        CategoryModel category = (CategoryModel) mCategorySpin.getSelectedItem();
        if (!isNotNull(category)) return;
        long id = category.getId();
        Log.d(TAG, "filterProductType: " + id);
        List<ProductType> list = new ArrayList<>();
        if (id == -1) {
            mTypeAdapter.replaceData(mProductTypes);
            return;
        }
        for (ProductType type : mProductTypes) {
            if (type.getCategoryId() == id)
                list.add(type);
        }
        mTypeAdapter.replaceData(list);
    }

    @Override
    protected boolean isAllLoaded() {
        return mCategoryLoaded && mTypeLoaded;
    }

    @OnClick(R.id.main_staff_product_btn)
    void searchProduct() {
        if (isAllLoaded()) {
            if (mTypeAdapter.getCount() > 1) {
                long type = ((ProductType) mTypeSpin.getSelectedItem()).getTypeId();
                mPresenter.loadProductList(type);
            } else {
                mContentAdapter.clearData();
            }
        }
    }
}
