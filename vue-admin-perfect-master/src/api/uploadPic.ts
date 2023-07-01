import request from "@/api/request";

/**
 * 上传图片
 */
export const uploadPic = param => {
    return request({
        url: 'upload/uploadPic',
        method: 'post',
        data: param,
        headers:{
            'content-type':'multipart/form-data'
        }
    });
};

/**
 * 删除图片
 */
export const delUploadFile=param=>{
    return request({
        url:"upload/delUploadFile",
        method:'post',
        data:param
    })
};