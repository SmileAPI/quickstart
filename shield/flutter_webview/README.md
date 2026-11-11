# Shield — Flutter WebView demo

English | [简体中文](./README-zh_CN.md)

Same page as the other clients, hosted in `webview_flutter`; the native side fetches the
`userToken` and logs callbacks over a JavaScript channel.

```
flutter_webview/
├── lib/main.dart            WebView + ShieldBridge channel + token fetch
├── pubspec.yaml
└── assets/shield.html, assets/shield-demo.js
```

## Run

```bash
# 1) demo server
cd frontend/quick-start/node && node index.js

# 2) app
cd frontend/quick-start/shield/flutter_webview
flutter pub get
flutter run                     # add -d <device> to pick a target
```

Newer Flutter versions need the platform folders (`android/`, `ios/`, ...): run
`flutter create .` inside this directory once, then `flutter run`.

- Android emulator → `10.0.2.2:8000`, iOS simulator → `localhost:8000` (see `_serverUrls`).
- Cleartext HTTP to the local server is allowed by the debug Android manifest; on a release build add a
  network-security-config like the native Android demo does.

## Bridge

| Direction | Mechanism |
| --- | --- |
| page → Flutter | `ShieldBridge.postMessage(JSON.stringify({type, payload}))` on every SDK callback |
| page → Flutter | `ShieldBridge.postMessage('requestToken')` when Start is pressed |
| Flutter → page | `runJavaScript('window.onNativeToken(token)')` / `window.shieldTokenFailed(msg)` |

`debugPrint('ShieldDemo callback=… payload=…')` lands in the `flutter run` console.

## What to check

| Step | Expect |
| --- | --- |
| Open the app | No loading overlay yet |
| Press Start Shield | Loading page; console prints `callback=onProfilingStarted` |
| Supplier finishes | Result page → auto close; `callback=onProfilingFinished` with `profilingResultCode` |
| Press again | New session (different `sessionId`), no stale overlay |
