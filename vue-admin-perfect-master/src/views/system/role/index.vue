<template>
  <div class="app-container">
    <div class="header">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" ref="ruleFormRef">
        <el-form-item label="角色名称" prop="role">
          <el-input v-model="formInline.role" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" :icon="Search">查询</el-button>
          <el-button @click="reset(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="footer">
      <div class="util">
        <el-button type="primary" @click="add">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
      </div>
      <div class="table-inner">
        <el-table
            v-loading="loading"
            :data="tableData" style="width: 100%" border>
          <el-table-column type="index" width="50" align="center"/>
          <el-table-column prop="id" label="Id" align="center"/>
          <el-table-column prop="role" label="角色名称" align="center" />
          <el-table-column prop="remark"
                           :show-overflow-tooltip="true"
                           align="center"
                           label="角色描述" />
          <el-table-column prop="status" label="操作" align="center">
            <template #default="scope">
              <el-button type="primary" size="small" icon="Edit" @click="edit(scope.row)">
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
    <RoleDrawer ref="roleDrawer" @refresh="getData"/>
  </div>
</template>

<script lang="ts" setup>
  import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
  import {onMounted, reactive, ref} from 'vue'
  import {Search } from '@element-plus/icons-vue'
  import RoleDrawer from './components/roleDrawer.vue'
  import { getRoleData , delRole } from '@/api/role'
  import methods from "codemirror/src/edit/methods";
  const tableData = ref([])

  const loading = ref(true)
  const roleDrawer = ref()
  const formSize = ref('default')
  const ruleFormRef = ref<FormInstance>()
  const formInline = reactive({})

  function getData(){
    loading.value = true;//加载动画
    getRoleData(formInline).then(res=>{
      tableData.value = res.data.data;
      loading.value = false;
    })
  }

//打开页面先查询
  getData()


  //查询
  const onSubmit = () => {
    getData()
  }

  //重置
  const reset = (formEl: FormInstance | undefined) => {
      if (!formEl) return
      formEl.resetFields()
      getData()
  }


  //添加
  const add = () => {
    roleDrawer.value.show()
  }

  //编辑
  const edit = (row) => {
    roleDrawer.value.show(row)
  }

  //删除
  const del = (row) => {
    ElMessageBox.confirm('你确定要删除当前项吗?', '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      draggable: true,
    })
      .then(() => {
          delRole(row).then(res => {
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
      .catch(() => {

      })
  }


  onMounted(()=>{
    //组件挂载的过程 包括数据 dom等
    setTimeout(()=>{
      loading.value = false
    },500)
  })


</script>

<style scoped lang="scss">
.header{
  display: flex;
  padding: 16px 16px 0px 16px;
  margin-bottom: 16px;
  border-radius: 4px;
  background: white;
  box-shadow: 0 0 12px rgb(0 0 0 / 5%);
}
.footer{
  flex: 1;
  display: flex;
  padding: 16px;
  flex-direction: column;
  border-radius: 4px;
  overflow: hidden;
  background: white;
  box-shadow: 0 0 12px rgb(0 0 0 / 5%);
  position: relative;
  box-sizing: border-box;
  .util{
    margin-bottom: 15px;
    display: flex;
    justify-content: flex-end;
    flex-shrink: 0;
  }
  .table-inner{
    flex: 1;
    position: relative;
  }
  .table{
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%
  }
}

</style>
