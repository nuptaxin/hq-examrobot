package ren.ashin.hq.examrobot.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ren.ashin.hq.examrobot.util.CookieUtil;

/**
 * @ClassName: MyService1
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class MyService1 {
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
        HttpPost httppost4 = new HttpPost("http://www.hq88.com/lms/member/course/course_myJob?type=0&isStudyFinished=-1");
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
                String htmlStr = EntityUtils.toString(entity4);
                Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
                // 获取用户名
                List<Element> et = d1.select("span.user-name");// 
                System.out.println(et.get(0).text());
                // 获取第一页的课程信息
                Element course = d1.getElementsByAttributeValue("class", "course course-float clearfix").first();// 
                List<Element> allCourse = course.getElementsByTag("li");
                for (Element element : allCourse) {
                    String name = element.getElementsByTag("h4").text();
                    String link = element.getElementsByTag("a").val("考试").last().attr("href");
                    String uuid = StringUtils.substringBetween(link, "(0,'", "','')");
                }
                
                
                
                System.out.println(htmlStr);
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
