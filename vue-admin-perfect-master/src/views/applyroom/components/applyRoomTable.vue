<template>
  <div class="m-user-table">
    <div class="header">
      <el-form :inline="true" :model="formInline" ref="ruleFormRef">
        <el-form-item label="教室名称" prop="roomname">
          <el-input v-model="formInline.roomname" placeholder="请输入教室名称"/>
        </el-form-item>
        <el-form-item label="教室地址" prop="address">
          <el-input v-model="formInline.address" placeholder="请输入教室地址"/>
        </el-form-item>
        <el-form-item label="教室类型" prop="typeid" >
          <el-select v-model="formInline.typeid" filterable placeholder="请选择或输入教室类型" clearable >
            <el-option v-for="(item, index) in options" :key="index" :label="item.roomtype" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="周次" prop="weeksList">
          <el-select
                  clearable
                  v-model="formInline.weeksList"
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="Select"
                  style="width: 240px"
          >
            <el-option
                    v-for="item in weeksList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
            >
              <span style="float: left">{{ item.label }}</span>
              <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">{{ item.value }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="星期" prop="weekList">
          <el-select
                  clearable
                  v-model="formInline.weekList"
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="Select"
                  style="width: 240px"
          >
            <el-option
                    v-for="item in weekList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
            >
              <span style="float: left">{{ item.label }}</span>
              <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">{{ item.value }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="节次" prop="sectionList">
          <el-select
                  clearable
                  v-model="formInline.sectionList"
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="Select"
                  style="width: 240px"
          >
            <el-option
                    v-for="item in sectionList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
            >
              <span style="float: left">{{ item.label }}</span>
              <span style="float: right;color: var(--el-text-color-secondary);font-size: 13px;">{{ item.value }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">

      <div class="table-inner"  >
        <el-table
            v-loading="loading"
            :empty-text="text"
            :data="tableData" style="width: 100%;height: 100%" border>
          <el-table-column fixed  type="index" width="50"  align="center"></el-table-column>
          <el-table-column prop="address" label="地址" align="center" />
          <el-table-column prop="roomtype" label="教室类型" align="center"/>
          <el-table-column prop="roomname" label="教室名称" align="center" />
          <el-table-column prop="contain" label="容纳人数" align="center" />
          <el-table-column prop="remark"
                           :show-overflow-tooltip="true"
                           label="备注" align="center"/>
          <el-table-column prop="operator" label="操作" width="200px" align="center" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" icon="Edit" @click="applyHandler(scope.row)">
                申请
              </el-button>
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

    <ClassRoomDialog ref="classRoomDialog" @refresh="getData"/>
  </div>
</template>
<script lang="ts" setup>
import {ElMessageBox, ElMessage, FormInstance, UploadInstance} from 'element-plus'
import {onMounted, reactive, ref} from 'vue'
import {getRoomTypeData} from "@/api/roomType";
import type { UploadProps, UploadUserFile } from 'element-plus'
import ClassRoomDialog from './applyRoomDialog.vue'
import axios from "axios";
import {exportExcel} from "@/utils/exprotExcel";
import {string} from "fast-glob/out/utils";
import {getEmptyRoomData} from "@/api/applyRoom";


const tableData = ref([])
const dialogVisible = ref(false)
const tableVisible = ref(false)
const classRoomDialog = ref()
const ruleFormRef = ref<FormInstance>()
const formInline = reactive({
  roomname : '',
  address : '',
  typeid : '',
  weeksList:ref([]),
  weekList:ref([]),
  sectionList:ref([]),
  page : 1, //当前页
  pageSize : 10  //一页多少条
})
const loading = ref(true)
const text = ref("请输入查询条件!")

//总条数
const total = ref()
//教室类型下拉数组
const options = ref([])

//循环变量
let i;
//周次
const weeksList = ref([])
for(i=1;i<=19;i++){
  weeksList.value.push({
    value:i,
    label:"第"+i+"周"
  })
}
//星期
const weekList = ref([])
const w=['星期一','星期二','星期三','星期四','星期五','星期六','星期日']
for(i=1;i<=7;i++){
  weekList.value.push({
    value:i,
    label: w[i-1]
  })
}
//节次
const sectionList = ref([])
for(i=1;i<=10;i++){
  sectionList.value.push({
    value:i,
    label:"第"+i+"节"
  })
}

//打开页面获取教室类型下拉
getRoomTypeData(null).then(res=>{
  options.value = res.data.data;
})

function getData(){
  loading.value = true
  //获取教室类型下拉
  getRoomTypeData(null).then(res=>{
    options.value = res.data.data;
  })
  //查询空闲教室
  getEmptyRoomData(formInline).then(res => {
    if (res.data.data.records.length===0) {
      text.value = "暂无数据"
    }
    tableData.value = res.data.data.records;
    total.value = res.data.data.total
    loading.value = false
  })
}

//查询
const onSubmit = () => {
  console.log(formInline)
   getData();
}

//重置按钮
const reset = (formEl: FormInstance | undefined) => {
  loading.value = true
  if (!formEl) return
  formEl.resetFields()
  tableData.value = []
  loading.value = false
}


//编辑
const applyHandler = (row) => {
  classRoomDialog.value.show(row)
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
