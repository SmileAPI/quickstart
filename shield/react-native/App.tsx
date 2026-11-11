/**
 * Smile Shield — React Native demo.
 *
 * The page is bundled (`assets/shield.html`) and hosts the Shield H5 SDK. Nothing runs until the
 * user presses **Start Shield** in the page; the page then asks this component for a `userToken`,
 * which is fetched from the quickstart Node server (the API secret must stay server-side).
 *
 * Callback flow: page → `window.ReactNativeWebView.postMessage({type, payload})` → `onMessage` below.
 */
import React, { useRef, useState } from 'react';
import { WebView } from 'react-native-webview';
import { Platform, SafeAreaView, StyleSheet, Text, View } from 'react-native';

// Android emulator reaches the host machine through 10.0.2.2; iOS simulator through localhost.
const SERVER_URLS =
  Platform.OS === 'android'
    ? ['http://10.0.2.2:8000', 'http://localhost:8000']
    : ['http://localhost:8000', 'http://127.0.0.1:8000'];

const App = () => {
  const webViewRef = useRef<WebView>(null);
  const [status, setStatus] = useState('Press Start Shield in the page');
  const [lastEvent, setLastEvent] = useState('');

  /** Fetches a token from the demo server and hands it to the page. */
  const provideToken = async () => {
    for (const baseUrl of SERVER_URLS) {
      try {
        const response = await fetch(`${baseUrl}/api/create_link_token`);
        if (!response.ok) continue;
        const data = await response.json();
        const token = data?.accessToken ?? data?.token?.accessToken;
        if (!token) throw new Error('accessToken missing in response');
        setStatus('userToken received');
        // injectJavaScript is deterministic on both platforms (postMessage is not)
        webViewRef.current?.injectJavaScript(
          `window.onNativeToken(${JSON.stringify(token)}); true;`,
        );
        return;
      } catch (error: any) {
        console.log(`token request failed for ${baseUrl}:`, error?.message ?? error);
      }
    }
    setStatus('cannot get userToken');
    webViewRef.current?.injectJavaScript(
      `window.shieldTokenFailed('cannot reach the demo server on :8000'); true;`,
    );
  };

  const onMessage = (event: any) => {
    let message: any = event.nativeEvent.data;
    try {
      message = JSON.parse(message);
    } catch {
      /* plain string */
    }
    if (message?.type === 'requestToken') {
      provideToken();
      return;
    }
    // Shield callbacks: onProfilingStarted / onProfilingFinished / onError
    console.log(`ShieldDemo callback=${message?.type} payload=${message?.payload}`);
    setLastEvent(`${message?.type}`);
    if (message?.type === 'onProfilingFinished') setStatus('profiling finished');
    if (message?.type === 'onError') setStatus('error');
  };

  const source =
    Platform.OS === 'ios'
      ? require('./assets/shield.html')
      : { uri: 'file:///android_asset/shield.html' };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Smile Shield — React Native</Text>
        <Text style={styles.subtitle}>{status}</Text>
        {!!lastEvent && <Text style={styles.subtitle}>last: {lastEvent}</Text>}
      </View>
      <WebView
        ref={webViewRef}
        originWhitelist={['*']}
        source={source}
        allowFileAccess
        allowUniversalAccessFromFileURLs
        javaScriptEnabled
        domStorageEnabled
        mixedContentMode="always"
        onMessage={onMessage}
        onError={(syntheticEvent: any) =>
          console.log('WebView error:', syntheticEvent.nativeEvent)
        }
        style={styles.webview}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FB' },
  header: { padding: 16 },
  title: { fontSize: 16, fontWeight: '600', color: '#111827' },
  subtitle: { marginTop: 4, fontSize: 12, color: '#6B7280' },
  webview: { flex: 1 },
});

export default App;
