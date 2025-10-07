package ru.agapovla.simplebank.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.agapovla.simplebank.entity.Account;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AccountService {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final int MAX_TRANSACTIONS = 30;

    @Getter
    private static volatile boolean running = true;

    public Account createAccount(){
        return new Account();
    }

    public boolean transfer(Account accFrom, Account accTo, int amount){
        if (amount <= 0){
            log.warn("Недостаточно средств: счёт {}, баланс = {}, сумма перевода = {}",
                    accFrom.getId(), accFrom.getMoney().get(), amount);
            return false;
        }

        Account first;
        Account second;

        // выстраиваем порядок блокировки в целях избегания дедлоков
        if (accFrom.getId().compareTo(accTo.getId()) < 0) {
            first = accFrom;
            second = accTo;
        } else {
            first = accTo;
            second = accFrom;
        }

        synchronized (first) {
            synchronized (second) {
                if (accFrom.getMoney().get() < amount) {
                    return false;
                }

                accFrom.getMoney().addAndGet(-amount);
                accTo.getMoney().addAndGet(amount);
                log.info("Перевод #{}: Откуда: {}, Куда: {}, Сумма перевода: {}" , counter, accFrom, accTo, amount);

                int count = counter.incrementAndGet();
                if (count >= MAX_TRANSACTIONS){
                    running = false;
                    log.info("Достигнуто максимальное количество транзакций: {}" , count);
                }
                return true;
            }
        }
    }

}
