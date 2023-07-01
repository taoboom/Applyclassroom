import request from './request'

/**
 * 查询教室类型
 */
export const getRoomTypeData = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url: 'roomtype/getRoomTypeData',
        method: 'get',
        params: query
    });
};

/**
 * 添加或者更新教室类型
 * @param query
 */
export const addOrUpdateRoomType = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"roomtype/addOrUpdateRoomType",
        method: 'put',
        data:query
    })
};

/**
 * 删除教室类型
 */
export const delRoomType = (param) => {
    return request({
        url: "roomtype/delRoomType",
        method: 'delete',
        data:param
    });
};


