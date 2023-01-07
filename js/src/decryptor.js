global.window = {}; // fake the window object
const JSEncrypt = require('jsencrypt/bin/jsencrypt');
const MD5 = require('crypto-js/md5')
const fingerprintjs2 = require('fingerprintjs2')

const config = require("../config/app_config.json")
const express = require("express");
const { check, validationResult } = require('express-validator');
const winston = require("winston")

const decrypt = new JSEncrypt();
const app = express();
const logger = winston.createLogger({
    level: 'info',
    format: winston.format.json(),
    transports: [
        new winston.transports.Console()
    ]
})

app.use(express.urlencoded({extended: true}));
app.use(express.json());

app.post('/decrypt',
    [
        check('privateKey').isString(),
        check('message').isString(),
        check('salt').isString()
    ],
    async (req, res) => {
        const errors = validationResult(req)
        if (errors.isEmpty()) {
            logger.info(`PrivateKey: ${req.body.privateKey}`)
            logger.info(`Message: ${req.body.message}`)
            logger.info(`Salt: ${req.body.salt}`)
            let token = getToken(req.body.privateKey, req.body.message)
            let ippSign = await getIppSignCookie(req.body.salt)
            res.status(200).send({
                ippKey: getIppKeyCookie(token),
                ippUid: getIppUidCookie(),
                ippSign
            })
        } else {
            res.status(404).send({
                message: 'Invalid parameters'
            })
        }
    });


app.listen(config.port, () => {
    logger.info(`App started on port: ${config.port}`)
})

function getToken(privateKey, message) {
    decrypt.setPrivateKey(privateKey)
    return decrypt.decrypt(message)
}

function getIppKeyCookie(token) {
    // return "ipp_key=" + token + "; Path=/"
    return "ipp_key=" + token
}

function getIppUidCookie() {
    // return "ipp_uid=1672947055996/PvjWsXSJWYaGlHvl/6toeCFTb6CRhtvDOf7jhjw==; Expires=Tue, 31 Dec 2030 23:59:59 GMT; Path=/"
    return "ipp_uid=1672947055996/PvjWsXSJWYaGlHvl/6toeCFTb6CRhtvDOf7jhjw=="
}

async function getIppSignCookie(salt) {
    const components = await fingerprintjs2.getPromise();
    const values = components.map(component => component.value);
    const fp = fingerprintjs2.x64hash128(values.join(""), 31);
    // return "ipp_sign=" + fp + "_" + salt + "_" + MD5(fp+salt).toString() + "; expires=Tue, 31 Dec 2030 23:59:59 GMT; path=/;"
    return "ipp_sign=" + fp + "_" + salt + "_" + MD5(fp+salt).toString()
}