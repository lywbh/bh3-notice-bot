package com.lyw;

import com.lyw.modules.MihoyoTakumiModule;
import com.lyw.utils.AsyncTaskUtils;
import com.sobte.cqp.jcq.event.JcqApp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Bh3noticebot extends Bh3noticeAppAbstract {

    private MihoyoTakumiModule mihoyoTakumiModule;

    private static final Set<Long> groupNos = new HashSet<>();
    private static final Integer GAP_SECOND = 300;

    static {
        groupNos.add(596917621L);
        groupNos.add(201012763L);
        groupNos.add(952070592L);
    }

    public int startup() {
        JcqApp.CQ.logInfo("bh3-notice-bot", "初始化插件...");
        mihoyoTakumiModule = new MihoyoTakumiModule();
        // 先读一遍当前条目，缓存下来，之后有变化的才是新增的
        mihoyoTakumiModule.getInfos();
        checkAndSendNews();
        JcqApp.CQ.logInfo("bh3-notice-bot", "初始化完毕");
        return 0;
    }

    private void checkAndSendNews() {
        AsyncTaskUtils.schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> news = mihoyoTakumiModule.getInfos();
                if (news.isEmpty()) {
                    JcqApp.CQ.logInfo("bh3-notice-bot", "没有新公告");
                } else {
                    news.forEach(message -> groupNos.forEach(groupNo -> {
                        JcqApp.CQ.sendGroupMsg(groupNo, message);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }));
                }
            }
        }, GAP_SECOND, TimeUnit.SECONDS);
    }

}
