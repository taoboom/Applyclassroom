<template>
  <div class="m-user-table">
    <div class="header">
      <el-form :inline="true" :model="formInline" ref="ruleFormRef">
        <el-form-item label="申请状态" prop="roomname">
          <el-select v-model="formInline.state" class="m-2" placeholder="Select" size="large" clearable>
            <el-option  :key="0" label="待审核" :value="0"/>
            <el-option  :key="1" label="审核通过" :value="1"/>
            <el-option  :key="2" label="审核未通过" :value="2"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">
      <div class="table-inner">
        <el-table
            v-loading="loading"
            :data="tableData" style="width: 100%;height: 100%" border>
          <el-table-column fixed  type="index" width="50"  align="center"></el-table-column>
          <el-table-column prop="address" label="地址" align="center" width="120"/>
          <el-table-column prop="roomname" label="教室名称" align="center" width="120"/>
          <el-table-column prop="weeks" label="申请周次" align="center" width="100"/>
          <el-table-column prop="week" label="申请星期" align="center" width="100"/>
          <el-table-column prop="section" label="申请节次" align="center" width="100"/>
          <el-table-column prop="checker" label="审核人" align="center" width="100"/>
          <el-table-column prop="state" label="一审状态" align="center" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.state===1?'success':(scope.row.state===2?'danger':'warning')"
                        >{{scope.row.state===1?'审核通过':(scope.row.state===2?'审核未通过':'待审核')}}</el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="state2" label="二审状态" align="center" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.state2===1?'success':(scope.row.state2===2?'danger':'warning')"
              >{{scope.row.state2===1?'审核通过':(scope.row.state2===2&&scope.row.state===2?'审核未通过':'待审核')}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="申请理由" align="center" width="200"/>
          <el-table-column prop="applytime" label="申请时间" align="center" width="200"/>
          <el-table-column prop="audittime" label="审核时间" align="center" width="200"/>
          <el-table-column prop="operator" label="操作" width="200px" align="center" fixed="right">
            <template #default="scope">
              <el-button v-if="scope.row.state===0"
                         type="danger" size="small" icon="Delete"
                         @click="del(scope.row)"
              >取消申请</el-button>
              <el-tag v-else-if="scope.row.state===1&&scope.row.state2===1" type="success" >审核通过</el-tag>
              <el-tag v-else-if="scope.row.state===1&&scope.row.state2===0" type="warning" >二审待审核</el-tag>
              <template v-else>
                <el-button ref="buttonRef" type="primary"  v-click-outside="onClickOutside"
                >查看原因</el-button>
                <el-popover
                        placement="left"
                        ref="popoverRef"
                        :virtual-ref="buttonRef"
                        trigger="click"
                        title="原因："
                        virtual-triggering
                >
                  <span>{{scope.row.failreason}}</span>
                </el-popover>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination">
        <el-pagination
            v-model:currentPage="formInline.page"
            :page-size="formInline.pageSize"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {ElMessageBox, ElMessage, FormInstance, UploadInstance} from 'element-plus'
import {onMounted, reactive, ref, unref} from 'vue'
import {getRoomTypeData} from "@/api/roomType";
import {delClassRoom, excelImport, getClassRoomData} from "@/api/classroom";
import type { UploadProps, UploadUserFile } from 'element-plus'

import axios from "axios";
import {exportExcel} from "@/utils/exprotExcel";
import {delApplyRoom, getApplyRecordData} from "@/api/applyRoom";

import { ClickOutside as vClickOutside } from 'element-plus'
import {useUserStore} from "@/store/modules/user";
const buttonRef = ref()
const popoverRef = ref()
const onClickOutside = () => {
  unref(popoverRef).popperRef?.delayHide?.()
}

const tableData = ref([])
const dialogVisible = ref(false)
const classRoomDialog = ref()
const ruleFormRef = ref<FormInstance>()
const UserStore = useUserStore()

const formInline = reactive({
  state:'',
  sno:UserStore.userInfo.sno,
  page : 1, //当前页
  pageSize : 10  //一页多少条
})
const loading = ref(true)

//总条数
const total = ref()

function getData(){
  loading.value = true
  //查询申请记录
  getApplyRecordData(formInline).then(res => {
    console.log(res.data)
    tableData.value = res.data.data.records
    total.value = res.data.data.total
    loading.value = false
  })
}

//打开页面先查询数据
getData()

//查询
const onSubmit = () => {
  getData();
}

//重置按钮
const reset = (formEl: FormInstance | undefined) => {
  loading.value = true
  if (!formEl) return
  formEl.resetFields()
  formInline.state = null;
  getData();
  loading.value = false

}


//删除
const del = (row) => {
  ElMessageBox.confirm('你确定要删除当前项吗?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  })
      .then(() => {
        delApplyRoom(row).then(res => {
          if (res.data.success) {
            ElMessage({
              message: '删除成功',
              type: 'success',
            });
            getData();
          } else {
            ElMessage({
              message: '删除失败',
              type: 'error',
            });
          }
        })
      })
      .catch(() => {})
}

//切换每页条数
const handleSizeChange = (val: number) => {
  formInline.pageSize = val;
  getData()
}
//切换页
const handleCurrentChange = (val: number) => {
  formInline.page = val
  getData()
}


onMounted(() => {
  setTimeout(() => {
      loading.value = false
  }, 1000)
})

</script>
<style lang="scss" scoped>
@import "../index";
</style>
