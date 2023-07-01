import request from './request'

/**
 * 登录
 * @param data
 */
export function login(data) {
    return request({
        url: 'user/login',
        method: 'post',
        data
    })
}
/**
 * 退出登录
 * @param data
 */
export function loginOut() {
    return request({
        url: 'user/loginOut',
        method: 'post'
    })
}

/**
 * 修改密码
 * @param query
 */
export const modifyPassword = query => {
    return request({
        url:"user/modifyPassword",
        method: 'put',
        data:query
    })
};



/**
 * 查询用户
 */
export const getUserData = query => {
    return request({
        url: 'user/getUserData',
        method: 'get',
        params: query
    });
};

/**
 * 添加或者更新用户
 * @param query
 */
export const addOrUpdateUser = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"user/addOrUpdateUser",
        method: 'put',
        data:query
    })
};

/**
 * 删除用户
 */
export const delUser = (param) => {
    return request({
        url: "user/delUser",
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
        url: "user/excelImport",
        method: 'post',
        header: { "Content-Type": "multipart/form-data" },
        data:param
    });
};


