## 下载依赖
```
 npm install
 cnpm install
 yarn 
 # npm install 安装失败，请升级 nodejs 到 16 以上，或尝试使用以下命令：
  npm install --registry=https://registry.npm.taobao.org
```
## 运行打包
```
 npm run dev
 npm run build 
```
## eslint+prettier
```
# eslint 检测代码
npm run lint

#prettier 格式化代码
npm run lint:prettier
```

## 文件目录结构
```
vue-admin-perfect
├─ public                 # 静态资源文件（忽略打包）
├─ src
│  ├─ api                 # API 接口管理
│  ├─ assets              # 静态资源文件
│  ├─ components          # 全局组件
│  ├─ config              # 全局配置项
│  ├─ hooks               # 常用 Hooks
│  ├─ layout              # 框架布局
│  ├─ routers             # 路由管理
│  ├─ store               # pinia store
│  ├─ styles              # 全局样式
│  ├─ utils               # 工具库
│  ├─ views               # 项目所有页面
│  ├─ App.vue             # 入口页面
│  └─ main.ts             # 入口文件
├─ .env                   # vite 常用配置
├─ .env.development       # 开发环境配置
├─ .env.production        # 生产环境配置
├─ .env.test              # 测试环境配置
├─ .eslintignore          # 忽略 Eslint 校验
├─ .eslintrc.cjs           # Eslint 校验配置
├─ .gitignore             # git 提交忽略
├─ .prettierignore        # 忽略 prettier 格式化
├─ .prettierrc.config.js         # prettier 配置
├─ index.html             # 入口 html
├─ yarn.lock      # 依赖包包版本锁
├─ package.json           # 依赖包管理
├─ README.md              # README 介绍
├─ tsconfig.json          # typescript 全局配置
└─ vite.config.ts         # vite 配置
```


