package com.ggkttd.kolmakov.testSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@ComponentScan("com.ggkttd.kolmakov.testSystem")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
//-----------------------------------------------------------------
//todo: add to administrator responsibility to check registered users
//todo: check if all questions have at least one right answer
//todo: add navigation between answers tutor create test
//todo: set answers amount max=5;
//todo: create html page style with inheritance from bootstrap
//todo: universal error page
//todo: add module to see user test with right and wrong answers
//todo: add navigation between right/wrong students answers
//todo: tutor can see user profile
//todo: tutor search test filters
//-----------------------------------------------------------------

//todo: create getMapping by id in tests, subjectcs ...

//todo: FRONTEND INTERACTION
//tutor after create, delete update show messages

//todo: USER STATISTIC
//пройдено тестов -> посмотреть какие
//осталось пройти тестов -> посмотреть какие
//среднее время на выполнение теста
//процент верных ответов -- рассчитать как суммарное количество правильных ответов всех тестов ко всем вопросам
//наибольшая успеваемость по предмету
//наименьшая успеваемость по предмету
//рейтинг среди сдавших тест (если одинаково у нескольких студентов может быть одинаковое значение рейтинга)


//todo: TUTOR STATISTIC
//создано тестов -> посмотреть список тестов
//пройденные студентами кол-во -> посмотреть кто прошел, какие тесты -> посмотреть результаты
//самые высокие баллы у студентов по тесту(ам)
//самые низкие баллы у студентов по тесту(ам)
//самая успешная группа по предмету (можно рейтинг 1-е 3 группы)
//наименее успешная группа по предмету
//


//	TODO: !!! stopped at debugging user statistic

// todo: сделать возможность добавления изображения/видео в вопросах теста

//todo: для сохранения видеофайлов добавить классу Question поле с именем видеофайла.