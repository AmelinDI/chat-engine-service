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
 - Написать ликубейс скрипты и разместить из в папке sql, написать структуру Message, MessageEntity, getters & setters, toString
 - Save all messages PUT /storage/message/all
 - Delete message by id DELETE /storage/message?messageId={messageId}

Амелин Дмитрий
 - Get all messages between sender and receiver since timestamp GET /storage/message/allSinceTime?sender={sender}&receiver={receiver}&timestamp={timestamp}
 - Save message PUT /storage/message

Дмитрий Демидов
 - Get message by id GET /storage/message?messageId={messageId}
 - Get all messages between sender and receiver GET /storage/message/all?sender={sender}&receiver={receiver}

Критерии приемки:
- методы сервиса должны быть протестированы.