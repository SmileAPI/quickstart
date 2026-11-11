# Smile Wink (Widget) — sample guide

English | [简体中文](./README-zh_CN.md)

The Wink widget is the hosted account-linking flow. Every sample opens the same page (`smile.v2.js`) inside a
WebView or a browser and asks the quickstart Node server for a `userToken` before launching it.

```
widget/
├── web/                 Web (browser)      — index.html + iframe sample
├── android-webview/     Android (Kotlin)   — WebView + @JavascriptInterface bridge
├── iOS-webview/         iOS (SwiftUI)      — WKWebView + WKScriptMessageHandler
├── react-native/        React Native       — react-native-webview
└── flutter_webview/     Flutter            — webview_flutter
```

> Shield samples live in [../shield](../shield).

## 0. Prerequisite: the demo server

```bash
cd frontend/quick-start/node
cp .env.example .env      # APP_PORT / API_KEY / API_SECRET / OPEN_API_HOST
node index.js             # 8000 by default
```

| Route | Purpose |
| --- | --- |
| `/` | landing menu (Widget / Shield) |
| `/widget/index.html` | Wink web sample |
| `/api/create_link_token` | exchanges the Open API key for a `userToken` |

Hosts: Android emulator `10.0.2.2`, iOS simulator `127.0.0.1`, physical device the machine's LAN IP.

## 1. Web

Open <http://localhost:8000/widget/index.html>. The page fetches a token and opens the widget immediately; use
`widget/web/iframe/iframe.html` to see the iframe embedding variant.

## 2. Android

```bash
cd frontend/quick-start/widget/android-webview
./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The activity calls the Node server itself and injects the token into the WebView; callbacks are logged by the
Javascript bridge.

## 3. iOS

```bash
open frontend/quick-start/widget/iOS-webview/SmileWinkWidget.xcodeproj
```

The bundled page (`HTML_files/index.html`) fetches `http://127.0.0.1:8000/api/create_link_token` and opens the
widget — the same pattern as the Shield iOS sample.

## 4. React Native / Flutter

See [react-native/README.md](./react-native/README.md) and
[flutter_webview/README.md](./flutter_webview/README.md) — both ship the page as an asset and need the demo server
for the token.

## 5. Checklist

| # | Scenario | Expectation |
| --- | --- | --- |
| 1 | Open the page | Widget entry renders, no token error |
| 2 | Link an account | Hosted flow completes and the callback fires |
| 3 | Node server down | Page reports it cannot get a `userToken` |
| 4 | Mobile viewport | Widget fills the screen, no horizontal scroll |
