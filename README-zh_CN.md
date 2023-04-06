![Smile Banner](https://a-cloud.b-cdn.net/media/iW=2340&iH=726&oX=0&oY=0&cW=2340&cH=726/f814cf1173a99228c8a8fb75f74e9d1b.png)

## Quickstart Sample Implementation

> 我们在[Github](https://github.com/SmileAPI/quickstart)中提供了示例代码，您可以根据自己的要求下载并修改。

示例代码中包含了一个 Node.js 服务，这个服务调用了 Smile 的 API 创建了一个 token，该 token 是被用来初始化 Wink Widget 的，这样才可以正常启动 Wink Widget。

该示例由两部分组成：

### 后端

-   在 /node 目录下, 你会看到一段用 node.js 写的服务代码，被用于生成 token 。 你需要下载 Node.js 并在 Node.js 环境下运行该代码。

### 前端

-   在 /frontend 目录下， 你会看到如何在 HTML 中嵌入 Wink Widget 的示例代码，这种方式适用于 Web 端
-   在 /android-webview 目录下， 你会看到如何在 Android 中嵌入 Wink Widget 的示例代码，这种方式适用于 Android WebView
-   在 /iOS-webview 目录下， 你会看到如何在 iOS 中嵌入 Wink Widget 的示例代码，这种方式适用于 iOS WebView
-   在 /react-native 目录下，你会看到如何在 react-native 中嵌入 Wink Widget 的示例代码，这种方式适用于 react-native WebView

### 运行步骤

Below steps are also included in the README.md document included in the the Quickstart [repository] and [archive].

1. 把 quick-start 下载到你的电脑上。

2. 在 quick-start 中找到并进入 /node 目录。

3. 在 /node 目录下创建一个名为 .env 的文件。

4. 确保你有足够的权限来创建 .env 文件。

> 例如在 Mac 或 Linux 上，你可以在终端中使用超级用户权限运行以下命令：

```bash
sudo touch .env
```

> 在 Windows 上，Windows 不允许你直接从 Windows 资源管理器创建一个 .env 文件，因为它不允许文件名以“.”开头。为了解决这个问题你可以这么做:

```
1. 打开记事本并写入文件的内容(见下文)。
2. Goto FILE-> SAVE AS Save as Screen in the notepad.
3. Select the All files() type in the selection window.
4. Save the file as ".env"
```

5. 在 .env 文件中添加以下内容:

```bash
# 示例服务代码中使用的端口号
APP_PORT=<portnumber>

# Smile Link API keys ( 你可以从access@getsmileapi.com请求访问权限来获得此功能)
API_KEY=<apikeyid>
API_SECRET=<apisecret>

#Open API Host. 例如: https://sandbox.smileapi.io/v1
OPEN_API_HOST=<openApiURL>

#Link api host. 例如: https://link-sandbox.smileapi.io/v1
LINK_API_HOST=<linkApiURL>
```

> **.env 文件通常会被系统隐藏。** 如果你希望看到它，你需要在系统首选项中启用显示隐藏文件。 **quick-start 中的 .env.example 文件提供了 .env 文件的编写示例，你可以参考它来编写属于你自己的 .env 文件**

6. 保存并关闭文件。

7. 如果你的机器上没有安装 Node.js，你需要安装 Node.js。你可以在 [Node.js 官网](https://nodejs.org/en/)上下载 Node.js 的安装包。你也可以使用 Node.js 的包管理器来安装 Node.js。更多详情请参考 node.js 官网的[安装指南](https://nodejs.org/en/download/package-manager/)。

8. 推荐使用 yarn 来安装依赖包。如果你的机器上没有安装 yarn，你需要安装 yarn。你可以在 [yarn 官网](https://yarnpkg.com/en/)上下载 yarn 的安装包。你也可以使用 yarn 的包管理器来安装 yarn。更多详情请参考 yarn 官网的[安装指南](https://yarnpkg.com/en/docs/install)。

> 在 Mac 或 Linux 中，您需要打开终端。如果您使用的是 Windows，则可以转到命令行。确保您仍然在刚刚下载的 quick-start/node 目录下。

```bash
npm install --global yarn
yarn install
```

> 如果您没有足够的权限，可能需要以超级用户身份运行。在 Mac 或 Linux 机器上，您可以使用'sudo'作为超级用户运行这些命令。在 Windows 上，您可以以管理员信任级别运行该命令，或者在 UI 中右键单击程序并选择“以管理员身份运行”。

8. 启动服务：

```bash
node index.js
```

9. 打开浏览器，打开示例 Wink Widget。例如，如果您在“。”中指定 port:8000。Env”配置文件，在 web 浏览器中打开http://127.0.0.1:8000

10. 好了，恭喜你，你已经成功运行了示例代码！

### 另外，如果你想在 Android Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 在你最喜欢的 IDE 中导入 /android-webview 文件夹(例如:Android Studio)。

2. 等所有依赖项成功加载后，启动你的应用程序。

### 如果你想在 iOS Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 在 Xcode 中导入 /iOS-webview 文件夹。

2. 等所有依赖项成功加载后，启动你的应用程序。

### 如果你想在 React Native Webview 中使用 Wink Javascript SDK

如果您还没有执行上述实现步骤 1 到 8，请在执行完后，继续执行以下步骤：

1. 创建一个新的 React Native 项目。请先配置好 React Native 的开发环境。[React Native 官网](https://facebook.github.io/react-native/docs/getting-started.html)上有详细的配置指南。

2. 在你的项目中安装依赖包。

```bash
npm install --global yarn
yarn install
```

3. 启动你的项目。

```bash
    // iOS
    yarn react-native run-ios
```

```bash
    // Android
    yarn react-native run-android
```
