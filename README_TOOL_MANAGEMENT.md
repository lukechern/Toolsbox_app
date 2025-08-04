# 工具数据项管理功能说明

## 功能概述
为Toolsbox应用的配置页面添加了完整的工具数据项管理功能，支持工具项的增加、删除、修改操作。

## 功能特性
- ✅ 添加工具项：支持添加新的工具项
- ✅ 编辑工具项：支持修改现有工具项的信息
- ✅ 删除工具项：支持删除不需要的工具项
- ✅ 数据持久化：使用SharedPreferences保存数据
- ✅ 排序功能：支持按排序序号排列工具项
- ✅ 点击打开：点击工具项可直接打开对应网址

## 数据字段
每个工具项包含以下4个字段：
1. **工具名称** - 工具的显示名称
2. **工具简介** - 工具的简要描述
3. **工具网址** - 工具的访问链接
4. **排序序号** - 用于排序的数字（数字越小排序越靠前）

## 模块结构
```
app/src/main/java/com/x7ree/Toolsbox/
├── data/
│   ├── model/
│   │   └── ToolItem.kt                    # 工具项数据模型
│   └── repository/
│       └── ToolRepository.kt              # 数据仓库，负责数据管理
├── ui/dashboard/
│   ├── adapter/
│   │   └── ToolItemAdapter.kt             # RecyclerView适配器
│   ├── dialog/
│   │   └── AddEditToolDialog.kt           # 添加/编辑对话框
│   ├── DashboardFragment.kt               # 配置页面Fragment
│   └── DashboardViewModel.kt              # 配置页面ViewModel
```

## 使用方法

### 添加工具项
1. 在配置页面点击右下角的"+"按钮
2. 填写工具名称、简介、网址和排序序号
3. 点击"保存"按钮

### 编辑工具项
1. 在工具项卡片上点击"编辑"按钮
2. 修改需要更改的信息
3. 点击"保存"按钮

### 删除工具项
1. 在工具项卡片上点击"删除"按钮
2. 在确认对话框中点击"删除"

### 访问工具
- 点击工具项卡片的任意位置（除了编辑/删除按钮）即可打开对应的网址

## 技术实现

### 数据持久化
- 使用SharedPreferences存储数据
- 使用Gson进行JSON序列化/反序列化
- 数据变更时自动保存

### 架构模式
- 采用MVVM架构模式
- 使用Repository模式管理数据
- 使用StateFlow进行响应式数据更新

### UI组件
- RecyclerView + CardView展示工具列表
- FloatingActionButton添加新工具
- DialogFragment实现添加/编辑功能
- Material Design组件提供良好的用户体验

## 依赖项
项目添加了以下依赖项：
- Gson：用于JSON序列化
- Kotlin Coroutines：用于异步操作
- CoordinatorLayout：用于协调布局
- CardView：用于卡片式布局
- Parcelize：用于数据传递

## 注意事项
1. 排序序号必须为数字
2. 所有字段都是必填项
3. 网址格式会自动验证
4. 数据会自动保存，无需手动保存
5. 删除操作不可撤销，请谨慎操作