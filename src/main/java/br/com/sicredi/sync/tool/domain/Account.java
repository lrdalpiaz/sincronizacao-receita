package br.com.sicredi.sync.tool.domain;

import java.util.Objects;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;

/**
 * Objeto de domÃ­nio que representa uma conta, lida do arquivo CSV.
 */
public class Account {

    @CsvBindByPosition(position = 0)
    private String agencia;
    @CsvBindByPosition(position = 1)
    private String conta;
    @CsvBindByPosition(position = 2)
    @CsvNumber("#,##")
    private double saldo;
    @CsvBindByPosition(position = 3)
    private String status;
    @CsvBindByPosition(position = 4)
    private String updateStatus;

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    /*
     * Optei por incluir essa regra aqui por simplicidade.
     * Num aplicaÃ§Ã£o real, talvez o melhor fosse separa os objetos que representam
     * uma linha do CSV e o objeto de domÃ­nio da ReceitaService.
     */
    public String getNumeroConta() {
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

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account other = (Account) obj;
            return Objects.equals(this.agencia,other.getAgencia())
                    && Objects.equals(this.conta, other.getConta());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.agencia, this.conta);
    }

    @Override
    public String toString() {
        return "Agencia: " + this.agencia + " Conta: " + this.conta;
    }
}
