package com.aleksung.android.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alek Sung on 3/8/2018.
 */

public class QuizBooleanArray implements Parcelable {
    private boolean[] mData;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(mData);
    }

    public static final Parcelable.Creator<QuizBooleanArray> CREATOR
            = new Parcelable.Creator<QuizBooleanArray>() {
        public QuizBooleanArray createFromParcel(Parcel source) {
            return new QuizBooleanArray(source);
        }
        public QuizBooleanArray[] newArray(int size) {
            return new QuizBooleanArray[size];
        }
    };

    private QuizBooleanArray(Parcel in) {
        in.readBooleanArray(mData);
    }

    public QuizBooleanArray(boolean[] data) {
        mData = data;
    }

    public boolean[] getData() {
        return mData;
    }
}
