package org.example.task2;

public class Test {
    public static void main(String[] args) {
        //Завдання 2:
        //Користувач з клавіатури вводить шлях до файлу. Після чого запускається три потоки. Перший потік
        //заповнює файл випадковими числами. Два других очікують заповнення. Коли файл заповнений, два інших
        //потоки запускаються. Перший потік шукає всі прості числа, другий потік факторіал кожного числа в файлі.
        //Результати пошуку кожен потік має записати в новий файл. В методі main необхідно відобразити
        //статистику виконаних операцій.
        ThreadFileOperations.getMenu();
    }
}
