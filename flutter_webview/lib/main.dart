import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:path_provider/path_provider.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Color(0xFF00d563)),
      ),
      home: const MyWebView(),
    );
  }
}

class MyWebView extends StatefulWidget {
  const MyWebView({super.key});

  @override
  State<MyWebView> createState() => _MyWebViewState();
}

class _MyWebViewState extends State<MyWebView> {
  late final WebViewController _controller;
  final String _htmlFilePath = 'assets/smile.html';

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Smile Flutter Demo'),
          backgroundColor: const Color(0xFF00d563),
          foregroundColor: const Color(0xFFffffff),
        ),
        body: WebViewWidget(
          controller: WebViewController()
            ..setJavaScriptMode(JavaScriptMode.unrestricted)
            ..setBackgroundColor(const Color(0x00000000))
            ..loadFlutterAsset(_htmlFilePath)
            ..setNavigationDelegate(NavigationDelegate(
              onProgress: (int progress) {},
              onPageStarted: (String url) {},
              onPageFinished: (String url) {
                print('Page finished loading: $url');
              },
            ))
            ..setOnConsoleMessage((JavaScriptConsoleMessage consoleMessage) {
              debugPrint(
                  '== JS == ${consoleMessage.level.name}: ${consoleMessage.message}');
            })
            ..addJavaScriptChannel("FlutterChannel",
                onMessageReceived: (message) {
              print(message.message);
            }),
        ));
  }
}
