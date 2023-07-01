<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="rules"
            label-width="100px"
    >
      <el-form-item label="使用前图片" prop="fileList">
        <el-image style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.beforeuse[0]"
                  :preview-src-list="ruleForm.beforeuse"
        ></el-image>
        <el-image v-if="ruleForm.beforeuse[1]!=null"
                  style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.beforeuse[1]"
                  :preview-src-list="ruleForm.beforeuse"
        ></el-image>
        <el-image v-if="ruleForm.beforeuse[2]!=null"
                  style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.beforeuse[2]"
                  :preview-src-list="ruleForm.beforeuse"
        ></el-image>
      </el-form-item>
      <el-form-item label="使用后图片" prop="afterfileList">
        <el-image style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.afteruse[0]"
                  :preview-src-list="ruleForm.afteruse"
        ></el-image>
        <el-image v-if="ruleForm.afteruse[1]!=null"
                  style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.afteruse[1]"
                  :preview-src-list="ruleForm.afteruse"
        ></el-image>
        <el-image v-if="ruleForm.afteruse[2]!=null"
                  style="width: 100px; height: 100px"
                  :zoom-rate="1.2"
                  fit="cover"
                  class="table-td-thumb"
                  :src="ruleForm.afteruse[2]"
                  :preview-src-list="ruleForm.afteruse"
        ></el-image>
      </el-form-item>
    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleClose(ruleFormRef)">确定</el-button>
        </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
import type { UploadProps, UploadUserFile } from 'element-plus'
import {defineEmits, reactive, ref} from "vue";
import {addOrUpdateUser} from "../../../api/user";
import {getRoleData} from "@/api/role";
import {getRoomTypeData} from "@/api/roomType";
import {addOrUpdateClassRoom} from "@/api/classroom";
import {checkApply} from "../../../api/applyRoom";
import {uploadPic} from "@/api/uploadPic";
import {updateClassRoomUse, updateRoomUsePic} from "@/api/roomUse";
const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref<boolean>(false)
const title = ref('上传')
const options = ref([])

const rules = reactive({

})

const ruleForm = reactive({
  id:'',
  beforeuse:[],
  afteruse:[]
})

function close() {
  //弹框消失
  dialogVisible.value = false
}

const show = (item={})=>{
  Object.keys(item).forEach(key=>{
    ruleForm[key] = item[key]
  })
  ruleForm.beforeuse = ruleForm.beforeuse.substring(1,ruleForm.beforeuse.length-1).split(',')
  ruleForm.afteruse = ruleForm.afteruse.substring(1,ruleForm.afteruse.length-1).split(',')
  dialogVisible.value = true
}


const handleClose = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //弹框消失
      dialogVisible.value = false
    }
  })
}



defineExpose({
  show,
})

</script>
<style lang="scss" scoped>

</style>

