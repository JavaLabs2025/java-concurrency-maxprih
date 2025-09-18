package org.labs;

import java.util.concurrent.locks.ReentrantLock;

public class Spoon extends ReentrantLock {
    private final int id;

    public Spoon(int id) {
        super(true);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
