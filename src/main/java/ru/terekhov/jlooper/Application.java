package ru.terekhov.jlooper;

import ru.terekhov.jlooper.scheduler.Scheduler;

public class Application {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.sayHello();
    }
}
