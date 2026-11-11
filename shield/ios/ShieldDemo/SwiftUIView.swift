//
//  SwiftUIView.swift
//  ShieldDemo
//
//  WKWebView wrapper that forwards the Shield SDK callbacks to native.
//

import SwiftUI
import WebKit

/// Receives `{ type, payload }` messages posted by the bundled page.
final class ShieldMessageHandler: NSObject, WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController,
                               didReceive message: WKScriptMessage) {
        guard let body = message.body as? [String: Any] else {
            print("[ShieldDemo] unexpected message: \(message.body)")
            return
        }
        let type = body["type"] as? String ?? "unknown"
        let payload = body["payload"] as? String ?? "{}"
        // This is the callback payload as the native side sees it
        print("[ShieldDemo] callback=\(type) payload=\(payload)")
    }
}

struct SwiftUIWebview: UIViewRepresentable {
    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        configuration.userContentController.add(ShieldMessageHandler(), name: "shield")
        let webView = WKWebView(frame: .zero, configuration: configuration)
        // Safari inspection requires iOS 16.4+
        if #available(iOS 16.4, *) {
            webView.isInspectable = true
        }
        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {
        guard let url = Bundle.main.url(forResource: "index", withExtension: "html", subdirectory: "HTML_files") else {
            print("[ShieldDemo] index.html not found in bundle")
            return
        }
        if uiView.url == nil {
            uiView.loadFileURL(url, allowingReadAccessTo: url.deletingLastPathComponent())
        }
    }
}
