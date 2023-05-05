package com.feng.server.service;

import com.feng.domain.po.Announcement;
import com.feng.domain.vo.AnnouncementVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.api.AnnouncementApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author f
 * @date 2023/5/3 21:20
 */
@Service
public class AnnounceService {

    @Reference
    private AnnouncementApi announcementApi;

    public ResponseEntity announcements(int page, int pageSize) {
        PageResult<Announcement> pageResult = announcementApi.findAll(page, pageSize);
        List<Announcement> records = pageResult.getItems();
        List<AnnouncementVo> list = new ArrayList<>();

        for (Announcement record : records) {
            AnnouncementVo vo = new AnnouncementVo();
            BeanUtils.copyProperties(record, vo);
            if (null != record.getCreated()) {
                vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(record.getCreated()));
            }
            list.add(vo);
        }

        PageResult result = new PageResult(pageResult.getCounts(), pageResult.getPagesize(), pageResult.getPages(), pageResult.getPage(), list);
        return ResponseEntity.ok(result);
    }
}
