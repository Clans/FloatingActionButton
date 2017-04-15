# WARNING! FURTHER DEVELOPMENT AND SUPPORT IS DISCONTINUED.
# FloatingActionButton
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.clans/fab/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.clans/fab) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Clans%2FFloatingActionButton-blue.svg?style=flat)](http://android-arsenal.com/details/1/1684)

Yet another implementation of [Floating Action Button](http://www.google.com/design/spec/components/buttons.html#buttons-floating-action-button) for Android with lots of features.

# Requirements
The library requires Android **API Level 14+**.

# Demo
Watch a short **[Demo Video](https://youtu.be/XngUY3PN1IQ)** on YouTube or try it using **[Android simulator in the browser](https://appetize.io/app/ffreudbwmyedw5trzhyjknd2jg)** on Appetize.io.  
Sample APK can be found in **[Releases](https://github.com/Clans/FloatingActionButton/releases)** section.

# Screenshots
![Main screen](/screenshots/main_screen.png) ![Menu closed](/screenshots/menu_closed.png) ![Menu default opened](/screenshots/menu_default_opened.png) ![Menu custom opened](/screenshots/menu_custom_opened.png) ![Menu mini opened](/screenshots/menu_mini_opened.png) ![Menu right opened](/screenshots/menu_right_opened.png) ![Menu down opened](/screenshots/menu_down_opened.png) ![Progress background](/screenshots/progress_background.png) ![Progress no background](/screenshots/progress_no_background.png)

# Features
- Ripple effect on Android Lollipop devices
- Option to set custom **normal**/**pressed**/**ripple** colors
- Option to set custom shadow color and offsets
- Option to disable shadow for buttons and (or) labels
- Option to set custom animations
- Option to set custom icon drawable
- Support for **normal** `56dp` and **mini** `40dp` button sizes
- Custom FloatingActionMenu icon animations
- Option to expand menu up and down
- Option to show labels to the left and to the right of the menu
- Option to show circle progress on `FloactinActionButton`
- Option to add button to the `FloatingActionMenu` programmatically
- Option to dim the `FloatinActionMenu`'s background
- *Option to remove all buttons from the `FloatingActionMenu`*
- *Option to set a label for the `FloatingActionMenu`'s button*

# Usage
Add a dependency to your `build.gradle`:
```
dependencies {
    compile 'com.github.clans:fab:1.6.4'
}
```
Add the `com.github.clans.fab.FloatingActionButton` to your layout XML file.
```XML
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:fab="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <com.github.clans.fab.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|right"
        android:layout_marginBottom="8dp"
        android:layout_marginRight="8dp"
        android:src="@drawable/ic_menu"
        fab:fab_colorNormal="@color/app_primary"
        fab:fab_colorPressed="@color/app_primary_pressed"
        fab:fab_colorRipple="@color/app_ripple"/>

</FrameLayout>
```
You can set an icon for the **FloatingActionButton** using `android:src` xml attribute. Use drawables of size `24dp` as specified by [guidlines](http://www.google.com/design/spec/components/buttons.html#buttons-floating-action-button). Icons of desired size can be generated with [Android Asset Studio](http://romannurik.github.io/AndroidAssetStudio/icons-generic.html).

### Floating action button
Here are all the **FloatingActionButton**'s xml attributes with their **default values** which means that you don't have to set all of them:
```XML
<com.github.clans.fab.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|right"
        android:layout_marginBottom="8dp"
        android:layout_marginRight="8dp"
        android:src="@drawable/your_icon_drawable"
        app:fab_colorNormal="#DA4336"
        app:fab_colorPressed="#E75043"
        app:fab_colorRipple="#99FFFFFF"
        app:fab_showShadow="true"
        app:fab_shadowColor="#66000000"
        app:fab_shadowRadius="4dp"
        app:fab_shadowXOffset="1dp"
        app:fab_shadowYOffset="3dp"
        app:fab_size="normal"
        app:fab_showAnimation="@anim/fab_scale_up"
        app:fab_hideAnimation="@anim/fab_scale_down"
        app:fab_label=""
        app:fab_progress_color="#FF009688"
        app:fab_progress_backgroundColor="#4D000000"
        app:fab_progress_indeterminate="false"
        app:fab_progress_max="100"
        app:fab_progress="0"
        app:fab_progress_showBackground="true"/>
```
All of these **FloatingActionButton**'s attributes has their corresponding getters and setters. So you can set them **programmatically**.

### Floating action menu
Here are all the **FloatingActionMenu**'s xml attributes with their **default values** which means that you don't have to set all of them:
```XML
<com.github.clans.fab.FloatingActionMenu
        android:id="@+id/menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_marginRight="10dp"
        android:layout_marginBottom="10dp"
        android:layout_marginLeft="10dp"
        fab:menu_fab_size="normal"
        fab:menu_showShadow="true"
        fab:menu_shadowColor="#66000000"
        fab:menu_shadowRadius="4dp"
        fab:menu_shadowXOffset="1dp"
        fab:menu_shadowYOffset="3dp"
        fab:menu_colorNormal="#DA4336"
        fab:menu_colorPressed="#E75043"
        fab:menu_colorRipple="#99FFFFFF"
        fab:menu_animationDelayPerItem="50"
        fab:menu_icon="@drawable/fab_add"
        fab:menu_buttonSpacing="0dp"
        fab:menu_labels_margin="0dp"
        fab:menu_labels_showAnimation="@anim/fab_slide_in_from_right"
        fab:menu_labels_hideAnimation="@anim/fab_slide_out_to_right"
        fab:menu_labels_paddingTop="4dp"
        fab:menu_labels_paddingRight="8dp"
        fab:menu_labels_paddingBottom="4dp"
        fab:menu_labels_paddingLeft="8dp"
        fab:menu_labels_padding="8dp"
        fab:menu_labels_textColor="#FFFFFF"
        fab:menu_labels_textSize="14sp"
        fab:menu_labels_cornerRadius="3dp"
        fab:menu_labels_colorNormal="#333333"
        fab:menu_labels_colorPressed="#444444"
        fab:menu_labels_colorRipple="#66FFFFFF"
        fab:menu_labels_showShadow="true"
        fab:menu_labels_singleLine="false"
        fab:menu_labels_ellipsize="none"
        fab:menu_labels_maxLines="-1"
        fab:menu_labels_style="@style/YourCustomLabelsStyle"
        fab:menu_labels_position="left"
        fab:menu_openDirection="up"
        fab:menu_backgroundColor="@android:color/transparent"
        fab:menu_fab_label="your_label_here"
        fab:menu_fab_show_animation="@anim/my_show_animation"
        fab:menu_fab_hide_animation="@anim/my_hide_animation">

        <com.github.clans.fab.FloatingActionButton
            android:id="@+id/menu_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_star"
            fab:fab_size="mini"
            fab:fab_label="Menu item 1" />

    </com.github.clans.fab.FloatingActionMenu>
```

If you're using custom style for labels - other labels attributes will be ignored.

Labels shadow preferences depends on their corresponding **FloatingActionButtons**' shadow preferences.

For more usage examples check the **sample** project.

# Changelog
Please see the [Changelog](https://github.com/Clans/FloatingActionButton/wiki/Changelog) page to see what's recently changed.

# Credits
I used [android-floating-action-button](https://github.com/futuresimple/android-floating-action-button) library by Jerzy Chalupski as a base for development.

# License
```
Copyright 2015 Dmytro Tarianyk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
