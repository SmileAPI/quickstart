# Shield — Flutter WebView 示例

[English](./README.md) | 简体中文

与其他客户端共用同一个页面，由 `webview_flutter` 承载；原生侧取 `userToken` 并通过 JavaScript channel 打印回调。

```
flutter_webview/
├── lib/main.dart            WebView + ShieldBridge 通道 + 取 token
├── pubspec.yaml
└── assets/shield.html, assets/shield-demo.js
```

## 运行

```bash
# 1) demo server
cd frontend/quick-start/node && node index.js

# 2) app
cd frontend/quick-start/shield/flutter_webview
flutter pub get
flutter run                     # 加 -d <device> 指定设备
```

较新的 Flutter 版本需要平台目录（`android/`、`ios/` 等）：先在本目录执行一次 `flutter create .`，再 `flutter run`。

- Android 模拟器 → `10.0.2.2:8000`，iOS 模拟器 → `localhost:8000`（见 `_serverUrls`）。
- 调试版 Android manifest 已允许访问本地明文 HTTP；release 构建需要像原生 Android 示例那样加
  network-security-config。

## 桥接方式

| 方向 | 机制 |
| --- | --- |
| page → Flutter | 每个 SDK 回调都会 `ShieldBridge.postMessage(JSON.stringify({type, payload}))` |
| page → Flutter | 点击 Start 时 `ShieldBridge.postMessage('requestToken')` |
| Flutter → page | `runJavaScript('window.onNativeToken(token)')` / `window.shieldTokenFailed(msg)` |

`debugPrint('ShieldDemo callback=… payload=…')` 输出到 `flutter run` 控制台。

## 验证点

| 步骤 | 期望 |
| --- | --- |
| 打开 App | 不出现 loading 遮罩 |
| 点击 Start Shield | 出现 loading 页；控制台打印 `callback=onProfilingStarted` |
| 供应商完成 | 结果页 → 自动收起；`callback=onProfilingFinished` 带 `profilingResultCode` |
| 再次点击 | 新会话（`sessionId` 不同），无残留遮罩 |
