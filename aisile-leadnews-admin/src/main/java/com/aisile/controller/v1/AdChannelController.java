package com.aisile.controller.v1;


import com.aisile.admin.AdminAdChannelApi;
import com.aisile.model.admin.dtos.ChannelDto;
import com.aisile.model.admin.pojos.AdChannel;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.service.IAdChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 频道信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-10-27
 */
@RestController
@RequestMapping("/api/v1/adChannel")
public class AdChannelController implements AdminAdChannelApi {

    @Autowired
    private IAdChannelService adChannelService;

    @PostMapping
    public ResponseResult<Object> showAllChannel(
            @RequestBody ChannelDto dto){
        return adChannelService.showAllChannel(dto);
    }

    @PostMapping("saveNewChannel")
    public ResponseResult<Object> addAllChannel(@RequestBody AdChannel adChannel){
        return adChannelService.addAdChannel(adChannel);
    }

    @Override
    @GetMapping
    public ResponseResult showAllChannelNoPage() {
        return ResponseResult.okResult(adChannelService.list());
    }

}
