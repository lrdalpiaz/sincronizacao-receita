package br.com.sicredi.sync.tool.balancer.balancer;

/**
 * Interface para delegar a execução de tasks que são retiradas da fila de
 * execução.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public interface TaskExecutor<Id> {

    void execute(ProcessRequest<Id> request);
}
