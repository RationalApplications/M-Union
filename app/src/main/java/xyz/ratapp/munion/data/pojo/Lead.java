
package xyz.ratapp.munion.data.pojo;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lead implements Serializable
{
    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("TITLE")
    @Expose
    private String title;
    @SerializedName("NAME")
    @Expose
    private String name;
    @SerializedName("SECOND_NAME")
    @Expose
    private String secondName;
    @SerializedName("LAST_NAME")
    @Expose
    private String lastName;
    @SerializedName("BIRTHDATE")
    @Expose
    private String birthday;
    @SerializedName("ASSIGNED_BY_ID")
    @Expose
    private int assignedById;
    @SerializedName("CREATED_BY_ID")
    @Expose
    private int createdByID;
    @SerializedName("DATE_CREATE")
    @Expose
    private String dateCreate;
    @SerializedName("UF_CRM_1514330608")
    @Expose
    private String password;
    @SerializedName("UF_CRM_1502183442")
    @Expose
    private int callsCount;
    @SerializedName("UF_CRM_1502183426")
    @Expose
    private int looksCount;
    @SerializedName("UF_CRM_1502809647")
    @Expose
    private List<Record> talksRecords = null;
    @SerializedName("PHONE")
    @Expose
    private List<Phone> phones = null;
    @SerializedName("PHOTO_URI")
    private String photoUri;
    @SerializedName("STATISTICS")
    private Statistics statistics;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getAssignedById() {
        return assignedById;
    }

    public int getCreatedByID() {
        return createdByID;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public List<Record> getTalksRecords() {
        return talksRecords;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public int getLooksCount() {
        return looksCount;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public class Record implements Serializable {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("downloadUrl")
        @Expose
        private String downloadUrl;

        public Record(String id, String downloadUrl) {
            this.id = id;
            this.downloadUrl = downloadUrl;
        }

        public String getId() {
            return id;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }

    public class Phone implements Serializable {
        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("VALUE")
        @Expose
        private String phone;

        public Phone(String id, String phone) {
            this.id = id;
            this.phone = phone;
        }

        public String getId() {
            return id;
        }

        public String getPhone() {
            return phone;
        }
    }

}
