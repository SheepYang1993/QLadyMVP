package me.sheepyang.qladymvp.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.autolayout.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.sheepyang.qladymvp.R;
import me.sheepyang.qladymvp.app.entity.ModelEntity;
import me.sheepyang.qladymvp.di.component.DaggerModelListComponent;
import me.sheepyang.qladymvp.di.module.ModelListModule;
import me.sheepyang.qladymvp.mvp.contract.ModelListContract;
import me.sheepyang.qladymvp.mvp.presenter.ModelListPresenter;
import me.sheepyang.qladymvp.mvp.ui.adapter.ModelDetailAdapter;

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
 * Created by SheepYang on 2017/5/1 16:37.
 */

public class ModelListFragment extends BaseFragment<ModelListPresenter> implements ModelListContract.View {
    private static final String PARAM_IS_SHOW_BANNAR = "param_is_show_bannar";
    private static final String PARAM_IS_IV_AVATAR_CLICKABLE = "param_is_iv_avatar_clickable";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    private ModelDetailAdapter mAdapter;
    private SinaRefreshView mHeadView;
    private boolean mIsShowBannar;
    private boolean mIsIvAvatarClickable = true;
    private List<ModelEntity> mData = new ArrayList<>();

    public static ModelListFragment newInstance(boolean isShowBannar, boolean isIvAvatarClickable) {
        ModelListFragment fragment = new ModelListFragment();
        Bundle args = new Bundle();
        args.putBoolean(PARAM_IS_SHOW_BANNAR, isShowBannar);
        args.putBoolean(PARAM_IS_IV_AVATAR_CLICKABLE, isIvAvatarClickable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsShowBannar = getArguments().getBoolean(PARAM_IS_SHOW_BANNAR, false);
            mIsIvAvatarClickable = getArguments().getBoolean(PARAM_IS_IV_AVATAR_CLICKABLE, true);
        }
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerModelListComponent
                .builder()
                .appComponent(appComponent)
                .modelListModule(new ModelListModule(this))//请将ModelListModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_refresh_rv, container, false);
    }

    @Override
    protected void initData() {
        initView();
        initListener();
    }

    private void initView() {
        mHeadView = new SinaRefreshView(mActivity);
        mHeadView.setArrowResource(R.drawable.ico_pink_arrow);
        mRefreshLayout.setHeaderView(mHeadView);
        mRefreshLayout.setBottomView(new LoadingView(mActivity));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mAdapter = new ModelDetailAdapter(mData);
        mAdapter.isFirstOnly(true);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);

        if (mIsShowBannar) {
            for (int i = 0; i < 5; i++) {
                mBannarList.add("http://img1.mm131.com/pic/2889/m.jpg");
            }

            View header = LayoutInflater.from(mActivity).inflate(R.layout.header_bannar, (ViewGroup) mRecyclerView.getParent(), false);
            mBannar = (Banner) header.findViewById(R.id.banner);
            mBannar.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth() / 3));
            mAdapter.addHeaderView(mBannar);
            //简单使用
            mBannar.setImages(mBannarList)
                    .setDelayTime(3000)
//                    .setBannerStyle(BannerConfig.NUM_INDICATOR)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(this)
                    .start();
        }
    }

    private void initListener() {
        if (mIsShowBannar) {
            mBannar.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (mApp.isLogin()) {
                        LogUtils.i("点击了banner:" + position);
                    } else {
                        mApp.toLogin(mActivity);
                    }
                }
            });
        }
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_avatar:
                        if (mIsIvAvatarClickable) {
                            startActivity(new Intent(mContext, ModelDetailActivity.class));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, ModelPhotoActivity.class));
            }
        });
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.clear();
                        for (int i = 0; i < 4; i++) {
                            ModelEntity entity = new ModelEntity();
                            entity.setImgPath("http://img1.mm131.com/pic/2889/m.jpg");
                            entity.setAvatarPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492624497119&di=298dc98d6977a37dab24f902d091ddd2&imgtype=0&src=http%3A%2F%2Fk2.jsqq.net%2Fuploads%2Fallimg%2F1702%2F7_170228144936_2.jpg");
                            mData.add(entity);
                        }
                        mAdapter.updata(mData);
                        mRefreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            ModelEntity entity = new ModelEntity();
                            entity.setLock(true);
                            entity.setImgPath("http://img1.mm131.com/pic/2889/m.jpg");
                            entity.setAvatarPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492624497119&di=298dc98d6977a37dab24f902d091ddd2&imgtype=0&src=http%3A%2F%2Fk2.jsqq.net%2Fuploads%2Fallimg%2F1702%2F7_170228144936_2.jpg");
                            mData.add(entity);
                        }
                        mAdapter.updata(mData);
                        mRefreshLayout.finishLoadmore();
                    }
                }, 1000);
            }
        });
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

}