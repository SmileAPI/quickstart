![Smile Banner](https://a-cloud.b-cdn.net/media/iW=2340&iH=726&oX=0&oY=0&cW=2340&cH=726/f814cf1173a99228c8a8fb75f74e9d1b.png)

## 快速上手 Smile Wink Widget

> 我们在 [Github](https://github.com/SmileAPI/quickstart) 中提供了示例代码，您可以根据自己的需求下载并修改。

示例代码中包含了一个 Node.js 服务，这个服务调用了 Smile 的 API 创建了一个 token，该 token 是被用来初始化 Wink Widget 的，这样才可以正常启动 Wink Widget。

该示例由两部分组成：

### 后端

-   在 /node 目录下, 您会看到一段用 node.js 写的服务代码，被用于生成 token 。 您需要下载 Node.js 并在 Node.js 环境下运行该代码。

### 前端

-   在 /frontend 目录下，您会看到如何在 Web 端中嵌入 Wink Widget 的示例代码。
-   在 /android-webview 目录下，您会看到如何在 Android WebView 中嵌入 Wink Widget 的示例代码。
-   在 /iOS-webview 目录下，您会看到如何在 iOS WebView 中嵌入 Wink Widget 的示例代码。
-   在 /react-native 目录下，您会看到如何在 React Native WebView 中嵌入 Wink Widget 的示例代码。

### 运行步骤

1. 把 quick-start 下载到您的电脑上。

2. 在 quick-start 中找到并进入 /node 目录。

3. 在 /node 目录下创建一个名为 .env 的文件。

4. 确保您有足够的权限来创建 .env 文件。

> 例如在 Mac 或 Linux 上，您可以在终端中使用超级用户权限运行以下命令：

```bash
sudo touch .env
```

> 在 Windows 上，Windows 不允许您直接从 Windows 资源管理器创建一个 .env 文件，因为它不允许文件名以“.”开头。

5. 在 .env 文件中添加以下内容:

```bash
# 示例服务代码中使用的端口号
APP_PORT=<portnumber>

# Smile Link API keys ( 您可以从access@getsmileapi.com请求访问权限来获得此功能)
API_KEY=<apikeyid>
API_SECRET=<apisecret>

#Open API Host. 例如: https://sandbox.smileapi.io/v1
OPEN_API_HOST=<openApiURL>

#Link api host. 例如: https://link-sandbox.smileapi.io/v1
LINK_API_HOST=<linkApiURL>
```

> **.env 文件通常会被系统隐藏。** 如果您希望看到它，您需要在系统首选项中启用显示隐藏文件。 **quick-start 中的 .env.example 文件提供了 .env 文件的编写示例，您可以参考它来编写属于您自己的 .env 文件**

6. 保存并关闭文件。

7. 如果您的机器上没有安装 Node.js，您需要安装 Node.js。您可以在 [Node.js 官网](https://nodejs.org/en/)上下载 Node.js 的安装包。您也可以使用 Node.js 的包管理器来安装 Node.js。更多详情请参考 node.js 官网的[安装指南](https://nodejs.org/en/download/package-manager/)。

8. 推荐使用 yarn 来安装依赖包。如果您的机器上没有安装 yarn，您需要安装 yarn。您可以在 [yarn 官网](https://yarnpkg.com/en/)上下载 yarn 的安装包。您也可以使用 yarn 的包管理器来安装 yarn。更多详情请参考 yarn 官网的[安装指南](https://yarnpkg.com/en/docs/install)。

> 在 Mac 或 Linux 中，您需要打开终端。如果您使用的是 Windows，则可以转到命令行。确保您仍然在刚刚下载的 quick-start/node 目录下。

```bash
npm install --global yarn
yarn install
```

> 如果您没有足够的权限，可能需要以超级用户身份运行。在 Mac 或 Linux 机器上，您可以使用'sudo'作为超级用户运行这些命令。在 Windows 上，您可以以管理员信任级别运行该命令，或者在 UI 中右键单击程序并选择“以管理员身份运行”。

9. 启动服务：

```bash
node index.js
```

10. 打开浏览器，打开示例 Wink Widget。（ 如果您在 “.env” 文件中指定 port:8000，在 web 浏览器中打开http://127.0.0.1:8000 ）

11. 好了，恭喜您，您已经成功运行了示例代码！

### 另外，如果您想在 Android Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 在您最喜欢的 IDE 中导入 /android-webview 文件夹(例如:Android Studio)。

2. 等所有依赖项成功加载后，启动您的应用程序。

### 如果您想在 iOS Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 在 Xcode 中导入 /iOS-webview 文件夹。

2. 等所有依赖项成功加载后，启动您的应用程序。

### 如果您想在 React Native Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 创建一个新的 React Native 项目。请先配置好 React Native 的开发环境。[React Native 官网](https://facebook.github.io/react-native/docs/getting-started.html)上有详细的配置指南。

2. 在您的项目中安装依赖包。

```bash
npm install --global yarn
yarn install
```

3. 启动您的项目。

```bash
    // iOS
    yarn react-native run-ios
```

```bash
    // Android
    yarn react-native run-android
```

### 如果您想在 Flutter Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 按照 Flutter [官方安装指南](https://flutter.dev/docs/get-started/install) 配置您的 Flutter 开发环境，以确保您的环境能够正常编译和运行 Flutter 应用程序。

🚨 **注意事项** 🚨
当你在安卓模拟器上运行项目时，请确保在嵌入的 HTML(`flutter_webview/assets/smile.html`) 文件中的所有请求地址上，将 `127.0.0.1` 更改为 `10.0.2.2`。这是因为安卓模拟器使用 `10.0.2.2` 地址来访问开发机器的 localhost。

2. 打开命令行或终端，切换到示例代码的 Flutter 项目目录：

```bash
    cd flutter_webview
```

3. 执行以下命令以获取项目依赖，并准备运行示例的 Flutter 应用：

```bash
    flutter pub get
```

4. 查看可用的设备列表，并选择您想在上面运行的设备：

```bash
    flutter devices
```

这个命令会列出所有已连接和可用的设备。

5. 启动项目：

```bash
    flutter run
```

如果您有多个设备连接，可能需要指定目标设备。
