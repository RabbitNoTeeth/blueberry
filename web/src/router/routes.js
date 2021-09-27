
const routes = [
  // {
  //   path: '/',
  //   component: () => import('layouts/MainLayout.vue'),
  //   children: [
  //     { path: '', component: () => import('pages/Index.vue') }
  //   ]
  // },
  {
    path: '/',
    redirect: '/device/list'
  },
  {
    path: '/device/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/device/DeviceList') }
    ]
  },
  {
    path: '/channels/:deviceType/:deviceId',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/channel/ChannelList') }
    ]
  },
  {
    path: '/stream/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/stream/StreamList') }
    ]
  },
  {
    path: '/schedule/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/schedule/ScheduleList') }
    ]
  },
  {
    path: '/videoQualityDetect/strategy/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/videoqualitydetect/arithmetic/VideoQualityDetectArithmeticList') }
    ]
  },
  {
    path: '/videoQualityDetect/record/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/videoqualitydetect/record/VideoQualityDetectRecordList') }
    ]
  },
  {
    path: '/videoQualityDetect/manual',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/videoqualitydetect/manual/VideoQualityDetectManualView') }
    ]
  },
  {
    path: '/videoQualityDetect/manual2',
    component: () => import('pages/videoqualitydetect/manual/VideoQualityDetectManualView')
  },
  {
    path: '/api/list',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/api/ApiDocumentView') }
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes
