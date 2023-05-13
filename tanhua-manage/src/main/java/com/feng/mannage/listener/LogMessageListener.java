package com.feng.mannage.listener;

import com.alibaba.fastjson.JSON;
import com.feng.mannage.domain.Log;
import com.feng.mannage.service.LogService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/12 23:06
 */
@Component
@RocketMQMessageListener(topic = "tanhua-log", consumerGroup = "tanhua-log-consumer")
public class LogMessageListener implements RocketMQListener<String> {

    @Resource
    private LogService logService;

    @Override
    public void onMessage(String s) {
        Map map = JSON.parseObject(s, Map.class);
        String userId = (String)map.get("userId");
        String type = (String)map.get("type");
        String date = (String)map.get("date");

        Log log = new Log();
        log.setUserId(Long.valueOf(userId));
        log.setType(type);
        log.setLogTime(date);

        logService.save(log);
    }
}
