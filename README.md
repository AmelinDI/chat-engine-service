# Сервис chat-engine-service

Данный сервис представляет ядро приложения SChat

Ответственный за сервис: Полинов Тимур

## План работ:

#### Создать таблицу recent_message. Написать liquibase скрипт. Данная таблица хранит сообщения пользователей, которые находятся сейчас в online

Поля таблицы message

  * id: string
  * sender: string
  * recipient: string
  * content: string
  * message_timestamp: LocalDateTime
  * last_access_time: LocalDateTime (время сохранения в БД)
  * was_read: boolean
  * read_time: LocalDateTime

### Реализовать rest-контроллер /chat/**/*, который обеспечивает работу с сообщениями пользователей

План работы:

Полинов Тимур
 - Написать ликубейс скрипты и разместить из в папке sql, написать структуру MessageInfo, getters & setters, toString
 - Authorize user PUT /chat/user/authorize?userId={userId}

Амелин Дмитрий
 - Logout user PUT /chat/user/logout?userId={userId}

Дмитрий Демидов

Критерии приемки:
- методы сервиса должны быть протестированы.