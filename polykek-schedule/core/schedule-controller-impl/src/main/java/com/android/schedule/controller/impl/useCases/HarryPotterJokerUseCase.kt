package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.api.week.IWeekResponse
import com.android.schedule.controller.impl.models.HarryPotterLesson
import com.android.shared.code.utils.markers.IUseCase
import java.util.*
import javax.inject.Inject

/**
 * Every April 1st users see titles from Harry Potter world.
 *
 * @constructor Create empty constructor for harry potter joker use case
 */
internal class HarryPotterJokerUseCase @Inject constructor() : IUseCase {
    private val listOfHarryPotterLessons by lazy { getHarryPotterLessons() }
    private var shift: Int = 0
    private var step = 0
    private var index: Int = 0

    /**
     * Replace lessons with Harry Potter memes.
     *
     * @param groupId Group id
     * @param weekResponse Request result week
     * @return [IWeekResponse] or null
     */
    internal fun replaceLessonsWithHarryPotterMemes(groupId: Int, weekResponse: IWeekResponse?): IWeekResponse? {
        initToDefault(groupId)
        weekResponse?.makeJoke()
        return weekResponse
    }

    /**
     * Init to default.
     *
     * @param groupId Group id
     */
    private fun initToDefault(groupId: Int) {
        this.step = groupId % 10
        this.index = 0
        this.shift = 0
    }

    /**
     * Make joke.
     *
     * @receiver [IWeekResponse]
     */
    private fun IWeekResponse.makeJoke() = days.forEach { requestDay ->
        for (i in requestDay.lessons.indices) {
            val harryPotterLesson = getHarryPotterLesson()
            val currentValue = requestDay.lessons[i]
            requestDay.lessons[i] = currentValue.copy(
                subjectShort = harryPotterLesson.title,
                auditories = currentValue.auditories?.firstOrNull()?.let {
                    listOf(it.copy(name = harryPotterLesson.place))
                } ?: Collections.emptyList(),
                teachers = currentValue.teachers?.firstOrNull()?.let {
                    listOf(it.copy(fullName = harryPotterLesson.teacher, chair = "Школа Чародейства и Волшебства Хогвартс"))
                } ?: Collections.emptyList()
            )
        }
    }

    /**
     * Every each group should see the unique order of lessons. The order should not change and ramin the same each app launch.
     *
     * @return [HarryPotterLesson]
     */
    private fun getHarryPotterLesson(): HarryPotterLesson {
        index += step
        if (index >= listOfHarryPotterLessons.size) {
            shift = (shift + 1) % listOfHarryPotterLessons.size
            index = shift
        }
        return listOfHarryPotterLessons[index]
    }

    /**
     * Get Harry Potter lessons.
     *
     * @return List of Harry Potter memes
     */
    private fun getHarryPotterLessons() = listOf(
        HarryPotterLesson("Зельеварение", "Гораций Слизнорт", "Класс зельеварения"),
        HarryPotterLesson("Астрономия", "Аврора Синистра", "Астрономическая башня"),
        HarryPotterLesson("Защита от Тёмных искусств", "Северус Снейп", "Класс Защиты от Тёмных искусств"),
        HarryPotterLesson("Заклинания", "Филиус Флитвик", "Класс заклинаний"),
        HarryPotterLesson("Защита от Тёмных искусств", "Римус Люпин", "Класс Защиты от Тёмных искусств"),
        HarryPotterLesson("Зельеварение", "Северус Снейп", "Комната смешивания зелий"),
        HarryPotterLesson("История магии", "Почти Безголовый Ник", "Класс Истории магии"),
        HarryPotterLesson("Защита от Тёмных искусств", "лже-Грозный Глаз Грюм", "Класс Защиты от Тёмных искусств"),
        HarryPotterLesson("Травология", "Помона Стебль", "Теплицы Хогвартса"),
        HarryPotterLesson("Трансфигурация", "Минерва Макгонагалл", "Класс Трансфигурации"),
        HarryPotterLesson("Защита от Тёмных искусств", "Златопуст Локонс", "Класс Защиты от Тёмных искусств"),
        HarryPotterLesson("Полёты на мётлах", "Роланда Трюк", "Поляна возле Запретного Леса"),
        HarryPotterLesson("Изучение Древних рун", "Батшеда Бабблинг", "Класс изучения древних рун"),
        HarryPotterLesson("Магловедение", "Квиринус Квиррелл", "Выручай комната"),
        HarryPotterLesson("Нумерология", "Септима Вектор", "Выручай комната"),
        HarryPotterLesson("Трансфигурация", "Альбус Дамблдор", "Класс Трансфигурации"),
        HarryPotterLesson("Прорицания", "Сивилла Трелони", "Класс Прорицаний"),
        HarryPotterLesson("Уход за магическими существами", "Рубеус Хагрид", "Запретный лес")
    )
}