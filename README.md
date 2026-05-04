# ✈️ Cadastro de Aeronaves — Teste Admissional Sonda

API REST + Frontend para gerenciamento de aeronaves, desenvolvido com **Spring Boot 3**, **PostgreSQL** e **React + Vite**, containerizado com Docker.

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Arquitetura da Solução](#-arquitetura-da-solução)
- [Tecnologias](#-tecnologias)
- [Endpoints da API](#-endpoints-da-api)
- [Como Executar](#-como-executar)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)

---

## 🧭 Visão Geral

O projeto é uma aplicação full-stack para gerenciamento de aeronaves. Permite cadastrar, listar, buscar, atualizar e remover aeronaves por meio de uma interface web integrada a uma API REST.

A solução é composta por três serviços independentes, cada um rodando em seu próprio container Docker:

- **Frontend** (React + Vite) — interface de usuário servida pelo Nginx
- **Backend** (Spring Boot 3) — API REST com regras de negócio
- **Banco de dados** (PostgreSQL 16) — persistência dos dados

---

## 🏗️ Arquitetura da Solução

### Visão macro

```
[ NAVEGADOR ]
     │
     │ HTTP (porta 3000)
     ▼
[ FRONTEND — Nginx ]
     │
     │ HTTP (porta 8080)
     ▼
[ BACKEND — Spring Boot ]
     │
     │ JDBC/JPA (porta 5432)
     ▼
[ BANCO — PostgreSQL ]
```

Os três serviços se comunicam por uma rede interna do Docker (`aeronaves-net`), isolada do host. O banco de dados só é acessível pelo backend — nunca diretamente pelo frontend.

---

### Camadas internas do Backend

O backend segue uma arquitetura em camadas bem definidas:

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Controller | `api/` | Recebe requisições HTTP, valida DTOs e retorna respostas |
| Service | `business/` | Regras de negócio e orquestração das operações |
| Repository | `infraestruture/repository/` | Acesso ao banco via Spring Data JPA |
| Domain | `infraestruture/domain/` | Entidades JPA mapeadas para as tabelas do banco |
| DTO + Mapper | `api/dto/` + `api/mapper/` | Transferência de dados via MapStruct |
| Exception Handler | `api/exceptions/` | Tratamento centralizado de erros HTTP |

---

### Entidade principal — Aeronave

Tabela `tb_aeronaves`:

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long (PK) | Identificador único gerado automaticamente |
| `nome` | String (unique) | Nome da aeronave — obrigatório, 3 a 70 caracteres |
| `marca` | Enum | `EMBRAER`, `BOEING` ou `AIRBUS` |
| `ano` | Integer | Ano de fabricação — mínimo 1900, máximo ano atual |
| `descricao` | String (TEXT) | Descrição da aeronave — obrigatória, 3 a 70 caracteres |
| `vendido` | boolean | Indica se a aeronave foi vendida (padrão: `false`) |
| `created` | LocalDateTime | Data/hora de criação (preenchido automaticamente) |
| `updated` | LocalDateTime | Data/hora da última atualização (preenchido automaticamente) |

---

### Infraestrutura Docker

O projeto usa Docker Compose para orquestrar os três serviços. Cada serviço tem seu próprio `Dockerfile` com **build em duas etapas (multi-stage build)**, garantindo imagens finais menores e sem dependências de build em produção.

O backend possui dois perfis de configuração:

| Perfil | Banco | Quando usar |
|---|---|---|
| `test` (padrão) | H2 in-memory | Desenvolvimento local e testes |
| `prod` | PostgreSQL | Docker e produção |

O perfil é controlado pela variável de ambiente `SPRING_PROFILES_ACTIVE`, sem necessidade de alterar o código-fonte.

---

## 🛠️ Tecnologias

| Camada | Tecnologia | Versão |
|---|---|---|
| Backend | Java + Spring Boot | 21 / 3.4 |
| ORM | JPA / Hibernate | — |
| Mapeamento | MapStruct | 1.6.0 |
| Validação | Spring Validation | — |
| Documentação | Springdoc OpenAPI (Swagger) | 2.8.5 |
| Frontend | React + TypeScript + Vite | 18 / 5 |
| HTTP Client | Axios | — |
| Banco (produção) | PostgreSQL | 16 |
| Banco (testes) | H2 in-memory | — |
| Container | Docker + Docker Compose | — |
| Servidor web | Nginx Alpine | — |

---

## 📡 Endpoints da API

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| GET | `/aeronaves` | Lista aeronaves paginadas | 200 |
| GET | `/aeronaves/{id}` | Busca aeronave por ID | 200 / 404 |
| GET | `/aeronaves/nome?nome=X` | Busca aeronave pelo nome | 200 / 404 |
| POST | `/aeronaves` | Cadastra nova aeronave | 201 |
| PUT | `/aeronaves` | Atualiza aeronave existente | 200 / 404 |
| DELETE | `/aeronaves/{id}` | Remove uma aeronave | 204 / 404 |

A documentação completa e interativa está disponível via Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🚀 Como Executar

### Pré-requisitos

**Para rodar com Docker:**
- [Docker](https://docs.docker.com/get-docker/) >= 24
- [Docker Compose](https://docs.docker.com/compose/) >= 2.x (já incluso no Docker Desktop)

**Para rodar sem Docker:**
- Java 21
- Maven 3.9+ ou use o wrapper `./mvnw` incluso no projeto
- Node.js 20+ e npm
- PostgreSQL 16 (apenas para o perfil `prod`)

---

### Opção 1 — Docker (recomendado)

Sobe o banco, o backend e o frontend com um único comando.

**1. Clone o repositório**

```bash
git clone https://github.com/bgarbero/teste-admiss.git
cd teste-admiss
```

**2. Suba os containers**

```bash
docker compose up --build
```

Na primeira execução o Docker baixa as imagens e compila o projeto — aguarde a mensagem no log:

```
Started TesteAdmissApplication
```

**3. Acesse a aplicação**

| Serviço | URL |
|---|---|
| Frontend | http://localhost:3000 |
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

**4. Parar os containers**

```bash
docker compose down        # para os containers (dados do banco são preservados)
docker compose down -v     # para e remove também o volume do banco
```

---

### Opção 2 — Sem Docker (perfil test — H2 in-memory)

Não requer nenhuma configuração de banco.

**Backend:**

```bash
cd backend
./mvnw spring-boot:run
```

**Frontend:**

```bash
cd frontend
npm install
npm run dev
```

O frontend sobe em `http://localhost:5173`.

---

### Opção 3 — Sem Docker (perfil prod — PostgreSQL local)

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE aeronaves_db;
```

Suba o backend com as variáveis de ambiente:

```bash
cd backend

SPRING_PROFILES_ACTIVE=prod \
DB_URL=jdbc:postgresql://localhost:5432/aeronaves_db \
DB_USERNAME=postgres \
DB_PASSWORD=postgres \
./mvnw spring-boot:run
```

---

### Executar os testes

```bash
cd backend
./mvnw test
```

Os testes rodam com H2 in-memory, sem necessidade de banco externo.

---

## 🔑 Variáveis de Ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `test` | Perfil ativo do Spring Boot |
| `DB_URL` | `jdbc:postgresql://localhost:5432/aeronaves_db` | URL de conexão com o banco |
| `DB_USERNAME` | `postgres` | Usuário do banco de dados |
| `DB_PASSWORD` | `postgres` | Senha do banco de dados |

---

## 👤 Autor

**Bruno Garbero** — [github.com/bgarbero](https://github.com/bgarbero)