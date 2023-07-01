<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="rules"
            label-width="100px"
    >
      <el-form-item label="学号(工号)" prop="sno">
        <el-input v-model="ruleForm.sno"  disabled/>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="ruleForm.name"  disabled/>
      </el-form-item>
      <el-form-item label="教室地址" prop="address" >
        <el-input v-model="ruleForm.address" placeholder="请输入教室地址" disabled/>
      </el-form-item>
      <el-form-item label="教室名称" prop="roomname">
        <el-input v-model="ruleForm.roomname" placeholder="请输入教室名称" disabled/>
      </el-form-item>
      <el-form-item label="教室类型" prop="typeid">
        <el-select v-model="ruleForm.typeid" placeholder="请选择" style="width: 100%" disabled >
          <el-option v-for="(item, index) in options" :key="index" :label="item.roomtype" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="使用班级" prop="classes">
         <el-input v-model="ruleForm.classes" placeholder="请输入班级" />
      </el-form-item>
      <el-form-item label="周次" prop="weeks">
        <el-select
                clearable
                v-model="ruleForm.weeks"
                placeholder="Select"
                style="width: 240px"
        >
          <el-option
                  v-for="item in weeks"
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
                v-model="ruleForm.week"
                placeholder="Select"
                style="width: 240px"
        >
          <el-option
                  v-for="item in week"
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
                v-model="ruleForm.sectionList"
                multiple
                collapse-tags-tooltip
                multiple-limit="2"
                placeholder="Select"
                style="width: 240px"
                @change="checkEmpty()"
                @click="selectSection()"
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
      <el-form-item label="申请理由" prop="reason">
        <el-input autosize type="textarea" v-model="ruleForm.reason" placeholder="请输入申请理由"/>
      </el-form-item>
      <el-form-item v-if="ruleForm.role==='学生'" label="审核人" prop="checker">
        <el-select v-model="ruleForm.checker" placeholder="请选择审核人" style="width: 100%" >
          <el-option v-for="(item, index) in checkerList" :key="index" :label="item.name" :value="item.name"/>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleClose(ruleFormRef)">确定</el-button>
        </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
import {defineEmits, reactive, ref} from "vue";
import {addOrUpdateUser} from "../../../../api/user";
import {getRoleData} from "@/api/role";
import {getRoomTypeData} from "@/api/roomType";
import {addOrUpdateClassRoom} from "@/api/classroom";
import {applyClassRoom, checkEmptyData, getCheckerList} from "@/api/applyRoom";
import {useUserStore} from "@/store/modules/user";

const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref<boolean>(false)
const title = ref('申请教室')
const options = ref([])
const checkerList = ref([])

const rules = reactive({
  roomname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' },
  ],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
  classes: [{ required: true, message: '请输入班级', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入申请理由', trigger: 'blur' }],
  typeid: [{required: true, message: '请选择教室类型', trigger: 'change',},],
  weeks: [{required: true, message: '请选择周次', trigger: 'change',},],
  week: [{required: true, message: '请选择星期', trigger: 'change',},],
  sectionList: [{required: true, message: '请选择节次', trigger: 'change',},],
  checker: [{required: true, message: '请选择审核人', trigger: 'change',},],
})
const UserStore = useUserStore()

const ruleForm = reactive({
  sno: UserStore.userInfo.sno,
  name: UserStore.userInfo.name,
  role: UserStore.userInfo.role,
  address: '',
  roomname: '',
  classes:'',
  typeid:null,
  weeks:'',
  week:'',
  state:0,
  state2:'',
  sectionList:"",
  reason:"",
  checker:""
})


function close() {
  ruleFormRef.value.resetFields()
  Object.keys(ruleForm).forEach(key=>{
    ruleForm[key] = null
  })
}
//循环变量
let i;
//周次
const weeks = ref([])
for(i=1;i<=19;i++){
  weeks.value.push({
    value:i,
    label:"第"+i+"周"
  })
}
//星期
const week = ref([])
const w=['星期一','星期二','星期三','星期四','星期五','星期六','星期日']
for(i=1;i<=7;i++){
  week.value.push({
    value:i,
    label: w[i-1]
  })
}

//节次
let sectionList = ref([])

//点击加载下拉
const selectSection = () =>{
  for(i=1;i<=10;i++){
    sectionList.value.push({
      value:i,
      label:"第"+i+"节"
    })
  }
}

const show = (item={})=>{
  ruleForm.sno=UserStore.userInfo.sno
  ruleForm.name=UserStore.userInfo.name
  //获取教室类型下拉
  getRoomTypeData(null).then(res=>{
    options.value = res.data.data;
  })
  //获取审核人
  getCheckerList().then(res=>{
    checkerList.value = res.data.data
  })

    Object.keys(item).forEach(key=>{
      ruleForm[key] = item[key]
  })

  dialogVisible.value = true
}

// 通过defineEmits来接受父组件绑定在子组件的属性名，即为refresh，再通过emit来调用函数
// defineEmits可用与函数数组，即传入多个函数。88行代码
const emit = defineEmits(['refresh'])

const checkEmpty = (formEl: FormInstance | undefined) =>{
  if (ruleForm.weeks != "" && ruleForm.week != ""&&ruleForm.sectionList.length>0) {
    checkEmptyData(ruleForm).then(res => {
      if (!res.data.success) {
        ElMessage({
          message: '此节次不可申请!，请重新选择',
          type: 'error',
        })
        ruleForm.sectionList=[]
      }
    });
  } else {
    ElMessage({
      message: '请先填写周次和星期!',
      type: 'error',
    })
    ruleForm.sectionList=[]
  }
}

const handleClose = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //弹框消失
      dialogVisible.value = false
      if (ruleForm.role === "教师") {
        ruleForm.state=1
      }

      //执行方法
      applyClassRoom(ruleForm).then(res=>{
        if (res.data.success) {
          ElMessage({
            message: '操作成功',
            type: 'success',
          })
        }
        //调用父组件方法
        emit('refresh')
      });
    } else {
      ElMessage({
        message: '操作失败',
        type: 'error',
      })
    }
  })
}

defineExpose({
  show,
})

</script>
<style lang="scss" scoped>

</style>

