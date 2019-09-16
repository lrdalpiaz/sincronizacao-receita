package br.com.sicredi.sync.tool;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.domain.AccountDispatcher;
import br.com.sicredi.sync.tool.parser.IParser;
import br.com.sicredi.sync.tool.report.IReport;

/**
 * ApliaÃ§Ã£o que lÃª um arquivo CSV com contas e encaminha a sua atualizaÃ§Ã£o para o serviÃ§o da Receita.
 * @author dalpiaz
 */
@Component
public class SyncAccountToolApplication {

    private static final String INVALID_INPUT_FILE_MESSAGE = "Invalid Input File!"; //$NON-NLS-1$
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncAccountToolApplication.class);

    private final IParser<Account> parser;
    private final AccountDispatcher accountDispatcher;
    private final IReport<Account> report;
    private final ApplicationArguments applicationArguments;

    @Autowired
    public SyncAccountToolApplication(IParser<Account> parser,
            AccountDispatcher accountDispatcher,
            IReport<Account> report,
            ApplicationArguments applicationArguments) {
        this.parser = parser;
        this.accountDispatcher = accountDispatcher;
        this.report = report;
        this.applicationArguments = applicationArguments;
    }

    public void start() throws Exception {
        if(applicationArguments.containsOption(CommandArguments.FILE.getArgument())) {
            readFile();
            waitComplete();
            closeReport();
        } else {
            LOGGER.error(INVALID_INPUT_FILE_MESSAGE);
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

    private void waitComplete() {
        while (report.totalDispatched() > report.totalProcessed()) {
            LOGGER.trace("Total processed = {}", report.totalProcessed());
        }
    }

    private void closeReport() {
        try {
            report.close();
        } catch (IOException e) {
            LOGGER.error("Error closing output file", e);
        }
    }
}
