package br.com.sicredi.sync.tool.balancer.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatcher de pedidos de update de status.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public class ProcessDispatcher<Id> implements ProcessDispatcheable<Id> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDispatcher.class);

    private final BalancerManageable<Id> balancerManager;

    public ProcessDispatcher(BalancerManageable<Id> balancerManager) {
        this.balancerManager = balancerManager;
    }

    @Override
    public void dispatch(ProcessRequest<Id> request) {
        Id processId = request.getProcessId();
        Lock processLock = balancerManager.getLockByProcessId(processId);

        try {
            if (processLock.tryLock()) {
                ProcessStatus<Id> statusUpdateProcess = balancerManager.retrieveProcessStatus(processId);

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
