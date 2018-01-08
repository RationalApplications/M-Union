
package xyz.ratapp.munion.data.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @SerializedName("COMMENTS")
    @Expose
    private String comments;
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
    @SerializedName("UF_CRM_1514422195")
    @Expose
    private String loyaltyCode;
    @SerializedName("UF_CRM_1514422511")
    @Expose
    private String money;
    @SerializedName("UF_CRM_1514422134")
    @Expose
    private Object invitedUsers;
    @SerializedName("PHONE")
    @Expose
    private List<Phone> phones = null;

    //My fields
    @SerializedName("PHOTO_URI")
    private String photoUri;
    @SerializedName("STATISTICS")
    private Statistics statistics;
    @SerializedName("FB_ENTITY")
    private String firebaseEntity;


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

    public String getComments() {
        return comments;
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

    public void setCallsCount(int callsCount) {
        this.callsCount = callsCount;
    }

    public void setLooksCount(int looksCount) {
        this.looksCount = looksCount;
    }

    public void setTalksRecords(List<Record> talksRecords) {
        //if zip unzip it
        this.talksRecords = talksRecords;
    }

    public String getFirebaseEntity() {
        return firebaseEntity;
    }

    public void setFirebaseEntity(String firebaseEntity) {
        this.firebaseEntity = firebaseEntity;
    }

    public String getLoyaltyCode() {
        return loyaltyCode;
    }

    public void setLoyaltyCode(String loyaltyCode) {
        if(loyaltyCode != null && !loyaltyCode.contains("-")) {
            StringBuilder builder = new StringBuilder();
            char[] chars = loyaltyCode.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if(i % 4 == 0 && i != 0) {
                    builder.append('-');
                }
                builder.append(chars[i]);
            }

            loyaltyCode = builder.toString();
        }

        this.loyaltyCode = loyaltyCode;
    }

    public String getMoney() {
        if (money == null) {
            money = "0|RUB";
        }

        return money.substring(0, money.indexOf('|'));
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<String> getInvitedUsers() {
        if(!(invitedUsers instanceof List)) {
            invitedUsers = new ArrayList<String>();
        }

        return (List<String>) invitedUsers;
    }

    public void setInvitedUsers(List<String> invitedUsers) {
        this.invitedUsers = invitedUsers;
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
