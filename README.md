# PicPay Simples

API REST em Java + Spring Boot que simula uma plataforma de pagamentos: é possível cadastrar usuários (comuns e lojistas) e transferir dinheiro entre eles.

Esse projeto foi feito como resolução de um desafio técnico de backend, tentando manter as camadas bem separadas (controller, service, repository, domain, dtos) e seguir boas práticas no geral.

## Sobre o projeto

Existem dois tipos de usuário:

- Usuário comum: pode enviar e receber dinheiro.
- Lojista: só recebe, não pode fazer transferências.

Antes de fechar uma transferência, o sistema chama um serviço autorizador externo (mock). Depois que a transação é concluída, é disparada uma notificação pros usuários envolvidos, também via mock. Essa notificação foi desacoplada de propósito, porque se ela falhar isso não pode derrubar a transação.

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA
- H2 Database (em memória)
- Maven

## Regras de negócio
- Nome completo, CPF, e-mail e senha são obrigatórios para os dois tipos de usuário. CPF/CNPJ e e-mail precisam ser únicos no sistema. Não pode existir dois cadastros com o mesmo CPF ou e-mail.
- Usuários podem transferir dinheiro para lojistas e para outros usuários.
- Lojistas só recebem, nunca enviam dinheiro.
- Antes de transferir, o sistema precisa validar se o usuário tem saldo suficiente.
- Antes de finalizar a transferência, é preciso consultar um serviço autorizador externo (mock `GET https://util.devi.tools/api/v2/authorize`).
- A transferência é uma transação: se algo der errado no meio do caminho, tudo é revertido e o dinheiro volta pra carteira de quem enviou.
- Quando alguém recebe um pagamento, precisa ser notificado (email/sms) por um serviço de terceiro (mock `POST https://util.devi.tools/api/v1/notify`). Esse serviço pode estar fora do ar, então isso não pode travar a transação.
- A API precisa ser RESTful.

## Endpoints

Cadastrar usuário:
```
POST /users
Content-Type: application/json

{
	"firstName": "exemplo",
	"lastName": "exemplo",
	"document": "123456789",
	"password": "exemplo",
	"email": "exemplo@exemplo.com",
	"userType": "COMMON",
	"balance": 2200
}
```

Criar transação:
```
POST /transactions
Content-Type: application/json

{
	"senderId": 1,
	"receiverId": 2,
	"value": 100
}
```

## Como executar o projeto

### Pré-requisitos

- Java 17+ (confira a versão no pom.xml)
- Maven não é obrigatório instalar globalmente, o projeto já vem com o wrapper (mvnw / mvnw.cmd)

### Passos

```bash
git clone https://github.com/GustavoKS412/picpay-simples.git
cd picpay-simples
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

## Banco de dados

Usa H2 em memória, então não precisa configurar nada pra rodar local.

*Os dados somem toda vez que a aplicação reinicia.*

## Roadmap

- [ ] Testes unitários e de integração
- [x] Tratamento de erros global
- [ ] Documentação da API
- [ ] Dockerizar a aplicação
- [ ] Trocar H2 por um banco persistente (PostgreSQL/MySQL)
