package bkhn.et.storemanage.ui.reportinfo;

import bkhn.et.storemanage.base.IBasePresenter;
import bkhn.et.storemanage.base.IBaseView;
import bkhn.et.storemanage.data.model.ReportModel;

/**
 * Created by PL_itto on 3/5/2018.
 */

public interface IReportInfoContract {
    interface IReportInfoView extends IBaseView {
        void updateReportInfo(ReportModel model);
    }

    interface IReportInfoPresenter<V extends IBaseView> extends IBasePresenter<V> {
        void loadReportInfo(String reportId);
    }
}
