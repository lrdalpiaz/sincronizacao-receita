package br.com.sicredi.sync.tool.balancer.api;

/**
 * Gerenciador de requisições.
 *
 * @param <Request>
 *            Requisição a ser processada
 *
 * @author dalpiaz
 */
public interface IBalancerManageable<Request> {

    /**
     * Retira uma requisição da fila. Aguarda uma requisição
     * ser encaminhada caso a fila esteja vazia.
     *
     * @return próxima {@link ProcessRequest} disponível.
     */
    ProcessRequest<Request> takeRequest() throws InterruptedException;

    /**
     * Altera o estado da requisição baseado em um novo pedido.
     */
    void changeRequestStatus(ProcessRequest<Request> request, ProcessExecutionState newState);

    /**
     * Recupera o lock da sessão crítica de uma requisição, a partir de seu identificador.
     */
    ILock getLockByRequest(Request request);

    /**
     * Recupera o estado de uma requisição, com informacões do estado corrente
     * de execucao.
     */
    ProcessStatus<Request> retrieveProcessStatus(Request request);

    /**
     * Adiciona uma requisição a fila de execução.
     */
    void schedule(ProcessRequest<Request> request);

    /**
     * Remove informacoes de uma requisição das estruturas de dados.
     */
    void deleteRequestFromStructures(Request request);

}
