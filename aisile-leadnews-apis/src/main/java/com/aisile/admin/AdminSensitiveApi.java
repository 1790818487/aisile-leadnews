package com.aisile.admin;

import com.aisile.model.admin.dtos.SensitiveDto;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
/**
 *
 * 实现敏感词的管理
 * @return:
*/
public interface AdminSensitiveApi {

    ResponseResult showAllSensitive(SensitiveDto dto);
}
