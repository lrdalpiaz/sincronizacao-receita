package br.com.sicredi.sync.tool.balancer.api;

/**
 * Interface para delegar a execução de tasks que são retiradas da fila de
 * execução.
 *
 * @param <Request>
 *            Requisição a ser processada.
 *
 * @author dalpiaz
 */
public interface ITaskExecutor<Request> {

    void execute(ProcessRequest<Request> request);
}
