package apollo.tianya.adapter;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Texel on 2016/6/1.
 */
public class ViewPageInfo implements Parcelable, Serializable {

    public final String tag;
    public final Class<?> refer;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Class<?> _refer, Bundle _args) {
        title = _title;
        tag = _tag;
        refer = _refer;
        args = _args;
    }

    public ViewPageInfo(Parcel in) {
        tag = in.readString();
        title = in.readString();
        refer = (Class<?>)in.readSerializable();
        args = in.readBundle();
    }

    public static final Creator<ViewPageInfo> CREATOR = new Creator<ViewPageInfo>() {
        @Override
        public ViewPageInfo createFromParcel(Parcel in) {
            return new ViewPageInfo(in);
        }

        @Override
        public ViewPageInfo[] newArray(int size) {
            return new ViewPageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(title);
        parcel.writeSerializable(refer);
        parcel.writeBundle(args);
    }
}
