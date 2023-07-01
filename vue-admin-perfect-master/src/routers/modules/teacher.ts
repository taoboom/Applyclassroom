/** When your routing table is too long, you can split it into small modules**/

import Layout from "@/layout/index.vue";

const teacherRouter = [{
    path: '/chat',
    component: Layout,
    redirect: '/charts/index',
    name: 'chat',
    alwaysShow:true,
    meta: {
        title: '教师申请',
        icon: 'chat-square',
        roles:['教师']
    },
    children: [
        {
            path: 'applyroom',
            component: () => import('@/views/applyroom/index.vue'),
            name: 'applyroom',
            meta: { title: '教室申请',  icon: 'Message',roles:['学生','教师']}
        },
        {
            path: 'applyrecordteacher',
            component: () => import('@/views/applyroom/applyrecordteacher/index.vue'),
            name: 'applyrecordteacher',
            meta: { title: '教师申请记录',  icon: 'View',roles:['教师']}
        },
    ]
}]

export default teacherRouter
