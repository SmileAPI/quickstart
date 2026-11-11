# Smile Wink（Widget）— 示例指南

[English](./README.md) | 简体中文

Wink widget 是托管的账号关联流程。所有示例都在 WebView 或浏览器里打开同一个页面（`smile.v2.js`），
启动前先向 quickstart 的 Node 服务换取 `userToken`。

```
widget/
├── web/                 Web（浏览器）    — index.html + iframe 示例
├── android-webview/     Android（Kotlin）— WebView + @JavascriptInterface 桥
├── iOS-webview/         iOS（SwiftUI）   — WKWebView + WKScriptMessageHandler
├── react-native/        React Native     — react-native-webview
└── flutter_webview/     Flutter          — webview_flutter
```

> Shield 示例见 [../shield](../shield)。

## 0. 前置：先启动 demo server

```bash
cd frontend/quick-start/node
cp .env.example .env      # APP_PORT / API_KEY / API_SECRET / OPEN_API_HOST
node index.js             # 默认 8000
```

| 路由 | 作用 |
| --- | --- |
| `/` | 入口菜单（Widget / Shield） |
| `/widget/index.html` | Wink 网页示例 |
| `/api/create_link_token` | 用 Open API key 换 `userToken` |

host：Android 模拟器 `10.0.2.2`，iOS 模拟器 `127.0.0.1`，真机用电脑的局域网 IP。

## 1. Web

打开 <http://localhost:8000/widget/index.html>。页面会取 token 并立即打开 widget；
想看 iframe 嵌入方式可以打开 `widget/web/iframe/iframe.html`。

## 2. Android

```bash
cd frontend/quick-start/widget/android-webview
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Activity 自己调用 Node 服务并把 token 注入 WebView；回调由 JavaScript 桥打印。

## 3. iOS

```bash
open frontend/quick-start/widget/iOS-webview/SmileWinkWidget.xcodeproj
```

打包进 App 的页面（`HTML_files/index.html`）会请求 `http://127.0.0.1:8000/api/create_link_token` 并打开 widget，
做法与 Shield iOS 示例一致。

## 4. React Native / Flutter

见 [react-native/README-zh_CN.md](./react-native/README-zh_CN.md) 与
[flutter_webview/README-zh_CN.md](./flutter_webview/README-zh_CN.md)：两者都把页面作为资源打包，同样需要 demo server 提供 token。

## 5. 验证清单

| # | 场景 | 期望 |
| --- | --- | --- |
| 1 | 打开页面 | widget 入口正常渲染，无 token 报错 |
| 2 | 关联账号 | 托管流程走完并触发回调 |
| 3 | Node 服务未启动 | 页面提示取不到 `userToken` |
| 4 | 手机视口 | widget 铺满、无横向滚动 |
