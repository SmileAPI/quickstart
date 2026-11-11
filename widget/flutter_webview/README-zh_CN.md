# flutter_webview（Widget 示例）

[English](./README.md) | 简体中文

用 Flutter WebView 承载 Wink（Widget）SDK。页面在 `assets/smile.html`，其中加载 `smile.v2.js`；
示例会先向 quickstart 的 Node 服务换取 `userToken`，再打开 widget。

## 运行

```bash
# 1) 启动 demo server（仓库根目录）
cd frontend/quick-start/node && cp .env.example .env && node index.js

# 2) 运行 App
cd frontend/quick-start/widget/flutter_webview
flutter pub get
flutter run                 # 加 -d <device> 指定设备
```

- Android 模拟器通过 `10.0.2.2` 访问宿主机，iOS 模拟器用 `127.0.0.1`（页面里的请求地址要对应）。
- 页面是 `assets/smile.html`，改 widget 参数（template id、providers 等）就改它。

## 验证点

| 步骤 | 期望 |
| --- | --- |
| 打开 App | WebView 里渲染出 Wink 入口页 |
| 走完流程 | widget 打开并能完整关联一个账号 |
| 回调 | Flutter 控制台打印关联成功/失败 |

其他 Wink 示例见 [../README.md](../README.md)。
