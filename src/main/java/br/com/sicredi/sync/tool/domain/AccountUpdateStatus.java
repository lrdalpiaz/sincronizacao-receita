package br.com.sicredi.sync.tool.domain;

public enum AccountUpdateStatus {
    SUCCESS("Success"),
    ERROR("Error");

    private String label;

    private AccountUpdateStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static AccountUpdateStatus valueOf(boolean booleanStatus) {
        return booleanStatus ? SUCCESS : ERROR;
    }
}
