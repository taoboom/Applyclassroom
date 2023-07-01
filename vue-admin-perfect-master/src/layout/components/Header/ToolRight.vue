<template>
  <div class="m-tool-right">
    <HeaderSearch class="item-children"/>
    <Setting class="item-children"/>
    <Avatar/>
  </div>
</template>

<script lang="ts" setup>
import HeaderSearch from './components/HeaderSearch.vue'
import Setting from './components/Setting.vue'
import Avatar from './components/Avatar.vue'
</script>

<style lang="scss" scoped>
.m-tool-right{
  display: flex;
  align-items: center;
  .item-children{
    margin-right: 22px;
  }
}
</style>
