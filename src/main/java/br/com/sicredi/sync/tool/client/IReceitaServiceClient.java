package br.com.sicredi.sync.tool.client;

import br.com.sicredi.sync.tool.domain.Account;

/**
 * Interface de client para atualizalção de contas.
 * @author dalpiaz
 *
 */
public interface IReceitaServiceClient {

    public void atualizarConta(Account conta);
}
