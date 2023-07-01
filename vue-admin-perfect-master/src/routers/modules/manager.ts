

/** When your routing table is too long, you can split it into small modules**/

import Layout from "@/layout/index.vue";

const managerRouter = [{
    path: '/',
    component: Layout,
    redirect: '/form/menu1',
    name: 'nested',
    alwaysShow:true,
    meta: {
        title: '楼管管理',
        icon: 'HelpFilled',
        roles:['楼管']
    },
    children: [
        {
            path: 'manage',
            component: () => import('@/views/manage/index.vue'),
            name: 'manage',
            meta: { title: '楼管管理处',  icon: 'MessageBox',roles:['楼管']}
        },
    ]
}]

export default managerRouter
