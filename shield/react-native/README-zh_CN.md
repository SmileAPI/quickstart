# Shield — React Native 示例

[English](./README.md) | 简体中文

用 WebView 承载 Shield H5 SDK；原生侧只负责取 `userToken` 和打印回调。

```
react-native/
├── App.tsx              WebView + 取 token + onMessage 桥
├── index.js             AppRegistry 入口
├── assets/shield.html   页面（Start 按钮 + SDK 脚本）
└── assets/shield-demo.js
```

## 运行

```bash
# 1) 先启动 demo server（仓库根目录）
cd frontend/quick-start/node && node index.js

# 2) 安装并运行
cd frontend/quick-start/shield/react-native
npm install
npx react-native run-android     # 或：npx react-native run-ios
```

- 需要安装 `react-native-webview`；iOS 还要 `cd ios && pod install`。
- 全新的 RN 工程没有平台目录（`android/`、`ios/`）。最省事的做法是用
  `npx react-native init ShieldDemo` 建一个，然后把 `App.tsx`、`index.js`、`app.json` 和 `assets/` 拷进去。

## token 流程

1. 页面显示 **Start Shield** —— 点击之前什么都不跑。
2. 点击时页面向原生发 `{type:'requestToken'}`。
3. `App.tsx` 请求 `http://10.0.2.2:8000/api/create_link_token`（Android 模拟器）或
   `http://localhost:8000/...`（iOS 模拟器），然后注入 `window.onNativeToken(token)`。
4. 回调以 `{type, payload}` 形式回到 `onMessage` → `console.log` / 顶部状态行。

真机：把 `SERVER_URLS` 里的 host 换成电脑的局域网 IP。

## 验证点

| 步骤 | 期望 |
| --- | --- |
| 打开 App | 不出现 loading 遮罩 |
| 点击 Start Shield | 出现 loading 页；日志打印 `callback=onProfilingStarted` |
| 等待供应商 | loading 切到结果页后自动收起；`callback=onProfilingFinished` |
| 再次点击 | 旧实例关闭，新的 `onProfilingStarted` 带不同 `sessionId` |
