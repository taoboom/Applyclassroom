<template>
  <div class="home-container">
    <el-row class="row-bg" :gutter="10">
        <el-card style="width: 1500%;height: 100%" shadow="always">
          <Date></Date>
        </el-card>
    </el-row>
  </div>
</template>
<script setup lang="ts">
 import Date from './date'
</script>

<style scoped lang="scss">
@import "./index";
</style>
