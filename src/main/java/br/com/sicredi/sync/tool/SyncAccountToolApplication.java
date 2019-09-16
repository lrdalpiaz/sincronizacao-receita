package br.com.sicredi.sync.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.client.ReceitaServiceClient;
import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.domain.AccountDispatcher;
import br.com.sicredi.sync.tool.parser.Parser;

@Component
public class SyncAccountToolApplication {

    private static final String INVALID_INPUT_FILE_MESSAGE = "Invalid Input File!"; //$NON-NLS-1$
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncAccountToolApplication.class);

    private final Parser<Account> parser;
    private final AccountDispatcher accountDispatcher;
    private final ReceitaServiceClient receitaServiceClient;
    private final ApplicationArguments applicationArguments;

    @Autowired
    public SyncAccountToolApplication(Parser<Account> parser,
            AccountDispatcher accountDispatcher,
            ReceitaServiceClient receitaServiceClient,
            ApplicationArguments applicationArguments) {
        this.parser = parser;
        this.accountDispatcher = accountDispatcher;
        this.receitaServiceClient = receitaServiceClient;
        this.applicationArguments = applicationArguments;
    }

    public void start() throws Exception {
        if(applicationArguments.containsOption(CommandArguments.FILE.getArgument())) {
            readFile();
            waitComplete();
        } else {
            System.out.println(INVALID_INPUT_FILE_MESSAGE);
        }
    }

    private void waitComplete() {
        while (accountDispatcher.accountsDispatched() > receitaServiceClient.getNumberOfEntriesProcessed()) {
            LOGGER.trace("Total processed = {}", receitaServiceClient.getNumberOfEntriesProcessed());
        }
        System.exit(0);
    }

    private void readFile() {
        String filePath = getInputFile();

        parser.parse(filePath, account -> accountDispatcher.dispatch(account));
    }

    private String getInputFile() {
        return applicationArguments.getOptionValues(CommandArguments.FILE.getArgument())
                .stream().findFirst().get() ;
    }
}
