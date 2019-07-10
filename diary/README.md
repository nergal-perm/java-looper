## Общее описание проекта

Приложение должно позволять создавать описание музыкального трека в виде
последовательности записываемых и воспроизводимых фрагментов звука, в итоге
должен получиться многодорожечный лупер с функцией автоматического воспроиз-
ведения или приостановки звука для каждой дорожки в заранее заданный момент
времени.

## Ход разработки

### 2019-06-07 14:02

Для начала решил создать некий условный `Controller`, занимающийся созданием
общего плана трека. Его функции на данный момент:

* парсинг файла с описанием трека. Пока не решил, в каком формате хранить эти
 данные - в JSON или YAML.
* создание полного таймлайна для трека. То есть описание того, в какой момент
 времени запускать запись или воспроизведение того или иного фрагмента звука.

**Что планируется сделать сегодня?**

Для начала думаю создать простой скрипт на `groovy`, чтобы удобно распарсить
описание трека. Плюс подготовить тестовую инфраструктуру для проекта. Пока не
буду заморачиваться с какими-либо системами сборки, все пока будет работать 
только из скрипта.

### 2019-06-19 04:39

Узнал о таком linux-пакете, как `sooperlooper` - это выполненный в виде сервера
программный лупер, с которым возможно взаимодействие по OSC-протоколу.

```bash
sudo apt-get update && sudo apt-get install linux-lowlatency --fix-missing
sudo apt-get install jackd2 pulseaudio-module-jack sooperlooper
sudo adduser eugene audio
sudo vim /etc/security/limits.d/audio.conf 
sooperlooper
```

Запущенный сервер `sooperlooper` получает OSC-сообщения, 
[список доступных команд](http://essej.net/sooperlooper/doc_osc.html).
Для работы с ними из java/groovy нужно подключить библиотеку `javaosc`, например,
так:

```groovy
#!/usr/bin/groovy
// https://mvnrepository.com/artifact/com.illposed.osc/javaosc-core
@Grapes( 
    @Grab('com.illposed.osc:javaosc-core:0.6') 
) 
 
import com.illposed.osc.transport.udp.OSCPortOut 
import com.illposed.osc.OSCMessage 
import java.net.InetAddress 
import java.net.InetSocketAddress 
 
OSCPortOut sender = new OSCPortOut(new InetSocketAddress(InetAddress.getLocalHost(), 9951)) 
OSCMessage message = new OSCMessage("/set", ['tempo', 140.0f]) 
 
try { 
    sender.send(message) 
} catch (Exception e) { 
    println e.getMessage() 
} 
```

### 2019-07-10 07:11

Предполагаю, что план трека будет использовать метки времени в виде:

```
1:0.00
^ ^ ^
│ │ └─── tick, сотая часть удара в такте
│ └───── beat, удар в такте (кол-во зависит от размера (time signature))
└─────── measure, порядковый номер такта
```

Соответственно, нужен объект, позволяющий переводить время в секундах в этот
формат и наоборот.
