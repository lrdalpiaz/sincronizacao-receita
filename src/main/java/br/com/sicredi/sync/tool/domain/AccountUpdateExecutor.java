package br.com.sicredi.sync.tool.domain;

import br.com.sicredi.sync.tool.balancer.api.ITaskExecutor;
import br.com.sicredi.sync.tool.balancer.api.ProcessRequest;
import br.com.sicredi.sync.tool.client.IReceitaServiceClient;
import br.com.sicredi.sync.tool.report.IReport;

public class AccountUpdateExecutor implements ITaskExecutor<Account>{

    private final IReceitaServiceClient receitaServiceClient;
    private final IReport<Account> report;

    public AccountUpdateExecutor(IReceitaServiceClient receitaServiceClient,
            IReport<Account> report) {
        this.receitaServiceClient = receitaServiceClient;
        this.report = report;
    }

    @Override
    public void execute(ProcessRequest<Account> request) {
        Account account = request.getRequest();
        receitaServiceClient.atualizarConta(account);
        report.writeResult(account);
    }

}
