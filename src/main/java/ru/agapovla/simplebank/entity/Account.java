package ru.agapovla.simplebank.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@ToString
public class Account {

    private String id;
    private AtomicInteger money;

    public Account(){
        this.id = UUID.randomUUID().toString();
        this.money = new AtomicInteger(10000);
    }
}
