package br.com.sicredi.sync.tool.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.domain.Account;

/**
 * Implementação fake para testes que não envia de fato a atualização das contas para Receita.
 * @author dalpiaz
 */
@Component
public class FakeReceitaClient implements IReceitaServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakeReceitaClient.class);

    @Override
    public void atualizarConta(Account conta) {
        LOGGER.info("Updating Account: {}", conta);
    }

}
