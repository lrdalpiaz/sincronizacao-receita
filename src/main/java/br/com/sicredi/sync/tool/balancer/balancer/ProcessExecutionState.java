package br.com.sicredi.sync.tool.balancer.balancer;

import java.io.Serializable;

/**
 * Possíveis estados de um update de status.
 * @author dalpiaz
 */
public enum ProcessExecutionState implements Serializable {
    SCHEDULED,
    RUNNING,
    STOPPED;
}
