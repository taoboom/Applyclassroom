<template>
  <el-dialog v-model="dialogVisible" title="修改密码" width="40%">
    <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      class="demo-ruleForm"
      :size="formSize"
    >
      <el-form-item label="学号(工号)">
        <el-input v-model="ruleForm.sno" disabled></el-input>
      </el-form-item>
      <el-form-item label="旧的密码" prop="password">
        <el-input v-model="ruleForm.password" type="password"></el-input>
      </el-form-item>
      <el-form-item label="新的密码" prop="newPassword">
        <el-input v-model="ruleForm.newPassword" type="password"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm(ruleFormRef)">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
  import { ref, defineExpose, reactive } from 'vue'
  import type {ElForm} from 'element-plus'
  import { ElMessage} from 'element-plus'
  const dialogVisible = ref(false)
  import {useUserStore} from "@/store/modules/user"
  import {modifyPassword} from "@/api/user";
  import {User} from "@element-plus/icons-vue";
  const UserStore = useUserStore()
  const show = () => {
    dialogVisible.value = true
  }
  const hide = () => {
    dialogVisible.value = false
  }
  type FormInstance = InstanceType<typeof ElForm>

  const formSize = ref('')
  const ruleFormRef = ref<FormInstance>()
  const ruleForm = reactive({
    id: UserStore.userInfo.id,
    sno: UserStore.userInfo.sno,
    password: '',
    newPassword: '',
  })
  const rules = reactive({
    newPassword: [{required: true, message: '请输入新的密码', trigger: 'blur',}],
    password: [{required: true, message: '请输入旧密码', trigger: 'blur',}],
  })
  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
      if (valid) {
        modifyPassword(ruleForm).then(res=>{
          if (res.data.success) {
            ElMessage({
              type: "success",
              message: "修改密码成功！"
            });
            dialogVisible.value = false
            ruleForm.password='';
            ruleForm.newPassword='';
          } else {
            ElMessage({
              type: "error",
              message: res.data.errorMsg
            });
          }
        })
      } else {
        console.log('error submit!')
        return false
      }
    })
  }

  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  defineExpose({
    show,
  })
</script>

<style scoped>
  .dialog-footer button:first-child {
    margin-right: 10px;
  }
</style>
