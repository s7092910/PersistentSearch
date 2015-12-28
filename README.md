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
###Persistent Search View
The `PersistentSearchView` has been created to be easy to use and highly customizable. But due to the customizability, it doesn't do much hand holding when it comes to drawables.

**XML**

Here is an example of **PersistentSearchView**'s xml attributes:
```XML
    <com.wanderingcan.persistentsearch.PersistentSearchView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/search_bar"
        app:navSrc="@drawable/someDrawable"
        app:endSrc="@drawable/someOtherDrawable"
        app:hint="Enter some text to search"
        app:hintAlwaysActive="true"
        app:showMenu="true/>
```

You can set the icons for both of the main icons on the `PersistentSearchView` in the xml by using the `app:navSrc` xml
attribute for the left icon. And for the right icon by using the `app:endSrc` xml attribute.

The `app:hint` attribute allows a text hint to be added to the `PersistentSearchView`. The `app:hintAlwaysActive` allows the
hint to be active when the user does not have the searchbar active or to only show it when there is no text in the searchbar
and the user has the searchbar active.

The `app:showMenu` determines if the `PersistentSearchView` should show the menu below the searchbar when active if there are
items in the search menu. If set to false it will never show the menu even if there are items in the menu.


**Java**

Inside the activity, there are a few listeners that will have to be set for the `PersistentSearchView` to be useful.
```JAVA
persistentSearchView.setOnSearchListener(new PersistentSearchView.OnSearchListener() {
            @Override
            public void onSearchOpened() {
                //Called when the Searchbar is opened by the user or by something calling
                //persistentSearchView.openSearch();
            }

            @Override
            public void onSearchClosed() {
                //Called when the searchbar is closed by the user or by something calling
                //persistentSearchView.closeSearch();
            }

            @Override
            public void onSearchCleared() {
                //Called when the searchbar has been cleared by the user by removing all 
                //the text or hitting the clear button. This also will be called if 
                //persistentSearchView.populateSearchText() is set with a null string or
                //an empty string
            }

            @Override
            public void onSearchTermChanged(CharSequence term) {
                //Called when the text in the searchbar has been changed by the user or 
                //by persistentSearchView.populateSearchText() with text passed in.
                //Best spot to handle giving suggestions to the user in the menu
            }

            @Override
            public void onSearch(CharSequence text) {
                //Called when the user hits the IME Action Search on the keyboard to search
                //Here is the best spot to handle searches
            }
        });
```

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
