<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="rules"
            label-width="100px"
    >
      <el-form-item label="审核" prop="state2">
        <el-select v-model="ruleForm.state2" class="m-2" placeholder="Select" size="large" clearable>
          <el-option  :key="0" label="待审核" :value="0"/>
          <el-option  :key="1" label="审核通过" :value="1"/>
          <el-option  :key="2" label="审核不通过" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item v-if="ruleForm.state2===2" label="未通过原因" prop="failreason">
        <el-input v-model="ruleForm.failreason" type="textarea" placeholder="请输入不通过原因"/>
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
  import {checkApply} from "../../../../api/applyRoom";  const ruleFormRef = ref<FormInstance>()
  const dialogVisible = ref<boolean>(false)
  const title = ref('审核')
  const options = ref([])

  const rules = reactive({
    failreason: [{ required: true, message: '请输入未通过原因', trigger: 'blur' }],
    state2: [{required: true, message: '请先审核', trigger: 'change',},],
  })

  const ruleForm = reactive({
    id:"",
    failreason:'',
    state2:''
  })

  function close() {
    ruleFormRef.value.resetFields()
    Object.keys(ruleForm).forEach(key=>{
      ruleForm[key] = null
    })
  }

  const show = (item={})=>{
    title.value = '审核'
    Object.keys(item).forEach(key=>{
      ruleForm[key] = item[key]
    })
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

        if (ruleForm.state2 === 1) {
          ruleForm.failreason =""
        }
        //执行方法
        checkApply(ruleForm).then(res=>{
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

