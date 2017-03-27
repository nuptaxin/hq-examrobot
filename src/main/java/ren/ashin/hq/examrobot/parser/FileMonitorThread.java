package ren.ashin.hq.examrobot.parser;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ren.ashin.hq.examrobot.ExamRobot;
import ren.ashin.hq.examrobot.bean.HqAnswer;
import ren.ashin.hq.examrobot.dao.HqAnswerDao;
import ren.ashin.hq.examrobot.dao.HqCourseDao;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @ClassName: FileMonitorThread
 * @Description: TODO
 * @author renzx
 * @date Mar 27, 2017
 */
public class FileMonitorThread extends Thread {
    private static final String DIR_PATH = System.getProperty("user.dir") + "/examResult";
    private static final String BACKUP_PATH = System.getProperty("user.dir") + "/BUCKUP";
    HqAnswerDao hqAnswerDao = ExamRobot.ctx.getBean(HqAnswerDao.class);
    HqCourseDao hqCourseDao = ExamRobot.ctx.getBean(HqCourseDao.class);
    private static final Logger LOG = Logger.getLogger(FileMonitorThread.class);

    @Override
    public void run() {


        File parentDir = FileUtils.getFile(DIR_PATH);
        final File backupDir = FileUtils.getFile(BACKUP_PATH);
        if (!parentDir.exists()) {
            try {
                FileUtils.forceMkdir(parentDir);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (!backupDir.exists()) {
            try {
                FileUtils.forceMkdir(backupDir);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        FileAlterationObserver observer = new FileAlterationObserver(parentDir);
        observer.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onFileCreate(File file) {
                System.out.println("File created: " + file.getName());
                // 开始解析文件
                readAndSaveFile(file);
                // 解析完成后移动文件到BACKUP目录中
                try {
                    FileUtils.moveFileToDirectory(file, backupDir, true);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFileDelete(File file) {
                System.out.println("File deleted: " + file.getName());
            }

            @Override
            public void onDirectoryCreate(File dir) {
                System.out.println("Directory created: " + dir.getName());
            }

            @Override
            public void onDirectoryDelete(File dir) {
                System.out.println("Directory deleted: " + dir.getName());
            }
        });

        // Add a monior that will check for events every x ms,
        // and attach all the different observers that we want.
        FileAlterationMonitor monitor = new FileAlterationMonitor(500, observer);
        try {
            monitor.start();

            monitor.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 第一次开启时要处理积压的文件
        File[] files = parentDir.listFiles();
        for (File file : files) {
            System.out.println("File created: " + file.getName());
            // 开始解析文件
            readAndSaveFile(file);
            // 解析完成后移动文件到BACKUP目录中
            try {
                FileUtils.moveFileToDirectory(file, backupDir, true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void readAndSaveFile(File file) {
        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(fileContent);
        String findTitle = d1.getElementsByTag("h3").first().text();
        // 根据题目名称获取题目id
        Long courseId = hqCourseDao.selectCourseIdByName(findTitle);
        // 获取当前题目的所有已知答案
        List<HqAnswer> hqAnswerList = hqAnswerDao.selectAnswerByQId(courseId);
        Map<String, HqAnswer> hqAnswerMap = Maps.newHashMap();
        for (HqAnswer hqAnswer : hqAnswerList) {
            hqAnswerMap.put(hqAnswer.getName(), hqAnswer);
        }
        String stringPattern = "<div id=\"qDiv(.*?</div>)";
        List<String> findList = RegexTool2findList(stringPattern, fileContent);
        for (String oneQuestion : findList) {
            // 读取题目类型
            String findQType = "<h3><strong>.、(.*?)</strong>";
            String qTypeString = RegexTool2findString(findQType, oneQuestion);
            int qType = 0;
            if ("单项选择题".equals(qTypeString))
                qType = 0;
            if ("多项选择题".equals(qTypeString))
                qType = 1;
            if ("判断题".equals(qTypeString))
                qType = 2;

            System.out.println(qType);
            // 读取题目
            String findQuestion = "<dl><dt>.{1,2}、(.*?)<span";
            String question = RegexTool2findString(findQuestion, oneQuestion);
            if (hqAnswerMap.containsKey(question)) {
                continue;
            }
            HqAnswer answer = new HqAnswer();
            answer.setCourseId(courseId);
            answer.setName(question);
            answer.setType((long) qType);
            // 读取答案A
            String findAnswerA = "disabled=\"disabled\">&nbsp;A、(.*?)</p>";
            String answerA = RegexTool2findString(findAnswerA, oneQuestion);
            if (Strings.isNullOrEmpty(answerA)) {
                answer.setAnswerA(null);
            }
            answer.setAnswerA(answerA);
            // 读取答案B
            String findAnswerB = "disabled=\"disabled\">&nbsp;B、(.*?)</p>";
            String answerB = RegexTool2findString(findAnswerB, oneQuestion);
            answer.setAnswerB(answerB);
            // 读取答案C
            String findAnswerC = "disabled=\"disabled\">&nbsp;C、(.*?)</p>";
            String answerC = RegexTool2findString(findAnswerC, oneQuestion);
            answer.setAnswerC(answerC);
            // 读取答案D
            String findAnswerD = "disabled=\"disabled\">&nbsp;D、(.*?)</p>";
            String answerD = RegexTool2findString(findAnswerD, oneQuestion);
            answer.setAnswerD(answerD);
            // 读取答案E
            String findAnswerE = "disabled=\"disabled\">&nbsp;E、(.*?)</p>";
            String answerE = RegexTool2findString(findAnswerE, oneQuestion);
            answer.setAnswerE(answerE);
            // 读取答案F
            String findAnswerF = "disabled=\"disabled\">&nbsp;F、(.*?)</p>";
            String answerF = RegexTool2findString(findAnswerF, oneQuestion);
            answer.setAnswerF(answerF);
            // 读取正确答案字母
            String findAnswerLetter = "</span> 标准答案：(.*?)</div>";
            String answerLetter = RegexTool2findString(findAnswerLetter, oneQuestion);
            answer.setAnswerLetter(answerLetter);
            answer.setCreateTime(new Date());
            answer.setSubjectId(null);
            if(answer.getCourseId()==null){
                LOG.error("无法找到对应的课程Id"+file.getAbsolutePath());
                return;
            }
            hqAnswerDao.insertAnswer(answer);
        }
    }

    private List<String> RegexTool2findList(String stringPattern, String fileContent) {
        Pattern pattern = Pattern.compile(stringPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContent);
        List<String> matchString = Lists.newArrayList();
        while (matcher.find()) {
            matchString.add(matcher.group(1));
        }
        return matchString;
    }

    private String RegexTool2findString(String stringPattern, String fileContent) {
        Pattern pattern = Pattern.compile(stringPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
