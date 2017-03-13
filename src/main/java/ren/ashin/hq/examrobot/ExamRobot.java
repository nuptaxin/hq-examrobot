package ren.ashin.hq.examrobot;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import ren.ashin.hq.examrobot.util.CookieUtil;

/**
 * @ClassName: ExamRobot
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class ExamRobot {
    public static void main(String[] args) {
        CookieStore cks = null;
        try {
            cks = CookieUtil.getCookieStore("927875z210", "888888");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        RequestConfig config = RequestConfig.custom().build();
        HttpPost httppost4 = new HttpPost("http://www.hq88.com/lms/member/index/index");
        httppost4.setConfig(config);
        CloseableHttpClient closeableHttpClient4 =
                HttpClients.custom().setDefaultCookieStore(cks).build();
        HttpResponse response4 = null;
        try {
            response4 = closeableHttpClient4.execute(httppost4);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpEntity entity4 = response4.getEntity();

        if (entity4 != null) {
            try {
                System.out.println(EntityUtils.toString(entity4));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
