package bkhn.et.storemanage.data.model;

/**
 * Created by PL_itto on 2/20/2018.
 */

public class ProductType {
    long typeId;
    long categoryId;
    String typeName;
    public ProductType(){

    }
    public ProductType(long typeId, long categoryId, String typeName) {
        this.typeId = typeId;
        this.categoryId = categoryId;
        this.typeName = typeName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
