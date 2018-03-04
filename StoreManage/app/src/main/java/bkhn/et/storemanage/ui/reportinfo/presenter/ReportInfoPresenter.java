package bkhn.et.storemanage.ui.reportinfo.presenter;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.ProductModel;
import bkhn.et.storemanage.data.model.ReportModel;
import bkhn.et.storemanage.ui.reportinfo.IReportInfoContract;
import bkhn.et.storemanage.ui.reportinfo.view.ReportInfoFragment;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/5/2018.
 */

public class ReportInfoPresenter<V extends IReportInfoContract.IReportInfoView> extends RxPresenter<V> implements IReportInfoContract.IReportInfoPresenter<V> {
    private static final String TAG = TAGG + ReportInfoFragment.class.getSimpleName();
    IDataManager mDataManager;

    @Inject
    public ReportInfoPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadReportInfo(String reportId) {
        mDataManager.getReportInfo(reportId, new ReportListener());
    }

    class ReportListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isNotNull(dataSnapshot)) {
                ReportModel reportModel = new ReportModel();
                reportModel.setId(dataSnapshot.getKey());
                reportModel.setReportType((Long) dataSnapshot.child("Type").getValue());
                reportModel.setTime((Long) dataSnapshot.child("Time").getValue());
                reportModel.setUserId((String) dataSnapshot.child("Staff/id").getValue());
                reportModel.setUserName(dataSnapshot.child("Staff/name").getValue(String.class));
                DataSnapshot productRef = dataSnapshot.child("Product");
                List<ProductModel> list = new ArrayList<>();
                Log.d(TAG, "ModeCount: " + productRef.getChildrenCount());
                for (DataSnapshot product : productRef.getChildren()) {
                    ProductModel.ModelInfo info = product.child("Info").getValue(ProductModel.ModelInfo.class);
                    ProductModel model = new ProductModel(info);
                    for (DataSnapshot productId : product.child("List").getChildren()) {
                        model.addProduct((String) productId.getValue());
                    }
                    list.add(model);
                }
                Log.d(TAG, "ModelList: "+list.size());
                reportModel.setModelList(list);
                if (isNotNull(mView))
                    mView.updateReportInfo(reportModel);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
