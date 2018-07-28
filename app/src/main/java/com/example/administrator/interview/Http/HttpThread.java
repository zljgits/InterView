package com.example.administrator.interview.Http;

import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/1/30.
 */

public class HttpThread extends Thread {
    private String url;
    private String json;
    private Handler handler;

    public HttpThread(String url, String json, Handler handler) {
        this.url = url;
        this.json = json;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            int res = httpResponse.getStatusLine().getStatusCode();
            if (res == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    String info = EntityUtils.toString(httpEntity);
//                    JSONObject jsonObject=new JSONObject(info);
//                    info=jsonObject.getString("serverinfo");
                    Message message = new Message();
                    message.what = res;
                    message.obj = info;
                    handler.sendMessage(message);
                }
            } else if (res == 901) {
                Message message = new Message();
                message.what = res;
                message.obj = "timeout!";
                handler.sendMessage(message);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
