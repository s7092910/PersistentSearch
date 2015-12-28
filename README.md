# PersistentSearch
An Android Persistent Search Bar like the one in Google Now and the Google Play Store. It follows Google's Material Design while
highly customizable

##Features
* `PersistentSearchView` that is the Persistent Search Bar
* Support for LTR and RTL layouts
* Support for custom drawables in all image locations
* Support for showing up to 5 items in a Search Menu that is active when the Search Bar is active
* Search Menu Items are customizable with 2 icons and a title
* Option to turn off the Search Menu

##Examples
![Hint Only](https://github.com/s7092910/PersistentSearch/blob/master/images/Hint.png)
![With the Menu Shown](https://github.com/s7092910/PersistentSearch/blob/master/images/Menu.png)
![Filled in with some text](https://github.com/s7092910/PersistentSearch/blob/master/images/Filled.png)

##Include in your project
Currently the project is waiting for approval to be added to jcenter. So in the mean time you will have to add the following to
your `build.gradle`:
```
repositories {
    maven {
        url 'https://dl.bintray.com/s7092910/maven/'
    }
}
```

Add a dependency to your `build.gradle`:
```
dependencies {
    compile 'com.wanderingcan.widget:persistentsearch:1.0.0'
}
```

##Usage
### Persistent Search View


##Caveats
Much of this has been made to be highly customizable, so there is very little hand holding. But it has been made 
as easy as possible to use. In the future some things might be automatically handled by the library but only if it is found useful and highly requested (or if I feel like it).

This library is `minSdkVersion=14` and if that changes, the version number will be increased, not decreased.

##License


    Copyright 2015 Christopher Beda

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
