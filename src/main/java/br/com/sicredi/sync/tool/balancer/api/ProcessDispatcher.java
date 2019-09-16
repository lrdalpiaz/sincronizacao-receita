package br.com.sicredi.sync.tool.balancer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatcher para encaminhamento de requisições.
 *
 * @param <Request>
 *            Requisição a ser encaminhada.
 *
 * @author dalpiaz
 */
public class ProcessDispatcher<Request> implements IProcessDispatcheable<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDispatcher.class);

    private final IBalancerManageable<Request> balancerManager;

    public ProcessDispatcher(IBalancerManageable<Request> balancerManager) {
        this.balancerManager = balancerManager;
    }

    @Override
    public void dispatch(ProcessRequest<Request> request) {
        Request processId = request.getRequest();
        ILock processLock = balancerManager.getLockByRequest(processId);

        try {
            if (processLock.tryLock()) {
                ProcessStatus<Request> statusUpdateProcess = balancerManager.retrieveProcessStatus(processId);

                if (statusUpdateProcess.getCurrentProcessState() == ProcessExecutionState.RUNNING
                        || statusUpdateProcess.getCurrentProcessState() == ProcessExecutionState.SCHEDULED) {
                    LOGGER.info(
                            "Request discarded because it has already been processed by another instance. Request: {}", //$NON-NLS-1$
                            request);
                } else if (statusUpdateProcess.getCurrentProcessState() == ProcessExecutionState.STOPPED) {
                    LOGGER.info("Scheduling new execution. Request: {}", request); //$NON-NLS-1$
                    balancerManager.schedule(request);
                    LOGGER.info("Execution scheduled. Request: {}", request); //$NON-NLS-1$
                }
            } else {
                LOGGER.info("Execution request is being processed by another instance. ProcessId: {}", processId); //$NON-NLS-1$
            }
        } finally {
            if (processLock.isHeldByCurrentThread()) {
                processLock.unlock();
            }
        }
    }
}
