package br.com.sicredi.sync.tool.balancer.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gerenciador de requisições.
 *
 * @param <Request>
 *            Requisição a ser processada.
 *
 * @author dalpiaz
 */
public class BalancerManager<Request> implements IBalancerManageable<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalancerManager.class);
    private static final String REQUEST_QUEUE_PATTERN = "REQUEST-QUEUE-%s"; //$NON-NLS-1$
    private static final String PROCESS_MAP_PATTERN = "PROCESS-MAP-%s"; //$NON-NLS-1$
    private static final String LOCK_PATTERN = "PROCESS-LOCK-%s-%s"; //$NON-NLS-1$

    private final DefaultDataStructuresProvider dataStructuresProvider;
    private final BlockingQueue<ProcessRequest<Request>> requestQueue;
    private final ConcurrentMap<Request, ProcessStatus<Request>> deviceMap;
    private final String processName;

    public BalancerManager(String processName, DefaultDataStructuresProvider dataStructuresProvider) {
        this.processName = processName;
        this.dataStructuresProvider = dataStructuresProvider;
        this.requestQueue = dataStructuresProvider.queue(String.format(REQUEST_QUEUE_PATTERN, processName));
        this.deviceMap = dataStructuresProvider.map(String.format(PROCESS_MAP_PATTERN, processName)); // $NON-NLS-1$
    }

    @Override
    public ProcessRequest<Request> takeRequest() throws InterruptedException {
        return requestQueue.take();
    }

    @Override
    public void changeRequestStatus(ProcessRequest<Request> request, ProcessExecutionState newState) {
        ProcessStatus<Request> statusUpdateProcess = retrieveProcessStatus(request.getRequest());
        statusUpdateProcess.setProcessState(newState);
        statusUpdateProcess.setLastRequestTimestamp(request.getTimestamp());
        deviceMap.put(statusUpdateProcess.getProcessId(), statusUpdateProcess);
    }

    @Override
    public ProcessStatus<Request> retrieveProcessStatus(Request processId) {
        return deviceMap.computeIfAbsent(processId, key -> new ProcessStatus<Request>(processId));
    }

    @Override
    public ILock getLockByRequest(Request processId) {
        return dataStructuresProvider.lock(String.format(LOCK_PATTERN, processName, processId));
    }

    @Override
    public void deleteRequestFromStructures(Request processId) {
        ILock deviceLock = getLockByRequest(processId);

        try {
            if (deviceLock.tryLock()) {
                deviceMap.remove(processId);
                requestQueue.removeIf(request -> request.getRequest().equals(processId));
            }
        } finally {
            if (deviceLock.isHeldByCurrentThread()) {
                deviceLock.unlock();
            }
        }
    }

    @Override
    public void schedule(ProcessRequest<Request> request) {
        changeRequestStatus(request, ProcessExecutionState.SCHEDULED);
        LOGGER.info("Status update SCHEDULED. Request: {}", request); //$NON-NLS-1$
        requestQueue.offer(request);
        LOGGER.info("Status update enqueued. Request: {}", request); //$NON-NLS-1$
    }
}
