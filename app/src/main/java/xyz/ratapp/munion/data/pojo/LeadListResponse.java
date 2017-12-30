
package xyz.ratapp.munion.data.pojo;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeadListResponse implements Serializable, Parcelable
{

    @SerializedName("result")
    @Expose
    private List<Lead> leads = null;
    @SerializedName("next")
    @Expose
    private Integer next;
    @SerializedName("total")
    @Expose
    private Integer total;
    public final static Parcelable.Creator<LeadListResponse> CREATOR = new Creator<LeadListResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LeadListResponse createFromParcel(Parcel in) {
            return new LeadListResponse(in);
        }

        public LeadListResponse[] newArray(int size) {
            return (new LeadListResponse[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7755860733208023403L;

    protected LeadListResponse(Parcel in) {
        in.readList(this.leads, (Lead.class.getClassLoader()));
        this.next = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public LeadListResponse() {
    }

    /**
     * 
     * @param total
     * @param leads
     * @param next
     */
    public LeadListResponse(List<Lead> leads, Integer next, Integer total) {
        super();
        this.leads = leads;
        this.next = next;
        this.total = total;
    }

    public List<Lead> getLeads() {
        return leads;
    }

    public void setLeads(List<Lead> lead) {
        this.leads = lead;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(leads);
        dest.writeValue(next);
        dest.writeValue(total);
    }

    public int describeContents() {
        return  0;
    }

}
