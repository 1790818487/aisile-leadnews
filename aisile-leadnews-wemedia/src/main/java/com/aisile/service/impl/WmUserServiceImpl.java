package com.aisile.service.impl;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.dtos.WmUserDto;
import com.aisile.model.media.pojos.WmUser;
import com.aisile.mapper.WmUserMapper;
import com.aisile.service.IWmUserService;
import com.aisile.utils.common.AppJwtUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 自媒体用户信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements IWmUserService {

    @Override
    //自媒体用户登录
    public ResponseResult loginWeUser(WmUserDto wmUserDto) {
        if (wmUserDto==null||wmUserDto.getName()==null||wmUserDto.getPassword()==null
        ||"".equals(wmUserDto.getName().trim())||"".equals(wmUserDto.getPassword().trim()))
           return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        WmUser wmUser = this.getOne(Wrappers.lambdaQuery(new WmUser())
                .eq(WmUser::getName, wmUserDto.getName()));
        if (wmUser==null)
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NONE_EXISTS);
        if (!wmUser.getPassword().equals(wmUserDto.getPassword()))
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        if (wmUser.getStatus()==0||wmUser.getStatus()==1)
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_DISABLED);
        wmUser.setLoginTime(new Date());
        //更新登录时间
        this.updateById(wmUser);
        //处理后的信息封装发给前端
        wmUser.setPassword("**********");
        String token = AppJwtUtil.getToken((long) wmUser.getId());
        HashMap<String, Object> hashMap = new HashMap<>();
        wmUser.setPassword("********");
        hashMap.put("token", token);
        hashMap.put("wmUser", wmUser);
        return ResponseResult.okResult(hashMap);
    }
}
