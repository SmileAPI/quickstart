![Smile Banner](https://a-cloud.b-cdn.net/media/iW=2340&iH=726&oX=0&oY=0&cW=2340&cH=726/f814cf1173a99228c8a8fb75f74e9d1b.png)

## Quickstart Sample Implementation

> We provide sample code in [Github](https://github.com/SmileAPI/quickstart) which you can download and modify according to your own requirements.

The example code installs a small server running on Node.js that automatically retrieves a token from our API, so you can instantiate the Wink widget.

The example implementation is composed of two parts:

### Backend

-   Under /node, you will find server-side Javascript code that will retrieve the token. You will need to download and run Node.js to run the code.

### Frontend

-   Under /frontend, you will find example code in HTML that already has the Wink Javascript SDK embedded already.
-   Under /android-webview, you will find example code in Android that already has Wink Javascript SDK embedded already.
-   Under /react-native, you will find example code in react-native that already has Wink Javascript SDK embedded already.

### Implementation steps

Below steps are also included in the README.md document included in the the Quickstart [repository] and [archive].

1. Download the Quickstart files onto your machine.

2. Go to /node directory of Quickstart.

3. Create a new file with called ".env" in that directory.

4. Make sure you have proper permissions in your machine to be able to create the file.

> For example, in Mac or Linux machines open up the Terminal, you can use vi and enter the following commands as a Super User:

```bash
sudo touch .env
```

> On Windows machines, Windows will not allow you to create a .env file directly from Windows Explorer since it will not allow file names starting with a ".". To get around this:

```
1. Open Notepad oand write the content of the file (see below).
2. Goto FILE-> SAVE AS Save as Screen in the notepad.
3. Select the All files() type in the selection window.
4. Save the file as ".env"
```

5. Open up the ".env" file that you just created in your favorite editor, and enter the following:

```bash
# The port you want the example server to listen to
APP_PORT=<portnumber>

# Smile Link API keys (you can get this by requesting access from access@getsmileapi.com)
API_KEY=<apikeyid>
API_SECRET=<apisecret>

#Open API Host. for example: https://sandbox.smileapi.io/v1
OPEN_API_HOST=<openApiURL>

#Link api host. for example: https://link-sandbox.smileapi.io/v1
LINK_API_HOST=<linkApiURL>
```

> **The .env file is normally hidden by your system.** You may want to enable showing of hidden files in your system preferences to be able to see it. **You can use ".env.example" file, included in the Quickstart repository, as a reference.**

6. Save and close your file.

7. If you don't have Node.js installed in your machine, install Node.js.

> For example on the Mac you can open up the Terminal and run:

```bash
curl "https://nodejs.org/dist/latest/node-${VERSION:-$(wget -qO- https://nodejs.org/dist/latest/ | sed -nE 's|.*>node-(.*)\.pkg</a>.*|\1|p')}.pkg" > "$HOME/Downloads/node-latest.pkg" && sudo installer -store -pkg "$HOME/Downloads/node-latest.pkg" -target "/"
```

> For Windows you can [download the installer](https://nodejs.org/en/#home-downloadhead).

> For other operating systems, [you can find the instructions](https://nodejs.org/en/download/package-manager/#macos) from the Node.js website.

8. Run Yarn with [npm package manager](https://www.npmjs.com/) which is included with Node.js and enter the following commands:

> In Mac or Linux, you will need to open up the Terminal. If you are using Windows, you can go to the command line. Make sure you are still in the /node directory of the Quickstart files you just downloaded onto your machine.

```bash
npm install --global yarn
yarn install
```

> You may need to run as a Super User if you don't have enough permissions. On a Mac or Linux machine, you can run the commands as a superuser by using 'sudo'. On Windows, you can run the command with an administrator trust-level, or by right-clicking the program in the UI and choosing "run as administrator."

8. Run the server:

```bash
node index.js
```

9. Open up the your browser and open up the example Wink Widget. For example, if you specified port:8000 in your ".env" configuration file, open up http://127.0.0.1:8000 in your web browser.

10. Sit back, relax, and pat yourself on the back for a job well done!

### Additionly, if you want to use Wink Javascript SDK in Android Webview

Run the above Implementation Steps 1 to 8 if you haven't done, continue the following steps:

1. Import the /android-webview folder in your favorite IDE(example:Android Studio)

2. After all dependencies successfully loaded, start the your App.

### Additionly, if you want to use Wink Javascript SDK in iOS Webview

Run the above Implementation Steps 1 to 8 if you haven't done, continue the following steps:

1. Import the /iOS-webview folder in your Xcode

2. After all dependencies successfully loaded, start the your App.

### Additionly, if you want to use Wink Javascript SDK in React Native Webview

Run the above Implementation Steps 1 to 8 if you haven't done, continue the following steps:

1. Create a React Native project and configure the React Native development environment to ensure normal operation [you can find the instructions](https://reactnative.dev/docs/getting-started)

2. Run Yarn with [npm package manager](https://www.npmjs.com/) which is included with Node.js and enter the following commands:

```bash
npm install --global yarn
yarn install
```

3. Start your App.

```bash
    // iOS
    yarn react-native run-ios
```

```bash
    // Android
    yarn react-native run-android
```
