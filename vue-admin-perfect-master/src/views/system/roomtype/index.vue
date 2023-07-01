<template>
  <div class="app-container">
    <div class="header">
      <el-form :inline="true" :model="formInline" ref="ruleFormRef">
        <el-form-item label="教室类型名称" prop="roomtype">
          <el-input v-model="formInline.roomtype" placeholder="请输入教室类型名称"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">
      <div class="util">
        <el-button type="primary" @click="addHandler">
          <el-icon><Plus /></el-icon>
          新增教室类型
        </el-button>
      </div>
      <div class="table-inner">
        <el-table
            v-loading="loading"
            row-key="id"
            :data="tableData"
            style="width: 100%"
            border>
          <el-table-column type="index" label="序号"  width="100" align="center"/>
          <el-table-column prop="roomtype" label="教室类型名称" align="center"/>
          <el-table-column prop="status" label="状态" align="center">
            <template #default="scope">
              <el-switch
                  inline-prompt
                  active-text="启用" inactive-text="禁用"
                  :active-value="1"  :inactive-value="0"
                  v-model="scope.row.status" @change="changeStatus(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="remark"
                           :show-overflow-tooltip="true"
                           width="300"
                           label="备注" align="center"/>

          <el-table-column prop="operator" label="操作" width="200px" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" icon="Edit" @click="editHandler(scope.row)">
                编辑
              </el-button>
              <el-button @click="del(scope.row)" type="danger" size="small" icon="Delete">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--    @属性=“函数”，将函数传入子组件-->
    <RoomTypeDIalog ref="roomTypeDIalog" @refresh="getData"/>
  </div>
</template>

<script lang="ts" setup>
  import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
  import {Search } from '@element-plus/icons-vue'
  import {onMounted, reactive, ref} from 'vue'
  import { deptData } from '@/mock/system'
  import RoomTypeDIalog from './components/roomTypeDIalog.vue'
  import {delRole, getRoleData} from "@/api/role";
  import {addOrUpdateRoomType, delRoomType, getRoomTypeData} from "@/api/roomType";
  import {addOrUpdateUser} from "@/api/user";

  const tableData = ref([])
  const loading = ref(true)
  const dialogVisible = ref(false)
  const roomTypeDIalog = ref()
  const ruleFormRef = ref<FormInstance>()
  const formInline = reactive({})

  onMounted(()=>{
    setTimeout(()=>{
      loading.value = false
    },500)
  })


  function getData(){
    loading.value = true;//加载动画
    getRoomTypeData(formInline).then(res=>{
      tableData.value = res.data.data;
      loading.value = false;
    })
  }

  //打开页面先查询
  getData()

  const onSubmit = () => {
    getData()
  }

  //重置
  const reset = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
    getData()
  }

  const addHandler = () => {
    roomTypeDIalog.value.show()
  }
  const editHandler = (row) => {
    roomTypeDIalog.value.show(row)
  }

  const del = (row) => {
    ElMessageBox.confirm('你确定要删除当前项吗?', '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      draggable: true,
    })
      .then(() => {
        delRoomType(row).then(res => {
          if (res) {
            ElMessage({
              message: '删除成功',
              type: 'success',
            });
            getData();
          } else {
            ElMessage({
              message: '删除失败',
              type: 'error',
            });
          }
        })
      })
      .catch(() => {})
  }
  const changeStatus = (row) => {
    ElMessageBox.confirm(
      `确定要${!row.status ? '禁用' : '启用'} "${row.roomtype}" 该类型吗？`,
      '温馨提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      },
    )
      .then(async () => {
        addOrUpdateRoomType(row).then(res=>{
          if (res) {
            ElMessage({
              message: '操作成功',
              type: 'success',
            })
          }
        })
      })
      .catch(() => {
        if (row.status === 1) {
          row.status = 0;
        } else {
          row.status = 1;
        }
        console.log(row.status)
      })
  }
</script>

<style scoped lang="scss">
@import "./index";
</style>
