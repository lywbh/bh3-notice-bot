package com.lyw.modules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lyw.utils.HttpUtils;
import com.sobte.cqp.jcq.event.JcqApp;
import com.sobte.cqp.jcq.message.CQCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MihoyoTakumiModule {

    private static final String baseUrl = "https://api-takumi.mihoyo.com/post/wapi/getNewsList";

    private Set<String> record = new LinkedHashSet<>();

    public List<String> getInfos() {
        JcqApp.CQ.logInfo("bh3-notice-bot", "当前已读缓存的内容为：" + record);
        List<String> result = new ArrayList<>();
        Set<String> currentRecord = new HashSet<>();
        for (int type = 1; type <= 3; type++) {
            String url = baseUrl + "?gids=1&last_id=0&page_size=20&type=" + type;
            String response = HttpUtils.get(url);
            if (response == null) {
                return null;
            }
            JSONObject respObj = JSON.parseObject(response);
            if (respObj.getInteger("retcode") != 0) {
                return null;
            }
            JSONArray contentList = respObj.getJSONObject("data").getJSONArray("list");
            for (Object content : contentList) {
                JSONObject postInfo = ((JSONObject) content).getJSONObject("post");
                String postId = postInfo.getString("post_id");
                currentRecord.add(postId);
                if (record.contains(postId)) {
                    continue;
                }
                String bbsUrl = "https://bbs.mihoyo.com/bh3/article/" + postId;
                String title = postInfo.getString("subject");
                String description = postInfo.getString("content");
                String imageUrl = null;
                JSONArray images = postInfo.getJSONArray("images");
                if (!images.isEmpty()) {
                    imageUrl = images.getString(0);
                }
                result.add(new CQCode().share(bbsUrl, title, description, imageUrl));
            }
        }
        record = currentRecord;
        JcqApp.CQ.logInfo("bh3-notice-bot", "已读缓存更新为：" + record);
        return result;
    }

}
