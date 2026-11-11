# AGENTS.md — Quickstart

Sample apps that show how to embed the Smile **Widget** (Wink) and **Shield** SDKs. Every sample
talks to the same tiny Node server, which mints a `userToken` from the Smile Open API — the API
secret never leaves the server.

## Layout

```
quick-start/
├── node/                     demo server (Express)
│   ├── index.js              /api/create_link_token + static mounts
│   ├── public/index.html     landing menu (links to both test pages)
│   └── .env.example          copy to .env, fill APP_PORT / API_KEY / API_SECRET / OPEN_API_HOST
├── widget/                   Wink (Widget) samples
│   ├── web/                  browser page (index.html, iframe/, icons)
│   ├── android-webview/      Android WebView (Kotlin)
│   ├── iOS-webview/          iOS WKWebView (SwiftUI)
│   ├── react-native/         React Native (react-native-webview)
│   └── flutter_webview/      Flutter (webview_flutter)
├── shield/                   Shield samples
│   ├── web/                  browser page (shield.html + shield-demo.js)
│   ├── android/              Android WebView + JS bridge
│   ├── ios/                 iOS WKWebView
│   ├── react-native/         React Native
│   ├── flutter_webview/      Flutter
│   └── README.md             Shield testing guide (all clients)
├── README.md / README-zh_CN.md
└── AGENTS.md                 this file
```

## Running the demo server

```bash
cd node
cp .env.example .env          # APP_PORT (default 8000), API_KEY, API_SECRET, OPEN_API_HOST
node index.js
```

Served routes:

| Route | Content |
| --- | --- |
| `/` | landing menu → Widget / Shield test pages |
| `/widget/*` | `widget/web` |
| `/shield/*` | `shield/web` |
| `/api/create_link_token` | exchanges the Open API key for a `userToken` |

Self-check: `curl -s http://localhost:8000/api/create_link_token | head -c 200`.

Host names per platform (documented in `shield/README.md`):

- Android emulator → `http://10.0.2.2:<APP_PORT>`
- iOS simulator → `http://127.0.0.1:<APP_PORT>`
- physical device → the machine's LAN IP

## Conventions

- **Never commit secrets.** `.env` is ignored (`.env.example` is the template); keep API keys in
  the server process only. Do not hardcode a `userToken` in a sample — always fetch it.
- **Bilingual docs**: every README exists in two files — `README.md` (English) and
  `README-zh_CN.md` (简体中文) — each starting with a language switcher. When you change one, change
  the other in the same commit. Generated/vendor READMEs (`node_modules/`, `Pods/`, Xcode asset
  folders) are excluded.
- **One README per sample** describing how to run it and what to check. Cross-cutting guidance
  lives in `widget/README.md` and `shield/README.md`.
- **Generated output stays out of git**: `node_modules/`, `build/`, `.gradle/`, `.cxx/`,
  `xcuserdata/`, `.DS_Store`, Flutter `.dart_tool/` are ignored. Lock files (`Podfile.lock`,
  `package-lock.json`, `yarn.lock`, `pubspec.lock`) are committed on purpose.
- **Keep the samples small.** They exist to prove the integration works, not to show app
  architecture: one screen, one WebView, one bridge.
- **Shield callbacks** are `onProfilingStarted` / `onProfilingFinished` / `onError`. The loading
  screen and the result screen are part of the SDK and cannot be disabled.
- **Shield is button-driven**: the page exposes a Start button; nothing runs on load, and pressing
  it again re-initialises the SDK. Keep that behaviour when editing the demos.

## Changing a sample

1. Edit the shared page (`shield/web/shield.html`, `widget/web/index.html`) or the per-sample
   assets under `shield/<platform>/assets` / `widget/<platform>`.
2. The same `shield-demo.js` is copied to Android / iOS / RN / Flutter; if you change one, sync the
   others (they only differ in how the token is fetched and which bridge name is used).
3. Run the sample on its platform; for the web pages, `node index.js` plus a browser is enough.
4. Update the matching README section when behaviour or URLs change.

## Known environment notes

- Android samples allow cleartext HTTP only for `127.0.0.1` / `10.0.2.2` / `localhost`
  (`network_security_config.xml`).
- iOS uses the app's generated Info.plist; if ATS blocks the local token request, add
  `NSAppTransportSecurity → NSAllowsLocalNetworking = YES`.
- Flutter / React Native samples ship source only (no `android/` + `ios/` platform folders for the
  Shield ones): run `flutter create .` or `npx react-native init` once inside the folder.
