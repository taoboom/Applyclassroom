import request from "@/api/request";

/**
 * 查询教室使用情况
 */
export const getEmptyRoomData = query => {
    return request({
        url: 'application/getEmptyRoomData',
        method: 'post',
        data: query
    });
};
/**
 * 查询教室是否为空
 * @param query
 */
export const checkEmptyData = query => {
    return request({
        url: 'application/checkEmptyData',
        method: 'post',
        data: query
    });
};

/**
 * 查询审核人
 */
export const getCheckerList = () => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url: 'application/getCheckerList',
        method: 'get'
    });
};
/**
 * 申请教室
 * @param query
 */
export const applyClassRoom = query => {
    return request({
        url: 'application/applyClassRoom',
        method: 'post',
        data: query
    });
};

/**
 * 查询申请记录
 * @param query
 */
export const getApplyRecordData = query => {
    return request({
        url: 'application/getApplyRecordData',
        method: 'get',
        params: query
    });
};

/**
 * 删除申请
 */
export const delApplyRoom = (param) => {
    return request({
        url: "application/delApplyRoom",
        method: 'delete',
        data:param
    });
};

/**
 * 审核申请
 * @param query
 */
export const checkApply = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"application/checkApply",
        method: 'post',
        data:query
    })
};