package xyz.ratapp.munion.data.pojo;

import java.util.List;
import java.util.Map;

/**
 * Created by timtim on 27/12/2017.
 */

public class Statistics {

    private String objectName;
    private int callsCount;
    private int looksCount;
    private int viewsCount;
    private Map<String, Float> data;
    private List<String> talksUrls;

    public Statistics(String objectName, int callsCount,
                      int looksCount, int viewsCount,
                      Map<String, Float> data,
                      List<String> talksUrls) {
        this.objectName = objectName;
        this.callsCount = callsCount;
        this.looksCount = looksCount;
        this.viewsCount = viewsCount;
        this.data = data;
        this.talksUrls = talksUrls;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public void setCallsCount(int callsCount) {
        this.callsCount = callsCount;
    }

    public int getLooksCount() {
        return looksCount;
    }

    public void setLooksCount(int looksCount) {
        this.looksCount = looksCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Map<String, Float> getData() {
        return data;
    }

    public void setData(Map<String, Float> data) {
        this.data = data;
    }

    public List<String> getTalksUrls() {
        return talksUrls;
    }

    public void setTalksUrls(List<String> talksUrls) {
        this.talksUrls = talksUrls;
    }
}
