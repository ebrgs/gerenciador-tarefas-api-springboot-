# Gerenciador de Tarefas API (Spring Boot) ⚙️

API REST completa para gerenciamento de tarefas, desenvolvida utilizando as melhores práticas do ecossistema Spring.

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.3.5
- **Banco de Dados:** PostgreSQL (Spring Data JPA)
- **Segurança & Validação:** Spring Boot Validation.
- **Documentação:** Springdoc OpenAPI (Swagger UI).
- **Outras Ferramentas:** Lombok, Docker (Compose).

## ✨ Funcionalidades

- Gerenciamento completo de tarefas (CRUD).
- Endpoints totalmente documentados interativamente via Swagger.
- Integração fácil e rápida com bancos de dados relacionais via JPA/Hibernate.
- Fácil deploy em contêineres utilizando Docker Compose.

## 🛠️ Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/ebrgs/gerenciador-tarefas-api-springboot-.git
   ```
2. Suba o banco de dados via Docker (se configurado no `docker-compose.yml`):
   ```bash
   docker-compose up -d
   ```
3. Execute o projeto localmente com o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(No Windows, utilize `mvnw.cmd spring-boot:run`)*
4. Acesse a documentação da API em:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## 📄 Licença
Desenvolvido por Elias (ebrgs).