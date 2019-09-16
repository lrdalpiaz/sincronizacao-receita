package br.com.sicredi.sync.tool.report;

import java.io.IOException;

public interface IReport<T> {

    void writeResult(T entry);

    void writeDispacthed(T entry);

    int totalDispatched();

    int totalProcessed();

    void close() throws IOException;
}
