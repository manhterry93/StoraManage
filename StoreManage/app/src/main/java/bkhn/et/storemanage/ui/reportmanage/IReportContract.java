package bkhn.et.storemanage.ui.reportmanage;

import java.util.List;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.SimpleReportModel;

/**
 * Created by PL_itto on 3/4/2018.
 */

public interface IReportContract {
    interface IReportView extends IBaseView {
        void updateReportList(List<SimpleReportModel> list);

        void openReportInfo(String reportId);
    }

    interface IReportPresenter<V extends IBaseView> extends IBasePresenter<V> {
        void loadAllReport();
    }
}
