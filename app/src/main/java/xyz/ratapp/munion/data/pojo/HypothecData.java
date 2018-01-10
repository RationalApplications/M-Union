package xyz.ratapp.munion.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by timtim on 10/01/2018.
 */

public class HypothecData implements Serializable {

    @SerializedName("NAME")
    private String name;
    @SerializedName("PHONE")
    private String phone;
    @SerializedName("COMMENTS")
    private String comments;

    public HypothecData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
