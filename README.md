<!-- PROJECT LOGO -->
<a href="https://play.google.com/store/apps/details?id=argument.twins.com.polykekschedule"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height=60px /></a>
<div align="center">
	<a href="https://english.spbstu.ru">
		<img src="images/logo.webp" alt="Logo" width="128" height="128">
	</a>
	<h2 align="center">Schedule application for Peter the Great St.Petersburg Polytechnic University</h2>
</div>

# About project
This application was developed specially for students and professors of St. Petersburg Polytechnic University. It provides quick access to schedule, group and department searches, campus navigation and also allows making some notes. The project was started in 2018, is maintained and regularly updated. This application is not the official application of the university, but has over 5 thousand users who use the app almost every day.

## Architecture and main technologies
+ Kotlin
+ Coroutines
+ Clear architecture (MVI with flows)
+ Single activity app
+ Multi-module-architecture with auto-clearing unused components from RAM (mechanism based on weak-references)
+ Unit tests for every each useCase, viewModel, repository + tests for room migrations and interceptors
+ Optimized UI (no xml-inflating for recycler view items - only native views)
+ Supports landscape mode
+ kDoc for everything (used an own plugin for Android studio <a href="https://plugins.jetbrains.com/plugin/17719-advance-kotlin-documentation-generator">
		<b>Advance Kotlin Documentation Generator</b>
	</a>)
+ Clear and careful coding (median size of fragments, useCases and viewModels is 125 lines)
+ Obfuscation for prod-version (aggressive mode of R8)
+ Compose (for new screens)

## Used the follow frameworks
+ Dagger
+ Room
+ Retrofit
+ Yandex-map-kit
+ Mockito
+ Cicerone (powerful framework for navigation. Very helpful for multi-module-architecture)
+ Firebase messaging (used for "Spirit of Peter": time to time, I send to students some funny messages or congratulations ;)
+ Firebase crashlytics & analytics
+ Paparazzi (for new screens)
+ Coil

## Demonstrating some features
<table>
    <tr>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/1_welcome_navigation.gif" width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/2_group_search.gif" width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/3_independed_tab_navigation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Smooth navigation between screens
        </td>
        <td>
            Animated search bar
        </td>
        <td>
            Independent navigation by tabs
        </td>
    </tr>
    <tr>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/4_yandex_map_kit.gif" width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/5_snow_animation.gif" width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/6_heartfall_animation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Campus navigation with help Yandex-map-kit
        </td>
        <td>
            Animated snow for winter holidays
        </td>
        <td>
            Animated "heartfall" for the Love days
        </td>
    </tr>
	    <tr>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/7_news_block.gif width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/8_switching_between_selected_items.gif" width="256"/>
        </td>
        <td>
			<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/9_smooth_animation.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td>
            Block with actual news of the university
        </td>
        <td>
            Update all content by switching between groups
        </td>
        <td>
            Just smooth animation =)
        </td>
    </tr>
</table>

## 🚀 About Me
I have been in Android since 2017. This is my pet project. You can judge me by this code. This application is not "dress app", I always use the same careful style for developing.

## 🔗 Contacts
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/georgiy-chebotarev/)

[![telegram](https://img.shields.io/badge/-telegram-red?color=white&logo=telegram)](https://t.me/georrge1994)

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## Changes
#v 2.3.3
• Implemented news & web-content modules
• Removed feedback module (changed to email intent)
• Slightly updated design
• Fixed crash for Samsungs
• Updated libraries (also JDK -> 21, kotlin -> 2.0)
• Replaced depreacated code
• Fixed some minor UI bugs
• Implemented Paparazzi and compose tests (for new screens)
• Disabled ```android.enableJetifier=true```

#v 2.3.0
• Migrated to kts from groovy
• Updated libraries