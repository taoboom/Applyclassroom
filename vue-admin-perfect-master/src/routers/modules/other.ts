/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout/index.vue'

const administratorsRouter = [{
  path: '/other',
  component: Layout,
  redirect: '/other/clipboard',
  name: 'other',
  alwaysShow:true,
  meta: {
    title: '审核',
    icon: 'management',
    roles:['教务处管理员']
  },
  children: [
    {
      path: 'administratorscheck',
      component: () => import('@/views/check/administrators/index.vue'),
      name: 'administratorscheck',
      meta: { title: '教务处管理员审核',  icon: 'Checked',roles:['教务处管理员']}
    },
  ]
}]

export default administratorsRouter
