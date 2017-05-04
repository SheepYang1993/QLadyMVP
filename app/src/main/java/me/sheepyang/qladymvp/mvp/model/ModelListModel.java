package me.sheepyang.qladymvp.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.sheepyang.qladymvp.app.entity.ModelEntity;
import me.sheepyang.qladymvp.mvp.contract.ModelListContract;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by SheepYang on 2017/5/1 16:36.
 */

@ActivityScope
public class ModelListModel extends BaseModel implements ModelListContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public ModelListModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public List<ModelEntity> getModelList() {
        List<ModelEntity> modelList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ModelEntity entity = new ModelEntity();
            entity.setImgPath("http://img1.mm131.com/pic/2889/m.jpg");
            entity.setAvatarPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492624497119&di=298dc98d6977a37dab24f902d091ddd2&imgtype=0&src=http%3A%2F%2Fk2.jsqq.net%2Fuploads%2Fallimg%2F1702%2F7_170228144936_2.jpg");
            modelList.add(entity);
        }
        return modelList;
    }

    @Override
    public List<String> getBannarList() {
        List<String> bannarList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bannarList.add("http://www.tuigirl.com/Public/webupload/rouruan/tg_58b2f30c88086.jpg");
        }
        return bannarList;
    }

    @Override
    public List<String> getBannarPlaceholderList() {
        List<String> bannarList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bannarList.add("http://img1.mm131.com/pic/2889/m.jpg");
        }
        return bannarList;
    }

    @Override
    public int getBannerDelayTime() {
        return 3000;
    }
}