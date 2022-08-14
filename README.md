# spaceArkade

## Описание

Онлайн-арканоид на 2 игрока. В игровом поле летает шар и разбивает плитки. Задача - разбить все плитки.
Если шар попадает в верхнюю или нижнюю стену, то он уничтожается. Чтобы этого не произошло, 2 игрока управляют платформами,
перемещающихся у верхней и нижней стен. При уничтожении плиток возможно выпадение бонусов и антибонусов,
изменяющих размер платформы, скорость и количество шаров и другие параметры.

## Как запустить игру?

1) Скачать и установить java-jre или java-jdk 8+ (например, отсюда: https://adoptopenjdk.net/upstream.html?variant=openjdk8&jvmVariant=hotspot)
2) Скачать исходный код (code->download ZIP или, если на вашем компьютере установлен git, командой: <b>git clone https://github.com/yuhsin7676/spaceArkade.git</b>)
3) Узнать свой ip-адрес (Набрать команду в командной строке Windows: <b>ipconfig</b>, в терминале Linux: <b>ip-address</b>). Обычно это 192.168... или 10...
4) Запустить <b>spaceArkade.jar</b> двойным кликом мыши или командой: <b>java -jar spaceArkade.jar</b>
5) Запустить на 2-х разных компьютерах, подключенных к одной сети, браузер и перейти на <b>http://{ваш ip-адрес из п.3}:8888</b>

## Как играть?

1) На 1-м компьютере нажать кнопку "Играть", после чего вверху, на черной панели появится надпись "Ждем второго игрока".
2) На 2-м компьютере нажать кнопку "Играть", после чего на обеих компьютерах появится поле с платформой, шаром и плиткой.
3) Для перемещения платформы нажимать клавиши A и D.