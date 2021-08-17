require('dotenv').config();
const express = require('express');
const cors = require('cors');
const bent = require('bent');

// Environment
const APP_PORT = process.env.APP_PORT || 8000;
const API_KEY_ID = process.env.API_KEY_ID;
const API_KEY_SECRET = process.env.API_KEY_SECRET;
const API_MODE = process.env.API_MODE || 'sandbox';

// Configuration
const SMILE_API_HOST = (API_MODE == 'sandbox') ? 'open-sandbox.smileapi.io' : 'open.smileapi.io';

// App
const app = express();
app.use(cors());
app.use(express.static('../frontend'));
app.post('/api/create_link_token', async(request, response, next) => {
    const post = bent(SMILE_API_HOST, 'POST', 'json', 200);
    const createUserResult = await post('users', {});
    const userId = createUserResult.data.id;
    const createTokenResult = await post('tokens' + userId, { userId });
    const userToken = createTokenResult.data;
    response.json(userToken);
});
app.listen(APP_PORT, () => {
    console.log(`Example app listening at http://localhost:${APP_PORT}`)
})