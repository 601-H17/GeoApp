# ![GeoApp-Icone] GeoApp

[![Build Status][travis-image-geo]][travis-url-geo]
## Summary

* [Introduction]
* [Install Android Studio]
* [Setup and configure Android Emulator]
* [Download the project]
* [Open project]
* [TestObject Configuration]
* [Release APK with Tag]
* [Schema of continuous integration of this project]

___

## Introduction
    GeoApp is an Android application to searches for a local and offers the shortest path from a custom map.

## Install Android Studio 

1. Setup [Android Studio] Tutorial

## Setup and configure Android Emulator
 > **Configuration Information:**
    Marshmallow, Intel Atom x86, API level 23, Android 6.0

1. You can follow this tutorial to help you [AVD Setup Tutorial] .

   **Note:**
        Don't forget to install HAXM by following the installation instructions in the link above.
3. Some [Android Studio docs] for running emulator.
    
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
1. Create your free account [TestObject] . 
 
2. Create your Android projet on dashboard and follow this tutorial: [Espresso Setup]

3. When APK configuration is done, read the README of this project :  [TestObject gradle plugin]

## Release APK with Tag
```bash
$ git tag -a [tagname] -m [description]
$ git push origin [tagname]
```
**Example:** 
```bash
$ git tag -a v1.4 -m "Release 1.4"
$ git push origin v1.4
```
>You can see the release location: [here][Release-Location]

> Need help with tag ? [Read this][Git-Tag-Help]

## Schema of continuous integration of this project
<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/5929986/23721169/f604cca6-040e-11e7-9684-603961057cb9.png" />
</p>

__
> E.g.: Icon made by [Freepik](http://www.freepik.com/) from [www.flaticon.com](www.flaticon.com) 




[GeoApp-Icone]: http://image.noelshack.com/fichiers/2017/10/1488988686-school.png
[travis-image-geo]: https://travis-ci.org/601-H17/GeoApp.svg?branch=master
[travis-url-geo]: https://travis-ci.org/601-H17/GeoApp
[Introduction]: https://github.com/601-H17/GeoApp#introduction
[Install Android Studio]: https://github.com/601-H17/GeoApp#install-android-studio
[Release APK with Tag]: https://github.com/601-H17/GeoApp#release-apk-with-tag
[Setup and configure Android Emulator]: https://github.com/601-H17/GeoApp#setup-and-configure-android-emulator
[Download the project]: https://github.com/601-H17/GeoApp#download-the-project
[Open project]: https://github.com/601-H17/GeoApp#open-project
[TestObject Configuration]: https://github.com/601-H17/GeoApp#testobject-configuration
[Schema of continuous integration of this project]: https://github.com/601-H17/GeoApp#schema-of-continuous-integration-of-this-project
[Android Studio]: https://developer.android.com/studio/index.html
[AVD Setup Tutorial]: https://docs.nativescript.org/tooling/android-virtual-devices
[Android Studio docs]: https://developer.android.com/studio/run/emulator.html
[TestObject]: https://app.testobject.com/#/signup
[Espresso Setup]: https://help.testobject.com/docs/testing-tools/robotium-espresso/setup/
[TestObject gradle plugin]: https://github.com/testobject/testobject-gradle-plugin
[Git-Tag-Help]: https://git-scm.com/book/en/v2/Git-Basics-Tagging
[Release-Location]: https://github.com/601-H17/GeoApp/releases