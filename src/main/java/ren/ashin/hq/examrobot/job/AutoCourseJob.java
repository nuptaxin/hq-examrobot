package ren.ashin.hq.examrobot.job;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqCourse;
import ren.ashin.hq.examrobot.bean.HqUcRelation;
import ren.ashin.hq.examrobot.bean.HqUser;
import ren.ashin.hq.examrobot.cache.UserCookieCache;
import ren.ashin.hq.examrobot.dao.HqCourseDao;
import ren.ashin.hq.examrobot.dao.HqUcRelationDao;
import ren.ashin.hq.examrobot.dao.HqUserDao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @ClassName: AutoCourseJob
 * @Description: 自动任务
 * @author renzx
 * @date Nov 11, 2016
 */
public class AutoCourseJob implements Job {
    private static final Logger LOG = Logger.getLogger(AutoCourseJob.class);
    private HqUserDao hqUserDao = ExamRobot.ctx.getBean(HqUserDao.class);
    private HqUcRelationDao hqUcRelationDao = ExamRobot.ctx.getBean(HqUcRelationDao.class);
    private HqCourseDao hqCourseDao = ExamRobot.ctx.getBean(HqCourseDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date fireTime = context.getFireTime();
        exeTask(fireTime);
    }

    private void exeTask(Date fireTime) {
        // 获取所有的可以自动答题的用户(且status为2)
        List<HqUser> autoUserList = hqUserDao.findUserByAutoFlag(1L);
        // 由于每天可以考两次，所以直接把用户加两遍
        autoUserList.addAll(autoUserList);
        for (HqUser hqUser : autoUserList) {
            CookieStore userCookie = UserCookieCache.getInstance().updateCookieByUser(hqUser);
            // 获取当前用户关联的题目
            List<HqUcRelation> relationList =
                    hqUcRelationDao.selectRelationByUserId(hqUser.getId());
            for (HqUcRelation hqUcRelation : relationList) {
                // 获取当前题目的详细信息
                HqCourse currCourse = hqCourseDao.selectCourseById(hqUcRelation.getCourseId());
                // 开始答题
                beginExam(hqUser,userCookie,currCourse);
            }

        }
    }

    private void beginExam(HqUser hqUser, CookieStore userCookie, HqCourse currCourse) {
        String newUrl = null;
        String resultUrl = null;
        if (currCourse.getType() == 0) {
            // 首先获取一个examuuid，然后才能继续考试
            // 测试甘特图
            // http://www.hq88.com/lms/member/course/exam_startExamPre?courseUuid=3b6c8a9c-e316-11e4-a6e0-549f350fb66a&chapterUuid=
            String url =
                    "http://www.hq88.com/lms/member/course/exam_startExamPre?courseUuid="
                            + currCourse.getUuId() + "&chapterUuid=";
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
                    Element scriptEle = d1.getElementsByTag("script").last();
                    String scriptStr = scriptEle.html();
                    newUrl =
                            StringUtils.substringBetween(scriptStr,
                                    "window.location.href=\"", "\"");
                    newUrl += currCourse.getName();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        // 直接跳转到考试页面
        if(newUrl!=null){
            resultUrl = examThis(newUrl, userCookie, currCourse);
        }
        if (resultUrl == null) {
            LOG.warn("无法自动答题");
        } else {
            // 获取答案
            resultThis(resultUrl, userCookie, currCourse);
        }
        
    }

    private void resultThis(String resultUrl, CookieStore userCookie, HqCourse currCourse) {
        RequestConfig config = RequestConfig.custom().build();
        HttpPost httpPost = new HttpPost(resultUrl);
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
            String htmlStr = null;
            try {
                htmlStr = EntityUtils.toString(entity);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
            // 将当前页面的数据保存为文件:命名方式为课程名+当前时间.txt
            // 首先在当前目录下创建一个文件夹examResult，用于存放答案
            String userDir = System.getProperty("user.dir");
            File examResultDir = FileUtils.getFile(userDir + "/examResult");
            if (!examResultDir.exists()) {
                try {
                    FileUtils.forceMkdir(examResultDir);
                } catch (IOException e) {
                    LOG.error("无法创建文件夹:" + examResultDir, e);
                }
            }
            // 创建临时文件
            File resultFile =
                    FileUtils.getFile(examResultDir, currCourse.getName()
                            + DateTime.now().toString("yyyy-MM-dd'T'HHmmss.SSS") + ".txt");
            File tmpResultFile = FileUtils.getFile(examResultDir, resultFile.getName() + ".tmp");
            LOG.info("tmp文件路径：" + tmpResultFile.getAbsolutePath());
            if (tmpResultFile.exists()) {
                try {
                    FileUtils.forceDelete(tmpResultFile);
                } catch (IOException e) {
                    LOG.error("删除" + tmpResultFile + "文件失败", e);
                }
            }
            try {
                FileUtils.writeStringToFile(tmpResultFile, htmlStr, "UTF-8");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (resultFile.exists()) {
                try {
                    FileUtils.forceDelete(resultFile);
                } catch (IOException e) {
                    LOG.error("删除" + resultFile + "文件失败", e);
                }
            }
            tmpResultFile.renameTo(resultFile);
        }

    }

    private String examThis(String newUrl, CookieStore userCookie, HqCourse currCourse) {

        RequestConfig config = RequestConfig.custom().build();
        HttpPost httpPost = new HttpPost(newUrl);
        httpPost.setConfig(config);
        CloseableHttpClient closeableHttpClient =
                HttpClients.custom().setDefaultCookieStore(userCookie).build();
        HttpResponse response = null;
        String resultUrl = null;
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
            String htmlStr = null;
            try {
                htmlStr = EntityUtils.toString(entity);
                // 需要从此页面获取答案页面
                if (submitAnswer(htmlStr, userCookie) != null) {
                    return null;
                };

                Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
                Element scriptEle = d1.getElementsByTag("script").last();
                String scriptStr = scriptEle.html();
                String[] allUrl =
                        StringUtils.substringsBetween(scriptStr, "window.location.href=\"", "\"");
                resultUrl = allUrl[1] + "项目进度管理";
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
            // 将
            System.out.println(d1);
        }
        return resultUrl;

    }

    private Long submitAnswer(String htmlStr, CookieStore userCookie) {
        Document d1 = Jsoup.parse(htmlStr);// 转换为Dom树
        Element formEle = d1.getElementById("examForm");
        if (formEle == null) {
            return 0L;
        }
        Elements inputEles = formEle.getElementsByTag("input");
        Map<String, String> inputMap = Maps.newHashMap();
        for (Element element : inputEles) {
            inputMap.put(element.attr("name"), element.attr("value"));
        }

        // 获取每个题目的唯一Id
        StringBuilder answersbd = new StringBuilder();
        List<Element> eleList = Lists.newArrayList();
        for (int i = 1;; i++) {
            Element eleDiv = d1.getElementById("qDiv" + i);
            if (eleDiv != null) {
                eleList.add(eleDiv);
            } else {
                break;
            }
        }
        for (Element element : eleList) {
            // 获取题目类型
            String subjectType =
                    element.getElementsByTag("h3").first().getElementsByTag("strong").text();

            // 获取题目的id
            Element course =
                    element.getElementsByAttributeValue("class", "exam-correction").first();
            String subjectid = course.attr("onclick");
            subjectid = StringUtils.substringBetween(subjectid, "','", "')");

            // 拼接字符串
            answersbd.append(subjectid);
            answersbd.append(",");
            if (subjectType.contains("选择题")) {
                answersbd.append("A");
            } else {
                answersbd.append("1");
            }
            answersbd.append(";");
        }

        // 去除最后一个;
        String theAnswer = answersbd.substring(0, answersbd.length() - 1);
        inputMap.put("answers", theAnswer);


        // 答题参数设置
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("exam_token", inputMap.get("exam_token")));
        formparams.add(new BasicNameValuePair("randomExamUuid", inputMap.get("randomExamUuid")));
        formparams.add(new BasicNameValuePair("courseUuid", inputMap.get("courseUuid")));
        formparams.add(new BasicNameValuePair("chapterUuid", inputMap.get("chapterUuid")));
        formparams.add(new BasicNameValuePair("isCompany", ""));
        formparams.add(new BasicNameValuePair("answers", inputMap.get("answers")));
        formparams.add(new BasicNameValuePair("isOverTime", inputMap.get("isOverTime")));
        UrlEncodedFormEntity entity1 = null;
        try {
            entity1 = new UrlEncodedFormEntity(formparams, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        RequestConfig config = RequestConfig.custom().build();
        HttpPost httpPost = new HttpPost("http://www.hq88.com/lms/member/course/exam_submitExam");
        httpPost.setConfig(config);
        httpPost.setEntity(entity1);
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
        return null;
    }

}
