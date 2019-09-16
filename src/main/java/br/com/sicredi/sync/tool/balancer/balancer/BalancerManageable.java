package br.com.sicredi.sync.tool.balancer.balancer;

/**
 * Gerenciador de pedidos de updade de status.
 *
 * @param <Id>
 *            Identificador do processo
 *
 * @author dalpiaz
 */
public interface BalancerManageable<Id> {

    /**
     * Retira uma requisicao de update status da fila. Aguarda uma requisicao
     * ser encaminhada caso a fila esteja vazia.
     *
     * @return proxima {@link ProcessRequest} disponivel.
     */
    ProcessRequest<Id> takeRequest() throws InterruptedException;

    /**
     * Altera o estado do processo de update de um device baseado em um novo
     * pedido.
     */
    void changeProcessBaseOnRequest(ProcessRequest<Id> request, ProcessExecutionState newState);

    /**
     * Recupera o lock da sessao critica de um equipamento, a partir de seu
     * identificador.
     */
    Lock getLockByProcessId(Id processId);

    /**
     * Recupera o matadado de um equipamento, com informacoes do estado corrente
     * de execucao.
     */
    ProcessStatus<Id> retrieveProcessStatus(Id processId);

    /**
     * Adiciona um pedido de update de status a fila de execucao.
     */
    void schedule(ProcessRequest<Id> request);

    /**
     * Remove informacoes de um equipamento das estruturas de dados.
     */
    void deleteDeviceFromStructures(Id processId);

}
