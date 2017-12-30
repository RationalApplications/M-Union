
package xyz.ratapp.munionagent.data.pojo;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse implements Serializable, Parcelable
{

    @SerializedName("result")
    @Expose
    private BitrixUser bitrixUser;
    public final static Parcelable.Creator<UserResponse> CREATOR = new Creator<UserResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        public UserResponse[] newArray(int size) {
            return (new UserResponse[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3483173174179396557L;

    protected UserResponse(Parcel in) {
        this.bitrixUser = ((BitrixUser) in.readValue((BitrixUser.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserResponse() {
    }

    /**
     * 
     * @param bitrixUser
     */
    public UserResponse(BitrixUser bitrixUser) {
        super();
        this.bitrixUser = bitrixUser;
    }

    public BitrixUser getBitrixUser() {
        return bitrixUser;
    }

    public void setBitrixUser(BitrixUser bitrixUser) {
        this.bitrixUser = bitrixUser;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bitrixUser);
    }

    public int describeContents() {
        return  0;
    }

}
