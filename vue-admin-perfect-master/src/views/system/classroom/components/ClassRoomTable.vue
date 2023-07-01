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
        <el-form-item label="教室类型" prop="typeid">
          <el-select v-model="formInline.typeid" filterable placeholder="请选择或输入教室类型">
            <el-option v-for="(item, index) in options" :key="index" :label="item.roomtype" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">
      <div class="util" style="justify-content: space-between">
        <div class="left" style="display: flex">
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
            <el-tooltip class="item" effect="dark" content="excel表头必须包含：['教室地址','教室类型','教室名称','容纳人数','备注']" placement="top">
               <el-button type="primary">导入excel</el-button>
            </el-tooltip>
          </el-upload>
          <el-button style="margin-left: 12px" type="primary"  @click="exportToExcelTemplate">导出模板<i class="el-icon-upload el-icon--right"></i></el-button>
          <el-button type="primary"  @click="exportToExcel">导出Excel<i class="el-icon-upload el-icon--right"></i></el-button>
        </div>


        <div class="right">
          <el-button type="primary" @click="addHandler">
            <el-icon><Plus /></el-icon>
            新增教室
          </el-button>
          <el-button type="danger" @click="batchDelete"
          ><el-icon><delete /></el-icon>批量删除</el-button
          >
        </div>

      </div>
      <div class="table-inner">
        <el-table @selection-change="handleSelectionChange"
            v-loading="loading"
            :data="tableData" style="width: 100%;height: 100%" border>
          <el-table-column type="selection" width="55" />
          <el-table-column fixed  type="index" width="50"  align="center"></el-table-column>
          <el-table-column prop="address" label="地址" align="center" width="150"/>
          <el-table-column prop="roomtype" label="教室类型" align="center"/>
          <el-table-column prop="roomname" label="教室名称" align="center" width="120"/>
          <el-table-column prop="contain" label="容纳人数" align="center" width="120"/>
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

    <ClassRoomDialog ref="classRoomDialog" @refresh="getData"/>
  </div>
</template>
<script lang="ts" setup>
import {ElMessageBox, ElMessage, FormInstance, UploadInstance} from 'element-plus'
import {onMounted, reactive, ref} from 'vue'
import {getRoomTypeData} from "@/api/roomType";
import {delClassRoom, excelImport, getClassRoomData} from "@/api/classroom";
import type { UploadProps, UploadUserFile } from 'element-plus'
import ClassRoomDialog from './ClassRoomDialog.vue'
import axios from "axios";
import {exportExcel} from "@/utils/exprotExcel";


const tableData = ref([])
const dialogVisible = ref(false)
const classRoomDialog = ref()
const ruleFormRef = ref<FormInstance>()
const formInline = reactive({
  roomname : '',
  address : '',
  typeid : '',
  page : 1, //当前页
  pageSize : 10  //一页多少条
})
const loading = ref(true)

//总条数
const total = ref()
//教室类型下拉数组
const options = ref([])
//多选数据
const multipleSelection = ref([])
//默认上传文件
const fileList = ref<UploadUserFile[]>([]);
//绑定上传组件用来调用方法清空上传列表
const uploadRef = ref<UploadInstance>()

function getData(){
  loading.value = true
  //获取教室类型下拉
  getRoomTypeData(null).then(res=>{
    options.value = res.data.data;
  })
  //查询教室
  getClassRoomData(formInline).then(res => {
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


//多选
const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

//批量删除
const batchDelete = () => {
  if (!multipleSelection.value.length) {
    return ElMessage.error('未选中任何行')
  }
  ElMessageBox.confirm('你确定要删除选中项吗?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  })
    .then(() => {
      delClassRoom(multipleSelection.value).then(res => {
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

//添加
const addHandler = () => {
  classRoomDialog.value.show()
}
//编辑
const editHandler = (row) => {
  classRoomDialog.value.show(row)
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
        delClassRoom([row]).then(res => {
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
  {label:'教室地址', name: 'address'},
  {label:'教室类型', name: 'roomtype'},
  {label:'教室名称', name: 'roomname'},
  {label:'容纳人数', name: 'contain'},
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
          filename: input.value || '教室表excel模板',
          format: format.value,
          autoWidth: true,
        })
      })
      .catch(() => {
      })
}

//导出excel数据
const exportToExcel=()=> {
  ElMessageBox.confirm(
    '确定导出数据？',
    'Warning',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      console.log(tableData.value)
      exportExcel({
        column,
        data:tableData.value,
        filename: input.value || '教室表',
        format: format.value,
        autoWidth: true,
      })
    })
    .catch(() => {
    })
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
