# backup_service

Сервис клиента архивирует каталог и передает по сети.
Сервер принимает и кладет архив в указанную папку.


Пример сервера: java -jar backup_service.jar -server 4545 F:\Destination_folder
Пример клиента: java -jar backup_service.jar -client 4545 address_server C:\Source_folder
PS: протокол TCP


Для работы необходима java 11
JDK 11 https://bell-sw.com/
