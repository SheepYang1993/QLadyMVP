package me.sheepyang.qladymvp.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ModelEntity implements Parcelable{
    private boolean isLock;
    private String avatarPath;
    private String imgPath;

    public ModelEntity() {

    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLock ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatarPath);
        dest.writeString(this.imgPath);
    }

    protected ModelEntity(Parcel in) {
        this.isLock = in.readByte() != 0;
        this.avatarPath = in.readString();
        this.imgPath = in.readString();
    }

    public static final Creator<ModelEntity> CREATOR = new Creator<ModelEntity>() {
        @Override
        public ModelEntity createFromParcel(Parcel source) {
            return new ModelEntity(source);
        }

        @Override
        public ModelEntity[] newArray(int size) {
            return new ModelEntity[size];
        }
    };
}
