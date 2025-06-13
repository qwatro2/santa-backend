## Запуск в прод окружении
```bash
cd santa-backend
docker-compose up --build -d
```

## Запуск в дев окружении (IDEA)
```bash
cd santa-backend
docker-compose up db redis zookeeper kafka -d
```
В IDEA добавить профиль `dev`
1. ![](/screenshots/img.png)
2. ![](/screenshots/img_1.png)
3. ![](/screenshots/img_2.png)
