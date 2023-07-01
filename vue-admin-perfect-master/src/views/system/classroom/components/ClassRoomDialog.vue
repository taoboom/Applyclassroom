<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="rules"
            label-width="100px"
    >
      <el-form-item label="教室地址" prop="address" >
        <el-input v-model="ruleForm.address" placeholder="请输入教室地址"/>
      </el-form-item>
      <el-form-item label="教室名称" prop="roomname">
        <el-input v-model="ruleForm.roomname" placeholder="请输入教室名称"/>
      </el-form-item>
      <el-form-item label="教室类型" prop="typeid">
        <el-select v-model="ruleForm.typeid" placeholder="请选择" style="width: 100%" >
          <el-option v-for="(item, index) in options" :key="index" :label="item.roomtype" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="容纳人数" prop="contain">
        <el-input v-model="ruleForm.contain" placeholder="请输入容纳人数"/>
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
  import {getRoomTypeData} from "@/api/roomType";
  import {addOrUpdateClassRoom} from "@/api/classroom";

  const ruleFormRef = ref<FormInstance>()
  const dialogVisible = ref<boolean>(false)
  const title = ref('新增用户')
  const options = ref([])

  const rules = reactive({
    roomname: [
      { required: true, message: '请输入教室名称', trigger: 'blur' },
      { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' },
    ],
    address: [{ required: true, message: '请输入教室地址', trigger: 'blur' },
      { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' },
      {pattern: /^([\u4e00-\u9fa5]{2,8})$/, message: '请输入正确教室地址', trigger: 'blur'}],
    typeid: [{required: true, message: '请选择教室类型', trigger: 'change',},],
    contain: [
      { required: true, message: '请输入容纳人数', trigger: 'blur' },
      {pattern: /^[1-9][0-9]{1,2}$/, message: '请输入正确的2-3位数字', trigger: 'blur'},
    ],
  })

  const ruleForm = reactive({
    id:"",
    address: '',
    roomname: '',
    typeid:null,
    contain: '',
    remark:''
  })

  function close() {
    ruleFormRef.value.resetFields()
    Object.keys(ruleForm).forEach(key=>{
      ruleForm[key] = null
    })
  }

  const show = (item={})=>{
    //获取教室类型下拉
    getRoomTypeData(null).then(res=>{
      options.value = res.data.data;
    })

    title.value = '新增教室'
    if(item.id){
      title.value = '编辑教室'
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
        addOrUpdateClassRoom(ruleForm).then(res=>{
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

