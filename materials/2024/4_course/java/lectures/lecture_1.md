# Лекція 1

# Утилітні класи

## 1. Клас Date

Клас Date інкапсулює поточну дату і час. Перед тим, як розпочати розгляд класу Date, важливо зазначити, що він зазнав
значних змін у порівнянні з оригінальною версією, визначеною в Java 1.0. Коли була випущена Java 1.1, багато функцій,
які виконувались оригінальним класом Date, були перенесені до класів Calendar та DateFormat, і в результаті багато
методів оригінального класу Date версії 1.0 були застарілі. Оскільки застарілі методи версії 1.0 не повинні
використовуватись у новому коді, вони тут не описані.

Табл. 1 - Незастарілі методи, визначені в класі Date

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_1.png)

Клас Date підтримує наступні незастарілі конструктори:

```java
Date()
        
Date(long millisec)
```

Перший конструктор ініціалізує об'єкт з поточною датою і часом. Другий конструктор приймає один аргумент, який дорівнює
кількості мілісекунд, що минули з півночі 1 січня 1970 року. Незастарілі методи, визначені в класі Date, показані в
табл. 1. Клас Date також реалізує інтерфейс Comparable.
Як видно з табл. 1, незастарілі функції класу Date не дозволяють отримати окремі компоненти дати або часу. Як показано в
наступній програмі, ви можете отримати дату і час лише у вигляді кількості мілісекунд, у його стандартному рядковому
представленні, яке повертає метод toString(), або у вигляді об'єкта Instant. Щоб отримати більш детальну інформацію про
дату і час, ви будете використовувати клас Calendar.

```java
// Show date and time using only Date methods.

import java.util.Date;

class DateDemo {
    public static void main(String[] args) {
        // Instantiate a Date object
        Date date = new Date();
        // display time and date using toString()
        System.out.println(date);
        // Display number of milliseconds since midnight, January 1, 1970 GMT
        long msec = date.getTime();
        System.out.println("Milliseconds since Jan. 1, 1970 GMT = " + msec);
    }
}
```

Sample output is shown here:

```text
Sat Jan 01 10:52:44 CST 2022
Milliseconds since Jan. 1, 1970 GMT = 1641056951341
```

## 2. Клас Calendar

Абстрактний клас Calendar надає набір методів, які дозволяють конвертувати час у мілісекундах у ряд корисних
компонентів. Прикладами типу інформації, яку можна отримати, є рік, місяць, день, година, хвилина та секунда.
Передбачається, що підкласи Calendar забезпечать специфічну функціональність для інтерпретації часової інформації
відповідно до своїх власних правил. Це один з аспектів бібліотеки класів Java, який дозволяє вам писати програми, що
можуть працювати в міжнародних середовищах. Прикладом такого підкласу є GregorianCalendar.

Клас Calendar не має публічних конструкторів. Calendar визначає кілька захищених змінних екземпляра. `areFieldsSet` — це
булева змінна, яка вказує, чи були встановлені компоненти часу. `fields` — це масив цілих чисел, який містить компоненти
часу. `isSet` — це булевий масив, який вказує, чи було встановлено конкретний компонент часу. `time` — це змінна
типу `long`, яка зберігає поточний час для цього об'єкта. `isTimeSet` — це булева змінна, яка вказує, чи було
встановлено поточний час. Зразок методів, визначених у класі Calendar, наведено в таблиці 2.

Табл. 2 - Зразок методів, визначених у класі Calendar:

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_2.png)

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_3.png)

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_4.png)

Клас Calendar визначає такі константи типу `int`, які використовуються при отриманні або встановленні компонентів
календаря.

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_5.png)

Наступна програма демонструє кілька методів класу Calendar:

```java
// Demonstrate Calendar

class CalendarDemo {
    public static void main(String[] args) {
        String[] months = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};

        // Create a calendar initialized with the
        // current date and time in the default
        // locale and timezone.
        Calendar calendar = Calendar.getInstance();

        // Display current time and date information.
        System.out.print("Date: ");
        System.out.print(months[calendar.get(Calendar.MONTH)]);
        System.out.print(" " + calendar.get(Calendar.DATE) + " ");
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.print("Time: ");
        System.out.print(calendar.get(Calendar.HOUR) + ":");
        System.out.print(calendar.get(Calendar.MINUTE) + ":");
        System.out.println(calendar.get(Calendar.SECOND));

        // Set the time and date information and display it.
        calendar.set(Calendar.HOUR, 10);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 22);
        System.out.print("Updated time: ");
        System.out.print(calendar.get(Calendar.HOUR) + ":");
        System.out.print(calendar.get(Calendar.MINUTE) + ":");
        System.out.println(calendar.get(Calendar.SECOND));
    }
}
```

Вивід програми наведено нижче:

```text
Date: Jan 1 2022
Time: 11:29:39
Updated time: 10:29:22
```

## 3. Клас GregorianCalendar

GregorianCalendar — це конкретна реалізація класу Calendar, яка реалізує звичайний григоріанський календар, з яким ви
знайомі. Метод `getInstance()` класу Calendar зазвичай повертає об'єкт GregorianCalendar, ініціалізований поточною датою
і часом за замовчуванням для локалі та часового поясу.

GregorianCalendar визначає два поля: AD і BC. Ці поля представляють дві ери, визначені григоріанським календарем.

Також існує кілька конструкторів для об'єктів GregorianCalendar. Конструктор за замовчуванням `GregorianCalendar()`
ініціалізує об'єкт поточною датою і часом за замовчуванням для локалі та часового поясу. Три інші конструктори
пропонують різні рівні специфічності:

```java
GregorianCalendar(int year,int month,int dayOfMonth)

GregorianCalendar(int year,int month,int dayOfMonth,int hours,int minutes)

GregorianCalendar(int year,int month,int dayOfMonth,int hours,int minutes,int seconds)
```

Усі три версії конструкторів встановлюють день, місяць і рік. Тут рік вказує на конкретний рік. Місяць задається
значенням `month`, при цьому нульове значення означає січень. День місяця задається значенням `dayOfMonth`. Перша версія
встановлює час на північ. Друга версія також встановлює години і хвилини. Третя версія додає секунди.

Також ви можете створити об'єкт GregorianCalendar, вказавши локаль і/або часовий пояс. Нижченаведені конструктори
створюють об'єкти, ініціалізовані поточною датою і часом з використанням вказаного часового поясу та/або локалі:

```java
GregorianCalendar(Locale locale)

GregorianCalendar(TimeZone timeZone)

GregorianCalendar(TimeZone timeZone,Locale locale)
```

GregorianCalendar надає реалізацію всіх абстрактних методів класу Calendar. Крім того, він надає деякі додаткові методи.
Мабуть, найцікавіший з них — це метод `isLeapYear()`, який перевіряє, чи є рік високосним. Його форма наступна:

```java
public boolean isLeapYear(int year)
```

Цей метод повертає true, якщо рік є високосним, і false в іншому випадку. Два інші методи, які можуть бути цікаві, — це
from() і toZonedDateTime(), які підтримують новий API для дат і часу, доданий у JDK 8 і упакований у java.time.

Наступна програма демонструє використання GregorianCalendar:

```java
// Demonstrate GregorianCalendar

import java.util.*;

class GregorianCalendarDemo {
    public static void main(String[] args) {
        String[] months = {
                "Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};
        int year;
        
        // Create a Gregorian calendar initialized
        // with the current date and time in the
        // default locale and timezone.
        GregorianCalendar gcalendar = new GregorianCalendar();
        
        // Display current time and date information.
        System.out.print("Date: ");
        System.out.print(months[gcalendar.get(Calendar.MONTH)]);
        System.out.print(" " + gcalendar.get(Calendar.DATE) + " ");
        System.out.println(year = gcalendar.get(Calendar.YEAR));
        System.out.print("Time: ");
        System.out.print(gcalendar.get(Calendar.HOUR) + ":");
        System.out.print(gcalendar.get(Calendar.MINUTE) + ":");
        System.out.println(gcalendar.get(Calendar.SECOND));
        
        // Test if the current year is a leap year
        if (gcalendar.isLeapYear(year)) {
            System.out.println("The current year is a leap year");
        } else {
            System.out.println("The current year is not a leap year");
        }
    }
}
```

Вивід програми наведено нижче:

```text
Date: Jan 1 2022
Time: 1:45:5
The current year is not a leap year
```

## 4. Клас TimeZone

Інший клас, що пов'язаний з часом, — це `TimeZone`. Абстрактний клас `TimeZone` дозволяє працювати з відступами часового поясу від середнього часу по Грінвічу (GMT), також відомого як Координований універсальний час (UTC). Він також обчислює час літнього (деньового) часу. Клас `TimeZone` постачає лише конструктор за замовчуванням.

Зразок методів, визначених у класі `TimeZone`, наведено в таблиці 3.

Табл. 3 - Зразок методів, визначених у класі TimeZone:

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_6.png)


![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_7.png)

## 5. Клас SimpleTimeZone

Клас `SimpleTimeZone` є зручним підкласом `TimeZone`. Він реалізує абстрактні методи класу `TimeZone` і дозволяє працювати з часовими поясами для григоріанського календаря. Він також обчислює час літнього (деньового) часу.

Клас `SimpleTimeZone` визначає чотири конструктори. Один з них:

```java
SimpleTimeZone(int timeDelta, String tzName)
```

Цей конструктор створює об'єкт `SimpleTimeZone`. Відступ від середнього часу по Грінвічу (GMT) задається параметром `timeDelta`. Часовий пояс отримує ім'я `tzName`.

Другий конструктор `SimpleTimeZone`:

```java
SimpleTimeZone(int timeDelta, String tzId, int dstMonth0,
               int dstDayInMonth0, int dstDay0, int time0,
               int dstMonth1, int dstDayInMonth1, int dstDay1,
               int time1)
```

Тут відступ від GMT задається параметром `timeDelta`. Ім'я часового поясу передається в `tzId`. Початок літнього часу задається параметрами `dstMonth0`, `dstDayInMonth0`, `dstDay0`, і `time0`. Кінець літнього часу задається параметрами `dstMonth1`, `dstDayInMonth1`, `dstDay1`, і `time1`.

Третій конструктор `SimpleTimeZone`:

```java
SimpleTimeZone(int timeDelta, String tzId, int dstMonth0,
               int dstDayInMonth0, int dstDay0, int time0,
               int dstMonth1, int dstDayInMonth1,
               int dstDay1, int time1, int dstDelta)
```

У цьому конструкторі параметр `dstDelta` вказує кількість мілісекунд, які економляться під час літнього часу.

Четвертий конструктор `SimpleTimeZone`:

```java
SimpleTimeZone(int timeDelta, String tzId, int dstMonth0,
               int dstDayInMonth0, int dstDay0, int time0,
               int time0mode, int dstMonth1, int dstDayInMonth1,
               int dstDay1, int time1, int time1mode, int dstDelta)
```

Тут `time0mode` і `time1mode` визначають тип годин (24-годинний або 12-годинний формат), що використовуються для початку і кінця літнього часу відповідно. `dstDelta` представляє кількість мілісекунд, які економляться під час літнього часу.

У цьому конструкторі time0mode визначає режим початкового часу, а time1mode визначає режим кінцевого часу. Дійсні значення режимів включають:

![img.png](https://www.wwwjavalearnapp.store/img/2024_4_course_java_lecture_1_img_8.png)

Режим часу вказує, як інтерпретуються значення часу. Значенням за замовчуванням, що використовується іншими конструкторами, є WALL_TIME.
