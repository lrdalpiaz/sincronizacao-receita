package br.com.sicredi.sync.tool.balancer.balancer;

/**
 * Interface para encaminhamento de pedidos de update de status.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public interface ProcessDispatcheable<Id> {

    /**
     * Encaminha um pedido de update de status.
     */
    void dispatch(ProcessRequest<Id> request);
}
