package br.com.sicredi.sync.tool.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;

import br.com.sicredi.sync.tool.parser.Entry;

/**
 * Linha do CSV de contas.
 */
public class Account implements Entry {

    @CsvBindByPosition(position = 0)
    private String agencia;
    @CsvBindByPosition(position = 1)
    private String conta;
    @CsvBindByPosition(position = 2)
    @CsvNumber("#,##")
    private double saldo;
    @CsvBindByPosition(position = 3)
    private String status;

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta.replace("-", "");
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
