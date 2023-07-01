import axios, { AxiosInstance, AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from "element-plus";
import {useUserStore} from "@/store/modules/user"
import {Message} from "@element-plus/icons-vue";
import router from "@/routers";
// 创建axios实例 进行基本参数配置
const service = axios.create({
    // 默认请求地址，根据环境的不同可在.env 文件中进行修改
    baseURL:'http://localhost:8081',
    // 设置接口访问超时时间
    timeout: 3000000, // request timeout，
    // 跨域时候允许携带凭证
    withCredentials: true
})

//  request interceptor 接口请求拦截
service.interceptors.request.use((config:AxiosRequestConfig)=>{
    /**
     * 用户登录之后获取服务端返回的token,后面每次请求都在请求头中带上token进行JWT校验
     * token 存储在本地储存中（storage）、vuex、pinia
     */
    const userStore = useUserStore();
    const token: string = userStore.token;
    // 自定义请求头
    if(token){
        config.headers['Authorization'] = token
    }
    return config
},(error: AxiosError) => {
    // 请求错误，这里可以用全局提示框进行提示
    return Promise.reject(error);
})

//  response interceptor 接口响应拦截
service.interceptors.response.use((response: AxiosResponse) =>{
    // 直接返回res，当然你也可以只返回res.data
    // 系统如果有自定义code也可以在这里处理
    return response


},(error: AxiosError) => {
    if(error.request){
        console.log(error.request)
    } else if(error.response){
        console.log(error.response.data);
        console.log(error.response.status);
    }
    if (error && error.response) {
        const UserStore = useUserStore();
        switch (error.response.status) {
            case 400: error.message = '请求错误(400)';
                break;
            case 401:error.message = '未授权，请重新登录(401)',UserStore.logout(),router.push("/login");
                break;
            case 403: error.message = '拒绝访问(403)',UserStore.logout(),router.push("/login");
                break;
            case 404: error.message = '请求出错(404)';
                break;
            case 408: error.message = '请求超时(408)';
                break;
            case 500: error.message = '服务器错误(500)',UserStore.logout(),router.push("/login");
                break;
            case 501: error.message = '服务未实现(501)';
                break;
            case 502: error.message = '网络错误(502)';
                break;
            case 503: error.message = '服务不可用(503)';
                break;
            case 504: error.message = '网络超时(504)';
                break;
            case 505: error.message = 'HTTP版本不受支持(505)';
                break;
            default: error.message = '连接出错';
        }
    }else{
        error.message ='连接服务器失败!'
    }
    return Promise.reject(error)
})


/**
 * @description 显示错误消息
 * opt 传入参数
 * err 错误信息
 * type 消息类型
 * duration 消息持续时间
 */
function showErrMessage (opt, err, type:any= 'error', duration:number = 5000){
    ElMessage({
        message: err.msg,
        type:type,
        duration: duration
    })
}

export default service
