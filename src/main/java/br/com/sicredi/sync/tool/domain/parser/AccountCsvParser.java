package br.com.sicredi.sync.tool.domain.parser;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.parser.IParser;

/**
 * Parser do arquivo CSV das contas.
 * @author dalpiaz
 */
@Component
public class AccountCsvParser implements IParser<Account> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCsvParser.class);
    @Value("${file.csv.header.skip:false}")
    private boolean skipHeader;

    @Override
    public void parse(String csvFilePath, Consumer<Account> consumer) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            CsvToBean<Account> csvToBean = new CsvToBeanBuilder<Account>(reader)
                    .withType(Account.class)
                    .withSeparator(';')
                    .withSkipLines(skipHeader ? 1 : 0)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            /*
             * Nesse ponto, poderia ser feito o parser completo do arquivo com o método csvToBean.parse()
             * Porém, como o enunciado aponta para um arquivo com muitas entradas, esse método não é aconselhado.
             */
            csvToBean.iterator().forEachRemaining(consumer);
        } catch (IOException e) {
            LOGGER.error("Error opening file {}", csvFilePath);
        }
    }
    
}
