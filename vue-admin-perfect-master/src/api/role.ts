import request from './request'

/**
 * 查询角色
 */
export const getRoleData = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url: 'role/getRoleData',
        method: 'get',
        params: query
    });
};

/**
 * 添加或者更新角色
 * @param query
 */
export const addOrUpdateRole = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"role/addOrUpdateRole",
        method: 'put',
        data:query
    })
};

/**
 * 删除角色
 */
export const delRole = (param) => {
    return request({
        url: "role/delRole",
        method: 'delete',
        data:param
    });
};


