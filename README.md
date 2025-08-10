# Toolsbox - 快捷工具箱

<p align="center">
  <img src="app\src\main\res\mipmap-xxxhdpi\ic_launcher.webp" alt="Toolsbox Logo" width="128" height="128">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white" alt="Jetpack Compose">
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="License: MIT">
  </a>
  <a href="https://github.com/lukechern/Toolsbox_app">
    <img src="https://img.shields.io/badge/version-2.1-blue.svg?style=for-the-badge" alt="Version">
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
- 📋 **剪贴板自动填充** - 自动读取系统剪贴板内容并填充到网页指定文本框 / **Clipboard Auto-Fill** - Automatically read system clipboard content and fill it into the specified text box on the web page

## 🎮 操作指南 / User Guide

### 工具使用 / Tool Usage
1. 打开应用进入工具页面
2. 点击右上角菜单按钮打开工具列表
3. 选择需要使用的工具，自动加载到WebView中

### 工具管理 / Tool Management
1. 切换到配置页面
2. 点击右下角"+"按钮添加新工具
3. 填写工具名称、简介、网址和排序序号
4. 编辑或删除现有工具项

### 剪贴板自动填充 / Clipboard Auto-Fill

Toolsbox 支持自动读取系统剪贴板内容，并将其填充到网页中的指定文本框。这对于需要频繁输入相同内容的在线工具非常有用。

#### 工作原理 / How It Works

1.  **读取剪贴板**: 当应用启动或从后台恢复时，应用会尝试读取系统剪贴板的内容。
2.  **页面加载完成**: 当 WebView 中的页面加载完成后，应用会检查当前工具是否配置了剪贴板目标文本框 ID。
3.  **自动填充**: 如果配置了目标 ID 且剪贴板内容不为空，应用会通过 JavaScript 将剪贴板内容填充到页面上具有该 ID 的文本框中。

#### 配置参数 / Configuration Parameters

要在工具中启用剪贴板自动填充功能，需要在添加或编辑工具时配置 `剪贴板目标文本框ID` 参数。

-   **参数名称**: `剪贴板目标文本框ID` / `Clipboard Target Textbox ID`
-   **参数说明**: 这是网页上目标文本框的 HTML ID 属性值。应用将使用此 ID 通过 `document.getElementById()` 来定位并填充文本框。
-   **填写示例**: 如果网页上目标文本框的 HTML 代码是 `<input type="text" id="content-input">`，那么你应该在此参数中填写 `content-input`。
-   **注意事项**: 
    - 如果此参数为空或未配置，应用将不会执行自动填充操作。
    - 确保网页上的文本框具有唯一的 ID 属性。
    - 应用在后台时无法访问剪贴板（这是 Android 10+ 的安全限制），只有在应用进入前台时才会尝试读取剪贴板内容。

### 编译方法 / Build Instructions
#### 环境要求 / Requirements
- Android Studio Arctic Fox (2020.3.1) 或更高版本
- Android SDK API 36 (Android 15)
- Kotlin 2.0.21 或更高版本
- Gradle 8.11.1 或更高版本

#### 编译步骤 / Build Steps
1. 克隆项目到本地 / Clone the project locally
   ```bash
   git clone https://github.com/lukechern/Toolsbox_app.git
   ```
2. 使用Android Studio打开项目 / Open the project with Android Studio
3. 等待Gradle同步完成 / Wait for Gradle sync to complete
4. 连接Android设备或启动模拟器 / Connect an Android device or start an emulator
5. 点击运行按钮或使用快捷键编译安装 / Click the run button or use shortcut to build and install
   - Windows/Linux: `Shift + F10`
   - macOS: `^R`

#### 构建APK / Build APK
- 调试版本 / Debug Build
  ```bash
  ./gradlew assembleDebug
  ```
- 发布版本 / Release Build
  ```bash
  ./gradlew assembleRelease
  ```

构建的APK文件位置：
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

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

Thanks to the following technologies and tools that made this project possible:

- **AI Large Models** - Providing intelligent code generation and problem-solving capabilities
- **AI Code Editors** - Improving development efficiency with intelligent completion and error detection
- **Android Studio** - Powerful IDE support
- **GitHub Copilot** - Intelligent code suggestions
- **Open Source Community** - Rich open-source libraries and documentation resources

## 📞 联系方式 / Contact

- **GitHub仓库**: [https://github.com/lukechern/Toolsbox_app](https://github.com/lukechern/Toolsbox_app)
- **问题反馈**: [Issues](https://github.com/lukechern/Toolsbox_app/issues)
- **贡献代码**: 欢迎提交Pull Request

## ⚠️ 免责条款 / Disclaimer

本项目app为爱发电，开源分享给全人类，不提供任何服务，不承担任何相关责任。用户在使用本应用时应自行承担所有风险，包括但不限于数据丢失、设备损坏、隐私泄露等。开发者不对因使用本应用而导致的任何直接或间接损失负责。

This project app is powered by love and shared open-source with all humanity. It provides no services and assumes no responsibility. Users should bear all risks when using this application, including but not limited to data loss, device damage, privacy leaks, etc. The developer is not responsible for any direct or indirect losses caused by using this application.

## 📄 许可证 / License

本项目采用MIT许可证，详情请见[LICENSE](LICENSE)文件。