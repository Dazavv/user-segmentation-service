# User Segmentation Service

Сервис для управления сегментацией пользователей с поддержкой аудита и авторизации через JWT.

## Основные возможности

- Регистрация и авторизация пользователей с ролями: `ADMIN`, `ANALYST`, `VIEWER`.
- Управление сегментами пользователей:
  - Создание, редактирование, удаление сегментов
  - Добавление пользователей в сегментах
  - Удаление пользователей из сегментов
  - Распределение пользователей в сегменты по заданному проценту
- Аудит действий пользователей:
  - Логирование изменений с привязкой к пользователю (login, email).
  - Хранение действий в базе данных.
- JWT авторизация и аутентификация.
- REST API для работы с пользователями и сегментами.

## Технологии

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- Hibernate
- PostgreSQL
- Lombok

## Схема ролей

| Роль     | Возможности                                                                              |
|----------|------------------------------------------------------------------------------------------|
| ADMIN    | Полный доступ к сегментам и пользователям                                                |
| ANALYST  | Просмотр и управление сегментами (добавление/удаление пользователей, создание сегментов) |
| VIEWER   | Только просмотр сегментов                                                                |

## Настройка проекта

1. Клонировать репозиторий:

```bash
git clone <REPO_URL>
cd user-segmentation-service
````

2. Настроить `application.properties` или `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/segmentation
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

jwt.secret.access=<BASE64_SECRET>
jwt.secret.refresh=<BASE64_SECRET>
```

3. Собрать и запустить:

```bash
./gradlew bootRun
```

## Аудит

Все изменения логируются в таблицу `audit_log` с полями:

* `entityName` — сущность, к которой применено действие
* `entityId` — ID сущности
* `action` — действие (`ADD`, `UPDATE`, `DELETE`...)
* `login`, `email` — пользователь, совершивший действие
* `timestamp` — время действия

### Пример аудита действий с сегментами

| ID  | action        | details               | email       | entity_id | entity_name         | login | Время                   |
|-----|-----------------|-----------------------|-------------------------|---------|---------------------------|---------------|------------------------|
| 7   | DELETE_SEGMENT  | Segment was deleted   | user1@gmail.com        | 7       | Segment CHAT_GPT | misha777       | 2025-08-15 21:44:19.900989 |
| 8   | ADD_SEGMENT  | Segment was added   | admin1@gmail.com        | 8       | Segment CLOUD_DISCOUNT_60  | admin         | 2025-08-15 21:48:56.684644 |


## Авторизация с JWT

Для доступа к защищённым API используется JWT (JSON Web Token).  

### Получение токена
- **Endpoint:** `POST /api/auth/login`  
- **Тело запроса:**
```json
{
  "login": "userLogin",
  "password": "userPassword"
}
````

* **Ответ:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Использование access token

* Добавляйте заголовок `Authorization` к запросам к защищённым ресурсам:

```
Authorization: Bearer <accessToken>
```

### Refresh token

* Для обновления access token без повторной авторизации:
* **Endpoint:** `POST /api/auth/refresh`
* **Тело запроса:**

```json
{
  "refreshToken": "<refreshToken>"
}
```

* **Ответ:** новый access token.

### Примечания

* `accessToken` действителен короткое время (например, 15 минут).
* `refreshToken` действителен дольше (например, 7 дней).
* Токены хранятся только на клиенте, сервер проверяет их через JWTProvider.


## API

Все защищённые эндпоинты требуют заголовок:
```
Authorization: Bearer <accessToken>
```

### Получить все сегменты
GET http://localhost:8080/api/segments  

### Получить сегмент по коду
GET http://localhost:8080/api/segments/{code}  

### Создать сегмент
POST http://localhost:8080/api/segments  
```json
{
  "code": "CLOUD_DISCOUNT_50",
  "info": "Скидка 50% на облако"
}
````

### Добавить пользователей в сегмент

POST [http://localhost:8080/api/segments/{code}/users](http://localhost:8080/api/segments/{code}/users)

```json
{
  "usersId": [1, 2, 3]
}
```

### Распределить сегмент случайным пользователям

POST [http://localhost:8080/api/segments/{code}/distribute](http://localhost:8080/api/segments/{code}/distribute)

```json
{
  "code": "CLOUD_DISCOUNT_30",
  "percent": 10
}
```

### Изменить сегмент

PUT [http://localhost:8080/api/segments/{code}](http://localhost:8080/api/segments/{code})

```json
{
  "newCode": "CLOUD_DISCOUNT_35",
  "info": "Обновленная скидка 35%",
  "usersId": [1, 2]
}
```

### Удалить сегмент

DELETE [http://localhost:8080/api/segments/{code}](http://localhost:8080/api/segments/{code})

### Удалить всех пользователей из сегмента

DELETE [http://localhost:8080/api/segments/{code}/users/all](http://localhost:8080/api/segments/{code}/users/all)

### Удалить конкретных пользователей из сегмента

DELETE [http://localhost:8080/api/segments/{code}/users](http://localhost:8080/api/segments/{code}/users)

```json
{
  "usersId": [1, 2]
}
```
