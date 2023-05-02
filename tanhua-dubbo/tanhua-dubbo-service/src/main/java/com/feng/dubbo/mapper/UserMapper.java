package com.feng.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author f
 * @date 2023/4/30 11:32
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
