<template>
  <div class="m-user-table">
    <div class="header">
      <el-form :inline="true" :model="formInline" ref="ruleFormRef">
        <el-form-item label="学号(工号)" prop="sno">
          <el-input v-model="formInline.sno" placeholder="请输入学号(工号)"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">
      <div class="util">
        <el-upload
                ref="uploadRef"
                v-model:file-list="fileList"
                class="upload-demo"
                action
                multiple
                :on-change="handleChanges"
                :show-file-list="false"
                :limit="1"
                :auto-upload="false"
                accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
        >
          <el-tooltip class="item" effect="dark" content="excel表头必须包含：['学号(工号)','姓名','电话','角色','备注'],密码默认为123456" placement="top">
            <el-button type="primary">导入excel</el-button>
          </el-tooltip>
        </el-upload>
        <el-button style="margin-left: 12px" type="primary"  @click="exportToExcelTemplate">导出模板<i class="el-icon-upload el-icon--right"></i></el-button>

        <el-button type="primary" @click="addHandler">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
      </div>
      <div class="table-inner">
        <el-table
            v-loading="loading"
            :data="tableData" style="width: 100%;height: 100%" border>
          <el-table-column type="index" width="50"  align="center"></el-table-column>
          <el-table-column prop="sno" label="学号（工号）" align="center" width="150"/>
          <el-table-column prop="name" label="姓名" align="center"/>
          <el-table-column prop="phone" label="电话" align="center"/>
          <el-table-column prop="role" label="关联角色" align="center" width="120"/>
          <el-table-column prop="status" label="用户状态" align="center">
            <template #default="scope">
              <el-switch
                  inline-prompt
                  active-text="启用" inactive-text="禁用"
                  :active-value="1"  :inactive-value="0"
                  v-model="scope.row.status" @change="changeStatus(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="remark"
                           :show-overflow-tooltip="true"
                           width="180"
                           label="备注" align="center"/>
          <el-table-column prop="operator" label="操作" width="200px" align="center" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" icon="Edit" @click="editHandler(scope.row)">
                编辑
              </el-button>
              <el-button @click="del(scope.row)" type="danger" size="small" icon="Delete">
                删除
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

    <UserDialog ref="userDialog" @refresh="getData"/>
  </div>
</template>
<script lang="ts" setup>
  import {ElMessageBox, ElMessage, FormInstance, UploadUserFile, UploadInstance} from 'element-plus'
import {onMounted, reactive, ref} from 'vue'
import UserDialog from './userDialog.vue'
import {getUserData, delUser, addOrUpdateUser, excelImport} from "../../../../api/user";
  import {exportExcel} from "@/utils/exprotExcel";


const tableData = ref([])
const dialogVisible = ref(false)
const userDialog = ref()
const ruleFormRef = ref<FormInstance>()
const formInline = reactive({
  sno : '',
  page : 1, //当前页
  pageSize : 10  //一页多少条
})
const loading = ref(true)

//总条数
const total = ref()

//默认上传文件
const fileList = ref<UploadUserFile[]>([]);
//绑定上传组件用来调用方法清空上传列表
const uploadRef = ref<UploadInstance>()


function getData(){
  loading.value = true
  getUserData(formInline).then(res => {
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
  getData();
  loading.value = false

}

//添加
const addHandler = () => {
  userDialog.value.show()
}
//编辑
const editHandler = (row) => {
  console.log(row)
  userDialog.value.show(row)
}

const del = (row) => {
  ElMessageBox.confirm('你确定要删除当前项吗?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  })
      .then(() => {
        delUser(row).then(res => {
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
const changeStatus = (row) => {
  console.log(row)
  ElMessageBox.confirm(
    `确定要${!row.status ? '禁用' : '启用'} ${row.deptName} 账户吗？`,
    '温馨提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    },
  )
    .then(async () => {
        addOrUpdateUser(row).then(res=>{
          if (res.data.success) {
            ElMessage({
              message: '操作成功',
              type: 'success',
            });
          } else {
            ElMessage({
              message: '操作失败',
              type: 'error',
            });
          }
        })
    })
    .catch(() => {
      if (row.status === 1) {
        row.status = 0;
      } else {
        row.status = 1;
      }
    })
}

//excel上传
const handleChanges = (file, fileList) => {
  if (file.raw.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'&&
          file.raw.type!=='application/vnd.ms-excel') {
    ElMessage.error("附件格式错误，请删除后重新上传!");//限制文件类型
    return false;
  } else {
    let param = new FormData();
    param.append('file', file.raw);
    console.log(param.get('file'));
    excelImport(param).then(res=>{
      if (res.data.success) {
        ElMessage({
          message: '导入成功',
          type: 'success',
        });
        //清空上传文件列表
        uploadRef.value!.clearFiles()
        //查询
        getData();
      } else {
        ElMessage({
          message: '导入失败',
          type: 'error',
        });
        //清空上传文件列表
        uploadRef.value!.clearFiles()
      }
    })

  }
};

const input = ref('')
const format = ref('xlsx')
//导出模板表头
const column = [
  {label:'学号(工号)', name: 'sno'},
  {label:'姓名', name: 'name'},
  {label:'电话', name: 'phone'},
  {label:'角色', name: 'role'},
  {label:'备注', name: 'remark'},
]

//导出模板excel
const exportToExcelTemplate=()=> {
  ElMessageBox.confirm(
          '确定下载模板？',
          'Warning',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
          }
  )
          .then(() => {
            exportExcel({
              column,
              data:[],
              filename: input.value || '用户excel模板',
              format: format.value,
              autoWidth: true,
            })
          })
          .catch(() => {
          })
}


const handleSizeChange = (val: number) => {
  formInline.pageSize = val;
  getData()
}

const handleCurrentChange = (val: number) => {
  formInline.page = val
  getData()
}

onMounted(()=>{
  setTimeout(()=>{
    loading.value = false
  },1000)
})

</script>
<style lang="scss" scoped>
@import "../index";
</style>
