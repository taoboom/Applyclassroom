<template>
  <div class="m-user-table">
    <div class="header">
      <el-form :inline="true" :model="formInline" ref="ruleFormRef">
        <el-form-item label="地址" prop="roomname">
          <el-select v-model="formInline.address" class="m-2" placeholder="Select" size="large" clearable>
            <el-option v-for="(item, index) in options" :key="index" :label="item.address" :value="item.address"/>
          </el-select>
        </el-form-item>
        <el-form-item label="周次" prop="weeks">
          <el-select
                  clearable
                  v-model="formInline.weeks"
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
        <el-form-item label="星期" prop="week">
          <el-select
                  clearable
                  v-model="formInline.week"
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
        <el-form-item label="节次" prop="section">
          <el-select
                  clearable
                  v-model="formInline.section"
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
      <div class="table-inner">
        <el-table
            v-loading="loading"
            :data="tableData" style="width: 100%;height: 100%" border>
          <el-table-column fixed  type="index" width="50"  align="center"></el-table-column>
          <el-table-column prop="sno" label="学号(工号)" align="center" />
          <el-table-column prop="apply" label="申请人" align="center" />
          <el-table-column prop="address" label="地址" align="center" />
          <el-table-column prop="roomname" label="教室名称" align="center" />
          <el-table-column prop="weeks" label="申请周次" align="center" />
          <el-table-column prop="week" label="申请星期" align="center" />
          <el-table-column prop="section" label="申请节次" align="center"/>
          <el-table-column prop="operator" label="操作" width="200px" align="center" fixed="right">
            <template #default="scope">
              <el-button v-if="scope.row.beforeuse!=null&&scope.row.beforeuse!=''&&scope.row.afteruse!=null&&scope.row.afteruse!=''"
                         type="primary"
                         size="small" icon="Edit"
                         @click="view(scope.row)"
              >查看</el-button>
              <el-button v-else
                      type="primary"
                      size="small" icon="Edit"
                      @click="editHandler(scope.row)"
              >上传使用情况图片</el-button>
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


    <ManageDialog ref="manageDialog" @refresh="getData"/>
    <ViewDialog ref="viewDialog" />
  </div>
</template>
<script lang="ts" setup>
import {ElMessageBox, ElMessage, FormInstance, UploadInstance} from 'element-plus'
import {onMounted, reactive, ref, unref} from 'vue'
import {getRoomTypeData} from "@/api/roomType";
import {delClassRoom, excelImport, getAddressData, getClassRoomData} from "@/api/classroom";
import type { UploadProps, UploadUserFile } from 'element-plus'
import ManageDialog from './manageDialog.vue'
import ViewDialog from './viewDialog.vue'
import axios from "axios";
import {exportExcel} from "@/utils/exprotExcel";
import {delApplyRoom, getApplyRecordData} from "@/api/applyRoom";
import {getManageData, getRoomUseData} from "@/api/roomUse";

const tableData = ref([])
const dialogVisible = ref(false)
const manageDialog = ref()
const viewDialog = ref()
const ruleFormRef = ref<FormInstance>()
const formInline = reactive({
  address:'',
  page : 1, //当前页
  pageSize : 10  //一页多少条
})

const loading = ref(true)

//总条数
const total = ref()
//教室地址下拉数组
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

function getData(){
  loading.value = true
  //获取教室地址下拉
  getAddressData().then(res=>{
    options.value = res.data.data;
  })

  //查询教室使用情况数据
  getManageData(formInline).then(res => {
    console.log(res)
    tableData.value = res.data.data.list
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

//编辑
const editHandler = (row) => {
  manageDialog.value.show(row)
}

//编辑
const view = (row) => {
  viewDialog.value.show(row)
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
