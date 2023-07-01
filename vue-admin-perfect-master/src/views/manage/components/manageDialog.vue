<template>
  <el-dialog @close="close" v-model="dialogVisible" :title="title" width="50%">
    <el-form
            ref="ruleFormRef"
            :model="ruleForm"
            :rules="rules"
            label-width="100px"
    >
      <el-form-item label="使用前图片" prop="fileList">
        <el-upload
                limit="3"
                v-model:file-list="fileList"
                action=""
                list-type="picture-card"
                :auto-upload="false"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :on-exceed="onExceed"
                :on-change="onchange"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
        <el-dialog v-model="uploadVisible">
          <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
      </el-form-item>
      <el-form-item label="使用后图片" prop="afterfileList">
        <el-upload
                limit="3"
                v-model:file-list="afterfileList"
                action=""
                list-type="picture-card"
                :auto-upload="false"
                :on-preview="afterPictureCardPreview"
                :on-remove="afterRemove"
                :on-exceed="afteronExceed"
                :on-change="afteronchange"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
        <el-dialog v-model="afteruploadVisible">
          <img w-full :src="afterImageUrl" alt="Preview Image" />
        </el-dialog>
      </el-form-item>
      <el-form-item label="上传" prop="upload" >
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitFile">上传到服务器</el-button>
      </el-form-item>

    </el-form>
    <template #footer>
        <span class="dialog-footer">
          <el-button @click="Cancel">取消</el-button>
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
import {delUploadFile, uploadPic} from "@/api/uploadPic";
import {updateClassRoomUse, updateRoomUsePic} from "@/api/roomUse";
const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref<boolean>(false)
const title = ref('上传')
const options = ref([])

const validatePass = (rule: any, value: any, callback: any) => {
  if (fileList.value.length == 0) {
    callback(new Error("请选择要上传的文件！"))
  }
  callback();
}


const validatePass2 = (rule: any, value: any, callback: any) => {
  if (afterfileList.value.length == 0) {
    callback(new Error("请选择要上传的文件！"))
  }
  callback();
}

const validatePass3 = (rule: any, value: any, callback: any) => {
  if (ruleForm.beforeusePic == null||ruleForm.beforeusePic=="") {
    callback(new Error("请先上传到服务器！"))
  }
  callback();
}

const rules = reactive({
  fileList: [
    {required: true, validator: validatePass, trigger: 'blur'}
  ],
  afterfileList: [
    {required: true, validator: validatePass2, trigger: 'blur'}
  ],
  upload: [
    {required: true, validator: validatePass3, trigger: 'blur'}
  ],
})





const ruleForm = reactive({
  id:'',
  beforeusePic:"",
  afterusePic:""
})

function Cancel() {
  if (ruleForm.beforeusePic !== null&&ruleForm.beforeusePic!=="") {
    delUploadFile(ruleForm).then(res=>{
      if (res.data.success) {
        ElMessage({
          message: '操作成功',
          type: 'success',
        })
      }
    })
  }
  ruleFormRef.value.resetFields()
  Object.keys(ruleForm).forEach(key=>{
    ruleForm[key] = null
  })
  fileList.value = []
  afterfileList.value = []
  dialogVisible.value = false
}

function close() {
  ruleFormRef.value.resetFields()
  Object.keys(ruleForm).forEach(key=>{
    ruleForm[key] = null
  })
  fileList.value = []
  afterfileList.value = []
  dialogVisible.value = false
}

const show = (item={})=>{
  Object.keys(item).forEach(key=>{
    ruleForm[key] = item[key]
  })
  dialogVisible.value = true
}

//上传使用前图片列表
const fileList = ref<UploadUserFile[]>([])
//上传使用后图片列表
const afterfileList = ref<UploadUserFile[]>([])

const dialogImageUrl = ref('')
const afterImageUrl = ref('')

const uploadVisible = ref(false)
const afteruploadVisible = ref(false)


//文件列表移除文件时的钩子
const handleRemove: (uploadFile, uploadFiles) => void = (uploadFile, uploadFiles) => {
  console.log(uploadFiles)
}

//文件列表移除文件时的钩子
const afterRemove: (uploadFile, uploadFiles) => void = (uploadFile, uploadFiles) => {
  console.log(uploadFiles)
}

//点击文件列表中已上传的文件时的钩子
const handlePictureCardPreview: (uploadFile) => void = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  uploadVisible.value = true
}

//点击文件列表中已上传的文件时的钩子
const afterPictureCardPreview: (uploadFile) => void = (uploadFile) => {
  afterImageUrl.value = uploadFile.url!
  afteruploadVisible.value = true
}

//当超出限制时，执行的钩子函数
const onExceed: (uploadFile) => void = (uploadFile) => {
  ElMessage({
    message: '只允许上传三张图片',
    type: 'success',
  })
}

//当超出限制时，执行的钩子函数
const afteronExceed: (uploadFile) => void = (uploadFile) => {
  ElMessage({
    message: '只允许上传三张图片',
    type: 'success',
  })
}

//文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
const onchange:(uploadFile, uploadFiles) => void = (uploadFile, uploadFiles) => {
  let isRightSize = uploadFile.size / 1024 / 1024 < 2;
  var testmsg=uploadFile.name.substring(uploadFile.name.lastIndexOf('.')+1)
  let suffixArray = ['jpg','png','jpeg'];
  if (suffixArray.indexOf(testmsg) === -1 || !isRightSize) {
    ElMessage({
      message: '只支持 jpg、jpeg、png格式的文件并且大小不能超过2MB！',
      type: 'error'
    });
    fileList.value.pop();
  } else {
    fileList.value.push(uploadFiles);
  }
}

//文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
const afteronchange:(uploadFile, uploadFiles) => void = (uploadFile, uploadFiles) => {
  let isRightSize = uploadFile.size / 1024 / 1024 < 2;
  var testmsg=uploadFile.name.substring(uploadFile.name.lastIndexOf('.')+1)
  let suffixArray = ['jpg','png','jpeg'];
  if (suffixArray.indexOf(testmsg) === -1 || !isRightSize) {
    ElMessage({
      message: '只支持 jpg、jpeg、png格式的文件并且大小不能超过2MB！',
      type: 'error'
    });
    afterfileList.value.pop();
  } else {
    afterfileList.value.push(uploadFiles);
  }
}

const submitFile:(uploadFile, uploadFiles) => void = (uploadFile, uploadFiles) => {
  if (fileList.value.length === 0) {
    ElMessage({
      message: '请上传图片',
      type: 'error'
    });
    return
  }

  let formData = new FormData() //创建一个表单
    fileList.value.forEach(file=>{
      formData.append("beforefiles",file.raw)//将文件传到表单中，files属性是后端接受的参数名
    })
    afterfileList.value.forEach(file=>{
      formData.append("afterfiles",file.raw)//将文件传到表单中，files属性是后端接受的参数名
  })


  uploadPic(formData).then(res=>{
      ElMessage({
        message: '上传成功！',
        type: 'success',
      });
      ruleForm.beforeusePic = res.data.data.before;
      ruleForm.afterusePic = res.data.data.after
    })
}



// 通过defineEmits来接受父组件绑定在子组件的属性名，即为refresh，再通过emit来调用函数
// defineEmits可用与函数数组，即传入多个函数。88行代码
const emit = defineEmits(['refresh'])

const handleClose = async (done: () => void) => {
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      //执行方法
      updateRoomUsePic(ruleForm).then(res=>{
        if (res.data.success) {
          ElMessage({
            message: '操作成功',
            type: 'success',
          })
        }
        //调用父组件方法
        emit('refresh')
        //弹框消失
        dialogVisible.value = false
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

