# NSU_Sports_Complex

Информационная система для СК НГУ. Реализуем размещение новостей, запись на секции и в тренажерный зал, админку для организаторов

Примерный план:
1) Размещение/создание новостей (делает админ)
2) Предложить новость. Ex.: если хочешь забронировать площадку и поиграть с кем-то, позвать других (разместит админ, предложить может любой)
3) Заполнение контента админом (секции и прочее)
4) Запись на секции и в трензал (без входа, подтверждение по почте)
5) Получение инфррмции о секциях (может каждый)
6) Редактирование новостей (админ)
7) Удаление новостей (админ)


# How to start our application

## Locally

`./gradlew.bat bootRun`

## Docker

`docker-compose up --build`

### Experimental feature

`./gradlew.bat bootRun -PuseDocker`