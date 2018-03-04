package bkhn.et.storemanage.ui.reportmanage.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bkhn.et.storemanage.base.RxPresenter;
import bkhn.et.storemanage.data.IDataManager;
import bkhn.et.storemanage.data.model.SimpleReportModel;
import bkhn.et.storemanage.ui.reportmanage.IReportContract;

import static bkhn.et.storemanage.util.AppConstant.TAGG;
import static bkhn.et.storemanage.util.AppUtils.isNotNull;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class ReportPresenter<V extends IReportContract.IReportView> extends RxPresenter<V> implements IReportContract.IReportPresenter<V> {
    private static final String TAG = TAGG + ReportPresenter.class.getSimpleName();
    IDataManager mDataManager;

    @Inject
    public ReportPresenter(IDataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void loadAllReport() {
        mDataManager.getAllReports(new ReportListener());
    }

    class ReportListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (isNotNull(dataSnapshot)) {
                List<SimpleReportModel> mReportList = new ArrayList<>();
                for (DataSnapshot report : dataSnapshot.getChildren()) {
                    SimpleReportModel model = new SimpleReportModel();
                    model.setReportId(report.getKey());
                    model.setReportType((Long) report.child("Type").getValue());
                    model.setCreateTime((Long) report.child("Time").getValue());
                    model.setStaffName((String) report.child("/Staff/name").getValue());
                    mReportList.add(model);
                }
                if (mReportList.size() > 0 && isNotNull(mView))
                    mView.updateReportList(mReportList);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
