# Encryption/decryption demo with Java and Google Tink

## Disclaimer

under no circumstance this code is intended nor shall be used for production applications.  this was created merely as an excercise to implement and use Google Tink in a Java springboot application.

Google tink documentation: https://github.com/google/tink

Please import the `Encryption-demo.postmant_collection.json` into Postman for a request overview.

this application already has a private key set.  You can re-generate it with a request like the following

```
curl --location --request POST 'http://localhost:8080/security/key'
```

this will create a new set of `my_keyset.json` and `my_mac_keyset.json` files.  Please beware that previously encrypted data will be unable to be decrypted if you do not backup the files before regenerating them.

You are able to rotate the private encryption key with the following request

```
curl --location --request PUT 'http://localhost:8080/security/key'
```

this will add a new key to the `my_keyset.json` file.  new encryption requests will use the new key, however previously encrypted data before key rotation will continue to be able to be decrypted.

to encrypt data, a request has to be made as follow:

```
curl --location --request PUT 'http://localhost:8080/security/' \
--header 'Content-Type: text/plain' \
--data-raw 'foo bar'
```

the response will be something like

```
AQt2GiN/2/V65lbCVpNP/PkTARBpATn3FQlI+N5HshpQYj15YKz5vPa5uSYDDJ8bsF8VP18JXCB/Qlx0Ug==
```

this is a base64 encoded string of the binary representation of the encrypted data.

to decrypt it to the original plain text, a request like the following has to be performed

```
curl --location --request POST 'http://localhost:8080/security/' \
--header 'Content-Type: text/plain' \
--data-raw 'AQt2GiN/2/V65lbCVpNP/PkTARBpATn3FQlI+N5HshpQYj15YKz5vPa5uSYDDJ8bsF8VP18JXCB/Qlx0Ug=='
```

the response should be like

```
foo bar
```

the encryption process uses the "[Authenticated Encryption with Associated Data](https://github.com/google/tink/blob/master/docs/PRIMITIVES.md#authenticated-encryption-with-associated-data)" primitive, which generates a different output for the same input, even without rotating private keys. as for now, the associated data is fixed, potentially being used for authentication purposes in the future.
