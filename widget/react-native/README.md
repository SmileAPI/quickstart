# Smile React Native demo (Widget)

English | [简体中文](./README-zh_CN.md)

React Native WebView implementation of the Wink (Widget) integration — the React Native counterpart of the
`flutter_webview` sample.

## Features

- WebView hosting the Wink page (`assets/smile.html`)
- JavaScript bridge between the page and React Native
- iOS and Android support with the required native configuration
- Fetches a `userToken` from the quickstart Node server before opening the widget

## Prerequisites

- Node.js
- A React Native development environment
- iOS: Xcode + CocoaPods

> Run through the [Set Up Your Environment](https://reactnative.dev/docs/set-up-your-environment) guide first.

## Run

```bash
# 0) demo server (repo root) — the app needs it for the userToken
cd frontend/quick-start/node && cp .env.example .env && node index.js

# 1) dependencies
cd frontend/quick-start/widget/react-native
npm install
cd ios && bundle exec pod install && cd ..     # iOS only

# 2) Metro
npm start

# 3) build & run (new terminal)
npm run android      # or: npm run ios
```

The Android emulator reaches the host at `10.0.2.2`, the iOS simulator at `localhost` / `127.0.0.1`; the embedded
page has to use the matching host.

## Editing

Open `App.tsx` — Fast Refresh applies changes on save. Full reload: <kbd>R</kbd> twice (Android, or **Reload** in the
Dev Menu) / <kbd>R</kbd> in the iOS Simulator.

## What to check

| Step | Expect |
| --- | --- |
| Open the app | The Wink entry page renders inside the WebView |
| Start the flow | The widget opens and links an account end to end |
| Callbacks | `console.log` shows the bridge messages posted by the page |

## Troubleshooting

- Cannot build: re-read the [React Native troubleshooting](https://reactnative.dev/docs/troubleshooting) page.
- `userToken` errors: the Node server is not running, or the page requests the wrong host (see above).

See [../README.md](../README.md) for the other Wink samples.
