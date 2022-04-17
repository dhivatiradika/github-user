# github-user

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:dhivatiradika/github-user.git
```

## Configuration
### API Token:
add your personal Github token to `core/keystore.gradle` with the following info:
```gradle
defaultConfig {
        ..
        
        buildConfigField("String", "GITHUB_TOKEN", '"YOUR_TOKEN"')
    }
```
you can get Github Token from [here](https://github.com/settings/tokens)

## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*
