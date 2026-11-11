# Smile Shield — 测试指南（全部客户端）

[English](./README.md) | 简体中文

Shield 的 H5 SDK（`shield.v1.js`）以 JS 形式分发，所以所有客户端本质上都是**在 WebView / 浏览器里跑同一个页面**，
再把 SDK 的回调桥接到原生。五个客户端共用同一份 `shield-demo.js`（桥接方式自动探测）：

```
shield/
├── web/                     Web（浏览器）——直接打开就能演示
├── android/                 Android（Kotlin）——Activity + WebView + @JavascriptInterface
├── ios/                     iOS（SwiftUI）  ——WKWebView + WKScriptMessageHandler
├── react-native/            React Native —— react-native-webview + onMessage 桥
└── flutter_webview/         Flutter       —— webview_flutter + JavaScriptChannel
```

> Wink（Widget）的示例不在这里，见 [../widget](../widget)。

---

## 0. 共同前提：先启动 demo server

所有客户端都**不内置** `userToken`：token 必须由后端用 Open API 换取（`API_SECRET` 不能出现在客户端）。
本目录的示例统一复用 quickstart 自带的 Node 服务：

```bash
cd frontend/quick-start/node
cp .env.example .env        # 填 APP_PORT / API_KEY / API_SECRET / OPEN_API_HOST
node index.js               # 默认监听 8000
```

服务提供两个静态目录和一个换取 token 的接口：

| 路径 | 作用 |
| --- | --- |
| `http://<host>:8000/widget/...` | Wink 示例页面（`/widget/index.html`） |
| `http://<host>:8000/shield/shield.html` | Shield Web 示例页面 |
| `http://<host>:8000/api/create_link_token` | 用 `.env` 里的 Open API key 换 `accessToken` |

自检：

```bash
curl -s http://localhost:8000/api/create_link_token | head -c 200
curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8000/shield/shield.html   # 期望 200
```

> 客户端连不上 host 时，页面会显示 `cannot get userToken — … Is the Node server on :8000 running?`
> - Android 模拟器访问宿主机用 `10.0.2.2`
> - iOS 模拟器用 `127.0.0.1`
> - 真机改成电脑的局域网 IP

---

## 1. Web（浏览器）

```bash
# node 服务已在跑
open http://localhost:8000/shield/shield.html
```

- 页面里点 **Start Shield** 才启动 SDK，**不会**自动加载；再点一次会 `close()` 旧实例并重新初始化
- 页面上能看到 `sessionId` / `userId` / `profilingResultCode` / `durationMs`
- 底部日志逐条打印 `onProfilingStarted` / `onProfilingFinished` / `onError`

**验证点**：loading 页出现 → 供应商回调后切成功页 2 秒 → 回到页面；重复点击能再次跑通。

---

## 2. Android（`shield/android`）

```bash
cd frontend/quick-start/shield/android
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -s ShieldDemo            # 回调会以 ShieldDemo 标签打印
```

- `MainActivity` 用 `WebViewAssetLoader` 把 `assets/` 挂在 `https://appassets.androidplatform.net/`，
  页面因此有真实 https origin（`file://` 会带 `Origin: null`，Link API 的 CORS 会拒）
- 点 **Start Shield** → 页面通过 `ShieldNative.requestToken()` 让原生去取 token（页面在 https 上，
  直接 fetch `http://10.0.2.2` 会被 mixed-content 拦）→ 原生回调 `window.onNativeToken(token)`
- 回调经 `ShieldNative.onShieldEvent(type, json)` → `ShieldJsBridge` → logcat
- 明文 HTTP 只对 `127.0.0.1` / `10.0.2.2` / `localhost` 放行（`res/xml/network_security_config.xml`）
- 真机：改 `TokenFetcher.DEFAULT_ENDPOINT` 里的 host

**验证点**：logcat 里能看到 `callback=onProfilingStarted` 和 `callback=onProfilingFinished`；
`chrome://inspect` 可看 WebView 控制台。

---

## 3. iOS（`shield/ios`）

```bash
open frontend/quick-start/shield/ios/ShieldDemo.xcodeproj
# 选模拟器 → Run；Xcode 控制台打印 [ShieldDemo] callback=…
```

- 页面打包在 `ShieldDemo/HTML_files/`（folder reference），原生不再传 token
- 点 **Start Shield** 后，页面自己 `fetch('http://127.0.0.1:8000/api/create_link_token')`，
  与 WinkWidget 示例（`../widget/iOS-webview`）一致
- `SwiftUIWebview` 注册 `window.webkit.messageHandlers.shield`，页面 post `{type, payload}`，
  `ShieldMessageHandler` 打印
- 真机：改 `shield-demo.js` 里的 `TOKEN_ENDPOINT`
- 若被 ATS 拦截：在 Info.plist 加 `NSAppTransportSecurity → NSAllowsLocalNetworking = YES`
  （工程当前用 generated Info.plist，需要显式添加）

**验证点**：控制台出现 `callback=onProfilingStarted` 与 `callback=onProfilingFinished`，payload 里的
`sessionId` 与页面上显示的一致。

---

## 4. React Native（`shield/react-native`）

```bash
cd frontend/quick-start/node && node index.js      # 先起 demo server
cd frontend/quick-start/shield/react-native
npm install && npx react-native run-android        # 或 run-ios
```

- 页面打包在 `assets/shield.html`，Android 走 `file:///android_asset/`，iOS 用 `require`
- 点 **Start Shield** → 页面 `window.ReactNativeWebView.postMessage({type:'requestToken'})`
  → `App.tsx` 取 token（Android 模拟器 `10.0.2.2:8000`，iOS `localhost:8000`）
  → `injectJavaScript("window.onNativeToken(token)")`
- 回调：页面 `postMessage({type, payload})` → `onMessage` → console + 顶部状态行
- 真机：改 `SERVER_URLS` 里的 host

**验证点**：`callback=onProfilingStarted` / `callback=onProfilingFinished` 出现在 Metro 终端；
再次点击按钮会开新会话。

---

## 5. Flutter（`shield/flutter_webview`）

```bash
cd frontend/quick-start/node && node index.js
cd frontend/quick-start/shield/flutter_webview
flutter pub get && flutter run
```

- `webview_flutter` 加载 `assets/shield.html`，注册 JavaScriptChannel `ShieldBridge`
- 点 **Start Shield** → 页面发 `requestToken` → `main.dart` 用 `dart:io` 取 token → `runJavaScript`
  注入 `window.onNativeToken(token)`
- 回调：`ShieldBridge.postMessage({type, payload})` → `debugPrint('ShieldDemo callback=…')`
- 新工程（没有 `android/`、`ios/`）先在目录里跑一次 `flutter create .`

**验证点**：`flutter run` 控制台打印 `ShieldDemo callback=onProfilingStarted` /
`onProfilingFinished`；结果页 2 秒后自动收起。

---

## 6. 通用测试清单

| # | 场景 | 期望 |
| --- | --- | --- |
| 1 | 打开页面但不点按钮 | 不出现 loading 页（SDK 未初始化） |
| 2 | 点 Start | 出现 loading 页；回调 `onProfilingStarted` 带 `sessionId` |
| 3 | 供应商正常回调 | 结果页 `Check complete`，2 秒后自动收起；`profilingResultCode=SUCCESS` |
| 4 | 供应商不回调（等满 20s 或调小 `completionTimeoutMs`） | 结果页 `Let's try that again`；`profilingResultCode=TIMEOUT` |
| 5 | 完成后再次点按钮 | 先 `close()` 旧实例，再开新会话（`sessionId` 变化） |
| 6 | Node 服务未启动 | 页面提示 `cannot get userToken`，不进入 loading |
| 7 | 手机端 | loading 页与结果页铺满、按钮可点、无横向滚动 |
| 8 | React Native / Flutter | 桥接回调在 Metro 终端 / `flutter run` 控制台可见，且与页面状态一致 |

> 服务端后续步骤：拿到 `sessionId` 后调 `POST /shield/sessions/{sessionId}/assessments` 取评估结果，
> 该接口按次计费；`GET .../assessments/{assessmentId}` 读缓存不计费。

---

## 7. 常见问题

| 现象 | 原因 / 处理 |
| --- | --- |
| `cannot get userToken` | Node 服务没起、端口不是 8000、或 `.env` 里 Open API key 无效 |
| Link API 404 / CORS 报错 | 页面 origin 不对（Android 要用 `appassets` 而不是 `file://`） |
| loading 页一直不结束 | 供应商没回调，等满超时；或用 `completionTimeoutMs` 调小便于演示 |
| 回调名对不上 | 当前 SDK 是 `onProfilingStarted` / `onProfilingFinished`（旧的 `onProfilingCompleted` 已改名） |
