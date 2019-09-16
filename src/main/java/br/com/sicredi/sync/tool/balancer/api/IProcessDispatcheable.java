package br.com.sicredi.sync.tool.balancer.api;

/**
 * Interface para encaminhamento de requisições.
 *
 * @param <Request>
 *            Requisição a ser encaminhada.
 *
 * @author dalpiaz
 */
public interface IProcessDispatcheable<Request> {

    /**
     * Encaminha uma requisição.
     */
    void dispatch(ProcessRequest<Request> request);
}
