package br.com.sicredi.sync.tool.parser;

import java.util.function.Consumer;

/**
 * Interface de parser de arquivos CSV.
 * @author dalpiaz
 */
public interface IParser<T> {

    /**
     * Executa o parser em um determinado arquivo.
     * @param csvFilePath do arquivo que contém os dados a serem processados.
     */
    void parse(String csvFilePath, Consumer<T> consumer);

}
