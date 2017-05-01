package me.sheepyang.qladymvp.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.sheepyang.qladymvp.R;
import me.sheepyang.qladymvp.app.entity.ModelEntity;
import me.sheepyang.qladymvp.app.utils.glide.GlideCircleTransform;


/**
 * Created by Administrator on 2017/4/19.
 */

public class ModelDetailAdapter extends BaseQuickAdapter<ModelEntity, BaseViewHolder> {
    private int mScreenWidth;
    private LinearLayout.LayoutParams mParams;

    public ModelDetailAdapter(List<ModelEntity> data) {
        super(R.layout.item_model_detail, data);
        mScreenWidth = com.blankj.utilcode.util.ScreenUtils.getScreenWidth();
        mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mScreenWidth);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ModelEntity item) {
        helper.getView(R.id.iv_desc).setLayoutParams(mParams);

        //加载头像
        Glide.with(mContext)
                .load(item.getAvatarPath())
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.drawable.anim_loading_view)
                .into((ImageView) helper.getView(R.id.iv_avatar));
        if (item.isLock()) {
            //图片已上锁，模糊图片
            Glide.with(mContext)
                    .load(item.getImgPath())
                    .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 10, 8))
                    .into((ImageView) helper.getView(R.id.iv_desc));
        } else {
            //加载封面
            Glide.with(mContext)
                    .load(item.getImgPath())
                    .centerCrop()
                    .into((ImageView) helper.getView(R.id.iv_desc));
        }

        helper.addOnClickListener(R.id.iv_avatar);
    }

    public void updata(List<ModelEntity> data) {
        mData = data;
        notifyDataSetChanged();
    }
}
