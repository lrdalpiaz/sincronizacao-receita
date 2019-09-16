package br.com.sicredi.sync.tool.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import br.com.sicredi.sync.tool.CommandArguments;
import br.com.sicredi.sync.tool.domain.Account;

/**
 * Configuração de relatório de saída.
 * @author dalpiaz
 */
@Configuration
public class ReportConfiguration {

    private static final String DATE_FILE_NAME_PREFIX = "yyyy-MM-dd-H'h'-m'm'-s's'"; //$NON-NLS-1$
    private static final String FILE_EXTENSION = "csv"; //$NON-NLS-1$

    @Autowired
    private ApplicationArguments applicationArguments;

    @Bean
    public IReport<Account> report() throws IOException {
        Writer writer = new FileWriter(getOutpuFileName());
        CSVWriter csvWriter = new CSVWriter(writer,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        StatefulBeanToCsv<Account> beanToCsvWriter = new StatefulBeanToCsvBuilder<Account>(csvWriter).build();
        return new CsvFileReport(beanToCsvWriter, writer);
    }

    private String getOutpuFileName() {
        return applicationArguments.containsOption(CommandArguments.OUTPUT.getArgument()) ?
                getOutputFileFromParameter() : getDefaultOutputFileName();
    }

    private String getOutputFileFromParameter() {
        return applicationArguments.getOptionValues(CommandArguments.OUTPUT.getArgument())
                .stream().findFirst().get() ;
    }

    private String getDefaultOutputFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FILE_NAME_PREFIX);
        String date = dateFormat.format(Date.from(Instant.now()));
        String inputFileName = applicationArguments.getOptionValues(CommandArguments.FILE.getArgument())
                .stream().findFirst().get();

        return String.format("%s-results-%s.%s", inputFileName, date, FILE_EXTENSION); //$NON-NLS-1$
    }
}
