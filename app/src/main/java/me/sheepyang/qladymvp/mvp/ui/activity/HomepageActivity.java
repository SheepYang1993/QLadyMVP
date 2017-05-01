package me.sheepyang.qladymvp.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.sheepyang.qladymvp.R;
import me.sheepyang.qladymvp.app.QApp;
import me.sheepyang.qladymvp.di.component.DaggerHomepageComponent;
import me.sheepyang.qladymvp.di.module.HomepageModule;
import me.sheepyang.qladymvp.mvp.contract.HomepageContract;
import me.sheepyang.qladymvp.mvp.presenter.HomepagePresenter;
import me.sheepyang.qladymvp.mvp.ui.adapter.HomepageAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by SheepYang on 2017/5/1 11:26.
 */

public class HomepageActivity extends BaseActivity<HomepagePresenter> implements HomepageContract.View, View.OnClickListener {
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private long mCurrentTime;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerHomepageComponent
                .builder()
                .appComponent(appComponent)
                .homepageModule(new HomepageModule(this)) //请将HomepageModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_homepage, null, false);
    }

    @Override
    protected void initData() {
        initListener();
    }

    private void initListener() {
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public void setAdapter(List<Fragment> fragmentList, List<String> titleList) {
        mViewPager.setAdapter(new HomepageAdapter(getSupportFragmentManager(), fragmentList, titleList));
    }

    @Override
    public void setTabData(ArrayList<CustomTabEntity> tabEntityList) {
        mTabLayout.setTabData(tabEntityList);
    }

    @Override
    @OnClick({R.id.iv_search, R.id.iv_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mine:
                if (((QApp) mApp).isLogin()) {
                    //TODO
//                    startActivity(new Intent(this, MineActivity.class));
                } else {
                    ((QApp) mApp).toLogin(this);
                }
                break;
            case R.id.iv_search:
                //TODO
//                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mCurrentTime < 2000) {
            mCurrentTime = 0;
            UiUtils.exitApp();
        } else {
            mCurrentTime = System.currentTimeMillis();
            showMessage("再次点击退出APP");
        }
    }
}