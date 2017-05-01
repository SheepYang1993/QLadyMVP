package me.sheepyang.qladymvp.mvp.model;

import android.app.Application;
import android.support.v4.app.Fragment;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.sheepyang.qladymvp.R;
import me.sheepyang.qladymvp.app.entity.TabEntity;
import me.sheepyang.qladymvp.mvp.contract.HomepageContract;
import me.sheepyang.qladymvp.mvp.ui.fragment.BlankFragment;
import me.sheepyang.qladymvp.mvp.ui.fragment.ModelListFragment;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by SheepYang on 2017/5/1 11:24.
 */

@ActivityScope
public class HomepageModel extends BaseModel implements HomepageContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HomepageModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
    public List<String> getTabTitleList() {
        List<String> titleList = new ArrayList<>();
        titleList.add("最新");
        titleList.add("分类");
        titleList.add("最热");
        return titleList;
    }

    @Override
    public ArrayList<CustomTabEntity> getTabEntityList(List<String> tabTitleList) {
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        for (String tabTitle :
                tabTitleList) {
            tabEntityList.add(new TabEntity(tabTitle, 0, 0));
        }
        return tabEntityList;
    }

    @Override
    public List<Fragment> getTabFragmentList() {
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(ModelListFragment.newInstance(true, true));
//        mFragmentList.add(SortFragment.newInstance("分类"));
//        mFragmentList.add(ModelListFragment.newInstance(false, true));
//        mFragmentList.add(BlankFragment.newInstance("", ""));
        mFragmentList.add(BlankFragment.newInstance("", ""));
        mFragmentList.add(BlankFragment.newInstance("", ""));
        return mFragmentList;
    }
}