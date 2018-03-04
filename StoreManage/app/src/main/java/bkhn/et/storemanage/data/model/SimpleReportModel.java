package bkhn.et.storemanage.data.model;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class SimpleReportModel {
    long mReportType;
    long mCreateTime;
    String mStaffName;
    String mReportId;

    public long getReportType() {
        return mReportType;
    }

    public void setReportType(long reportType) {
        mReportType = reportType;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        mCreateTime = createTime;
    }

    public String getStaffName() {
        return mStaffName;
    }

    public void setStaffName(String staffName) {
        mStaffName = staffName;
    }

    public String getReportId() {
        return mReportId;
    }

    public void setReportId(String reportId) {
        mReportId = reportId;
    }
}
