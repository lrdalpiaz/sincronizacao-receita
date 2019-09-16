package br.com.sicredi.sync.tool.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.domain.AccountUpdateStatus;
import br.com.sicredi.sync.tool.external.ReceitaService;

/**
 * Cliente que supostamente acessaria o serviço externo da Receita para atualização de contas.
 * @author dalpiaz
 */
@Component
public class ReceitaServiceClient implements IReceitaServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceitaServiceClient.class);

    @Value("${account.update.retries:2}")
    private int retries;
    
    private final ReceitaService receitaService;

    @Autowired
    public ReceitaServiceClient(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    public void atualizarConta(Account conta) {
        boolean wasSuccessful = false;
        int currentRetriesCounter = this.retries;

        LOGGER.info("Updating account {}", conta);
        try {
            wasSuccessful = receitaService.atualizarConta(
                    conta.getAgencia(),
                    conta.getNumeroConta(),
                    conta.getSaldo(),
                    conta.getStatus());
            while (!wasSuccessful && currentRetriesCounter-- > 0) {
                LOGGER.error("Account {} not updated. Retry {} of {}", conta,
                        this.retries - currentRetriesCounter, this.retries);
                wasSuccessful = receitaService.atualizarConta(
                        conta.getAgencia(),
                        conta.getNumeroConta(),
                        conta.getSaldo(),
                        conta.getStatus());
            }
        } catch (Exception e) {
            LOGGER.error("Error updating account {}. Reason: {}", conta, e);
            wasSuccessful = false;
        } finally {
            conta.setUpdateStatus(AccountUpdateStatus.valueOf(wasSuccessful).label());
            LOGGER.info("Account update status: {}",
                    AccountUpdateStatus.valueOf(wasSuccessful).label());
        }
    }
}
