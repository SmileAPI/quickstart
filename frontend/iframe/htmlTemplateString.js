//Using iframe to embed can effectively avoid style conflicts, it is recommended to use
const htmlParameter =
    `<!DOCTYPE html>` +
    `<html>` +
    `<head>` +
    `<meta charset="utf-8" />` +
    `<meta http-equiv="X-UA-Compatible" content="IE=edge" />` +
    `<meta name="viewport" content="width=device-width, initial-scale=1.0">` +
    `<link rel="icon" href="smileicon32.webp" sizes="32x32">` +
    `<link rel="icon" href="smileicon192.webp" sizes="192x192">` +
    `<title>Smile Wink Quickstart</title>` +
    `&lt;` +
    `script src="https://web.smileapi.io/v2/smile.v2.js">` +
    `&lt;` +
    `/script>`;

const html1 =
    `</head>` +
    `<body>` +
    `&lt;` +
    `script type="text/javascript">` +
    `let token = "`;

const html2 = `";` + `let host = "`;

const html3 =
    `";` +
    `const smileLinkModal = new SmileLinkModal({` +
    `apiHost: host,` + // linkApiHost
    `userToken: token,` + // use for Wink Widget init token, create by your backend service
    `templateId: "",` + // The winkTemplateId configured in developer-portal
    `enableSampleModel: true,` + // enable sample model, please don't use it in production environment
    `onAccountCreated: ({ accountId, userId, providerId }) => {},` + // callback when account created
    `onAccountConnected: ({ accountId, userId, providerId }) => {},` + // callback when account connected
    `onAccountRemoved: ({ accountId, userId, providerId }) => {},` + // callback when account removed
    `onTokenExpired: () => {},` + // callback when token expired
    `onClose: () => {` + // callback when Wink Widget closed
    `   parent.postMessage('close','*');` +
    `},` +
    `onAccountError: ({ accountId, userId, providerId, errorCode }) => {},` + // callback when account create error
    `});` +
    `smileLinkModal.open();` +
    `&lt;` +
    `/script>` +
    `</body>` +
    `</html>`;
