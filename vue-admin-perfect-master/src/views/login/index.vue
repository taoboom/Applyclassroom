<template>
  <div class="login-container">
    <div class="login-box" >
      <SwitchDark class="login-dark"/>
      <div class="login-left">
        <img src="@/assets/image/login/side-logo.png" >
      </div>
      <div class="login-form">
        <LoginForm v-if="accountLogin"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import {ref} from 'vue'
  import LoginForm from './components/LoginForm.vue'
  import SwitchDark from '@/components/SwitchDark/index.vue'

  const accountLogin = ref<boolean>(true)

  const handleClick = ()=>{
    console.log('=======',accountLogin)
    accountLogin.value=!accountLogin.value
  }
</script>
<style lang="scss" scoped>
  @import "./index";
</style>
