
# Polykek-schedule-app
This app was developed specially for students and professors of St. Petersburg Polytechnic University. It provides quick access to the schedule, group and department searches, campus navigation and also allows to make some notes. The project started in 2018, is maintained and regularly updated. This application is not an official application of the university, but has over 5 thousand users who use app almost every day.

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/) 

## Demonstrating some features
<table>
    <tr>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/1_welcome_navigation.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/2_group_search.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/3_independed_tab_navigation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Smooth navigation between screens.
        </td>
        <td>
            Animated searchbar.
        </td>
        <td>
            Independent tab navigation
        </td>
    </tr>
    <tr>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/4_yandex_map_kit.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/5_snow_animation.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/6_heartfall_animation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Navigation with help Yandex-map-kit.
        </td>
        <td>
            Animated snow - shows on the winter holidays.
        </td>
        <td>
            Animated heartfall - shows on the Valentine's Day
        </td>
    </tr>
	    <tr>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/7_harry_potter_feature.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/8_switching_between_selected_items.gif" width="256"/>
        </td>
        <td>
            <img src="https://github.com/georrge1994/polykek-schedule-app/raw/master/gifs/9_smooth_animation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            1st April joke - changes all lessons to Harry Potter lessons.
        </td>
        <td>
            Simple switching between groups and professors.
        </td>
        <td>
            Just smooth animation =)
        </td>
    </tr>
</table>

##Arhitecture and main technologies
+ Kotlin
+ Coroutines
+ Clear arhitecture (MVVM without databingins - just directly liveData)
+ Single activity app
+ Multi-module-arhitecture with auto-clearing unused modules from RAM by GC (used mechanism based on soft-references)
+ Unit tests for every each useCase, viewModel, repository + tests for room migrations and interceptors
+ Optimized UI (no xml-inlfating for recycler view items - only native views)
+ Supports landscape mode
+ kDoc for everything (used the own plugin for Android studio https://plugins.jetbrains.com/plugin/17719-advance-kotlin-documentation-generator)
+ Clear and careful coding (median size of fragments, usecases and viewmodels is 100 lines)
+ Obfuscated prod-version

##Frameworks
+ Dagger
+ Room
+ Retrofit
+ Yandex-map-kit
+ Mockito
+ Cicerone - powerfull framework for navigation. Very helpful for multi-module-arhitecture
+ Firebase messaging - used for spirit of Peter. Time to time I send users some messages or congragulations ;)
+ Crashlytic

## 🚀 About Me
I'm an Android developer with 5.5 years of experience. This is my pet project. You can judge me by his code. This application is not "dress app", I always use the same careful style for developing.

## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/)
[![telegram](https://img.shields.io/badge/-telegram-red?color=white&logo=telegram)](https://t.me/georrge1994)
