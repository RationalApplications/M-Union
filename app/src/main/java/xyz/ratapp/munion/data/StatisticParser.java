package xyz.ratapp.munion.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by timtim on 02/01/2018.
 */

public class StatisticParser {

    public static void parse() {

        List<String> urls = new ArrayList<>();
        urls.add("http://emls.ru/fullinfo/1/1132613.html");
        urls.add("http://www.mirkvartir.ru/184836313/");
        urls.add("http://www.restate.ru/base/9675892.html");
        urls.add("http://spb.rucountry.ru/vtorichka/21537323.html");
        urls.add("https://realty.yandex.ru/offer/7564220877592794625/");

        Map<String, Float> data = new HashMap<>();

        for (String url : urls) {
            data.put(url, parse(url));
        }

        for (Map.Entry<String, Float> entry : data.entrySet()) {
            System.out.printf("%s\t%f\n",
                    entry.getKey(),
                    entry.getValue());
        }
    }

    private static Float parse(String url) {
        if(url.startsWith("http://www.emls.ru/")) {
            return parseEmls(url);
        }
        else if(url.startsWith("http://www.mirkvartir.ru/")) {
            url = url.replaceAll("http://www\\.mirkvartir\\.ru/", "");
            long id = url.endsWith("/") ?
                    Long.parseLong(url.substring(0, url.length() - 1))
                    :
                    Long.parseLong(url);

            return parseMirkvartir(id);
        }
        else if(url.startsWith("http://www.restate.ru/")) {
            return parseRestate(url);
        }
        else if(url.startsWith("http://spb.rucountry.ru/")) {
            return parseRucountry(url);
        }
        else if(url.startsWith("https://realty.yandex.ru/")) {
            return parseYandex(url);
        }

        return 0f;
    }

    private static Float parseRestate(String url) {
        try {
            Document html = Jsoup.connect(url).
                    timeout(10000).
                    validateTLSCertificates(false).get();

            return Float.parseFloat(html.body().
                    getElementsByClass("rbobj").
                    last().getElementsByIndexEquals(3).text());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0f;
    }

    private static Float parseRucountry(String url) {
        return 0f;
    }

    private static Float parseYandex(String url) {
        /*try {
            Document html = Jsoup.connect(url).
                    timeout(10000).
                    validateTLSCertificates(false).get();

            return Float.parseFloat(html.body().
                    getElementsByClass("offer-card__views-count").
                    text().replaceAll("Просмотры: ", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //сделать через WebView

        return 0f;
    }

    private static Float parseMirkvartir(long id) {
        try {
            String url = "http://www.mirkvartir.ru/handlers/getEstateViewCount.ashx?estateId=" + id;

            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new URL(url).
                            openConnection().getInputStream()));

            return gson.fromJson(reader, JsonObject.class).
                    get("EventCount").getAsFloat();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0f;
    }

    private static Float parseEmls(String url) {
        //сделать через WebView

        return 0f;
    }

}
