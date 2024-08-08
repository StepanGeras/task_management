Task Management System
Описание проекта
Этот проект представляет собой систему управления задачами (Task Management System) на базе Java с использованием Spring Boot, Spring Security, JWT для аутентификации и авторизации, а также Docker для контейнеризации. Система позволяет пользователям создавать, редактировать, удалять и просматривать задачи. Также поддерживается работа с комментариями и фильтрация задач.

Содержание
Требования
Запуск проекта
API
Документация
Тестирование
Примечания
Требования
Java 17+
Docker и Docker Compose
PostgreSQL или MySQL (выбирается в конфигурации)
Запуск проекта
1. Клонируйте репозиторий
bash
git clone https://github.com/ваш-репозиторий/task-management-system.git
cd task-management-system
2. Настройте файл конфигурации
Создайте файл .env в корневой директории проекта и добавьте необходимые переменные окружения. Пример содержимого файла .env:

env
Копировать код
JWT_SECRET=ваш_секретный_ключ
JWT_EXPIRATION_MS=86400000
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/task_management
SPRING_DATASOURCE_USERNAME=ваш_пользователь
SPRING_DATASOURCE_PASSWORD=ваш_пароль
3. Запустите Docker Compose
Убедитесь, что Docker и Docker Compose установлены. Затем запустите Docker Compose для создания и запуска контейнеров.

bash
Копировать код
docker-compose up --build
4. Запуск приложения
После того как контейнеры будут запущены, приложение будет доступно по адресу:

arduino
Копировать код
http://localhost:8080
Swagger UI доступен по адресу:

bash
Копировать код
http://localhost:8080/swagger-ui.html
API
Регистрация пользователя
POST /auth/register

Тело запроса:

json
Копировать код
{
  "email": "user@example.com",
  "password": "password"
}
Вход в систему
POST /auth/signin

Тело запроса:

json
Копировать код
{
  "email": "user@example.com",
  "password": "password"
}
Ответ:

json
Копировать код
{
  "token": "jwt_token"
}
Получение задач
GET /tasks
Заголовок запроса: Authorization: Bearer <jwt_token>
Создание задачи
POST /tasks

Тело запроса:

json
Копировать код
{
  "title": "Task Title",
  "description": "Task Description",
  "status": "in_progress",
  "priority": "high",
  "assignee": "assignee@example.com"
}
Получение комментариев для задачи
GET /tasks/{taskId}/comments
Заголовок запроса: Authorization: Bearer <jwt_token>
Создание комментария
POST /tasks/{taskId}/comments

Тело запроса:

json
Копировать код
{
  "text": "Comment text",
  "author": "author@example.com"
}
Документация
Документация API доступна по адресу:

bash
Копировать код
http://localhost:8080/v3/api-docs
Swagger UI доступен по адресу:

bash
Копировать код
http://localhost:8080/swagger-ui.html
Тестирование
Для тестирования можно использовать Postman или любой другой инструмент для отправки HTTP-запросов. Убедитесь, что все необходимые эндпоинты и функции протестированы.

Примеры тестов
Регистрация нового пользователя:

Запрос: POST /auth/register
Тело запроса: { "email": "user@example.com", "password": "password" }
Ожидаемый ответ: HTTP 200 OK
Вход в систему:

Запрос: POST /auth/signin
Тело запроса: { "email": "user@example.com", "password": "password" }
Ожидаемый ответ: { "token": "jwt_token" }
Примечания
Убедитесь, что Docker и Docker Compose установлены и правильно настроены.
Настройте переменные окружения в .env перед запуском.
Для дальнейшей настройки и поддержки обратитесь к документации Spring Boot и Docker.
