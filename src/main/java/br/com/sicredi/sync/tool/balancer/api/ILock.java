package br.com.sicredi.sync.tool.balancer.api;

import java.util.concurrent.TimeUnit;

/**
 * Um wrapper para {@link java.util.concurrent.locks.Lock} que pode ser uma implementação local ou distribuída.
 */
public interface ILock {

    boolean tryLock();

    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    void unlock();

    boolean isHeldByCurrentThread();
}
