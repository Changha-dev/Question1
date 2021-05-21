package com.mentenseoul.samplexml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    EditText edit;
    TextView text;
    XmlPullParser xpp;

    String key="vAcRfh9RCNLcxdQqt1DrGHvWEiAuMlci";
    String data;

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.result);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg)

        {
            new GetXMLTask().execute();
        }
    };

    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            Document doc = null;
            URL url;

            try {
                url = new URL("https://eep.energy.or.kr/new_api/certi_148.aspx?key=vAcRfh9RCNLcxdQqt1DrGHvWEiAuMlci&modelname=R-T801PHHG");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Log.d("test", e.toString());
                //오류 메시지  org.w3c.dom.DOMException: 업체명칭
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            NodeList nodeList = doc.getElementsByTagName("view");

            for(int i = 0; i< 1; i++){
                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList rqno = fstElmnt.getElementsByTagName("rqno");
                s += "rqno = "+  rqno.item(0).getChildNodes().item(0).getNodeValue() +"\n";

                NodeList upchae = fstElmnt.getElementsByTagName("업체명칭");
                s += "업체명칭 = "+  upchae.item(0).getChildNodes().item(0).getNodeValue() +"\n";

                NodeList gigha  = fstElmnt.getElementsByTagName("기자재명칭");
                s += "기자재명칭 = "+ gigha.item(0).getChildNodes().item(0).getNodeValue() +"\n";

                NodeList wallgan = fstElmnt.getElementsByTagName("월간소비전력량");
                s += "월간소비전력량 = "+  wallgan.item(0).getChildNodes().item(0).getNodeValue() +"\n";
            }

            text.setText(s);

            super.onPostExecute(doc);
        }
    }
}