# Instruções para rodar o programa:
1) Compilar:
    mvn clean install
2) Rodar:
    java -jar target/sincronizacao-receita.jar --file=nome_do_arquivo_de_entrada.cvs

Opções:
    --output
        Define o arquivo de saída do programa. Default = nome_do_arquivo_de_entrada_timestamp.csv
    --dry
        Se igual a "true", roda o programa em modo de teste. Default = false
    --file.csv.header.skip
        Indica se é necessário ignorar a linha de cabecalho do arquivo de entrada. Default = false.
    --account.update.maxConcurrent
        Indica o número de threads em paralelo que vão executar o update nas contas.
    --account.sync.retries
        Indica o número máximo de tentativas de update de uma conta em caso de erro na atualização
        
# Decisão técnicas
No teste, optei por desenvolver um programa que recebe um arquivo de entrada no formato CSV sugerido, via argumento do programa. Conforme esclarecimentos por email, esse seria um MVP inicial e isso poderia evoluir para um serviço, que executa periodicamente ou que é acionado via REST.

Optei por dividir a tarefa em duas partes: Parser e Atualização.
O parser é executado linha a linha, pois como diz o enunciado, o arquivo de entrada pode conter um volume expressivo de dados.
Sendo assim, a execução linha a linha garante que o parser do arquivo não seja feito por completo, economizando recursos.
Ao processar cada linha, faço uma espécie de escalonamento de um processo. A requisição é colocada numa fila de execução, que é consumida pelo executor do processo de atualização.
Fiz dessa forma para simular uma fila de execução que poderia ser facilmente escalável, utilizando Kafta, JMS, ou qualquer outra implementação. O intuito foi mostrar que a atualização de cada conta por ocorrer em paralelo. No caso do teste, é possível parametrizar o número de threads que vão processar as entradas. Isso desacopla a tarefa do "produtor", ou seja, o parser do CSV, da tarefa do "consumidor", que é quem efetivamente faz o update das contas.
Também deixei a possibilidade de definição de retries, para os casos onde o serviço da Receita responde com erro.

De forma simplificada, tentei simular atacar alguns problemas da aplicação real, como a possibilidade de escalar o serviço de forma mais fácil e prever erros no processo de update da conta.

# Evolução
Como evolução, eu diria que poderia avançar nos seguintes pontos, dependendo do requisito:
* Além do retry no momento do envio, o programa poderia se realimentar do próprio arquivo de saída para enviar novamente a entradas que ainda terminarem com falhas, no caso do requisito exigir que 100 das entradas sejam enviadas com sucesso, sem que o usuário precise disparar o comando novamente.
* Para fins de exercícios, assumi que o arquivo de entrada já estaria em um formato adequado. Não fiz validação para isso, porém, dependendo do cenário, isso poderia ser interessante.
* O programa poderia ser um serviço que fica disponível com uma página de upload do arquivo, com o processo de update rodando em background. Poderia colocar uma persistência, talvez até alguma key-value, para manter o status e talvez fazer uma cache e evitar o reenvio de informações que não foram alteradas (se isso fosse interessante para o requisito).
* Poderia criar de fato os projetos com "produtor/consumidor" ao invés de simular, como eu fiz no teste, criando micro-serviços distintos para cada um.
