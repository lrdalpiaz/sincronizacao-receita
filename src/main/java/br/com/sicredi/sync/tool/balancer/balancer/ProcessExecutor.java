package br.com.sicredi.sync.tool.balancer.balancer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processador de pedidos de update de status.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public class ProcessExecutor<Id> implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutor.class);

    private final BalancerManageable<Id> balancerManager;
    private final ExecutorService executor;
    private final TaskExecutor<Id> taskExecutor;
    private final LabeledThreadFactory threadFactory;
    private final Semaphore semaphore;

    public ProcessExecutor(BalancerManageable<Id> balancerManager,
            TaskExecutor<Id> taskExecutor, String processName, int poolSize) {
        this.threadFactory = new LabeledThreadFactory(processName);
        this.semaphore = new Semaphore(poolSize, true);
        this.executor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(poolSize), threadFactory) {

            @Override
            protected void afterExecute(Runnable runnable, Throwable throwable) {
                ProcessExecutor.this.semaphore.release();
            }
        };
        this.balancerManager = balancerManager;
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void start() {
        Executors.newSingleThreadExecutor(threadFactory).execute(this);
    }

    @Override
    public void run() {
        while (true) {
            this.tryTakeAndPoll();
        }
    }

    private void tryTakeAndPoll() {
        try {
            this.semaphore.acquire();
            final ProcessRequest<Id> request = balancerManager.takeRequest();
            LOGGER.info("New status update request taken from request queue. Request: {}", request); //$NON-NLS-1$
            this.executor.submit(() -> {
                try {
                    balancerManager.changeProcessBaseOnRequest(request, ProcessExecutionState.RUNNING);
                    LOGGER.info("Process RUNNING. Request: {}", request); //$NON-NLS-1$
                    taskExecutor.execute(request);
                } finally {
                    balancerManager.changeProcessBaseOnRequest(request, ProcessExecutionState.STOPPED);
                    LOGGER.info("Process STOPPED. Request: {}", request); //$NON-NLS-1$
                }
            });
        } catch (InterruptedException e) {
            LOGGER.error("Executor interrupted.", e); //$NON-NLS-1$
        }
    }
}
