# Simulador do Brasileirão

API REST para simular o Campeonato Brasileiro (Série A) com 20 times. O usuário escolhe o placar de cada jogo do calendário e a classificação é recalculada automaticamente, com os critérios de desempate da competição. Os dados ficam salvos em um banco PostgreSQL.

## Funcionalidades

- Cadastro automático dos 20 times na primeira execução
- Geração do calendário completo: 38 rodadas e 380 jogos (turno e returno, com mando de campo invertido no returno)
- Definição do placar de um confronto específico do calendário
- Classificação ordenada por: pontos, vitórias, saldo de gols e gols marcados
- Reset da simulação (zera estatísticas e placares)
- Validações de placar e tratamento global de erros

## Tecnologias

- Java 21
- Spring Boot (Web e Data JPA)
- Hibernate
- PostgreSQL 16
- Docker e Docker Compose
- Lombok
- Maven

## Como rodar

**Pré-requisitos:** Java 21, Maven e Docker Desktop.

1. Clone o repositório:

   ```bash
   git clone https://github.com/lucasryan-dev/simulador-brasileirao.git
   cd simulador-brasileirao
   ```

2. (Opcional) Crie um arquivo `.env` na raiz com a senha do banco. Sem ele, a senha padrão é `postgres`:

   ```
   DB_PASSWORD=sua_senha
   ```

3. Suba o banco de dados:

   ```bash
   docker compose up -d
   ```

   O PostgreSQL fica disponível em `localhost:15432`.

4. Rode a aplicação pela IDE (classe `ApplicationStart`) ou pelo Maven. Se você definiu uma senha no passo 2, informe a mesma na variável de ambiente `DB_PASSWORD` ao rodar.

   A API sobe em `http://localhost:8080`. As tabelas, os 20 times e o calendário são criados automaticamente na primeira execução.

## Endpoints

Base: `/Simulacoes`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/times` | Lista os 20 times |
| GET | `/classificacao` | Tabela ordenada pelos critérios de desempate |
| GET | `/calendario` | Lista todos os confrontos (380) |
| GET | `/calendario/rodada/{numero}` | Confrontos de uma rodada |
| PATCH | `/confronto/{id}/placar?golsMandante=2&golsVisitante=1` | Define o placar de um confronto |
| POST | `/definir-placar?mandante=FLA&visitante=PAL&golsMandante=2&golsVisitante=1` | Aplica um resultado informando as siglas dos times |
| POST | `/resetar` | Zera estatísticas dos times e placares dos confrontos |

### Exemplo de uso

```bash
# 1. Descobre os confrontos da rodada 1 e pega um id
curl http://localhost:8080/Simulacoes/calendario/rodada/1

# 2. Define o placar desse confronto
curl -X PATCH "http://localhost:8080/Simulacoes/confronto/1/placar?golsMandante=2&golsVisitante=1"

# 3. Confere a classificação atualizada
curl http://localhost:8080/Simulacoes/classificacao
```

### Erros

- `400 Bad Request`: gols negativos, time contra ele mesmo ou confronto já definido
- `404 Not Found`: time ou confronto inexistente

## Arquitetura

O projeto segue a separação em camadas do Spring:

- **domain:** entidades JPA (`Times`, `Confronto`)
- **repository:** acesso ao banco com Spring Data JPA
- **services:** regras de negócio (times, calendário, placares e classificação)
- **controller:** endpoints REST
- **exception:** exceções de negócio e tratamento global com `@RestControllerAdvice`
- **config:** configuração de CORS

O calendário é gerado com o método do círculo (round-robin): um time fica fixo e os demais rodam a cada rodada, o que garante que todos se enfrentem uma vez no turno.

## Próximos passos

- [ ] Front-end consumindo a API
- [ ] Testes automatizados (JUnit e Mockito)
- [ ] Documentação da API com Swagger
- [ ] Deploy

## Autor

Feito por [lucasryan-dev](https://github.com/lucasryan-dev) como projeto de estudo de Spring Boot e PostgreSQL.