package com.example.projetodontpadentrega2;

import android.os.Parcel;
import android.os.Parcelable;

public class TagContent implements Parcelable, Comparable<TagContent> {

    private String text;
    private String tag;
    private String email;
    private String image;
    public TagContent(String text, String tag, String email, String image) {
        this.text = text;
        this.tag = tag;
        this.email = email;
        this.image = image;
    }
    protected TagContent(Parcel in) {
        text = in.readString();
        tag = in.readString();
        email = in.readString();
        image = in.readString();
    }

    public static final Creator<TagContent> CREATOR = new Creator<TagContent>() {
        @Override
        public TagContent createFromParcel(Parcel in) {
            return new TagContent(in);
        }

        @Override
        public TagContent[] newArray(int size) {
            return new TagContent[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(TagContent o) {
        return this.tag.compareToIgnoreCase(o.tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(tag);
        dest.writeString(email);
        dest.writeString(image);
    }
}