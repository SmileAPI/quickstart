// Smile Shield — Flutter WebView demo.
//
// The bundled page (`assets/shield.html`) hosts the Shield H5 SDK. Nothing runs until the user
// presses **Start Shield**; the page then asks this widget for a `userToken` through the
// `ShieldBridge` JavaScript channel, and the token is fetched from the quickstart Node server
// (the Open API secret must stay server-side).
//
// Callbacks arrive on the same channel as JSON: {"type": "...", "payload": "{...}"}.
import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

/// Android emulator reaches the host through 10.0.2.2, iOS simulator through localhost.
List<String> get _serverUrls => Platform.isAndroid
    ? ['http://10.0.2.2:8000', 'http://localhost:8000']
    : ['http://localhost:8000', 'http://127.0.0.1:8000'];

void main() => runApp(const ShieldDemoApp());

class ShieldDemoApp extends StatelessWidget {
  const ShieldDemoApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Smile Shield',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(colorSchemeSeed: const Color(0xFF0B9758)),
      home: const ShieldWebViewPage(),
    );
  }
}

class ShieldWebViewPage extends StatefulWidget {
  const ShieldWebViewPage({super.key});

  @override
  State<ShieldWebViewPage> createState() => _ShieldWebViewPageState();
}

class _ShieldWebViewPageState extends State<ShieldWebViewPage> {
  late final WebViewController _controller;
  String _status = 'Press Start Shield in the page';
  String _lastEvent = '';

  @override
  void initState() {
    super.initState();
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0xFFF8F9FB))
      ..addJavaScriptChannel('ShieldBridge', onMessageReceived: _onBridgeMessage)
      ..loadFlutterAsset('assets/shield.html');
  }

  /// Handles `requestToken` from the page plus the SDK callbacks.
  void _onBridgeMessage(JavaScriptMessage message) {
    final raw = message.message;
    Map<String, dynamic>? parsed;
    try {
      parsed = jsonDecode(raw) as Map<String, dynamic>;
    } catch (_) {
      parsed = null;
    }
    if (raw == 'requestToken' || parsed?['type'] == 'requestToken') {
      _provideToken();
      return;
    }
    final type = parsed?['type'] ?? 'message';
    final payload = parsed?['payload'] ?? raw;
    debugPrint('ShieldDemo callback=$type payload=$payload');
    setState(() {
      _lastEvent = '$type';
      if (type == 'onProfilingFinished') _status = 'profiling finished';
      if (type == 'onError') _status = 'error';
    });
  }

  /// Fetches a `userToken` from the Node demo server and injects it into the page.
  Future<void> _provideToken() async {
    final client = HttpClient()..connectionTimeout = const Duration(seconds: 8);
    for (final baseUrl in _serverUrls) {
      try {
        final request = await client.getUrl(Uri.parse('$baseUrl/api/create_link_token'));
        final response = await request.close();
        if (response.statusCode != 200) continue;
        final body = await response.transform(utf8.decoder).join();
        final data = jsonDecode(body) as Map<String, dynamic>;
        final token = data['accessToken'] ?? (data['token'] as Map?)?['accessToken'];
        if (token == null) continue;
        setState(() => _status = 'userToken received');
        await _controller.runJavaScript('window.onNativeToken(${jsonEncode(token)});');
        return;
      } catch (error) {
        debugPrint('token request failed for $baseUrl: $error');
      }
    }
    client.close();
    setState(() => _status = 'cannot get userToken');
    await _controller.runJavaScript(
      "window.shieldTokenFailed('cannot reach the demo server on :8000');",
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Smile Shield — Flutter'),
        bottom: PreferredSize(
          preferredSize: const Size.fromHeight(38),
          child: Padding(
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 10),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text(
                _lastEvent.isEmpty ? _status : '$_status · last: $_lastEvent',
                style: const TextStyle(fontSize: 12, color: Colors.black54),
              ),
            ),
          ),
        ),
      ),
      body: WebViewWidget(controller: _controller),
    );
  }
}
