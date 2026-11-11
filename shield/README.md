# Smile Shield — testing guide (all clients)

English | [简体中文](./README-zh_CN.md)

The Shield H5 SDK (`shield.v1.js`) is distributed as JavaScript, so every client is really **the same page running
inside a WebView or a browser**, with the SDK callbacks bridged to native code. This folder keeps all of those
samples:

```
shield/
├── web/                     Web (browser)      — open the page and go
├── android/                 Android (Kotlin)   — Activity + WebView + @JavascriptInterface
├── ios/                     iOS (SwiftUI)      — WKWebView + WKScriptMessageHandler
├── react-native/            React Native       — react-native-webview + onMessage bridge
└── flutter_webview/         Flutter            — webview_flutter + JavaScriptChannel
```

> Wink (Widget) samples live in [../widget](../widget).

---

## 0. Prerequisite: start the demo server

None of the clients embed a `userToken`: the token has to be minted server-side with the Open API
(`API_SECRET` must never ship in an app). Every sample reuses the Node server that comes with this quickstart:

```bash
cd frontend/quick-start/node
cp .env.example .env        # APP_PORT / API_KEY / API_SECRET / OPEN_API_HOST
node index.js               # listens on 8000 by default
```

The server exposes two static mounts plus the token endpoint:

| Route | Purpose |
| --- | --- |
| `http://<host>:8000/widget/...` | Wink pages (`/widget/index.html`) |
| `http://<host>:8000/shield/shield.html` | Shield web sample |
| `http://<host>:8000/api/create_link_token` | exchanges the `.env` Open API key for an `accessToken` |

Self-check:

```bash
curl -s http://localhost:8000/api/create_link_token | head -c 200
curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8000/shield/shield.html   # expect 200
```

> When a client cannot reach the server the page shows
> `cannot get userToken — … Is the Node server on :8000 running?`
> - Android emulator reaches the host machine at `10.0.2.2`
> - iOS simulator reaches it at `127.0.0.1`
> - a physical device needs the machine's LAN IP

---

## 1. Web (browser)

```bash
# with the Node server already running
open http://localhost:8000/shield/shield.html
```

- Press **Start Shield** to run the SDK — nothing happens on load; pressing again calls `close()` on the previous
  instance and re-initialises it
- The page shows `sessionId` / `userId` / `profilingResultCode` / `durationMs`
- The log at the bottom prints `onProfilingStarted` / `onProfilingFinished` / `onError`

**Checks**: loading screen appears → after the supplier callback the success screen shows for 2s → the overlay closes;
a second click runs a fresh session.

---

## 2. Android (`shield/android`)

```bash
cd frontend/quick-start/shield/android
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -s ShieldDemo            # callbacks are logged under the ShieldDemo tag
```

- `MainActivity` serves `assets/` through `WebViewAssetLoader`, so the page runs on a real https origin
  (`https://appassets.androidplatform.net/`). A `file://` page would send `Origin: null` and the Link API CORS check
  would reject it.
- Pressing **Start Shield** makes the page call `ShieldNative.requestToken()`; native fetches the token and calls back
  `window.onNativeToken(token)`. The fetch happens natively because the page is on https and an `http://10.0.2.2`
  request would be blocked as mixed content.
- Callbacks travel `ShieldNative.onShieldEvent(type, json)` → `ShieldJsBridge` → logcat
- Cleartext HTTP is allowed only for `127.0.0.1` / `10.0.2.2` / `localhost`
  (`res/xml/network_security_config.xml`)
- Physical device: change the host in `TokenFetcher.DEFAULT_ENDPOINT`

**Checks**: logcat shows `callback=onProfilingStarted` and `callback=onProfilingFinished`; `chrome://inspect` shows the
WebView console.

---

## 3. iOS (`shield/ios`)

```bash
open frontend/quick-start/shield/ios/ShieldDemo.xcodeproj
# pick a simulator → Run; the Xcode console prints [ShieldDemo] callback=…
```

- The page is bundled from `ShieldDemo/HTML_files/` (folder reference); native no longer passes a token
- Pressing **Start Shield** makes the page fetch
  `http://127.0.0.1:8000/api/create_link_token` itself, the same way the WinkWidget sample
  (`../widget/iOS-webview`) does
- `SwiftUIWebview` registers `window.webkit.messageHandlers.shield`; the page posts `{type, payload}` and
  `ShieldMessageHandler` prints it
- Physical device: change `TOKEN_ENDPOINT` in `shield-demo.js`
- If ATS blocks the request, add
  `NSAppTransportSecurity → NSAllowsLocalNetworking = YES` (the project currently uses a generated Info.plist, so it
  has to be added explicitly)

**Checks**: the console shows `callback=onProfilingStarted` and `callback=onProfilingFinished`, and the `sessionId` in
the payload matches what the page displays.

---

## 4. React Native (`shield/react-native`)

```bash
cd frontend/quick-start/node && node index.js      # start the demo server first
cd frontend/quick-start/shield/react-native
npm install && npx react-native run-android        # or run-ios
```

- The page is bundled at `assets/shield.html`: Android loads it from `file:///android_asset/`, iOS uses `require`
- Pressing **Start Shield** posts `window.ReactNativeWebView.postMessage({type:'requestToken'})`; `App.tsx` fetches
  the token (Android emulator `10.0.2.2:8000`, iOS `localhost:8000`) and injects
  `window.onNativeToken(token)`
- Callbacks: the page posts `{type, payload}` → `onMessage` → console and the header line
- Physical device: change the host in `SERVER_URLS`

**Checks**: `callback=onProfilingStarted` / `callback=onProfilingFinished` appear in the Metro console, and pressing
Start again opens a new session.

---

## 5. Flutter (`shield/flutter_webview`)

```bash
cd frontend/quick-start/node && node index.js
cd frontend/quick-start/shield/flutter_webview
flutter pub get && flutter run
```

- `webview_flutter` loads `assets/shield.html` and registers the `ShieldBridge` JavaScript channel
- Pressing **Start Shield** sends `requestToken`; `main.dart` fetches the token with `dart:io` and injects
  `window.onNativeToken(token)` through `runJavaScript`
- Callbacks: `ShieldBridge.postMessage({type, payload})` → `debugPrint('ShieldDemo callback=…')`
- A brand-new Flutter app has no `android/` / `ios/` folders yet: run `flutter create .` inside the directory once

**Checks**: `flutter run` prints `ShieldDemo callback=onProfilingStarted` / `onProfilingFinished`, and the result
screen closes by itself after 2s.

---

## 6. Shared test checklist

| # | Scenario | Expectation |
| --- | --- | --- |
| 1 | Open the page, do nothing | No loading screen (SDK not initialised) |
| 2 | Press Start | Loading screen; `onProfilingStarted` carries a `sessionId` |
| 3 | Supplier confirms | Result screen `Check complete`, closes after 2s; `profilingResultCode=SUCCESS` |
| 4 | Supplier never confirms (wait 20s, or lower `completionTimeoutMs`) | Result screen `Let's try that again`; `profilingResultCode=TIMEOUT` |
| 5 | Press Start again after finishing | Previous instance closed, new session (`sessionId` changes) |
| 6 | Node server not running | Page reports `cannot get userToken`, no loading screen |
| 7 | Mobile viewport | Loading and result screens fill the screen, button tappable, no horizontal scroll |
| 8 | React Native / Flutter | Bridged callbacks visible in the Metro / `flutter run` console and consistent with the page |

> Server-side follow-up: once you have the `sessionId`, call
> `POST /shield/sessions/{sessionId}/assessments` to get an assessment (billed per call);
> `GET .../assessments/{assessmentId}` reads the stored result and is free.

---

## 7. Troubleshooting

| Symptom | Cause / fix |
| --- | --- |
| `cannot get userToken` | Node server not running, not on port 8000, or the Open API key in `.env` is invalid |
| Link API 404 / CORS error | Wrong page origin (Android must use `appassets`, not `file://`) |
| Loading screen never ends | The supplier never sent a completion notification; wait for the timeout or lower `completionTimeoutMs` |
| Callback name mismatch | The current SDK emits `onProfilingStarted` / `onProfilingFinished` (`onProfilingCompleted` was renamed) |
