<template>
  <el-drawer v-model="dialogVisible" :title="title" size="50%" @close="close">
    <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="角色名称" prop="role">
        <el-input v-model="ruleForm.role" placeholder="请输入角色名称"/>
      </el-form-item>
      <el-form-item label="角色描述"  >
        <el-input v-model="ruleForm.remark"
                  type="textarea"
                  placeholder="请输入角色描述"/>
      </el-form-item>


    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleClose(ruleFormRef)">确定</el-button>
        </span>
    </template>
  </el-drawer>
</template>
<script lang="ts" setup>
  import {reactive, ref  , defineEmits , defineExpose} from "vue";
  import {FormInstance,ElMessage} from "element-plus";
  import {addOrUpdateRole} from '@/api/role'


  const ruleFormRef = ref<FormInstance>()
  const dialogVisible = ref(false)
  const title = ref('新增角色')
  const ruleForm = reactive({
    id: '',
    role: '',
    remark:null
  })


  //关闭
  function close() {
    ruleFormRef.value.resetFields()
    Object.keys(ruleForm).forEach(key=>{
      ruleForm[key] = null
    })
  }

  //回写
  const show = (item={})=>{
    title.value = '新增角色'
    if(item.role){
      title.value = '编辑角色'
      Object.keys(item).forEach(key=>{
        ruleForm[key] = item[key]
      })
    }
    dialogVisible.value = true
  }

  const rules = reactive({
    role: [
      { required: true, message: '请输入角色名称', trigger: 'blur' },
      { min: 2, max: 6, message: '长度在 2 到 6 个字符', trigger: 'blur' },
      {pattern: /^([\u4e00-\u9fa5]{2,6})$/, message: '请输入正确的汉字', trigger: 'blur'}
    ]
  })

  // 通过defineEmits来接受父组件绑定在子组件的属性名，即为refresh，再通过emit来调用函数
  // defineEmits可用与函数数组，即传入多个函数。88行代码
  const emit = defineEmits(['refresh'])


  //确定
  const handleClose = async (done: () => void) => {
    await ruleFormRef.value.validate((valid, fields) => {
      if (valid) {
        //弹框消失
        dialogVisible.value = false
        //执行方法
        addOrUpdateRole(ruleForm).then(res=>{
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

  // 子组件抛出函数，变量给父组件
  defineExpose({
    show,
  })

</script>
