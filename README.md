# Toolsbox - 快捷工具箱

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="Toolsbox Logo" width="128" height="128">
</p>

<p align="center">
  <a href="https://github.com/your-username/Toolsbox/releases">
    <img src="https://img.shields.io/badge/version-1.0-blue.svg?style=flat-square" alt="Version 1.0">
  </a>
  <a href="https://github.com/your-username/Toolsbox/stargazers">
    <img src="https://img.shields.io/github/stars/your-username/Toolsbox.svg?style=flat-square" alt="GitHub Stars">
  </a>
  <a href="https://github.com/your-username/Toolsbox/issues">
    <img src="https://img.shields.io/github/issues/your-username/Toolsbox.svg?style=flat-square" alt="GitHub Issues">
  </a>
  <a href="https://github.com/your-username/Toolsbox/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/your-username/Toolsbox.svg?style=flat-square" alt="GitHub License">
  </a>
</p>

<p align="center">
  <a href="https://github.com/your-username/Toolsbox/stargazers">
    <img src="https://img.shields.io/github/stars/your-username/Toolsbox.svg?style=flat-square" alt="GitHub Stars">
  </a>
  <a href="https://github.com/your-username/Toolsbox/issues">
    <img src="https://img.shields.io/github/issues/your-username/Toolsbox.svg?style=flat-square" alt="GitHub Issues">
  </a>
  <a href="https://github.com/your-username/Toolsbox/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/your-username/Toolsbox.svg?style=flat-square" alt="GitHub License">
  </a>
</p>

## 📱 功能概述 / Feature Overview

Toolsbox是一款简洁高效的工具箱应用，专为收集和快速访问常用在线工具而设计。将您经常使用的工具网址集中管理，一键快速打开，告别繁琐的书签查找和广告干扰。

Toolsbox is a concise and efficient toolbox application designed to collect and quickly access commonly used online tools. It centralizes the management of your frequently used tool URLs, allowing one-click quick access while eliminating the hassle of bookmark searching and ad interference.

### 核心特性 / Core Features
- 🚀 **极速启动** - 轻量级应用，秒速启动 / **Lightning Fast Startup** - Lightweight app, instant launch
- 🚫 **无广告体验** - 纯净界面，专注工具使用 / **Ad-Free Experience** - Clean interface, focused tool usage
- 📚 **工具管理** - 随时随地添加、编辑、删除工具 / **Tool Management** - Add, edit, and delete tools anytime, anywhere
- 🎯 **智能排序** - 自定义工具排序，重要工具优先显示 / **Smart Sorting** - Custom tool ordering, important tools displayed first
- 🌐 **一键访问** - 点击即达，无需复制粘贴 / **One-Click Access** - Click to reach, no copy-pasting needed
- 📱 **响应式设计** - 适配各种屏幕尺寸 / **Responsive Design** - Adapts to various screen sizes

## 🎮 操作指南

### 工具使用
1. 打开应用进入工具页面
2. 点击右上角菜单按钮打开工具列表
3. 选择需要使用的工具，自动加载到WebView中

### 工具管理
1. 切换到配置页面
2. 点击右下角"+"按钮添加新工具
3. 填写工具名称、简介、网址和排序序号
4. 编辑或删除现有工具项

## 🛠 技术栈 / Tech Stack

- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **框架**: Android Jetpack Components
- **UI库**: Material Design Components
- **导航**: Navigation Component
- **异步处理**: Kotlin Coroutines
- **数据序列化**: Gson
- **数据存储**: SharedPreferences
- **构建工具**: Gradle (Kotlin DSL)
- **最低支持**: Android 7.0 (API 24)

### 主要依赖 / Main Dependencies
```kotlin
// Android核心组件
implementation "androidx.core:core-ktx:1.16.0"
implementation "androidx.appcompat:appcompat:1.6.1"
implementation "androidx.constraintlayout:constraintlayout:2.1.4"
implementation "com.google.android.material:material:1.10.0"

// 生命周期管理
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.9.1"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1"

// 导航组件
implementation "androidx.navigation:navigation-fragment-ktx:2.6.0"
implementation "androidx.navigation:navigation-ui-ktx:2.6.0"

// 数据处理
implementation "com.google.code.gson:gson:2.10.1"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

// UI组件
implementation "androidx.coordinatorlayout:coordinatorlayout:1.2.0"
implementation "androidx.cardview:cardview:1.0.0"
implementation "androidx.drawerlayout:drawerlayout:1.2.0"
```

## 📁 项目结构 / Project Structure

```
app/src/main/java/com/x7ree/Toolsbox/
├── MainActivity.kt                    # 主Activity
├── data/
│   ├── model/
│   │   └── ToolItem.kt               # 工具项数据模型
│   └── repository/
│       └── ToolRepository.kt         # 数据仓库
├── ui/
│   ├── home/                         # 工具页面
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   └── adapter/
│   │       └── ToolDrawerAdapter.kt
│   └── dashboard/                    # 配置页面
│       ├── DashboardFragment.kt
│       ├── DashboardViewModel.kt
│       ├── adapter/
│       │   └── ToolItemAdapter.kt
│       └── dialog/
│           └── AddEditToolDialog.kt
└── utils/
    └── SampleDataHelper.kt           # 示例数据工具类
```

## 🙏 致谢 / Acknowledgements

感谢以下技术和工具让这个项目成为可能：

- **AI大模型** - 提供智能代码生成和问题解决能力
- **AI代码编辑器** - 提高开发效率，智能补全和错误检测
- **Android Studio** - 强大的IDE支持
- **GitHub Copilot** - 智能代码建议
- **开源社区** - 丰富的开源库和文档资源

## 📞 联系方式 / Contact

- **GitHub仓库**: [https://github.com/your-username/Toolsbox](https://github.com/your-username/Toolsbox)
- **问题反馈**: [Issues](https://github.com/your-username/Toolsbox/issues)
- **贡献代码**: 欢迎提交Pull Request

## 📄 许可证 / License

本项目采用MIT许可证，详情请见[LICENSE](LICENSE)文件。

## ⚠️ 免责条款 / Disclaimer

本项目app为爱发电，开源分享给全人类，不提供任何服务，不承担任何相关责任。用户在使用本应用时应自行承担所有风险，包括但不限于数据丢失、设备损坏、隐私泄露等。开发者不对因使用本应用而导致的任何直接或间接损失负责。