package xyz.ratapp.munion.data.pojo;

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

    public Statistics(String objectName, int callsCount,
                      int looksCount, int viewsCount,
                      Map<String, Float> data) {
        this.objectName = objectName;
        this.callsCount = callsCount;
        this.looksCount = looksCount;
        this.viewsCount = viewsCount;
        this.data = data;
    }

    public String getObjectName() {
        return objectName;
    }

    public int getCallsCount() {
        return callsCount;
    }

    public int getLooksCount() {
        return looksCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public Map<String, Float> getData() {
        return data;
    }
}
