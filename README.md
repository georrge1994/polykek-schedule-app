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
+ Clear architecture (MVVM without dataBinding - just directly liveData)
+ Single activity app
+ Multi-module-architecture with auto-clearing unused components from RAM (mechanism based on soft-references)
+ Unit tests for every each useCase, viewModel, repository + tests for room migrations and interceptors
+ Optimized UI (no xml-inflating for recycler view items - only native views)
+ Supports landscape mode
+ kDoc for everything (used an own plugin for Android studio <a href="https://plugins.jetbrains.com/plugin/17719-advance-kotlin-documentation-generator">
		<b>Advance Kotlin Documentation Generator</b>
	</a>)
+ Clear and careful coding (median size of fragments, useCases and viewModels is 100 lines)
+ Obfuscation for prod-version

## Used the follow frameworks
+ Dagger
+ Room
+ Retrofit
+ Yandex-map-kit
+ Mockito
+ Cicerone (powerful framework for navigation. Very helpful for multi-module-architecture)
+ Firebase messaging (used for "Spirit of Peter": time to time, I send to students some funny messages or congratulations ;)
+ Crashlytics


## Demonstrating some features
<table>
    <tr>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/1_welcome_navigation.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/1_welcome_navigation.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/2_group_search.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/2_group_search.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/3_independed_tab_navigation.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/3_independed_tab_navigation.gif" width="256"/>
			</a>
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
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/4_yandex_map_kit.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/4_yandex_map_kit.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/5_snow_animation.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/5_snow_animation.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/6_heartfall_animation.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/6_heartfall_animation.gif" width="256"/>
			</a>
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
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/7_harry_potter_feature.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/7_harry_potter_feature.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/8_switching_between_selected_items.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/8_switching_between_selected_items.gif" width="256"/>
			</a>
        </td>
        <td>
			<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs/9_smooth_animation.gif">
				<img src="https://github.com/georrge1994/polykek-schedule-app/blob/main/gifs/9_smooth_animation.gif" width="256"/>
			</a>
        </td>
    </tr>
    <tr>
        <td>
            1-st April joke. Changes all items to items from Harry Potter universe
        </td>
        <td>
            Update all content by switching between groups
        </td>
        <td>
            Just smooth animation =)
        </td>
    </tr>
</table>

## Links
<a href="https://github.com/georrge1994/polykek-schedule-app/blob/main/high_quality_gifs">High quality gifs</a>


## 🚀 About Me
I'm an Android developer with 5.5 years of experience. This is my pet project. You can judge me by this code. This application is not "dress app", I always use the same careful style for developing.

## 🔗 Contacts
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/georgiy-chebotarev/)

[![telegram](https://img.shields.io/badge/-telegram-red?color=white&logo=telegram)](https://t.me/georrge1994)

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/) 