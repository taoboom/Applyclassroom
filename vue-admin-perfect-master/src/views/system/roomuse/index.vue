<template>
  <div class="app-container" ref="appContainer">
    <PropTable
        :loading="loading"
        @selection-change="selectionChange"
        @handleCurrentChange="CurrentChange"
        @handleSizeChange="SizeChange"
        :columns="column"
        :data="list"
        :totals="total"
        :currentPage1="currentPage"
        @reset="reset"
        @onSubmit="onSubmit"
    >
      <template v-slot:btn>
        <div style="display: flex; justify-content: space-between">
          <div class="left" style="display: flex;margin-left:12px">
            <el-upload
                    ref="uploadRef"
                    v-model:file-list="fileList"
                    class="upload-demo"
                    action
                    multiple
                    :on-change="handleChanges"
                    :limit="1"
                    :show-file-list="false"
                    :auto-upload="false"
                    accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
            >
              <el-tooltip class="item" effect="dark" content="excel表头必须包含：['教室名称','课程名称','使用教师','使用班级','申请人','使用周次','使用星期','使用节次']" placement="top">
                <el-button type="primary">导入excel</el-button>
              </el-tooltip>
            </el-upload>
            <el-button style="margin-left: 12px" type="primary"  @click="exportToExcelTemplate">导出模板<i class="el-icon-upload el-icon--right"></i></el-button>
            <el-button type="primary"  @click="exportToExcel">导出Excel<i class="el-icon-upload el-icon--right"></i></el-button>
          </div>
          <el-button type="danger" @click="batchDelete"
          ><el-icon><delete /></el-icon>批量删除</el-button
          >
        </div>
      </template>

      <template v-slot:operation="scope">
        <el-button type="primary" size="small" icon="Edit" @click="edit(scope.row)">
          编辑
        </el-button>
        <el-button @click="del(scope.row)" type="danger" size="small" icon="Delete">
          删除
        </el-button>
      </template>
    </PropTable>

    <el-dialog v-model="dialogVisible" :title="title" width="50%">
      <el-form
          ref="ruleFormRef"
          :model="ruleForm"
          :rules="rules"
          :show-file-list="false"
          label-width="120px"
          class="demo-ruleForm"
          :size="formSize"
      >
        <el-form-item label="使用教师" prop="teacher">
          <el-input v-model="ruleForm.teacher" />
        </el-form-item>
        <el-form-item label="课程名称" prop="course">
          <el-input v-model="ruleForm.course" />
        </el-form-item>
        <el-form-item label="使用班级" prop="classes">
          <el-input v-model="ruleForm.classes" />
        </el-form-item>
        <el-form-item label="使用周次" prop="weeks">
          <el-input v-model="ruleForm.weeks" />
        </el-form-item>
        <el-form-item label="使用星期" prop="week">
          <el-input v-model="ruleForm.week" />
        </el-form-item>
        <el-form-item label="使用节次" prop="section">
          <el-input v-model="ruleForm.section" />
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleClose(ruleFormRef)">确定</el-button>
          </span>
      </template>
    </el-dialog>
  </div>
</template>
<script lang="ts" setup name="comprehensive">
import {ref, reactive, onMounted, nextTick} from 'vue'
import * as dayjs from 'dayjs'
import {ElMessage, ElMessageBox, UploadInstance, UploadUserFile} from 'element-plus'
import type { FormInstance } from 'element-plus'
const loading = ref(true)
const appContainer = ref(null)
import PropTable from '@/views/system/roomuse/PropTable/index'
import {getRoomTypeData} from "@/api/roomType";
import {delRoomUse, getRoomUseData, updateClassRoomUse, excelImport} from "@/api/roomUse";
import {exportExcel} from "@/utils/exprotExcel";

//表格列
const column = [
  { type: 'selection',fixed:true, width: 60 ,fixed: 'left'},
  { name: 'roomname', label: '教室名称', inSearch: true, valueType: 'input', width: 100},
  { name: 'course', label: '课程名称', inSearch: true, valueType: 'input', width: 200},
  { name: 'teacher', label: '使用教师', inSearch: true, valueType: 'input', width: 100 },
  { name: 'classes', label: '使用班级', inSearch: true, valueType: 'input', width: 200 },
  { name: 'apply', label: '申请人'},
  { name: 'weeks', label: '使用周次', inSearch: true, valueType: 'input'},
  { name: 'week', label: '使用星期', sorter: true, inSearch: true, valueType: 'input'},
  { name: 'section', label: '使用节次',sorter: true, inSearch: true, valueType: 'input'},
  // { name: 'beforeuse', label: '使用前图片' },
  // { name: 'afteruse', label: '使用后图片' },
  { name: 'operation',fixed:"right", slot: true, fixed: 'right', width: 200,label: '操作'  },
]
//表格数据
const list = ref([])
let total = Number
const currentPage = ref()

const formSize = ref('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive({
  id:'',
  teacher: '',
  classes: '',
  weeks:'',
  week:'',
  section:'',
  course:''
})

const rules = reactive({
  teacher: [
    { required: true, message: '请输入使用教师', trigger: 'blur' },
    { min: 3, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' },
  ],
  classes: [{ required: true, message: '请输入班级', trigger: 'blur' }],
  weeks: [{ required: true, message: '请输入周次', trigger: 'blur' }],
  week: [{ required: true, message: '请输入星期', trigger: 'blur' }],
  section: [{ required: true, message: '请输入节次', trigger: 'blur' }],
  course: [{ required: true, message: '请输入节次', trigger: 'blur' }],
})

const dialogVisible = ref(false)
const title = ref('新增')
const rowObj = ref({})
const selectObj = ref([])
//默认上传文件
const fileList = ref<UploadUserFile[]>([]);

//upload的ref
const uploadRef = ref<UploadInstance>()

//初始查询条件
let condition = reactive({
  roomname : '',
  teacher : '',
  class : '',
  weeks : '',
  week : '',
  section : '',
  page : 1, //当前页
  pageSize : 10  //一页多少条
})


const getData = (val) =>{
  loading.value = true
  //查询教室使用情况数据
  getRoomUseData(val).then(res => {
      list.value = res.data.data.records;
    console.log(res.data.data.records)
      total = res.data.data.total
      // list = res.data.data.total
      loading.value = false;
  })
}

//打开页面先查询数据
getData(condition)

const onSubmit = (val) => {
  condition = val;
  getData(condition)
}

//编辑按钮
const edit = (row) => {
  title.value = '编辑'
  rowObj.value = row
  dialogVisible.value = true
  ruleForm.id = row.id
  ruleForm.teacher = row.teacher
  ruleForm.classes = row.classes
  ruleForm.weeks = row.weeks
  ruleForm.week = row.week
  ruleForm.section = row.section
  ruleForm.course = row.course
}


//编辑页面确认按钮方法
const handleClose = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //弹框消失
      dialogVisible.value = false
      //执行方法
      updateClassRoomUse(ruleForm).then(res=> {
        if (res.data.success) {
          ElMessage({
            message: '操作成功',
            type: 'success',
          })
        }
        //更新完重新查询
        getData(condition)
      })
    } else {
      ElMessage({
        message: '操作失败',
        type: 'error',
      })
    }
  })
}

//批量删除按钮
const batchDelete = () => {
  if (!selectObj.value.length) {
    return ElMessage.error('未选中任何行')
  }
  ElMessageBox.confirm('你确定要删除选中行吗?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  })
      .then(() => {
        delRoomUse(selectObj.value).then(res => {
          if (res.data.success) {
            ElMessage({
              message: '删除成功',
              type: 'success',
            });
            getData(condition);
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

//多选
const selectionChange = (val) => {
  selectObj.value = val
}

const CurrentChange = (val) => {
  condition.page = val;
  getData(condition)
}
const SizeChange = (val) => {
  condition.pageSize = val
  getData(condition)
}



//删除按钮方法
const del = (row) => {
  ElMessageBox.confirm('你确定要删除当前项吗?', '温馨提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true,
  })
      .then(() => {
        delRoomUse([row]).then(res => {
          if (res.data.success) {
            ElMessage({
              message: '删除成功',
              type: 'success',
            });
            getData(condition);
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

//重置
const reset = () => {
  loading.value = true
  getData(condition);
  loading.value = false
}

//excel上传
const handleChanges = (file, fileList) => {
  //判断格式是否正确
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
        getData(condition);
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


const excelColumn = [
  { name: 'roomname', label: '教室名称'},
  { name: 'course', label: '课程名称'},
  { name: 'teacher', label: '使用教师' },
  { name: 'classes', label: '使用班级' },
  { name: 'apply', label: '申请人'},
  { name: 'weeks', label: '使用周次'},
  { name: 'week', label: '使用星期'},
  { name: 'section', label: '使用节次'},
  // { name: 'beforeuse', label: '使用前图片' },
  // { name: 'afteruse', label: '使用后图片' },
]
//导出名称
const input = ref('')
const format = ref('xlsx')

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
              column:excelColumn,
              data:[],
              filename: input.value || '课表模板',
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
            exportExcel({
              column:excelColumn,
              data:list.value,
              filename: input.value || '教室使用情况表',
              format: format.value,
              autoWidth: true,
            })
          })
          .catch(() => {
          })
}



onMounted(() => {
  nextTick(()=>{
    // let data = appContainer.value.
  })
  setTimeout(() => {
    loading.value = false
  }, 500)
})
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.app-container{
  flex: 1;
  display: flex;
  width: 100%;
  padding: 16px;
  box-sizing: border-box;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
