package br.com.sicredi.sync.tool;

/**
 * Enum com os possÃ­veis argumentos do programa de atualizaÃ§Ã£o de contas.
 * @author dalpiaz
 */
public enum CommandArguments {
    DRY ("dry"), //$NON-NLS-1$
    OUTPUT("output"), //$NON-NLS-1$
    FILE("file"); //$NON-NLS-1$
    
    private String argument;

    CommandArguments(String argument) {
        this.argument = argument;
    }
    
    public String getArgument() {
        return argument;
    }
}
