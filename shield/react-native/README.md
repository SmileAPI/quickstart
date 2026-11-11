# Shield — React Native demo

English | [简体中文](./README-zh_CN.md)

WebView hosts the Shield H5 SDK; the native side only fetches the `userToken` and logs callbacks.

```
react-native/
├── App.tsx              WebView + token fetch + onMessage bridge
├── index.js             AppRegistry entry
├── assets/shield.html   the page (Start button + SDK script)
└── assets/shield-demo.js
```

## Run

```bash
# 1) start the demo server (repo root)
cd frontend/quick-start/node && node index.js

# 2) install & run
cd frontend/quick-start/shield/react-native
npm install
npx react-native run-android     # or: npx react-native run-ios
```

- `react-native-webview` must be installed and pod-installed (`cd ios && pod install`) on iOS.
- A brand-new RN app also needs the platform folders (`android/`, `ios/`). Easiest path: create one with
  `npx react-native init ShieldDemo`, then copy `App.tsx`, `index.js`, `app.json` and `assets/` into it.

## How the token flows

1. Page shows **Start Shield** — nothing runs before the press.
2. On press the page posts `{type:'requestToken'}` to native.
3. `App.tsx` calls `http://10.0.2.2:8000/api/create_link_token` (Android emulator) or
   `http://localhost:8000/...` (iOS simulator) and injects `window.onNativeToken(token)`.
4. Callbacks come back as `{type, payload}` on `onMessage` → `console.log` / the header line.

Physical device: replace the host in `SERVER_URLS` with your machine's LAN IP.

## What to check

| Step | Expect |
| --- | --- |
| Open the app | No loading overlay yet |
| Press Start Shield | Loading page; log `callback=onProfilingStarted` |
| Wait for the supplier | Loading switches to the result page, then closes; `callback=onProfilingFinished` |
| Press again | Old instance closed, new `sessionId` in the next `onProfilingStarted` |
