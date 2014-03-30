SuperToasts Library
=================

The SuperToasts library enhances and builds upon the Android Toast class. Some of the features of this library include context sensitive toasts (SuperActivityToasts), toasts with buttons and icons, non context sensitive toasts (SuperToasts), and a new style of toasts (SuperCardToasts) with many customization options for each. Some of the ideas for this library came from the [UndoBar Library](https://code.google.com/p/romannurik-code/source/browse/misc/undobar)
and the [Crouton Library](https://github.com/keyboardsurfer/Crouton).


###The library is in beta and under active development, expect bugs. <br>

![Screenshot](http://i1016.photobucket.com/albums/af284/Turbopwned/supertoasts_group_screenshot_two.png)


Adding SuperToasts to your project
================
###Eclipse
1) Download the ZIP file <br>
2) Extract the SuperToasts project from the ZIP file <br>
3) In Eclipse: New > Android Project from Existing Code > SuperToasts project > Finish <br>
4) Right click on your project <br>
5) Android > Library: Add > SuperToasts <br>


###IntelliJ
1) Download the ZIP file <br>
2) Extract the SuperToasts project from the ZIP file <br>
3) In IntelliJ: File > Project Structure > Modules > Add > Import Module > SuperToasts <br>
4) Select your project module in project structure > Dependencies > Add > Module dependency > SuperToasts


###Gradle
Add this to your project's build.gradle file
```xml
dependencies {

    compile 'com.github.johnpersano:supertoasts:1.3.4@aar'

}
```


Using the library
================
Check out the Wiki pages [here](https://github.com/JohnPersano/SuperToasts/wiki).


Demo Application
================
A simple demo application is available on Google Play. This demo application does not showcase all of the libraries functions 
rather it is a short demonstration of major features/functions of the library.

<a href="https://play.google.com/store/apps/details?id=com.supertoastsdemo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_60.png" />
</a>

Developer
=========
[John Persano](https://plus.google.com/+JohnPersano)


License
=======

    Copyright 2014 John Persano

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

