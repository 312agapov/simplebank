package ru.agapovla.simplebank;

import ru.agapovla.simplebank.entity.Account;
import ru.agapovla.simplebank.service.AccountService;
import ru.agapovla.simplebank.transfer.TransferTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        AccountService service = new AccountService();
        List<Account> accounts = new ArrayList<>();
        Random random = new Random();
        int randNum = random.nextInt(4,8);

        for (int i = 0; i < randNum; i++) {
            accounts.add(service.createAccount());
        }

        System.out.println("Созданные счета:");
        accounts.forEach(System.out::println);

        Random treadRandom = new Random();
        int trRandom = treadRandom.nextInt(3,10);

        ExecutorService executor = Executors.newFixedThreadPool(trRandom);

        for (int i = 0; i < trRandom; i++) {
            executor.submit(new TransferTask(service, accounts));
        }

        while (AccountService.isRunning()) {
            Thread.sleep(500);
        }

        executor.shutdown();
        System.out.println("Все потоки завершены. Финальные балансы:");
        accounts.forEach(System.out::println);

    }
}