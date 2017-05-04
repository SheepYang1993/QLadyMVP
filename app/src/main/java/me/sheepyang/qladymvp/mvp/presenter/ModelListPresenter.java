package me.sheepyang.qladymvp.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.imageloader.ImageLoader;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.sheepyang.qladymvp.app.entity.BannerModelListEntity;
import me.sheepyang.qladymvp.app.entity.ModelEntity;
import me.sheepyang.qladymvp.app.utils.RxUtils;
import me.sheepyang.qladymvp.mvp.contract.ModelListContract;
import me.sheepyang.qladymvp.mvp.ui.fragment.ModelListFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


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
public class ModelListPresenter extends BasePresenter<ModelListContract.Model, ModelListContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public ModelListPresenter(ModelListContract.Model model, ModelListContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public int requestDelayTime() {
        return mModel.getBannerDelayTime();
    }

    public List<String> requestBannarPlaceholderList() {
        return mModel.getBannarPlaceholderList();
    }

    public List<String> requestBannarList() {
        return mModel.getBannarList();
    }

    public void pullToRefresh(boolean isPullToRefresh) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(() -> {
            //request permission success, do something.
        }, mRootView.getRxPermissions(), mRootView, mErrorHandler);

        Observable<BannerModelListEntity> result =
                Observable.zip(
                        Observable.just(mModel.getBannarList()),
                        Observable.just(mModel.getModelList()), new Func2<List<String>, List<ModelEntity>, BannerModelListEntity>() {
                            @Override
                            public BannerModelListEntity call(List<String> strings, List<ModelEntity> modelEntities) {
                                return new BannerModelListEntity(strings, modelEntities);
                            }
                        }
                );

        result.subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (isPullToRefresh)
                        mRootView.hideLoading();//隐藏下拉加载更多的进度条
                    else
                        mRootView.hideLoadMore();//隐藏上拉刷新的进度条
                })
                .compose(RxUtils.bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new Subscriber<BannerModelListEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BannerModelListEntity entity) {
                        if (mRootView instanceof ModelListFragment) {
                            ((ModelListFragment) mRootView).setData(entity);
                        }
                    }
                });
    }
}