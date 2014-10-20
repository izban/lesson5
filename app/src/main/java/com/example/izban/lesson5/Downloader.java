package com.example.izban.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izban on 20.10.14.
 */
public class Downloader extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayAdapter<String> adapter;
    XmlPullParser parser;
    ListView lv;

    Downloader(Context context, ListView lv) {
        this.context = context;
        this.lv = lv;
        this.adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
    }

    void download() throws IOException, XmlPullParserException {
        URL url = new URL("http://bash.im/rss/");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream is = connection.getInputStream();
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.i("", "START");
        try {
            download();
            adapter.clear();
            ArrayList<Item> items = new Parser(parser).parse();
            Log.i("", Integer.toString(items.size()));
            for (int i = 0; i < items.size(); i++) {
                adapter.add(items.get(i).description);
            }
            if (adapter.isEmpty()) {
                throw new Exception();
            }
            Log.i("", "OK");
        } catch (Exception e) {
            Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
            Log.i("", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i("", Integer.toString(adapter.getCount()));
        Log.i("", "OK");
        lv.setAdapter(adapter);
    }
}
