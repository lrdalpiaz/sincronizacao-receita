package br.com.sicredi.sync.tool.client;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.external.ReceitaService;

@Component
public class ReceitaServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceitaServiceClient.class);

    private final AtomicInteger counter = new AtomicInteger();
    private final ReceitaService receitaService;

    @Autowired
    public ReceitaServiceClient(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }
    
    public void atualizarConta(Account conta, boolean dryRun) {
        try {
            if (dryRun) {
                LOGGER.info("ADIONANDO DEVICE {}", conta);
            } else {
                receitaService.atualizarConta(
                        conta.getAgencia(),
                        conta.getConta(),
                        conta.getSaldo(),
                        conta.getStatus());
            }
        } catch (Exception e) {
            LOGGER.error("Erro inserting device {}. Reason: {}", conta, e); //$NON-NLS-1$
        } finally {
            LOGGER.info("Total processed: {}", counter.incrementAndGet());
        }
    }
    
    public int getNumberOfEntriesProcessed() {
        return counter.get();
    }
}
