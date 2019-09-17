package br.com.sicredi.sync.tool.report;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import br.com.sicredi.sync.tool.domain.Account;

public class CsvFileReport implements IReport<Account> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileReport.class);

    private AtomicInteger totalDispatched = new AtomicInteger();
    private AtomicInteger totalProcessed = new AtomicInteger();

    private final StatefulBeanToCsv<Account> csvWriter;
    private final Writer writer;

    public CsvFileReport(StatefulBeanToCsv<Account> csvWriter, Writer writer) {
        this.csvWriter = csvWriter;
        this.writer = writer;
    }

    public void incrementDispatched() {
        totalDispatched.incrementAndGet();
    }
    @Override
    public synchronized void writeResult(Account account) {
        try {
            csvWriter.write(Lists.newArrayList(account));
            writer.flush();
            totalProcessed.incrementAndGet();
            LOGGER.trace("Total accounts processed: {}", totalProcessed.get());
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            LOGGER.error("Error writing account result. Account: {}", account);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void writeDispacthed(Account entry) {
        totalDispatched.incrementAndGet();
        LOGGER.trace("Total accounts dispatched: {}", totalDispatched.get());
    }

    @Override
    public int totalDispatched() {
        return totalDispatched.get();
    }

    @Override
    public int totalProcessed() {
        return totalProcessed.get();
    }
}
