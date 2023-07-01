<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="学号(工号)" prop="sno" >
        <el-input v-model="ruleForm.sno" placeholder="请输入学号（工号）"/>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="ruleForm.name" placeholder="请输入姓名"/>
      </el-form-item>
      <el-form-item label="电话" prop="phone" >
        <el-input v-model="ruleForm.phone" placeholder="请输入电话"/>
      </el-form-item>
      <el-form-item label="关联角色" prop="roleid">
        <el-select v-model="ruleForm.roleid" placeholder="请选择" style="width: 100%" >
          <el-option v-for="(item, index) in roleList" :key="index" :label="item.role"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账户密码" prop="password">
        <el-input v-model="ruleForm.password"
                  placeholder="请输入新密码"
                  type="password" clearable/>
      </el-form-item>
      <el-form-item label="用户状态" >
        <el-switch v-model="ruleForm.status"  inline-prompt active-text="启用" inactive-text="禁用" :active-value="1"  :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="备注" prop="remark" >
        <el-input v-model="ruleForm.remark"
                  type="textarea"
                  placeholder="请输入备注"/>
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

const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref<boolean>(false)
const title = ref('新增用户')
const roleList = ref([])

const rules = reactive({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' },
    {pattern: /^([\u4e00-\u9fa5]{2,5})$/, message: '请输入正确姓名', trigger: 'blur'}
  ],
  sno: [{ required: true, message: '请输入学号(工号)', trigger: 'blur' },
        {pattern: /^[1-9][0-9]{5,12}$/, message: '请输入正确的6-13位学号或工号,并且不能0开头', trigger: 'blur'},],
  phone: [{ required: true, message: '请输入联系方式', trigger: 'blur' },
          {pattern: /^[1]([3-9])[0-9]{9}$/, message: '请正确输入11位手机号码', trigger: 'blur'}],
  roleid: [{required: true, message: '请选择角色', trigger: 'change',},],
})

const ruleForm = reactive({
  id:"",
  sno: '',
  name: '',
  roleid:null,
  status: null,
  remark:'',
  password:null

})

function close() {
  ruleFormRef.value.resetFields()
  Object.keys(ruleForm).forEach(key=>{
     ruleForm[key] = null
  })
}

const show = (item={})=>{
  getRoleData(undefined).then(res => {
    roleList.value = res.data.data
  })

  title.value = '新增用户'
  if(item.sno){
    title.value = '编辑用户'
    Object.keys(item).forEach(key=>{
      ruleForm[key] = item[key]
    })
  }
  dialogVisible.value = true
}

// 通过defineEmits来接受父组件绑定在子组件的属性名，即为refresh，再通过emit来调用函数
// defineEmits可用与函数数组，即传入多个函数。88行代码
const emit = defineEmits(['refresh'])

const handleClose = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //弹框消失
      dialogVisible.value = false

      //执行方法
      addOrUpdateUser(ruleForm).then(res=>{
        if (res.data.success) {
          ElMessage({
            message: '操作成功',
            type: 'success',
          })
        }

        //调用父组件方法
        emit('refresh')
      })
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

