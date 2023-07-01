<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="教室类型" prop="roomtype">
        <el-input v-model="ruleForm.roomtype" placeholder="请输入教室类型名称"/>
      </el-form-item>
      <el-form-item label="状态" >
        <el-switch v-model="ruleForm.status" inline-prompt active-text="启用" inactive-text="禁用" :active-value="1"  :inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="备注"  >
        <el-input v-model="ruleForm.remark"
                  type="textarea"
                  placeholder="请输入备注"/>
      </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfim(ruleFormRef)">确定</el-button>
        </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
import {defineEmits, reactive, ref} from "vue";
import {addOrUpdateRoomType} from "@/api/roomType";

const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref<boolean>(false)
const title = ref('新增部门')
const ruleForm = reactive({
  id:'',
  roomtype: '',
  remark:'',
  status:1
})

const rules = reactive({
  roomtype: [{ required: true, message: '请输入教室类型名称', trigger: 'blur' },
    { min: 2, max: 6, message: '长度在 2 到 6 个字符', trigger: 'blur' },
    {pattern: /^([\u4e00-\u9fa5]{2,6})$/, message: '请输入正确的汉字', trigger: 'blur'}
    ],
})


function close() {
  ruleFormRef.value.resetFields()
  Object.keys(ruleForm).forEach(key=>{
    ruleForm[key] = null
  })
}

const show = (item={})=>{
  title.value = '新增教室类型'
  if(item.roomtype){
    title.value = '编辑教室类型'
    Object.keys(item).forEach(key=>{
      ruleForm[key] = item[key]
    })
  }
  dialogVisible.value = true
}

// 通过defineEmits来接受父组件绑定在子组件的属性名，即为refresh，再通过emit来调用函数
// defineEmits可用与函数数组，即传入多个函数。88行代码
const emit = defineEmits(['refresh'])

const handleConfim = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //弹框消失
      dialogVisible.value = false
      //执行方法
      addOrUpdateRoomType(ruleForm).then(res=>{
        if (res) {
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

