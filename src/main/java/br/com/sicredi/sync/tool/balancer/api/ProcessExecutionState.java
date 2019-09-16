package br.com.sicredi.sync.tool.balancer.api;

import java.io.Serializable;

/**
 * Possíveis estados de uma requisição.
 * @author dalpiaz
 */
public enum ProcessExecutionState implements Serializable {
    SCHEDULED,
    RUNNING,
    STOPPED;
}
