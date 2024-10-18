# Diagrama de Arquitetura - Event Driven Architecture (EDA) utilizando RabbitMQ

graph TB
    subgraph "Camada de Cliente"
        Client[Cliente]
    end

    subgraph "Camada de Gateway"
        APIGW["Gateway (Spring Cloud Gateway)"]
    end

    subgraph "Camada de Descoberta de Serviço"
        Eureka[Eureka Server]
    end

    subgraph "Camada de Microsserviços"
        MS1[Microsserviço <br>Produtos]
        MS2[Microsserviço <br>Pedidos]
        MS3[Microsserviço <br>Usuários]
        MS4[Microsserviço <br>ConsumerService]
    end

    subgraph "Camada de Mensageria"
        RabbitMQ[RabbitMQ]
    end

    subgraph "Camada de Dados"
        DB1[(MySQL<br>Produtos)]
        DB2[(MySQL<br>Pedidos)]
        DB3[(MySQL<br>Usuários)]
    end

    Client -->|HTTP/HTTPS| APIGW
    APIGW -->|HTTP| MS1
    APIGW -->|HTTP| MS2
    APIGW -->|HTTP| MS3

    MS1 -->|Registra| Eureka
    MS2 -->|Registra| Eureka
    MS3 -->|Registra| Eureka
    MS4 -->|Registra| Eureka
    APIGW -->|Consulta| Eureka

    MS1 <-->|Pub/Sub| RabbitMQ
    MS2 <-->|Pub/Sub| RabbitMQ
    MS3 <-->|Pub/Sub| RabbitMQ
    RabbitMQ -->|Consume| MS4

    MS1 -->|JDBC| DB1
    MS2 -->|JDBC| DB2
    MS3 -->|JDBC| DB3

    classDef microservice fill:#f9f,stroke:#333,stroke-width:2px;
    classDef database fill:#bdf,stroke:#333,stroke-width:2px;
    classDef gateway fill:#fdfd96,stroke:#333,stroke-width:2px;
    classDef discovery fill:#90EE90,stroke:#333,stroke-width:2px;
    classDef messaging fill:#FFA07A,stroke:#333,stroke-width:2px;
    class MS1,MS2,MS3,MS4 microservice;
    class DB1,DB2,DB3 database;
    class APIGW gateway;
    class Eureka discovery;
    class RabbitMQ messaging;

- Criar todas as imagens: ./build-image.sh
- Executar o docker compose: docker-compose up

## Exemplos de Chamadas para os Endpoints

Aqui estão exemplos de como chamar os endpoints dos serviços que implementamos, organizados na ordem Cliente, Produto e Pedido. Certifique-se de que os serviços estejam em execução antes de testar estas chamadas.

### Serviço de Clientes

1. Criar um novo cliente:
   ```bash
   curl -X POST http://localhost:8080/api/customers \
   -H "Content-Type: application/json" \
   -d '{
     "name": "João Silva",
     "email": "joao.silva@email.com",
     "password": "senha123"
   }'
   ```

2. Obter todos os clientes:
   ```bash
   curl http://localhost:8080/api/customers
   ```

3. Obter um cliente específico (substitua {id} pelo ID real do cliente):
   ```bash
   curl http://localhost:8080/api/customers/{id}
   ```

4. Atualizar um cliente (substitua {id} pelo ID real do cliente):
   ```bash
   curl -X PUT http://localhost:8080/api/customers/{id} \
   -H "Content-Type: application/json" \
   -d '{
     "name": "João Silva Jr.",
     "email": "joao.silva.jr@email.com"
   }'
   ```

5. Deletar um cliente (substitua {id} pelo ID real do cliente):
   ```bash
   curl -X DELETE http://localhost:8080/api/customers/{id}
   ```

### Serviço de Produtos

1. Criar um novo produto:
   ```bash
   curl -X POST http://localhost:8080/api/products \
   -H "Content-Type: application/json" \
   -d '{
     "name": "Smartphone XYZ",
     "description": "Último modelo com câmera de alta resolução",
     "price": 999.99,
     "stock": 100
   }'
   ```

2. Obter todos os produtos:
   ```bash
   curl http://localhost:8080/api/products
   ```

3. Obter um produto específico (substitua {id} pelo ID real do produto):
   ```bash
   curl http://localhost:8080/api/products/{id}
   ```

4. Atualizar um produto (substitua {id} pelo ID real do produto):
   ```bash
   curl -X PUT http://localhost:8080/api/products/{id} \
   -H "Content-Type: application/json" \
   -d '{
     "name": "Smartphone XYZ - Edição Especial",
     "description": "Versão atualizada com mais memória",
     "price": 1099.99,
     "stock": 50
   }'
   ```

5. Deletar um produto (substitua {id} pelo ID real do produto):
   ```bash
   curl -X DELETE http://localhost:8080/api/products/{id}
   ```

6. Atualizar o estoque de um produto (substitua {id} pelo ID real do produto):
   ```bash
   curl -X PATCH http://localhost:8080/api/products/{id}/stock?quantity=20
   ```

### Serviço de Pedidos

1. Criar um novo pedido:
   ```bash
   curl -X POST http://localhost:8080/api/orders \
   -H "Content-Type: application/json" \
   -d '{
     "customerId": 1,
     "items": [
       {
         "productId": 1,
         "quantity": 2,
         "price": 999.99
       }
     ],
     "totalAmount": 1999.98
   }'
   ```

2. Obter todos os pedidos:
   ```bash
   curl http://localhost:8080/api/orders
   ```

3. Obter um pedido específico (substitua {id} pelo ID real do pedido):
   ```bash
   curl http://localhost:8080/api/orders/{id}
   ```

4. Atualizar o status de um pedido (substitua {id} pelo ID real do pedido):
   ```bash
   curl -X PATCH http://localhost:8080/api/orders/{id}/status?status=CONFIRMED
   ```

Lembre-se de que essas chamadas estão sendo feitas através do API Gateway na porta 8080. O gateway redireciona as requisições para os serviços apropriados com base no path da URL.

Ao testar essas chamadas, você deve ver as respostas dos serviços e, nos logs, poderá observar os eventos sendo publicados no Kafka. Isso demonstra como a arquitetura orientada a eventos está funcionando em nosso sistema.

### Fluxo de Teste Recomendado

Para testar o sistema de forma mais realista, você pode seguir este fluxo:

1. Crie um cliente usando o endpoint do Serviço de Clientes.
2. Crie alguns produtos usando o endpoint do Serviço de Produtos.
3. Crie um pedido para o cliente criado, incluindo os produtos criados, usando o endpoint do Serviço de Pedidos.
4. Atualize o status do pedido para "CONFIRMED".
5. Verifique se o estoque dos produtos foi atualizado adequadamente.
6. Vc poderá verificar as mennsagens criadas a partir do microsserviço: consumer-service

Este fluxo simula um cenário típico de e-commerce e permite que você observe como os diferentes serviços interagem entre si através dos eventos publicados no Kafka.

