# url-shortener
URL Shortener API using Spark, Kotlin and Dagger

## Use:

### Generate

**URL:** ` / `

**Method:** ` POST ` 

**Parameters:**

|Name     |Type    |Example                  | 
|---------|:------:|-------------------------|
| address | String | `http://www.google.com` |


**Response:**

**Content-type:** `text/json` 

|Name     |Type    |Example                  | 
|---------|:------:|-------------------------|
| generatedAddress | String | `http://www.yoururl.com/:code` |
