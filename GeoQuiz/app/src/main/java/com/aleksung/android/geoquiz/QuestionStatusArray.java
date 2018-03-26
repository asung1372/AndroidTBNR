package com.aleksung.android.geoquiz;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionStatusArray implements Parcelable {
    private static final String TAG = "QuesitonStatusArray";

    private enum QuestionStatus {
        UNANSWERED, ANSWERED_CORRECTLY, ANSWERED_INCORRECTLY, CHEATED
    }

    private QuestionStatus[] mData;

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int[] data = new int[mData.length];
        for (int i = 0; i < mData.length; i++) {
            data[i] = mData[i].ordinal();
        }
        dest.writeIntArray(data);
    }

    public static final Parcelable.Creator<QuestionStatusArray> CREATOR
            = new Parcelable.Creator<QuestionStatusArray>() {
        public QuestionStatusArray createFromParcel(Parcel source) {
            return new QuestionStatusArray(source);
        }
        public QuestionStatusArray[] newArray(int size) { return new QuestionStatusArray[size];}
    };

    private QuestionStatusArray(Parcel in) {
        int[] data = new int[mData.length];
        in.readIntArray(data);
        for (int i = 0; i < mData.length; i++) {
            mData[i] = QuestionStatus.values()[data[i]];
        }
    }

    QuestionStatusArray(int size) {
        mData = new QuestionStatus[size];
        for (int i = 0; i < size; i++)
            mData[i] = QuestionStatus.UNANSWERED;
    }

    boolean unanswered(int index) { return mData[index] == QuestionStatus.UNANSWERED; }

    boolean answeredCorrectly(int index) { return mData[index] == QuestionStatus.ANSWERED_CORRECTLY; }

    boolean answeredIncorrectly(int index) { return mData[index] == QuestionStatus.ANSWERED_INCORRECTLY; }

    boolean cheated(int index) { return mData[index] == QuestionStatus.CHEATED; }

    void setUnanswered(int index) { mData[index] = QuestionStatus.UNANSWERED; }

    void setAnsweredCorrectly(int index) { mData[index] = QuestionStatus.ANSWERED_CORRECTLY; }

    void setAnsweredIncorrectly(int index) { mData[index] = QuestionStatus.ANSWERED_INCORRECTLY; }

    void setCheated(int index) { mData[index] = QuestionStatus.CHEATED; }
}
