require('dotenv').config();
const express = require('express');
const cors = require('cors');
const bent = require('bent');

// Environment
const APP_PORT = process.env.APP_PORT || 8000;
const API_KEY_ID = process.env.API_KEY_ID;
const API_KEY_SECRET = process.env.API_KEY_SECRET;
const SMILE_API_HOST = process.env.API_HOST || 'https://sandbox.smileapi.io/v1';

// Configuration
const SMILE_OPEN_API_SIGNATURE = Buffer.from(API_KEY_ID + ':' + API_KEY_SECRET).toString('base64');

// App
const app = express();
app.use(cors());
app.use(express.static('../frontend'));
app.get('/api/create_link_token', async (request, response, next) => {
    const post = bent(SMILE_API_HOST, 'POST', 'json', 201);
    const createUserResult = await post('/users', {}, {"Authorization": "Basic " + SMILE_OPEN_API_SIGNATURE});
    const userToken = createUserResult.data.token;
    response.json(userToken);
});
app.listen(APP_PORT, () => {
    console.log(`Example app listening at http://localhost:${APP_PORT}`)
})

// Loop
const getJSON = bent('json');
let items = []
let init = false
const checkNewIdentities = async () => {
    let cursor = ''
    let size = 2
    let result;
    try {
        result = await getJSON(`${SMILE_API_HOST}/identities?size=${size}&cursor=${cursor}`, {}, {"Authorization": "Basic " + SMILE_OPEN_API_SIGNATURE});
    } catch (error) {
        console.log('error: ', error);
    }
    if (result && result.data.items.length > 0) {
        result.data.items.forEach(item => {
            if (items.indexOf(item.id) < 0 ) {
                items.push(item.id)
                if (init) {
                    console.log(item);
                }
            }
        })

    }
    init = true
};
setInterval(checkNewIdentities, 5000);
