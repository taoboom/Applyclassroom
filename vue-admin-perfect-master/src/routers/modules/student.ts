import Layout from "@/layout/index.vue";

const studentRouter = [{
    path: '/',
    component: Layout,
    redirect: 'noRedirect',
    name: 'external-link',
    alwaysShow:true,
    meta: {
        title: '教室申请',
        icon: 'link',
        roles:['学生']
    },
    children: [
        {
            path: 'applyroom',
            component: () => import('@/views/applyroom/index.vue'),
            name: 'applyroom',
            meta: { title: '教室申请',  icon: 'ChatSquare',roles:['学生']}
        },
        {
            path: 'applyrecord',
            component: () => import('@/views/applyroom/applyrecordstudent/index.vue'),
            name: 'applyrecord',
            meta: { title: '学生申请记录',  icon: 'View',roles:['学生']}
        },
    ]
}]

export default studentRouter
