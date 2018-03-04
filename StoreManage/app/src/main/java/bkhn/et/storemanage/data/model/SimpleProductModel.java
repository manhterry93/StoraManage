package bkhn.et.storemanage.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PL_itto on 3/4/2018.
 */

public class SimpleProductModel {
    String mModelId;
    List<String> mProductList;

    public SimpleProductModel(String modelId) {
        mModelId = modelId;
        mProductList = new ArrayList<>();
    }

    public SimpleProductModel(String modelId, String productId) {
        this(modelId);
        mProductList.add(productId);
    }

    public int getProductCount() {
        return mProductList.size();
    }

    public void addProduct(String productId) {
        if (!mProductList.contains(productId))
            mProductList.add(productId);
    }

    public String getModelId() {
        return mModelId;
    }

    public void setModelId(String modelId) {
        mModelId = modelId;
    }

    public List<String> getProductList() {
        return mProductList;
    }

    public void setProductList(List<String> productList) {
        mProductList = productList;
    }
}
