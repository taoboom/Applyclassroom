import Layout from "@/layout/index.vue";

const officeRouter = [{
    path: '/',
    component: Layout,
    redirect: 'noRedirect',
    name: 'external-link',
    alwaysShow:true,
    meta: {
        title: '班主任审核',
        icon: 'link',
        roles:['班主任']
    },
    children: [
        {
            path: 'teachercheck',
            component: () => import('@/views/check/teacher/index.vue'),
            name: 'teachercheck',
            meta: { title: '班主任审核申请',  icon: 'TrendCharts',roles:['班主任']}
        },
    ]
}]

export default officeRouter
