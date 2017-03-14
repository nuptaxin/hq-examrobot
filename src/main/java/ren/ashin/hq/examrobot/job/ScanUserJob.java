package ren.ashin.hq.examrobot.job;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqTask;
import ren.ashin.hq.examrobot.bean.HqUser;
import ren.ashin.hq.examrobot.cache.UserCookieCache;
import ren.ashin.hq.examrobot.dao.HqTaskDao;
import ren.ashin.hq.examrobot.dao.HqUserDao;
import ren.ashin.hq.examrobot.service.TaskQueueService;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * @ClassName: ScanJob
 * @Description: 任务扫描job
 * @author renzx
 * @date Nov 11, 2016
 */
public class ScanUserJob implements Job {
    private static final Logger LOG = Logger.getLogger(ScanUserJob.class);
    private HqUserDao hqUserDao = ExamRobot.ctx.getBean(HqUserDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        exeTask(fireTime);
    }

    private void exeTask(Date fireTime) {
            LOG.info("用户表扫描执行中" + new DateTime(fireTime).toString("yyyy-MM-dd HH:mm:ss"));
            List<HqUser> userList = Lists.newArrayList();
            userList = hqUserDao.findUserByStatus(0L);
            LOG.debug("检测到未执行任务数量：" + userList.size());
            for (HqUser user : userList) {
                // 用户登录并获取当前的用户题目关联信息
                CookieStore userCookie = UserCookieCache.getInstance().getCookieByUser(user);
                
                String url = "http://www.hq88.com/lms/member/course/course_myJob?type=0&isStudyFinished=-1";
                RequestConfig config = RequestConfig.custom().build();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(config);
                CloseableHttpClient closeableHttpClient =
                        HttpClients.custom().setDefaultCookieStore(userCookie).build();
                HttpResponse response = null;
                try {
                    response = closeableHttpClient.execute(httpPost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try {
                        String htmlStr = EntityUtils.toString(entity);
                        Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
                        // 获取用户名
                        Element userName = d1.select("span.user-name").first();// 
                        if(userName!=null&&!Strings.isNullOrEmpty(userName.text())){
                            // 更新用户表该用户任务执行中，如果找不到直接设为失败
                            user.setStatus(1L);
                            user.setName(userName.text());
                            hqUserDao.updateUserStatus(user);
                        }else{
                            user.setStatus(3L);
                            hqUserDao.updateUserStatus(user);
                            continue;
                        }
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

}
