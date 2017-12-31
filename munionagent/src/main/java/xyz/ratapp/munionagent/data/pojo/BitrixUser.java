
package xyz.ratapp.munionagent.data.pojo;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BitrixUser implements Serializable, Parcelable
{

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("ACTIVE")
    @Expose
    private Boolean aCTIVE;
    @SerializedName("EMAIL")
    @Expose
    private String eMAIL;
    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("LAST_NAME")
    @Expose
    private String lAST_NAME;
    @SerializedName("SECOND_NAME")
    @Expose
    private String sECOND_NAME;
    @SerializedName("PERSONAL_GENDER")
    @Expose
    private String pERSONAL_GENDER;
    @SerializedName("PERSONAL_PROFESSION")
    @Expose
    private Object pERSONAL_PROFESSION;
    @SerializedName("PERSONAL_WWW")
    @Expose
    private String pERSONAL_WWW;
    @SerializedName("PERSONAL_BIRTHDAY")
    @Expose
    private String pERSONAL_BIRTHDAY;
    @SerializedName("PERSONAL_PHOTO")
    @Expose
    private Object pERSONAL_PHOTO;
    @SerializedName("PERSONAL_ICQ")
    @Expose
    private Object pERSONAL_ICQ;
    @SerializedName("PERSONAL_PHONE")
    @Expose
    private Object pERSONAL_PHONE;
    @SerializedName("PERSONAL_FAX")
    @Expose
    private Object pERSONAL_FAX;
    @SerializedName("PERSONAL_MOBILE")
    @Expose
    private String pERSONAL_MOBILE;
    @SerializedName("PERSONAL_PAGER")
    @Expose
    private Object pERSONAL_PAGER;
    @SerializedName("PERSONAL_STREET")
    @Expose
    private Object pERSONAL_STREET;
    @SerializedName("PERSONAL_CITY")
    @Expose
    private String pERSONAL_CITY;
    @SerializedName("PERSONAL_STATE")
    @Expose
    private Object pERSONAL_STATE;
    @SerializedName("PERSONAL_ZIP")
    @Expose
    private Object pERSONAL_ZIP;
    @SerializedName("PERSONAL_COUNTRY")
    @Expose
    private Object pERSONAL_COUNTRY;
    @SerializedName("WORK_COMPANY")
    @Expose
    private Object wORK_COMPANY;
    @SerializedName("WORK_POSITION")
    @Expose
    private String wORK_POSITION;
    @SerializedName("WORK_PHONE")
    @Expose
    private String wORK_PHONE;
    @SerializedName("UF_DEPARTMENT")
    @Expose
    private List<Integer> uF_DEPARTMENT = null;
    @SerializedName("UF_INTERESTS")
    @Expose
    private Object uF_INTERESTS;
    @SerializedName("UF_SKILLS")
    @Expose
    private Object uF_SKILLS;
    @SerializedName("UF_WEB_SITES")
    @Expose
    private Object uF_WEB_SITES;
    @SerializedName("UF_XING")
    @Expose
    private Object uF_XING;
    @SerializedName("UF_LINKEDIN")
    @Expose
    private Object uF_LINKEDIN;
    @SerializedName("UF_FACEBOOK")
    @Expose
    private Object uF_FACEBOOK;
    @SerializedName("UF_TWITTER")
    @Expose
    private Object uF_TWITTER;
    @SerializedName("UF_SKYPE")
    @Expose
    private Object uF_SKYPE;
    @SerializedName("UF_DISTRICT")
    @Expose
    private Object uF_DISTRICT;
    @SerializedName("UF_PHONE_INNER")
    @Expose
    private Object uF_PHONE_INNER;
    public final static Parcelable.Creator<BitrixUser> CREATOR = new Creator<BitrixUser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BitrixUser createFromParcel(Parcel in) {
            return new BitrixUser(in);
        }

        public BitrixUser[] newArray(int size) {
            return (new BitrixUser[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8163358539325379738L;

    protected BitrixUser(Parcel in) {
        this.iD = ((int) in.readValue((int.class.getClassLoader())));
        this.aCTIVE = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.eMAIL = ((String) in.readValue((String.class.getClassLoader())));
        this.nAME = ((String) in.readValue((String.class.getClassLoader())));
        this.lAST_NAME = ((String) in.readValue((String.class.getClassLoader())));
        this.sECOND_NAME = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_GENDER = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_PROFESSION = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_WWW = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_BIRTHDAY = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_PHOTO = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_ICQ = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_PHONE = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_FAX = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_MOBILE = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_PAGER = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_STREET = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_CITY = ((String) in.readValue((String.class.getClassLoader())));
        this.pERSONAL_STATE = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_ZIP = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pERSONAL_COUNTRY = ((Object) in.readValue((Object.class.getClassLoader())));
        this.wORK_COMPANY = ((Object) in.readValue((Object.class.getClassLoader())));
        this.wORK_POSITION = ((String) in.readValue((String.class.getClassLoader())));
        this.wORK_PHONE = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.uF_DEPARTMENT, (java.lang.Integer.class.getClassLoader()));
        this.uF_INTERESTS = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_SKILLS = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_WEB_SITES = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_XING = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_LINKEDIN = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_FACEBOOK = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_TWITTER = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_SKYPE = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_DISTRICT = ((Object) in.readValue((Object.class.getClassLoader())));
        this.uF_PHONE_INNER = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public BitrixUser() {
    }

    /**
     * 
     * @param pERSONAL_PHOTO
     * @param aCTIVE
     * @param uF_SKYPE
     * @param uF_DEPARTMENT
     * @param pERSONAL_PHONE
     * @param eMAIL
     * @param pERSONAL_STREET
     * @param pERSONAL_MOBILE
     * @param uF_FACEBOOK
     * @param uF_TWITTER
     * @param wORK_POSITION
     * @param uF_SKILLS
     * @param pERSONAL_ICQ
     * @param pERSONAL_BIRTHDAY
     * @param pERSONAL_STATE
     * @param wORK_COMPANY
     * @param pERSONAL_WWW
     * @param pERSONAL_GENDER
     * @param wORK_PHONE
     * @param pERSONAL_COUNTRY
     * @param uF_DISTRICT
     * @param uF_INTERESTS
     * @param nAME
     * @param pERSONAL_PAGER
     * @param uF_LINKEDIN
     * @param uF_XING
     * @param pERSONAL_ZIP
     * @param iD
     * @param sECOND_NAME
     * @param pERSONAL_FAX
     * @param pERSONAL_CITY
     * @param pERSONAL_PROFESSION
     * @param uF_PHONE_INNER
     * @param lAST_NAME
     * @param uF_WEB_SITES
     */
    public BitrixUser(int iD, Boolean aCTIVE, String eMAIL, String nAME, String lAST_NAME, String sECOND_NAME, String pERSONAL_GENDER, Object pERSONAL_PROFESSION, String pERSONAL_WWW, String pERSONAL_BIRTHDAY, Object pERSONAL_PHOTO, Object pERSONAL_ICQ, Object pERSONAL_PHONE, Object pERSONAL_FAX, String pERSONAL_MOBILE, Object pERSONAL_PAGER, Object pERSONAL_STREET, String pERSONAL_CITY, Object pERSONAL_STATE, Object pERSONAL_ZIP, Object pERSONAL_COUNTRY, Object wORK_COMPANY, String wORK_POSITION, String wORK_PHONE, List<Integer> uF_DEPARTMENT, Object uF_INTERESTS, Object uF_SKILLS, Object uF_WEB_SITES, Object uF_XING, Object uF_LINKEDIN, Object uF_FACEBOOK, Object uF_TWITTER, Object uF_SKYPE, Object uF_DISTRICT, Object uF_PHONE_INNER) {
        super();
        this.iD = iD;
        this.aCTIVE = aCTIVE;
        this.eMAIL = eMAIL;
        this.nAME = nAME;
        this.lAST_NAME = lAST_NAME;
        this.sECOND_NAME = sECOND_NAME;
        this.pERSONAL_GENDER = pERSONAL_GENDER;
        this.pERSONAL_PROFESSION = pERSONAL_PROFESSION;
        this.pERSONAL_WWW = pERSONAL_WWW;
        this.pERSONAL_BIRTHDAY = pERSONAL_BIRTHDAY;
        this.pERSONAL_PHOTO = pERSONAL_PHOTO;
        this.pERSONAL_ICQ = pERSONAL_ICQ;
        this.pERSONAL_PHONE = pERSONAL_PHONE;
        this.pERSONAL_FAX = pERSONAL_FAX;
        this.pERSONAL_MOBILE = pERSONAL_MOBILE;
        this.pERSONAL_PAGER = pERSONAL_PAGER;
        this.pERSONAL_STREET = pERSONAL_STREET;
        this.pERSONAL_CITY = pERSONAL_CITY;
        this.pERSONAL_STATE = pERSONAL_STATE;
        this.pERSONAL_ZIP = pERSONAL_ZIP;
        this.pERSONAL_COUNTRY = pERSONAL_COUNTRY;
        this.wORK_COMPANY = wORK_COMPANY;
        this.wORK_POSITION = wORK_POSITION;
        this.wORK_PHONE = wORK_PHONE;
        this.uF_DEPARTMENT = uF_DEPARTMENT;
        this.uF_INTERESTS = uF_INTERESTS;
        this.uF_SKILLS = uF_SKILLS;
        this.uF_WEB_SITES = uF_WEB_SITES;
        this.uF_XING = uF_XING;
        this.uF_LINKEDIN = uF_LINKEDIN;
        this.uF_FACEBOOK = uF_FACEBOOK;
        this.uF_TWITTER = uF_TWITTER;
        this.uF_SKYPE = uF_SKYPE;
        this.uF_DISTRICT = uF_DISTRICT;
        this.uF_PHONE_INNER = uF_PHONE_INNER;
    }

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public Boolean getACTIVE() {
        return aCTIVE;
    }

    public void setACTIVE(Boolean aCTIVE) {
        this.aCTIVE = aCTIVE;
    }

    public String getEMAIL() {
        return eMAIL;
    }

    public void setEMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getLAST_NAME() {
        return lAST_NAME;
    }

    public void setLAST_NAME(String lAST_NAME) {
        this.lAST_NAME = lAST_NAME;
    }

    public String getSECOND_NAME() {
        return sECOND_NAME;
    }

    public void setSECOND_NAME(String sECOND_NAME) {
        this.sECOND_NAME = sECOND_NAME;
    }

    public String getPERSONAL_GENDER() {
        return pERSONAL_GENDER;
    }

    public void setPERSONAL_GENDER(String pERSONAL_GENDER) {
        this.pERSONAL_GENDER = pERSONAL_GENDER;
    }

    public Object getPERSONAL_PROFESSION() {
        return pERSONAL_PROFESSION;
    }

    public void setPERSONAL_PROFESSION(Object pERSONAL_PROFESSION) {
        this.pERSONAL_PROFESSION = pERSONAL_PROFESSION;
    }

    public String getPERSONAL_WWW() {
        return pERSONAL_WWW;
    }

    public void setPERSONAL_WWW(String pERSONAL_WWW) {
        this.pERSONAL_WWW = pERSONAL_WWW;
    }

    public String getPERSONAL_BIRTHDAY() {
        return pERSONAL_BIRTHDAY;
    }

    public void setPERSONAL_BIRTHDAY(String pERSONAL_BIRTHDAY) {
        this.pERSONAL_BIRTHDAY = pERSONAL_BIRTHDAY;
    }

    public Object getPERSONAL_PHOTO() {
        return pERSONAL_PHOTO;
    }

    public void setPERSONAL_PHOTO(Object pERSONAL_PHOTO) {
        this.pERSONAL_PHOTO = pERSONAL_PHOTO;
    }

    public Object getPERSONAL_ICQ() {
        return pERSONAL_ICQ;
    }

    public void setPERSONAL_ICQ(Object pERSONAL_ICQ) {
        this.pERSONAL_ICQ = pERSONAL_ICQ;
    }

    public Object getPERSONAL_PHONE() {
        return pERSONAL_PHONE;
    }

    public void setPERSONAL_PHONE(Object pERSONAL_PHONE) {
        this.pERSONAL_PHONE = pERSONAL_PHONE;
    }

    public Object getPERSONAL_FAX() {
        return pERSONAL_FAX;
    }

    public void setPERSONAL_FAX(Object pERSONAL_FAX) {
        this.pERSONAL_FAX = pERSONAL_FAX;
    }

    public String getPERSONAL_MOBILE() {
        return pERSONAL_MOBILE;
    }

    public void setPERSONAL_MOBILE(String pERSONAL_MOBILE) {
        this.pERSONAL_MOBILE = pERSONAL_MOBILE;
    }

    public Object getPERSONAL_PAGER() {
        return pERSONAL_PAGER;
    }

    public void setPERSONAL_PAGER(Object pERSONAL_PAGER) {
        this.pERSONAL_PAGER = pERSONAL_PAGER;
    }

    public Object getPERSONAL_STREET() {
        return pERSONAL_STREET;
    }

    public void setPERSONAL_STREET(Object pERSONAL_STREET) {
        this.pERSONAL_STREET = pERSONAL_STREET;
    }

    public String getPERSONAL_CITY() {
        return pERSONAL_CITY;
    }

    public void setPERSONAL_CITY(String pERSONAL_CITY) {
        this.pERSONAL_CITY = pERSONAL_CITY;
    }

    public Object getPERSONAL_STATE() {
        return pERSONAL_STATE;
    }

    public void setPERSONAL_STATE(Object pERSONAL_STATE) {
        this.pERSONAL_STATE = pERSONAL_STATE;
    }

    public Object getPERSONAL_ZIP() {
        return pERSONAL_ZIP;
    }

    public void setPERSONAL_ZIP(Object pERSONAL_ZIP) {
        this.pERSONAL_ZIP = pERSONAL_ZIP;
    }

    public Object getPERSONAL_COUNTRY() {
        return pERSONAL_COUNTRY;
    }

    public void setPERSONAL_COUNTRY(Object pERSONAL_COUNTRY) {
        this.pERSONAL_COUNTRY = pERSONAL_COUNTRY;
    }

    public Object getWORK_COMPANY() {
        return wORK_COMPANY;
    }

    public void setWORK_COMPANY(Object wORK_COMPANY) {
        this.wORK_COMPANY = wORK_COMPANY;
    }

    public String getWORK_POSITION() {
        return wORK_POSITION;
    }

    public void setWORK_POSITION(String wORK_POSITION) {
        this.wORK_POSITION = wORK_POSITION;
    }

    public String getWORK_PHONE() {
        return wORK_PHONE;
    }

    public void setWORK_PHONE(String wORK_PHONE) {
        this.wORK_PHONE = wORK_PHONE;
    }

    public List<Integer> getUF_DEPARTMENT() {
        return uF_DEPARTMENT;
    }

    public void setUF_DEPARTMENT(List<Integer> uF_DEPARTMENT) {
        this.uF_DEPARTMENT = uF_DEPARTMENT;
    }

    public Object getUF_INTERESTS() {
        return uF_INTERESTS;
    }

    public void setUF_INTERESTS(Object uF_INTERESTS) {
        this.uF_INTERESTS = uF_INTERESTS;
    }

    public Object getUF_SKILLS() {
        return uF_SKILLS;
    }

    public void setUF_SKILLS(Object uF_SKILLS) {
        this.uF_SKILLS = uF_SKILLS;
    }

    public Object getUF_WEB_SITES() {
        return uF_WEB_SITES;
    }

    public void setUF_WEB_SITES(Object uF_WEB_SITES) {
        this.uF_WEB_SITES = uF_WEB_SITES;
    }

    public Object getUF_XING() {
        return uF_XING;
    }

    public void setUF_XING(Object uF_XING) {
        this.uF_XING = uF_XING;
    }

    public Object getUF_LINKEDIN() {
        return uF_LINKEDIN;
    }

    public void setUF_LINKEDIN(Object uF_LINKEDIN) {
        this.uF_LINKEDIN = uF_LINKEDIN;
    }

    public Object getUF_FACEBOOK() {
        return uF_FACEBOOK;
    }

    public void setUF_FACEBOOK(Object uF_FACEBOOK) {
        this.uF_FACEBOOK = uF_FACEBOOK;
    }

    public Object getUF_TWITTER() {
        return uF_TWITTER;
    }

    public void setUF_TWITTER(Object uF_TWITTER) {
        this.uF_TWITTER = uF_TWITTER;
    }

    public Object getUF_SKYPE() {
        return uF_SKYPE;
    }

    public void setUF_SKYPE(Object uF_SKYPE) {
        this.uF_SKYPE = uF_SKYPE;
    }

    public Object getUF_DISTRICT() {
        return uF_DISTRICT;
    }

    public void setUF_DISTRICT(Object uF_DISTRICT) {
        this.uF_DISTRICT = uF_DISTRICT;
    }

    public Object getUF_PHONE_INNER() {
        return uF_PHONE_INNER;
    }

    public void setUF_PHONE_INNER(Object uF_PHONE_INNER) {
        this.uF_PHONE_INNER = uF_PHONE_INNER;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iD);
        dest.writeValue(aCTIVE);
        dest.writeValue(eMAIL);
        dest.writeValue(nAME);
        dest.writeValue(lAST_NAME);
        dest.writeValue(sECOND_NAME);
        dest.writeValue(pERSONAL_GENDER);
        dest.writeValue(pERSONAL_PROFESSION);
        dest.writeValue(pERSONAL_WWW);
        dest.writeValue(pERSONAL_BIRTHDAY);
        dest.writeValue(pERSONAL_PHOTO);
        dest.writeValue(pERSONAL_ICQ);
        dest.writeValue(pERSONAL_PHONE);
        dest.writeValue(pERSONAL_FAX);
        dest.writeValue(pERSONAL_MOBILE);
        dest.writeValue(pERSONAL_PAGER);
        dest.writeValue(pERSONAL_STREET);
        dest.writeValue(pERSONAL_CITY);
        dest.writeValue(pERSONAL_STATE);
        dest.writeValue(pERSONAL_ZIP);
        dest.writeValue(pERSONAL_COUNTRY);
        dest.writeValue(wORK_COMPANY);
        dest.writeValue(wORK_POSITION);
        dest.writeValue(wORK_PHONE);
        dest.writeList(uF_DEPARTMENT);
        dest.writeValue(uF_INTERESTS);
        dest.writeValue(uF_SKILLS);
        dest.writeValue(uF_WEB_SITES);
        dest.writeValue(uF_XING);
        dest.writeValue(uF_LINKEDIN);
        dest.writeValue(uF_FACEBOOK);
        dest.writeValue(uF_TWITTER);
        dest.writeValue(uF_SKYPE);
        dest.writeValue(uF_DISTRICT);
        dest.writeValue(uF_PHONE_INNER);
    }

    public int describeContents() {
        return  0;
    }

}
