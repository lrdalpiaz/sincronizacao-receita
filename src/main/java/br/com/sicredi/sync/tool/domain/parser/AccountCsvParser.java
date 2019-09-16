package br.com.sicredi.sync.tool.domain.parser;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.parser.Parser;


/**
 */
@Component
public class AccountCsvParser implements Parser<Account> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCsvParser.class);

    @Override
    public void parse(String csvFilePath, Consumer<Account> consumer) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            CsvToBean<Account> csvToBean = new CsvToBeanBuilder<Account>(reader)
                    .withType(Account.class)
                    .withSeparator(';')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            csvToBean.iterator().forEachRemaining(consumer);
        } catch (IOException e) {
            LOGGER.error("Error opening file {}", csvFilePath);
        }
    }
    
}
