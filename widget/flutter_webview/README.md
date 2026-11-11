# flutter_webview (Widget sample)

English | [简体中文](./README-zh_CN.md)

Flutter WebView that hosts the Wink (Widget) SDK. The page lives in `assets/smile.html` and loads
`smile.v2.js`; the sample asks the quickstart Node server for a `userToken` before opening the widget.

## Run

```bash
# 1) start the demo server (repo root)
cd frontend/quick-start/node && cp .env.example .env && node index.js

# 2) run the app
cd frontend/quick-start/widget/flutter_webview
flutter pub get
flutter run                 # add -d <device> to pick a target
```

- Android emulator reaches the host through `10.0.2.2`, iOS simulator through `127.0.0.1`
  (the embedded page must use the matching host).
- The page is `assets/smile.html`; edit it to change the widget options (template id, providers, …).

## What to check

| Step | Expect |
| --- | --- |
| Open the app | The Wink entry page renders inside the WebView |
| Start the flow | The widget opens and links an account end to end |
| Callbacks | Link success/failure is printed by the Flutter console |

See [../README.md](../README.md) for the other Wink samples.
