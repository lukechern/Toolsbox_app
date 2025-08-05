# Android WebView 剪贴板访问解决方案

## 问题描述
在 Android 应用的 WebView 中访问部署在 Cloudflare Pages 的网页时，JavaScript 无法正常获取系统剪贴板内容，而手机系统自带浏览器没有此问题。

## 解决方案

### 1. 权限配置
在 `app/src/main/AndroidManifest.xml` 中添加了剪贴板访问权限：
```xml
<uses-permission android:name="android.permission.READ_CLIPBOARD" />
<uses-permission android:name="android.permission.WRITE_CLIPBOARD" />
```

### 2. WebView 配置增强
在 `HomeFragment.kt` 中对 WebView 进行了以下配置：

#### 基础设置
- 启用文件访问和内容访问
- 允许混合内容模式
- 设置用户代理模拟桌面浏览器

#### 权限处理
- 重写 `WebChromeClient.onPermissionRequest()` 方法
- 自动授予所有权限请求，确保网页能够访问系统功能
- 包括剪贴板、音频、视频等权限的统一处理

#### JavaScript 接口
- 添加了 `ClipboardJavaScriptInterface` 类
- 提供 `readClipboard()` 和 `writeClipboard()` 方法
- 通过 `AndroidClipboard` 对象在网页中调用

### 3. 网络安全配置
在 `network_security_config.xml` 中添加了 Cloudflare Pages 相关域名：
- `pages.dev`
- `cloudflare.com`

### 4. 测试页面
创建了 `app/src/main/assets/clipboard_test.html` 测试页面，包含三种剪贴板访问方法：
1. 现代 Clipboard API
2. Android 接口方法
3. execCommand 兼容性方法

## 使用方法

### 在网页中使用 Android 接口
```javascript
// 读取剪贴板
function readClipboard() {
    if (typeof AndroidClipboard !== 'undefined') {
        const text = AndroidClipboard.readClipboard();
        console.log('剪贴板内容:', text);
        return text;
    }
    return '';
}

// 写入剪贴板
function writeClipboard(text) {
    if (typeof AndroidClipboard !== 'undefined') {
        const success = AndroidClipboard.writeClipboard(text);
        console.log('写入结果:', success);
        return success;
    }
    return false;
}
```

### 兼容性处理
建议在网页中使用以下兼容性代码：
```javascript
async function getClipboardText() {
    // 优先使用现代 Clipboard API
    if (navigator.clipboard && navigator.clipboard.readText) {
        try {
            return await navigator.clipboard.readText();
        } catch (err) {
            console.log('Clipboard API 失败，尝试 Android 接口');
        }
    }
    
    // 回退到 Android 接口
    if (typeof AndroidClipboard !== 'undefined') {
        return AndroidClipboard.readClipboard();
    }
    
    throw new Error('无法访问剪贴板');
}

async function setClipboardText(text) {
    // 优先使用现代 Clipboard API
    if (navigator.clipboard && navigator.clipboard.writeText) {
        try {
            await navigator.clipboard.writeText(text);
            return true;
        } catch (err) {
            console.log('Clipboard API 失败，尝试 Android 接口');
        }
    }
    
    // 回退到 Android 接口
    if (typeof AndroidClipboard !== 'undefined') {
        return AndroidClipboard.writeClipboard(text);
    }
    
    return false;
}
```

## 测试方法

1. 编译并安装应用
2. 在应用中加载测试页面：`file:///android_asset/clipboard_test.html`
3. 测试各种剪贴板操作方法
4. 验证与 Cloudflare Pages 网站的兼容性

## 注意事项

1. **权限请求**：应用会自动处理 WebView 的权限请求，无需用户手动授权
2. **HTTPS 要求**：现代 Clipboard API 通常要求 HTTPS 环境
3. **用户交互**：某些浏览器要求剪贴板操作必须在用户交互事件中触发
4. **兼容性**：提供了多种访问方法以确保最大兼容性

## 版本兼容性

- **最低 Android 版本**：API 24 (Android 7.0)
- **目标 Android 版本**：API 36
- **WebView 版本**：支持现代 WebView 功能

这个解决方案应该能够解决 Android WebView 中无法访问系统剪贴板的问题，同时保持与各种网页的兼容性。