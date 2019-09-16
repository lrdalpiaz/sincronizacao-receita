package br.com.sicredi.sync.tool.parser;

import java.util.function.Consumer;

import br.com.sicredi.sync.tool.domain.Account;

/**
 * Parser para arquivos que contenham os dados necessários para adicionar um equipamento.
 * @author dalpiaz
 */
public interface Parser<T> {

    /**
     * Executa o parser em um determinado arquivo.
     * @param filePath do arquivo que contém os dados dos equipamentos.
     * @return uma lista contendo as linhas desse arquivo, devidamente parseadas.
     */
    void parse(String csvFilePath, Consumer<Account> consumer);

}
