import React, { useRef } from 'react';
import { WebView } from 'react-native-webview';
import { Platform, SafeAreaView, StyleSheet } from 'react-native';

const App = () => {
  const webViewRef = useRef(null);

  const onMessage = (e) => {
    console.log('WebView onMessage:', e.nativeEvent.data);
  };

  const onLoadEnd = async () => {
    console.log('WebView loaded, fetching token...');

    const serverUrls = Platform.OS === 'android'
      ? ['http://10.0.2.2:8000', 'http://localhost:8000']
      : ['http://localhost:8000', 'http://127.0.0.1:8000'];

    for (const baseUrl of serverUrls) {
      try {
        console.log(`Trying to fetch token from: ${baseUrl}`);
        const response = await fetch(`${baseUrl}/api/create_link_token`);

        if (response.ok) {
          const data = await response.json();
          console.log('Token fetched successfully:', data);

          if (webViewRef.current) {
            webViewRef.current.postMessage(JSON.stringify(data));
          }
          return;
        }
      } catch (error) {
        console.log(`Failed to connect to ${baseUrl}:`, error.message);
      }
    }

    console.error('Failed to fetch token from all URLs');
    if (webViewRef.current) {
      webViewRef.current.postMessage(JSON.stringify({ error: 'Failed to fetch token' }));
    }
  };

  const source = Platform.OS === 'ios'
    ? require('./assets/smile.html')
    : { uri: 'file:///android_asset/smile.html' };

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        originWhitelist={['*']}
        source={source}
        allowFileAccess={true}
        domStorageEnabled={true}
        javaScriptEnabled={true}
        allowUniversalAccessFromFileURLs={true}
        mixedContentMode="always"
        style={styles.webview}
        ref={webViewRef}
        onLoadEnd={onLoadEnd}
        onMessage={onMessage}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // backgroundColor: '#00d563',
  },
  webview: {
    flex: 1,
  },
});

export default App;
