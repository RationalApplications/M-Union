package xyz.ratapp.munion.data.retrofit;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import xyz.ratapp.munion.R;
import xyz.ratapp.munion.data.pojo.LeadListResponse;

/**
 * Created by timtim on 27/12/2017.
 * <p>
 * Interface for working with REST-API
 * of https://m-union.bitrix24.ru/rest/
 */

public interface BitrixAPI {

    static String getBaseUrl(Context context) {
        String userId = context.getString(R.string.user_id);
        String hookId = context.getString(R.string.hook);

        return String.format(Locale.getDefault(),
                "https://m-union.bitrix24.ru/rest/%s/%s/",
                userId, hookId);
    }

    @GET("crm.lead.list?select[]=TITLE&select[]=NAME&select[]=SECOND_NAME&" +
            "select[]=LAST_NAME&select[]=COMMENTS&select[]=BIRTHDATE&select[]=ASSIGNED_BY_ID&" +
            "select[]=CREATED_BY_ID&select[]=DATE_CREATE&select[]=UF_CRM_1514330608&select[]=UF_CRM_1502809647&" +
            "select[]=PHONE&select[]=UF_CRM_1502183426&select[]=UF_CRM_1502183442&select[]=UF_CRM_1514422195&" +
            "select[]=UF_CRM_1514422134&select[]=UF_CRM_1514422511")
    Call<LeadListResponse> loadLeadByPhone(@Query("filter[PHONE]") String phone);

    @GET("crm.lead.list?select[]=TITLE&select[]=NAME&select[]=SECOND_NAME&" +
            "select[]=LAST_NAME&select[]=COMMENTS&select[]=BIRTHDATE&select[]=ASSIGNED_BY_ID&" +
            "select[]=CREATED_BY_ID&select[]=DATE_CREATE&select[]=UF_CRM_1514330608&select[]=UF_CRM_1502809647&" +
            "select[]=PHONE&select[]=UF_CRM_1502183426&select[]=UF_CRM_1502183442&select[]=UF_CRM_1514422195&" +
            "select[]=UF_CRM_1514422134&select[]=UF_CRM_1514422511")
    Call<LeadListResponse> loadLeadByLoyaltyCode(@Query("filter[UF_CRM_1514422195]") String loyaltyCode);

    @GET("crm.lead.update?params[REGISTER_SONET_EVENT]=Y")
    Call<JsonObject> setInvitedFriends(@Query("id") String id,
                                       //@Query("fields[UF_CRM_1514422134]") String friendId); -> for single is ok,
                                       // but for more then one need to send query like this "fields[UF_CRM_1514422134][0]"
                                       //we will use QueryMap
                                       @QueryMap Map<String, String> map);

    @GET("crm.lead.update?params[REGISTER_SONET_EVENT]=Y")
    Call<JsonObject> setLoyaltyCode(@Query("id") String id,
                                    @Query("fields[UF_CRM_1514422195]") String loyaltyCode);

    @GET("crm.contact.add?params[REGISTER_SONET_EVENT]=Y&fields[PHONE][0][VALUE_TYPE]=WORK&fields[TYPE_ID]=3&fields[SOURCE_ID]=1")
    Call<JsonObject> createContact(@Query("fields[NAME]") String name,
                                    @Query("fields[PHONE][0][VALUE]") String phone,
                                   @Query("fields[COMMENTS]") String status);

    @GET("crm.contact.get?select[]=COMMENTS")
    Call<JsonObject> loadContactComments(@Query("id") String id);
}
