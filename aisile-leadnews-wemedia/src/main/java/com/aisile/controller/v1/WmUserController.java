package com.aisile.controller.v1;


import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.dtos.WmUserDto;
import com.aisile.model.media.pojos.WmUser;
import com.aisile.service.IWmUserService;
import com.aisile.wemedia.WeMediaUserControllerApi;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 自媒体用户信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@RestController
@RequestMapping("/api/wmuser")
public class WmUserController implements WeMediaUserControllerApi {

    @Autowired
    private IWmUserService wmUserService;

    @Override
    @PostMapping("add/wemedia")
    public WmUser addWmUser(@RequestBody WmUser wmUser) {
        wmUserService.save(wmUser);
        return wmUser;
    }

    @Override
    @GetMapping("fing/{id}")
    public WmUser findByUserId(@PathVariable("id") int user_id) {
        return wmUserService.getById(user_id);
    }

    @Override
    @PostMapping("update")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ResponseResult updateById(@RequestBody WmUser wmUser) {
         wmUserService.updateById(wmUser);
         return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    @PostMapping("login/in")
    public ResponseResult loginWeUser(@RequestBody WmUserDto wmUserDto) {
        return wmUserService.loginWeUser(wmUserDto);
    }
}
