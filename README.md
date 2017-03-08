# ![](http://image.noelshack.com/fichiers/2017/10/1488988686-school.png) GeoApp

[![Build Status](https://travis-ci.org/601-H17/GeoApp.svg?branch=master)](https://travis-ci.org/601-H17/GeoApp)
## Summary

* [Introduction](https://github.com/601-H17/GeoApp#introduction)
* [Install Android Studio](https://github.com/601-H17/GeoApp#install-android-studio)
* [Setup and configure Android Emulator](https://github.com/601-H17/GeoApp#setup-and-configure-android-emulator)
* [Download the project](https://github.com/601-H17/GeoApp#download-the-project)
* [Open project](https://github.com/601-H17/GeoApp#open-project)
* [TestObject Configuration](https://github.com/601-H17/GeoApp#testobject-configuration)
* [Schema of continuous integration of this project](https://github.com/601-H17/GeoApp#schema-of-continuous-integration-of-this-project)

___

## Introduction
    GeoApp is an Android application to searches for a local and proposes the shortest path from a custom map.

## Install Android Studio 

1. Setup [Android Studio](https://developer.android.com/studio/index.html) Tutorial

## Setup and configure Android Emulator
 > **Configuration Information:**
    Marshmallow, Intel Atom x86, API level 23, Android 6.0

1. You can follow this tutorial to help you
    [AVD Setup Tutorial](https://docs.nativescript.org/tooling/android-virtual-devices) .

   **Note:**
        Don't forget to install HAXM by following the installation instructions in the link above.
3. Some [Android Studio docs](https://developer.android.com/studio/run/emulator.html) for running emulator.
    
4. Open Emulator and turn off animation scale :

    - Setting -> Dev tools -> Developer Options
        - Window animation scale : off
        - Transition animation scale : off
        - Animator duration scale : off

## Download the project
```bash
$ git clone https://github.com/601-H17/GeoApp.git
```
## Open project

1. In Android Studio. File -> Open...
2. Compile.

## TestObject Configuration
1. Create your free account [TestObject](https://app.testobject.com/#/signup) . 
 
2. Create your Android projet on dashboard and follow this tutorial: [Espresso Setup](https://help.testobject.com/docs/testing-tools/robotium-espresso/setup/) 

3. When APK configuration is done, read the README of this project :  [TestObject gradle plugin](https://github.com/testobject/testobject-gradle-plugin)

## Schema of continuous integration of this project
<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/5929986/23721169/f604cca6-040e-11e7-9684-603961057cb9.png" />
</p>





__
> E.g.: Icon made by [Freepik](http://www.freepik.com/) from [www.flaticon.com](www.flaticon.com) 