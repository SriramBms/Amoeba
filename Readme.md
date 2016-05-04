# This is a work in progress


This repo contains two features that might be separated into two libraries in future

**FissionColony**, a FloatingActionButton with organic animations

and 

**FanoutButton**, a FloatingActionButton that expands radially to display multiple FloatingActionButtons


Installation
============

### Gradle

```
compile 'com.yfo.opensource:amoeba:1.0'
```


FanoutButton
============

![Fanout Button](images/fanoutgif.gif)

Usage
=====

Use Resource IDs ```fanout_button_top```, ```fanout_button_left```, ```fanout_button_angled``` to assign actions to the top, left and the button angled at 45 degrees

```fan_margin``` - Assigns FloatingActionButton margin in its' parent. This is in addition to layout_margin. You can have ```layout_margin="0dp"``` and use this instead (At this point this option is redundant and will make sense when the backdrop feature is implemented)

```fan_start_rotation``` and ```fan_end_rotation``` defines the start and end rotation of the main FloatingActionButton (+)

```
<com.eo5.amoeba.views.FanOutButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fan_margin"
        app:fan_margin="@dimen/fan_margin2"
        app:fan_elevation="0dp"
        app:fan_enable_backdrop="true"
        app:fan_main_icon="@drawable/ic_add"
        app:fan_angled_icon="@drawable/ic_food"
        app:fan_left_icon="@drawable/ic_movies"
        app:fan_top_icon="@drawable/ic_location"
        app:fan_start_rotation="@integer/start_rot"
        app:fan_end_rotation="@integer/end_rot"/>

```

==============
### Features not yet completely implemented

#### FanOutButton

```
fan_enable_backdrop

```


#### FissionColony


================
### Travis-CI status

[![Build Status](https://travis-ci.org/SriramBms/Amoeba.svg?branch=master)](https://travis-ci.org/SriramBms/Amoeba)

================
### License
#### [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)