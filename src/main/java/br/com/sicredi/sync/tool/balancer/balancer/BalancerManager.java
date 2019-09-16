package br.com.sicredi.sync.tool.balancer.balancer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gerenciador de pedidos de updade de status.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public class BalancerManager<Id> implements BalancerManageable<Id> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalancerManager.class);
    private static final String REQUEST_QUEUE_PATTERN = "REQUEST-QUEUE-%s"; //$NON-NLS-1$
    private static final String PROCESS_MAP_PATTERN = "PROCESS-MAP-%s"; //$NON-NLS-1$
    private static final String LOCK_PATTERN = "PROCESS-LOCK-%s-%s"; //$NON-NLS-1$

    private final DefaultDataStructuresProvider dataStructuresProvider;
    private final BlockingQueue<ProcessRequest<Id>> requestQueue;
    private final ConcurrentMap<Id, ProcessStatus<Id>> deviceMap;
    private final String processName;

    public BalancerManager(String processName, DefaultDataStructuresProvider dataStructuresProvider) {
        this.processName = processName;
        this.dataStructuresProvider = dataStructuresProvider;
        this.requestQueue = dataStructuresProvider.queue(String.format(REQUEST_QUEUE_PATTERN, processName));
        this.deviceMap = dataStructuresProvider.map(String.format(PROCESS_MAP_PATTERN, processName)); // $NON-NLS-1$
    }

    @Override
    public ProcessRequest<Id> takeRequest() throws InterruptedException {
        return requestQueue.take();
    }

    @Override
    public void changeProcessBaseOnRequest(ProcessRequest<Id> request, ProcessExecutionState newState) {
        ProcessStatus<Id> statusUpdateProcess = retrieveProcessStatus(request.getProcessId());
        statusUpdateProcess.setProcessState(newState);
        statusUpdateProcess.setLastRequestTimestamp(request.getTimestamp());
        deviceMap.put(statusUpdateProcess.getProcessId(), statusUpdateProcess);
    }

    @Override
    public ProcessStatus<Id> retrieveProcessStatus(Id processId) {
        return deviceMap.computeIfAbsent(processId, key -> new ProcessStatus<Id>(processId));
    }

    @Override
    public Lock getLockByProcessId(Id processId) {
        return dataStructuresProvider.lock(String.format(LOCK_PATTERN, processName, processId));
    }

    @Override
    public void deleteDeviceFromStructures(Id processId) {
        Lock deviceLock = getLockByProcessId(processId);

        try {
            if (deviceLock.tryLock()) {
                deviceMap.remove(processId);
                requestQueue.removeIf(request -> request.getProcessId().equals(processId));
            }
        } finally {
            if (deviceLock.isHeldByCurrentThread()) {
                deviceLock.unlock();
            }
        }
    }

    @Override
    public void schedule(ProcessRequest<Id> request) {
        changeProcessBaseOnRequest(request, ProcessExecutionState.SCHEDULED);
        LOGGER.info("Status update SCHEDULED. Request: {}", request); //$NON-NLS-1$
        requestQueue.offer(request);
        LOGGER.info("Status update enqueued. Request: {}", request); //$NON-NLS-1$
    }
}
