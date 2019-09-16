package br.com.sicredi.sync.tool.balancer.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Factory de threads nomeadas.
 *
 * Os nomes das threads seguirão o padrão baseLabel-ID, onde ID é um long incremental.
 *
 */
public class LabeledThreadFactory implements ThreadFactory {
    private static final String THREAD_PATTERN = "%s-%d"; //$NON-NLS-1$
    private final String baseLabel;
    private final AtomicLong idSource;

    public LabeledThreadFactory(String baseLabel) {
        this.baseLabel = baseLabel; //$NON-NLS-1$
        this.idSource = new AtomicLong();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread newOne = Executors.defaultThreadFactory().newThread(r);
        newOne.setDaemon(true);

        newOne.setName(String.format(THREAD_PATTERN, this.baseLabel, this.idSource.incrementAndGet()));

        return newOne;
    }
}
