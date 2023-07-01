<template>
  <div class="login-title">
    <img class="icon" src="@/assets/image/logo.png" alt="logo" />
    <h2 class="title">线上教室申请系统</h2>
  </div>
  <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
  >
    <el-form-item label="" prop="sno">
      <el-input
          placeholder="请输入学号或工号"
          autoComplete="on"
          style="position: relative"
          v-model="ruleForm.sno"
          @keyup.enter.native="submitForm(ruleFormRef)"
      >
        <template #prefix>
          <el-icon class="el-input__icon"><UserFilled /></el-icon>
        </template>
      </el-input>
    </el-form-item>

    <el-form-item label="" prop="password">
      <el-input
          placeholder="请输入密码"
          autoComplete="on"
          @keyup.enter.native="submitForm(ruleFormRef)"
          v-model="ruleForm.password"
          :type="passwordType"
      >
        <template #prefix>
          <el-icon class="el-input__icon"><GoodsFilled /></el-icon>
        </template>
        <template #suffix>
          <div class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </div>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item style="width: 100%" label="角色:">
      <el-radio-group v-model="ruleForm.roleid" class="ml-4" >
        <el-radio v-for="(item, index) in roleList" :key="index" :label="item.id" style="width: 64px"
                  :value="item.id">{{item.role}}</el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item style="width: 100%"  prop="Verify">
        <Verify v-model:value="form.isVerifyPass"></Verify>
    </el-form-item>


    <el-form-item style="width: 100%">
      <el-button
          :loading="loading"
          class="login-btn"
          type="primary"
          @click="submitForm(ruleFormRef)"
      >登录</el-button
      >
    </el-form-item>
  </el-form>
</template>
<script lang="ts" setup>
import { ref, reactive } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import { ElNotification } from "element-plus";
import { useRouter } from 'vue-router'
import {useUserStore} from "@/store/modules/user"
import {getTimeStateStr} from '@/utils/index'
import {getRoleData} from "@/api/role";
import Verify from '@/views/login/verify/verify.vue'
import {login} from "@/api/user";
// 提交表单数据
const form = reactive({
  isVerifyPass: false, // 滑块验证结果
})

const router = useRouter()
const UserStore = useUserStore()
const ruleFormRef = ref<FormInstance>()
const passwordType = ref('password')
const loading = ref(false)
const roleList = ref([])


const validatePass = (rule: any, value: any, callback: any) => {
  if (!form.isVerifyPass) {
    callback(new Error("请先滑块验证！"))
  }
  callback();
}

const rules = reactive({
  sno: [{ required: true, message: "请输入学号(工号)", trigger: "blur" },
      // {pattern: /^[1-9][0-9]{8,12}$/, message: '请输入正确的9-13位学号或工号', trigger: 'blur'},
  ],
  username: [{ required: true, message: "请输入密码", trigger: "blur" }],
  Verify: [{ required: true, validator: validatePass, trigger: "blur" }],
})

// 表单数据
const ruleForm = reactive({
  sno: '',
  password: '',
  roleid:1
})

// 显示密码图标
const showPwd = () => {
  passwordType.value = passwordType.value === 'password'?'':'password'
}
//获取角色
getRoleData(undefined).then(res => {
  roleList.value = res.data.data
})


const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      loading.value = true
      login(ruleForm).then(res=>{
        if (res.data.success) {
          //登录
          setTimeout(async () => {
            await UserStore.login(res.data.data)
            await router.push({
              path: '/',
            })
            ElNotification({
              title: getTimeStateStr(),
              message: "欢迎登录 线上教室申请系统",
              type: "success",
              duration: 3000
            });
            loading.value = true
          }, 1000);
        } else {
          loading.value = false
          ElMessage({
            message: '用户名或密码错误',
            type: 'error',
          })
        }
      })
    } else {
      console.log('error submit!')
      return false
    }
  })
}

</script>

<style lang="scss" scoped>
@import "../index.scss";

</style>
