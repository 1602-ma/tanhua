package com.feng.server.service;

import com.aliyuncs.utils.StringUtils;
import com.feng.commons.templates.OssTemplate;
import com.feng.domain.po.FollowUser;
import com.feng.domain.po.UserInfo;
import com.feng.domain.po.Video;
import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.VideoVo;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.dubbo.api.VideoApi;
import com.feng.server.interceptor.UserHolder;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author f
 * @date 2023/5/9 22:19
 */
@Service
public class VideoService {

    @Reference
    private VideoApi videoApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private FdfsWebServer fdfsWebServer;

    @Resource
    private OssTemplate ossTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发布小视频
     * @param videoThumbnail video
     * @param videoFile      video
     */
    public void post(MultipartFile videoThumbnail, MultipartFile videoFile) {
        try {
            Long userId = UserHolder.getUserId();
//        String picUrl = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
            String picUrl = "C:\\Users\\feng\\Desktop\\projects\\tanhua\\doc\\images\\2.jpg";

            String originalFilename = videoFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(videoFile.getInputStream(), videoFile.getSize(), suffix, null);
            String videoURl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
            Video video = new Video();
            video.setPicUrl(picUrl);
            video.setText("good");
            video.setUserId(userId);
            videoApi.save(video);
        } catch (IOException e) {

        }
    }

    /**
     * 分页查询
     * @param page
     * @param pagesize
     * @return
     */
    public PageResult<VideoVo> findPage(int page, int pagesize) {
        //1、调用api分页查询数据列表  PageResult<Video>
        PageResult result = videoApi.findPage(page,pagesize);
        //2、获取分页数据中数据列表   List<Video>
        List<Video> items = (List<Video>) result.getItems();
        //3、循环遍历数据集合，一个video构造一个vo
        List<VideoVo> list = new ArrayList<>();
        if(items != null) {
            for (Video item : items) {
                UserInfo info = userInfoApi.findById(item.getUserId());
                VideoVo vo = new VideoVo();
                BeanUtils.copyProperties(info,vo);
                BeanUtils.copyProperties(item,vo);
                vo.setCover(item.getPicUrl());
                vo.setId(item.getId().toHexString());
                if(StringUtils.isNotEmpty(item.getText())){
                    vo.setSignature(item.getText());//签名
                }
                else
                {
                    vo.setSignature("默认签名");//签名
                }
                //从redis中获取操作记录，如果存在返回1，不存在返回0
                String key = "video_follow_"+UserHolder.getUserId()+"_"+item.getUserId();
                if(redisTemplate.hasKey(key)) {
                    vo.setHasFocus(1); //TODO 是否关注
                }else{
                    vo.setHasFocus(0); //TODO 是否关注
                }
                vo.setHasLiked(0); //是否点赞
                list.add(vo);
            }
        }
        //4、存入pageResult中
        result.setItems(list);
        //5、构造返回值
        return result;
    }
    /**
     * 关注视频的作者
     * @param userId
     */
    public void followUser(long userId) {
        // 构建关注对象
        FollowUser followUser = new FollowUser();
        followUser.setUserId(UserHolder.getUserId());
        followUser.setFollowUserId(userId);
        // 调用API添加关系
        videoApi.followUser(followUser);
        // 记录redis，表示当前用户关系了这个作者
        String key = "video_follow_" + UserHolder.getUserId()+ "_" + userId;
        redisTemplate.opsForValue().set(key,"1");
    }

    /**
     * 取消关注
     * @param userId
     */
    public void unfollowUser(long userId) {
        // 构建关注对象
        FollowUser followUser = new FollowUser();
        followUser.setUserId(UserHolder.getUserId());
        followUser.setFollowUserId(userId);
        // 调用API取消关系
        videoApi.unfollowUser(followUser);
        // 删除redis的记录
        String key = "video_follow_" + UserHolder.getUserId()+ "_" + userId;
        redisTemplate.delete(key);
    }

}
