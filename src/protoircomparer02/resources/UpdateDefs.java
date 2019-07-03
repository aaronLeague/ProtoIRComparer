/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protoircomparer02.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Aaron
 */
public class UpdateDefs {

    protected static String[][] Data;
    protected static ArrayList<String> titles = new ArrayList<>();
    protected static ArrayList<String> na = new ArrayList<>();
    protected static ArrayList<String> no = new ArrayList<>();
    protected static ArrayList<String> li = new ArrayList<>();
    protected static ArrayList<String> tr = new ArrayList<>();
    protected static ArrayList<String> fu = new ArrayList<>();
    protected static ArrayList<String> ba = new ArrayList<>();

    public UpdateDefs() throws Exception {
        
        Data = null;
        titles.clear();
        na.clear();
        no.clear();
        li.clear();
        tr.clear();
        fu.clear();
        ba.clear();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        //Define variables for dealing with API pagination
        boolean lastPage = false;
        int pageNum = 1;

        while (lastPage == false) {
            Request request = new Request.Builder()
                    .url("https://api.osf.io/v2/nodes/d8av6/files/osfstorage/?page=" + pageNum)
                    .get()
                    .addHeader("authorization", "Basic " + UserAuth.key)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "ed354a34-4b4c-e847-deea-c0bb6cac0954")
                    .build();

            Response response = client.newCall(request).execute();

            String responseString = response.body().string();
            JSONObject fileData = new JSONObject(responseString);
            JSONArray data = fileData.getJSONArray("data");
            JSONArray tags;
            String[] tag;

            if (pageNum == 1) {
                int numEls = fileData.getJSONObject("links").getJSONObject("meta")
                        .getInt("total");
                Data = new String[numEls][9];
            }

            for (int i = 0; i < data.length(); i++) {
                int num = i + (pageNum - 1) * 10;

                //Stores the title of each file, as well as adding it to the array.
                String title = data.getJSONObject(i).getJSONObject("attributes").getString("name");
                titles.add(title);
                Data[num][0] = title;

                //Stores the download link for each file, for easy access during download.
                String download = data.getJSONObject(i).getJSONObject("links").getString("download");
                Data[num][8] = download;

                tags = data.getJSONObject(i).getJSONObject("attributes").getJSONArray("tags");
                for (int j = 0; j < tags.length(); j++) {
                    tag = tags.get(j).toString().split(":");

                    switch (tag[0]) {
                        case "na":
                            if (!na.contains(tag[1]) && !tag[1].equals("N/A")) {
                                na.add(tag[1]);
                            }
                            Data[num][1] = tag[1];
                            break;
                        case "no":
                            if (!no.contains(tag[1]) && !tag[1].equals("N/A")) {
                                no.add(tag[1]);
                            }
                            Data[num][2] = tag[1];
                            break;
                        case "li":
                            if (!li.contains(tag[1]) && !tag[1].equals("N/A")) {
                                li.add(tag[1]);
                            }
                            Data[num][3] = tag[1];
                            break;
                        case "tr":
                            if (!tr.contains(tag[1]) && !tag[1].equals("N/A")) {
                                tr.add(tag[1]);
                            }
                            Data[num][4] = tag[1];
                            break;
                        case "fu":
                            if (!fu.contains(tag[1]) && !tag[1].equals("N/A")) {
                                fu.add(tag[1]);
                            }
                            Data[num][5] = tag[1];
                            break;
                        case "ba":
                            if (!ba.contains(tag[1]) && !tag[1].equals("N/A")) {
                                ba.add(tag[1]);
                            }
                            Data[num][6] = tag[1];
                            break;
                        case "exp":
                            Data[num][7] = "1";
                            break;
                    }
                }
            }

            //Go to the next page of data, or stop if this is the last page
            if (fileData.getJSONObject("links").get("next").equals(null)) {
                lastPage = true;
            } else {
                pageNum++;
            }
        }
    }

    public void Organize() {
        Collections.sort(titles, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(na, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(no, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(li, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(tr, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(fu, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(ba, String.CASE_INSENSITIVE_ORDER);
    }

    public void AddDummies() {
        na.add(0, "[new name]");
        no.add(0, "N/A");
        no.add(0, "[new node]");
        li.add(0, "N/A");
        li.add(0, "[new linker]");
        tr.add(0, "N/A");
        tr.add(0, "[new truncation]");
        fu.add(0, "N/A");
        fu.add(0, "[new functional]");
        ba.add(0, "N/A");
        ba.add(0, "[new basis set]");
    }
}
