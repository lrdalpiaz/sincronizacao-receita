package br.com.sicredi.sync.tool.balancer.balancer;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper for a {@link java.util.concurrent.locks.Lock} that could be a default or shared implementation.
 */
public interface Lock {

    /**
     * Acquires the lock only if it is not held by another thread at the time of invocation.
     */
    boolean tryLock();

    /**
     * Acquires the lock if it is not held by another thread within the given waiting time and the current thread
     * has not been interrupted.
     * @param time the time to wait for the lock
     * @param unit the time unit of the timeout argument
     * @return {@code true} if the lock was free and was acquired by the
     *         current thread, or the lock was already held by the current
     *         thread; and {@code false} if the waiting time elapsed before
     *         the lock could be acquired
     * @throws InterruptedException if the current thread is interrupted
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Attempts to release this lock.
     */
    void unlock();

    /**
     * Queries if this lock is held by the current thread.
     */
    boolean isHeldByCurrentThread();
}
