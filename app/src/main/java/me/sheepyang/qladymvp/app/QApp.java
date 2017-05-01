package me.sheepyang.qladymvp.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.Utils;
import com.jess.arms.base.BaseApplication;
import com.socks.library.KLog;

import me.sheepyang.qladymvp.BuildConfig;

/**
 * Created by SheepYang on 2017/4/30 20:45.
 */

public class QApp extends BaseApplication {
    private boolean mLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        KLog.init(BuildConfig.LOG_DEBUG, "SheepYang");
    }

    public boolean isLogin() {
        return mLogin;
    }

    public void setLogin(boolean login) {
        this.mLogin = login;
    }

    public void toLogin(Context context) {
        if (context instanceof Activity) {
            //TODO
//            ((Activity) context).startActivityForResult(new Intent(context, LoginActivity.class), REQUEST_LOGIN);
        }
    }
}
