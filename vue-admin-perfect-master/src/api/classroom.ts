import request from "@/api/request";

/**
 * 查询教室
 */
export const getClassRoomData = query => {
    return request({
        url: 'classroom/getClassRoomData',
        method: 'get',
        params: query
    });
};

/**
 * 添加或者更新教室
 * @param query
 */
export const addOrUpdateClassRoom = query => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url:"classroom/addOrUpdateClassRoom",
        method: 'put',
        data:query
    })
};

/**
 * 删除教室
 */
export const delClassRoom = (param) => {
    return request({
        url: "classroom/delClassRoom",
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
        url: "classroom/excelImport",
        method: 'post',
        header: { "Content-Type": "multipart/form-data" },
        data:param
    });
};

/**
 * 获取教室地址下拉
 */
export const getAddressData = () => {
    /*axios 发出异步请求 2、中写法 $ajax  $post
    *
    * */
    return request({
        url: 'classroom/getAddressData',
        method: 'get'
    });
};


