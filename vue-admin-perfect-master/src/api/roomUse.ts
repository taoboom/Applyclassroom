import request from "@/api/request";

/**
 * 查询教室使用情况
 */
export const getRoomUseData = query => {
    return request({
        url: 'roomuse/getRoomUseData',
        method: 'get',
        params: query
    });
};

/**
 * 更新教室使用情况信息
 * @param query
 */
export const updateClassRoomUse = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"roomuse/updateRoomUse",
        method: 'put',
        data:query
    })
};

/**
 * 删除教室使用情况
 */
export const delRoomUse = (param) => {
    return request({
        url: "roomuse/delRoomUse",
        method: 'delete',
        data:param
    });
};

/**
 * excel导入
 * @param param
 */
export const excelImport = (param) => {
    // @ts-ignore
    return request({
        url: "roomuse/excelImport",
        method: 'post',
        header: { "Content-Type": "multipart/form-data" },
        data:param
    });
};

/**
 * 查询教室使用情况数据（楼管端）
 */
export const getManageData = query => {
    return request({
        url: 'roomuse/getManageData',
        method: 'get',
        params: query
    });
};

/**
 * 更新教室使用情况中图片信息
 * @param query
 */
export const updateRoomUsePic = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"roomuse/updateRoomUsePic",
        method: 'put',
        data:query
    })
};


