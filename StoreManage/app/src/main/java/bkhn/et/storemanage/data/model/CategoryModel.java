package bkhn.et.storemanage.data.model;

/**
 * Created by PL_itto on 2/20/2018.
 */

public class CategoryModel {
    long id;
    String name;

    public CategoryModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
