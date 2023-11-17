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

    ResponseResult addSensitive(SensitiveDto dto);

    ResponseResult delSensitive(int id);

    /*
     *管理员审核文章
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    ResponseResult articleExamine(int userId);

    /**
     * 导出所有的敏感词信息
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    ResponseResult exportSensitive();
}
