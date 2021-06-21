# Сервис chat-engine-service

Данный сервис представляет ядро приложения SChat

Ответственный за сервис: Полинов Тимур

## План работ:

#### Создать таблицу recent_message. Написать liquibase скрипт. Данная таблица хранит сообщения пользователей, которые находятся сейчас в online

Поля таблицы recent_message

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
 
 - UserCache, инициализация UserCache
 
 - GET /chat/user/all
   List<UserInfo> getAllUsers();
 
 - POST /chat/user/logout?userId={userId}
   void logout(String userId);

Амелин Дмитрий
 - POST /chat/user/authorize?userId={userId}
   void authorize(String userId);

 - POST /chat/message
   MessageInfo send(MessageInfo message);

Дмитрий Демидов
 -  GET /chat/message/sinceTime?sender={sender}&recipient={recipient}&timestamp={timestamp}
    List<MessageInfo> getMessages(String sender, String recipient, LocalDateTime lastSyncTime);
	
 - GET /chat/all?userId={userId}
   List<ChatInfo> getChatsInfo(String userId);

Критерии приемки:
- методы сервиса должны быть протестированы.