package bkhn.et.storemanage.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PL_itto on 2/20/2018.
 */

public class ProductModel {

    List<String> mProductList;
    ModelInfo mModelInfo;

    public ProductModel(ModelInfo modelInfo) {
        mModelInfo = modelInfo;
        mProductList = new ArrayList<>();
    }

    public List<String> getProductList() {
        return mProductList;
    }

    public void setProductList(List<String> productList) {
        mProductList = productList;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        mModelInfo = modelInfo;
    }

    public String getProductId() {
        return mModelInfo.getId();
    }

    public void setProductId(String productId) {
        mModelInfo.setId(productId);
    }

    public String getProductTitle() {
        return mModelInfo.getName();
    }

    public void setProductTitle(String productTitle) {
        mModelInfo.setName(productTitle);
    }

    public long getProductType() {
        return mModelInfo.getType();
    }

    public void setProductType(long productType) {
        mModelInfo.setType(productType);
    }

    public long getProductCost() {
        return mModelInfo.getCost();
    }

    public void setProductCost(long productCost) {
        mModelInfo.setCost(productCost);
    }

    public String getProductImage() {
        return mModelInfo.getImage();
    }

    public void setProductImage(String productImage) {
        mModelInfo.setImage(productImage);
    }

    public int getProductCount() {
        return mProductList.size();
    }

    public static class ModelInfo {
        String id;
        String name;
        long type;
        long cost;
        String image;

        public ModelInfo() {

        }

        public ModelInfo(String productTitle, long productType, long productCost, String productImage) {
            this.name = productTitle;
            this.type = productType;
            this.cost = productCost;
            this.image = productImage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
        }

        public long getCost() {
            return cost;
        }

        public void setCost(long cost) {
            this.cost = cost;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
