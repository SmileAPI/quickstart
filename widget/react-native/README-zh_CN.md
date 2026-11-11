# Smile React Native 示例（Widget）

[English](./README.md) | 简体中文

Wink（Widget）集成的 React Native WebView 版本 —— 对应 `flutter_webview` 示例的 RN 实现。

## 功能

- 用 WebView 承载 Wink 页面（`assets/smile.html`）
- 页面与 React Native 之间的 JavaScript 桥
- iOS / Android 原生配置齐全
- 打开 widget 前先从 quickstart 的 Node 服务取 `userToken`

## 前置条件

- Node.js
- React Native 开发环境
- iOS：Xcode + CocoaPods

> 先按 [Set Up Your Environment](https://reactnative.dev/docs/set-up-your-environment) 配置好环境。

## 运行

```bash
# 0) demo server（仓库根目录）—— App 需要它来换取 userToken
cd frontend/quick-start/node && cp .env.example .env && node index.js

# 1) 依赖
cd frontend/quick-start/widget/react-native
npm install
cd ios && bundle exec pod install && cd ..     # 仅 iOS

# 2) Metro
npm start

# 3) 构建运行（另开一个终端）
npm run android      # 或：npm run ios
```

Android 模拟器通过 `10.0.2.2` 访问宿主机，iOS 模拟器用 `localhost` / `127.0.0.1`；页面里的请求地址要对应。

## 修改

改 `App.tsx`，保存后 Fast Refresh 会自动生效。需要整页重载时：Android 连按两次 <kbd>R</kbd>（或 Dev Menu 里选
**Reload**），iOS 模拟器按 <kbd>R</kbd>。

## 验证点

| 步骤 | 期望 |
| --- | --- |
| 打开 App | WebView 里渲染出 Wink 入口页 |
| 走完流程 | widget 打开并能完整关联一个账号 |
| 回调 | `console.log` 打印页面发来的桥接消息 |

## 排查

- 构建失败：先看 [React Native troubleshooting](https://reactnative.dev/docs/troubleshooting)。
- `userToken` 报错：Node 服务没起，或页面请求的 host 不对（见上）。

其他 Wink 示例见 [../README.md](../README.md)。
