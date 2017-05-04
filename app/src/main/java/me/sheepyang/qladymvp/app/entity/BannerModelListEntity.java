package me.sheepyang.qladymvp.app.entity;

import java.util.List;

/**
 * Created by SheepYang on 2017/5/4.
 */

public class BannerModelListEntity {
    private List<String> bannerList;
    private List<ModelEntity> modelEntityList;

    public BannerModelListEntity(List<String> bannerList, List<ModelEntity> modelEntityList) {
        this.bannerList = bannerList;
        this.modelEntityList = modelEntityList;
    }

    public List<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<String> bannerList) {
        this.bannerList = bannerList;
    }

    public List<ModelEntity> getModelEntityList() {
        return modelEntityList;
    }

    public void setModelEntityList(List<ModelEntity> modelEntityList) {
        this.modelEntityList = modelEntityList;
    }
}
