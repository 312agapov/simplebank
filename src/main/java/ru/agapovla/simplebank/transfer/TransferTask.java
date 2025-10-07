package ru.agapovla.simplebank.transfer;

import lombok.extern.slf4j.Slf4j;
import ru.agapovla.simplebank.entity.Account;
import ru.agapovla.simplebank.service.AccountService;

import java.util.List;
import java.util.Random;

@Slf4j
public class TransferTask implements Runnable {

    private final AccountService service;
    private final List<Account> accounts;
    private final Random random = new Random();

    public TransferTask(AccountService service, List<Account> accounts) {
        this.service = service;
        this.accounts = accounts;
    }

    @Override
    public void run() {
        while (AccountService.isRunning()) {
            try {
                int fromIndex = random.nextInt(accounts.size());
                int toIndex = random.nextInt(accounts.size());

                if (fromIndex == toIndex){
                    log.warn("Попытка перевода на свой же счет!");
                    continue;
                }

                Account from = accounts.get(fromIndex);
                Account to = accounts.get(toIndex);

                int amount = 100 + random.nextInt(900);

                service.transfer(from, to, amount);

                Thread.sleep(1000 + random.nextInt(1000));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " завершил работу.");
    }
}

