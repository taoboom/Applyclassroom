/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout/index.vue'

const systemRouter = [{
    path: '/',
    component: Layout,
    redirect: '/system/page',
    name: 'system',
    meta: {
        title: '系统管理',
        icon: 'Setting',
        roles:['教务处管理员']
    },
    children: [
        {
            path: 'user',
            component: () => import('@/views/system/user/index.vue'),
            name: 'user',
            meta: { title: '用户管理' , icon: 'UserFilled',roles:['教务处管理员']}
        },
        {
            path: 'roomtype',
            component: () => import('@/views/system/roomtype/index.vue'),
            name: 'roomtype',
            meta: { title: '教室类型管理' , icon: 'Notebook',roles:['教务处管理员']}
        },
        {
            path: 'role',
            component: () => import('@/views/system/role/index.vue'),
            name: 'role',
            meta: { title: '角色管理', icon: 'Tickets',roles:['教务处管理员'] }
        },
        {
            path: 'classroom',
            component: () => import('@/views/system/classroom/index.vue'),
            name: 'classroom',
            meta: { title: '教室管理',  icon: 'Memo',roles:['教务处管理员']}
        },
        {
            path: 'roomuse',
            component: () => import('@/views/system/roomuse/index.vue'),
            name: 'roomuse',
            meta: { title: '教室使用情况',  icon: 'Collection',roles:['教务处管理员']}
        },

    ]
}]

// @ts-ignore
export default systemRouter
