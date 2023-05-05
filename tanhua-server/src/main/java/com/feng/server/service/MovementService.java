package com.feng.server.service;

import com.feng.commons.templates.OssTemplate;
import com.feng.domain.po.Publish;
import com.feng.domain.po.UserInfo;
import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.api.PublishApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.interceptor.UserHolder;
import com.feng.server.utils.RelativeDateFormat;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author f
 * @date 2023/5/4 21:23
 */
@Service
public class MovementService {

    @Resource
    private OssTemplate ossTemplate;

    @Reference
    private PublishApi publishApi;

    @Reference
    private UserInfoApi userInfoApi;

    /**
     * 发布动态
     * @param publishVo publish
     * @param image     image
     */
    public void save(PublishVo publishVo, MultipartFile[] image) {
        List<String> media = new ArrayList<>();
        for (MultipartFile multipartFile : image) {
            String filename = multipartFile.getOriginalFilename();
//            String path = ossTemplate.upload(filename, multipartFile.getInputStream());
            String path = "1.jpg";
            media.add(path);
        }

        publishVo.setUserId(UserHolder.getUserId());
        publishVo.setMedias(media);
        publishApi.add(publishVo);
    }

    /**
     * 查询好友动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          list
     */
    public PageResult<MomentVo> queryFriendPublishList(int page, int pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult result = publishApi.findFriendPublishByTimeLine(page, pageSize, userId);
        List<Publish> items = (List<Publish>)result.getItems();

        List<MomentVo> list = new ArrayList<>();
        if (null != items) {
            MomentVo vo = new MomentVo();
            for (Publish item : items) {
                vo = new MomentVo();
                UserInfo userInfo = userInfoApi.findById(item.getUserId());
                if (null != userInfo) {
                    BeanUtils.copyProperties(userInfo, vo);
                    if (null != userInfo.getTags()) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                BeanUtils.copyProperties(item, vo);
                vo.setId(item.getId().toHexString());
                vo.setCreateDate(RelativeDateFormat.format(new Date(item.getCreated())));
                vo.setHasLiked(0);
                vo.setHasLoved(1);
                vo.setImageContent(item.getMedias().toArray(new String[]{}));
                vo.setDistance("1m");
                list.add(vo);
            }
        }
        result.setItems(list);
        return result;
    }

    /**
     * 查询推荐动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          result
     */
    public PageResult<MomentVo> queryRecommendPublishList(int page, int pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult result = publishApi.findRecommendPublish(page, pageSize, userId);
        List<Publish> items = (List<Publish>)result.getItems();
        List<MomentVo> list = new ArrayList<>();
        if (null != items) {
            for (Publish item : items) {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = userInfoApi.findById(item.getUserId());
                if (null != userInfo) {
                    BeanUtils.copyProperties(userInfo, vo);
                    if (null != userInfo.getTags()) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                BeanUtils.copyProperties(item, vo);
                vo.setId(item.getId().toHexString());
                vo.setHasLoved(0);
                vo.setHasLiked(1);
                vo.setImageContent(item.getMedias().toArray(new String[]{}));
                vo.setDistance("1m");
                list.add(vo);
            }
        }
        result.setItems(list);
        return result;
    }
}
