package ren.ashin.hq.examrobot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;


public class CookieUtil {
    public static CookieStore getCookieStore(String userName, String password)
            throws ClientProtocolException, IOException {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        // 依次是代理地址，代理端口号，协议类型
        RequestConfig config = RequestConfig.custom().build();



        // 第一次连接，需要带回一个JSESSIONID
        // 设置登录参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appLoginPage", "http://www.hq88.com:80/lms/"));
        formparams.add(new BasicNameValuePair("service", "v3v4check"));
        formparams.add(new BasicNameValuePair("v3Url",
                "http://v3.hq88.com/member/login/login_gotoIndex.do"));
        formparams.add(new BasicNameValuePair("v4Url",
                "http://www.hq88.com:80/lms/member/login/gotoIndex"));
        formparams.add(new BasicNameValuePair("ticket", ""));
        formparams.add(new BasicNameValuePair("renew", "true"));
        formparams.add(new BasicNameValuePair("accessType", "pc_member"));
        formparams.add(new BasicNameValuePair("username", userName));
        formparams.add(new BasicNameValuePair("password", password));
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "utf-8");

        // 新建Http post请求
        HttpPost httppost = new HttpPost("http://www.hq88.com/lms/");
        httppost.setConfig(config);


        // 处理请求，得到响应
        HttpResponse response = closeableHttpClient.execute(httppost);

        String set_cookie = response.getFirstHeader("Set-Cookie").getValue();

        // 打印Cookie值
        System.out.println(set_cookie.substring(0, set_cookie.indexOf(";")));
        CookieStore cks = new BasicCookieStore();
        BasicClientCookie ck =
                new BasicClientCookie(StringUtils.substringBefore(
                        set_cookie.substring(0, set_cookie.indexOf(";")), "="),
                        StringUtils.substringAfter(
                                set_cookie.substring(0, set_cookie.indexOf(";")), "="));
        ck.setDomain("www.hq88.com");
        cks.addCookie(ck);

        // 添加一个记住密码的cookie
        BasicClientCookie ckm = new BasicClientCookie("rmbUsername", userName);
        ckm.setDomain("www.hq88.com");
        cks.addCookie(ckm);


        // 第二次登陆，需要带上form里的相关信息直接登陆
        HttpPost httppost2 = new HttpPost("http://sso.hq88.com/userLogin");
        httppost2.setConfig(config);
        httppost2.setEntity(entity1);

        HttpResponse response2 = closeableHttpClient.execute(httppost2);

        Header[] h = response2.getHeaders("Set-Cookie");
        for (Header header : h) {
            String set_cookie1 = header.getValue();
            if (set_cookie1.contains(";"))
                set_cookie1 = StringUtils.substringBefore(set_cookie1, ";");
            BasicClientCookie ck1 =
                    new BasicClientCookie(StringUtils.substringBefore(set_cookie1, "="),
                            StringUtils.substringAfter(set_cookie1, "="));
            ck1.setDomain("www.hq88.com");
            cks.addCookie(ck1);
        }
        // 获取登陆使用location
        String loc = response2.getFirstHeader("Location").getValue();


        // 第三次登陆，需要带上所有的cookie信息（作用应该是与服务器建立session连接，跳过这一步是无法后续正常取数据的）
        HttpPost httppost3 = new HttpPost(loc);
        httppost3.setConfig(config);
        CloseableHttpClient closeableHttpClient3 =
                HttpClients.custom().setDefaultCookieStore(cks).build();
        closeableHttpClient3.execute(httppost3);

        return cks;

    }
}
