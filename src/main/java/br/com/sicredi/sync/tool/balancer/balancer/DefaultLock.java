package br.com.sicredi.sync.tool.balancer.balancer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of {@link Lock} backed by an instance of the JAVA {@link ReentrantLock}.
 */
public class DefaultLock implements Lock {

    private final ReentrantLock reference;

    public DefaultLock(ReentrantLock reference) {
        this.reference = reference;
    }

    @Override
    public boolean tryLock() {
        return reference.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return reference.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        reference.unlock();
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return reference.isHeldByCurrentThread();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof DefaultLock) {
            DefaultLock that = (DefaultLock) obj;

            return Objects.equals(this.reference, that.reference);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }
}
