package br.com.sicredi.sync.tool.balancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.sicredi.sync.tool.balancer.api.IBalancerManageable;
import br.com.sicredi.sync.tool.balancer.api.BalancerManager;
import br.com.sicredi.sync.tool.balancer.api.DefaultDataStructuresProvider;
import br.com.sicredi.sync.tool.balancer.api.ProcessDispatcher;
import br.com.sicredi.sync.tool.balancer.api.ProcessExecutor;
import br.com.sicredi.sync.tool.client.FakeReceitaClient;
import br.com.sicredi.sync.tool.client.ReceitaServiceClient;
import br.com.sicredi.sync.tool.domain.Account;
import br.com.sicredi.sync.tool.domain.AccountUpdateExecutor;
import br.com.sicredi.sync.tool.report.IReport;

/**
 * ConfiguraÃ§Ã£o Spring para criaÃ§Ã£o do balanceamento das demandas de atualizaÃ§Ã£o das contas.
 * @author dalpiaz
 */
@Configuration
public class SyncAccountBalancerConfiguration {

    @Value("${account.update.maxConcurrent:20}")
    private int poolSize;
    @Value("${dry:false}")
    private boolean dryRun;

    @Bean
    public DefaultDataStructuresProvider standaloneDataStructuresProvider() {
        return new DefaultDataStructuresProvider();
    }

    @Bean
    public IBalancerManageable<Account> balancerManageable(DefaultDataStructuresProvider dataStructuresProvider) {
        return new BalancerManager<>("sync-account-tool", dataStructuresProvider);
    }

    @Bean
    public ProcessDispatcher<Account> processDispatcheable(IBalancerManageable<Account> balancerManager) {
        return new ProcessDispatcher<>(balancerManager);
    }

    @Bean
    public ProcessExecutor<Account> processExecutor(IBalancerManageable<Account> balancerManager,
            AccountUpdateExecutor accountUpdateExecutor) {
        return new ProcessExecutor<>(balancerManager, accountUpdateExecutor, "sync-account-tool", poolSize);
    }

    @Bean
    public AccountUpdateExecutor accountUpdateExecutor(ReceitaServiceClient receitaServiceClient,
            FakeReceitaClient fakeReceitaClient, IReport<Account> report) {
        return new AccountUpdateExecutor(dryRun ? fakeReceitaClient : receitaServiceClient,
                report);
    }
}
