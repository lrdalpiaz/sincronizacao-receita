package br.com.sicredi.sync.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import br.com.sicredi.sync.tool.balancer.SyncAccountBalancerConfiguration;
import br.com.sicredi.sync.tool.report.ReportConfiguration;

/**
 * Ponto de entrada do programa, que sobe o contexto spring-boot.
 * @author dalpiaz
 */
@Configuration
@SpringBootApplication
@Import({SyncAccountBalancerConfiguration.class,
        ReportConfiguration.class})
public class Main implements ApplicationRunner {

    @Autowired
    private SyncAccountToolApplication starter;
    
    public static void main(String[] args) {
        new SpringApplicationBuilder().headless(false).web(WebApplicationType.NONE).sources(Main.class).bannerMode(Mode.OFF).run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        starter.start();
    }
}

