SuperToasts Library
=================
[![Download](https://api.bintray.com/packages/johnpersano/maven/com.github.johnpersano%3Asupertoasts/images/download.svg) ](https://bintray.com/johnpersano/maven/com.github.johnpersano%3Asupertoasts/_latestVersion)

The SuperToasts library enhances and builds upon the Android Toast class. This library includes support for context sensitive [SuperActivityToasts](https://github.com/JohnPersano/Supertoasts/wiki/SuperActivityToast) that can show progress and handle button clicks as well as non-context sensitive [SuperToasts](https://github.com/JohnPersano/Supertoasts/wiki/SuperToast) which offer many customization options over the standard Android Toast class. 

![Screenshot](https://github.com/JohnPersano/SuperToasts/blob/master/art/SuperToasts_Banner.png)


Adding SuperToasts to your project
==================================
SuperToasts are now available on Jcenter.
Add the following to your module's build.gradle file:
```xml
dependencies {
    compile 'com.github.johnpersano:supertoasts:2.0'
}
```

Using the library
================
Simple sample:
```java
SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
    .setButtonText("UNDO")
    .setButtonIconResource(R.drawable.ic_undo)
    .setOnButtonClickListener("good_tag_name", null, onButtonClickListener)
    .setProgressBarColor(Color.WHITE)
    .setText("Email deleted")
    .setDuration(Style.DURATION_LONG)
    .setFrame(Style.FRAME_LOLLIPOP)
    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PURPLE))
    .setAnimations(Style.ANIMATIONS_POP).show();
```
Check out the [Wiki pages*](https://github.com/JohnPersano/SuperToasts/wiki) for more detailed samples. <br>
*Please note that the Wiki pages are still being updated and may not reflect the most recent changes in version 2.0.

Demo Application
================
A simple demo application is available on Google Play. This demo application does not showcase all of the libraries functions rather it is a short demonstration of major features of the library.

<a href="https://play.google.com/store/apps/details?id=com.github.johnpersano.supertoasts.demo">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_60.png" />
</a>

Considerations
==============
Some of the ideas for this library came from the [UndoBar Library](https://code.google.com/p/romannurik-code/source/browse/misc/undobar)
and the [Crouton Library](https://github.com/keyboardsurfer/Crouton).

Developer
=========
[John Persano](https://plus.google.com/+JohnPersano)


License
=======

    Copyright 2013-2016 John Persano

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

