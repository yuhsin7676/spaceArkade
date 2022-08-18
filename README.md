# spaceArkade

## Описание

Онлайн-арканоид на 2 игрока. В игровом поле летает шар и разбивает плитки. Задача - разбить все плитки.
Если шар попадает в верхнюю или нижнюю стену, то он уничтожается. Чтобы этого не произошло, 2 игрока управляют платформами,
перемещающихся у верхней и нижней стен. При уничтожении плиток возможно выпадение бонусов и антибонусов,
изменяющих размер платформы, скорость и количество шаров и другие параметры.

## Как запустить игру?

1) Скачать и установить java-jre или java-jdk 8+ (например, отсюда: (https://adoptopenjdk.net/upstream.html?variant=openjdk8&jvmVariant=hotspot))
2) Скачать исходный код (code->download ZIP или, если на вашем компьютере установлен git, командой: <b>git clone https://github.com/yuhsin7676/spaceArkade.git</b>)
3) Узнать свой ip-адрес (Набрать команду в командной строке Windows: <b>ipconfig</b>, в терминале Linux: <b>ip-address</b>). Обычно это 192.168... или 10...
4) Запустить <b>spaceArkade.jar</b> двойным кликом мыши или командой: <b>java -jar spaceArkade.jar</b>
5) Запустить на 2-х разных компьютерах, подключенных к одной сети, браузер и перейти на <b>http://{ваш ip-адрес из п.3}:8888</b>

## Как играть?

1) На 1-м компьютере нажать кнопку "Играть", после чего вверху, на черной панели появится надпись "Ждем второго игрока".
2) На 2-м компьютере нажать кнопку "Играть", после чего на обеих компьютерах появится поле с платформой, шаром и плиткой.
3) Для перемещения платформы нажимать клавиши A и D.

## Дополнительно

Миры (расположение плиток) хранятся в файле <b>spaceArkade.s3db</b> (на данный момент там 3 мира).
Путь к данному файлу находится в файле <b>application.properties</b>, лежащий внутри <b>spaceArkade.jar</b>.
Чтобы редактировать этот файл, откройте <b>spaceArkade.jar</b> архиватором (например, WinRAR), 
перейдите в папку <b>/BOOT-INF/classes/</b> и откройте <b>application.properties</b> текстовым редактором.
В файле есть следующие настройки:

>
    server.port = 8888
    db.url = jdbc:sqlite:/home/ilya/spaceArkade.s3db
    db.dbName =
    db.username =
    db.password =

Где <b>server.port</b> - число, стоящее после ip-адреса. Если изменить на 8887, то к игре нужно будет подключаться по <b>http://{ваш ip-адрес из п.3}:8887</b>,
<b>db.url</b> - путь к файлу

## Добавление своих миров

<b>spaceArkade.s3db</b> - файл базы данных SQLite. Редактировать его можно, например, в программе DBeaver.
Миры храняться в таблице <b>begincomponents</b> в столбце <b>components</b> и представлены в виде массива:

>
    [
    ["Tile3","Tile3","Tile3","Tile3","     ","     ","Tile3","Tile3","Tile3","Tile3"],
    ["Tile3","Tile3","Tile3","Tile3","     ","     ","Tile3","Tile3","Tile3","Tile3"],
    ["Tile2","Tile2","Tile2","Tile3","     ","     ","Tile2","Tile2","Tile2","Tile2"],
    ["Tile2","Tile2","Tile2","Tile3","     ","     ","Tile2","Tile2","Tile2","Tile2"],
    ["     ","     ","     ","Tile3","     ","     ","Tile3","     ","     ","     "],
    ["Tile1","Tile1","     ","Tile3","     ","     ","Tile3","Tile1","     ","     "],
    ["Tile2","Tile2","     ","Tile3","     ","     ","     ","Tile1","Tile1","     "],
    ["     ","     ","     ","Tile3","     ","     ","     ","     ","     ","     "],
    ["     ","Tile2","Tile2","Tile3","     ","     ","     ","     ","     ","Tile2"],
    ["     ","Tile2","Tile2","Tile3","     ","     ","     ","     ","     ","     "],
    ["     ","     ","     ","     ","     ","     ","Tile3","Tile2","Tile2","     "],
    ["Tile2","     ","     ","     ","     ","     ","Tile3","Tile2","Tile2","     "],
    ["     ","     ","     ","     ","     ","     ","Tile3","     ","     ","     "],
    ["     ","Tile1","Tile1","     ","     ","     ","Tile3","     ","Tile2","Tile2"],
    ["     ","     ","Tile1","Tile3","     ","     ","Tile3","     ","Tile1","Tile1"],
    ["     ","     ","     ","Tile3","     ","     ","Tile3","     ","     ","     "],
    ["Tile2","Tile2","Tile2","Tile2","     ","     ","Tile3","Tile2","Tile2","Tile2"],
    ["Tile2","Tile2","Tile2","Tile2","     ","     ","Tile3","Tile2","Tile2","Tile2"],
    ["Tile3","Tile3","Tile3","Tile3","     ","     ","Tile3","Tile3","Tile3","Tile3"],
    ["Tile3","Tile3","Tile3","Tile3","     ","     ","Tile3","Tile3","Tile3","Tile3"]
    ]


Здесь Tile1 - зеленая плитка
Здесь Tile2 - желтая плитка
Здесь Tile3 - рыжая плитка

Для добавления нового мира нужно создать ноый ряд и заполнить <b>components</b> своим массивом.
