package br.com.sicredi.sync.tool.domain;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.balancer.api.ProcessDispatcher;
import br.com.sicredi.sync.tool.balancer.api.ProcessRequest;
import br.com.sicredi.sync.tool.report.IReport;

/**
 * Classe responsÃ¡vel por encaminhar os pedidos de atualizaÃ§Ã£o de conta para a fila de requisiÃ§Ãµes.
 * @author dalpiaz
 */
@Component
public class AccountDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDispatcher.class);

    private final ProcessDispatcher<Account> dispatcher;
    private final IReport<Account> report;

    @Autowired
    public AccountDispatcher(ProcessDispatcher<Account> dispatcher, IReport<Account> report) {
        this.dispatcher = dispatcher;
        this.report = report;
    }

    /**
     * Encaminha a atualizaÃ§Ã£o de uma conta para uma fila de requisiÃ§Ãµes.
     */
    public void dispatch(Account account) {
        LOGGER.trace("Dispatching account update. Account: {}", account);
        dispatcher.dispatch(new ProcessRequest<Account>(account, Date.from(Instant.now())));
        report.writeDispacthed(account);
    }

}
