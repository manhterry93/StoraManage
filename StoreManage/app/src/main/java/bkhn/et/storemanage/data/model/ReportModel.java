package bkhn.et.storemanage.data.model;

import java.util.List;

/**
 * Created by PL_itto on 3/5/2018.
 */

public class ReportModel {
    String mId;
    long mTime;
    List<ProductModel> mModelList;
    String mUserId;
    String mUserName;
    long mReportType;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public List<ProductModel> getModelList() {
        return mModelList;
    }

    public void setModelList(List<ProductModel> modelList) {
        mModelList = modelList;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getModelCount() {
        return mModelList.size();
    }

    public ProductModel getModelAt(int pos) {
        return mModelList.get(pos);
    }

    public long getReportType() {
        return mReportType;
    }

    public void setReportType(long reportType) {
        mReportType = reportType;
    }
}
