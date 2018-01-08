package xyz.ratapp.munion.data.statistic.audio;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import xyz.ratapp.munion.data.pojo.Lead;

/**
 * Created by timtim on 02/01/2018.
 */

public class BitrixOAuthAudiosTask extends
        AsyncTask<String, Void, List<String>> {

    private String appFolder;
    private List<Lead.Record> talksRecords = new ArrayList<>();

    @Override
    protected List<String> doInBackground(String... params) {

        String mask = "https://m-union.bitrix24.ru/oauth/token?client_id=%s&grant_type=authorization_code&client_secret=%s&redirect_uri=%s&code=%s&scope=%s";
        String clientId = params[0];
        String clientSecret = params[1];
        String redirectUri = params[2];
        String code = params[3];
        String scope = params[4];
        appFolder = params[5];

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
                return loadUrls(accessToken);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> loadUrls(String accessToken) throws Exception {

        List<String> result = new ArrayList<>();
        String mask = "https://m-union.bitrix24.ru%s";

        for (Lead.Record talksRecord : talksRecords) {
            //loadUrls
            String urlString = String.format(Locale.getDefault(),
                    mask, talksRecord.getDownloadUrl());
            urlString = urlString.replaceFirst("auth=",
                    "auth=" + accessToken);
            String url = loadUrl(urlString,
                    talksRecord.getId() + ".wav");

            if (url != null) {
                result.add(url);
            }
        }

        return result;
    }

    private String loadUrl(String urlString, String file) {
        File path = new File(appFolder, file);
        URI u = URI.create(urlString);

        try (InputStream in = u.toURL().openStream()) {
            copy(in, path);

            return path.toString();

        } catch (Exception e) {
            return null;
        }

    }

    public static void copy(InputStream in, File dst) throws IOException {
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }


    public void setTalksRecords(List<Lead.Record> talksRecords) {
        this.talksRecords = talksRecords;
    }
}