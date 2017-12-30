package xyz.ratapp.munionagent.ui.activities.auth;

import android.os.AsyncTask;


import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import xyz.ratapp.munionagent.data.pojo.BitrixUser;
import xyz.ratapp.munionagent.data.pojo.UserResponse;


/**
 * Created by timtim on 29/12/2017.
 */

public class BitrixOAuthTask extends AsyncTask<String, Void, BitrixUser> {

    @Override
    protected BitrixUser doInBackground(String... params) {

        String mask = "https://m-union.bitrix24.ru/oauth/token?client_id=%s&grant_type=authorization_code&client_secret=%s&redirect_uri=%s&code=%s&scope=%s";
        String clientId = params[0];
        String clientSecret = params[1];
        String redirectUri = params[2];
        String code = params[3];
        String scope = params[4];

        String url = String.format(Locale.getDefault(), mask,
                clientId, clientSecret, redirectUri, code, scope);

        try {
            OAuth2Client client = new OAuth2Client.Builder(
                    clientId,
                    clientSecret,
                    url).build();

            OAuthResponse response = client.requestAccessToken();

            if (response.isSuccessful()) {

                String accessToken = response.getAccessToken();
                return loadUser(accessToken);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private BitrixUser loadUser(String accessToken) throws Exception {
        String mask = "https://m-union.bitrix24.ru/rest/user.current.json?auth=%s";
        String urlString = String.format(Locale.getDefault(), mask, accessToken);

        //http request
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.connect();
        //parse json
        Gson gson = new Gson();

        return gson.fromJson(
                new InputStreamReader(connection.getInputStream()),
                UserResponse.class).getBitrixUser();
    }
}
