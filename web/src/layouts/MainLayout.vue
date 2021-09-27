<template>
  <q-layout view="hHh lpr fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left"/>

        <q-toolbar-title>
          <!--          <q-avatar>-->
          <!--            <img src="/logo.jpg">-->
          <!--          </q-avatar>-->
          Blueberry
        </q-toolbar-title>
      </q-toolbar>

      <q-tabs align="left">

      </q-tabs>
    </q-header>

    <q-drawer show-if-above v-model="left" side="left" bordered>
      <q-list>
        <menu-tree :data="menuData"/>
      </q-list>
    </q-drawer>

    <q-page-container style="width: 100%;height: 100%;position: fixed">
      <router-view/>
    </q-page-container>

  </q-layout>

</template>

<script>

import MenuTree from "layouts/MenuTree";

export default {
  name: 'MainLayout',
  components: {MenuTree},
  data() {
    return {
      left: false,
      menuData: [
        {
          name: '设备列表',
          icon: 'list',
          group: '/device',
          groupName: 'first',
          to: '/device/list'
        },
        {
          name: '视频列表',
          icon: 'videocam',
          group: '/stream',
          groupName: 'first',
          to: '/stream/list'
        },
        {
          name: '定时任务',
          icon: 'watch_later',
          group: '/schedule',
          groupName: 'first',
          to: '/schedule/list'
        },
        {
          name: '视频质量检测',
          icon: 'view_in_ar',
          group: '/videoQualityDetect',
          groupName: 'first',
          children: [
            {
              name: '算法列表',
              group: '/videoQualityDetect',
              to: '/videoQualityDetect/strategy/list'
            },
            {
              name: '检测记录',
              group: '/videoQualityDetect',
              to: '/videoQualityDetect/record/list'
            },
            {
              name: '手动检测',
              group: '/videoQualityDetect',
              to: '/videoQualityDetect/manual'
            }]
        },
        {
          name: 'API 接口文档',
          icon: 'description',
          group: '/api',
          groupName: 'first',
          to: '/api/list'
        }
      ]
    }
  }
}
</script>
