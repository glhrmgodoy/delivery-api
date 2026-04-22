# Delivery API

API REST para gerenciamento de restaurantes e produtos, desenvolvida com Java 21, Spring Boot, MySQL e RabbitMQ.

## 📋 Sobre o Projeto

Sistema de delivery que permite o cadastro de restaurantes, categorias e produtos. Eventos assíncronos são publicados no RabbitMQ a cada criação ou atualização de produto, além de mudanças de status do restaurante — ao fechar um restaurante, todos os seus produtos são automaticamente marcados como indisponíveis.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.3**
- **Spring Data JPA**
- **MySQL**
- **RabbitMQ**
- **Spring AMQP**
- **MapStruct**
- **Lombok**
- **Bean Validation**
- **SpringDoc OpenAPI (Swagger)**
- **Docker + Docker Compose**

## 📐 Arquitetura

```
src/main/java/com/empresa/delivery/
├── config/
├── controller/
├── service/
├── repository/
├── domain/
│   ├── entity/
│   └── enums/
├── dto/
│   ├── request/
│   └── response/
├── mapper/
├── messaging/
│   ├── producer/
│   ├── consumer/
│   └── event/
└── exception/
```

## 🗄️ Entidades

| Entidade | Descrição |
|----------|-----------|
| `Restaurant` | Restaurantes cadastrados na plataforma |
| `Category` | Categorias dos produtos (PIZZA, BURGER, SUSHI, BRAZILIAN, DESSERT) |
| `Product` | Produtos do cardápio vinculados a um restaurante e categoria |

## 🐇 Fluxo RabbitMQ

```
ProductService/RestaurantService → ProductEventProducer → delivery.exchange
                                                                  ↓
                                                    delivery.products.queue → ProductEventConsumer
                                                                  ↓ (falha)
                                                    delivery.dlx → delivery.products.dlq
```

| Routing Key | Evento |
|-------------|--------|
| `product.created` | Produto cadastrado |
| `product.updated` | Produto atualizado |
| `restaurant.status` | Status do restaurante alterado |

## 📌 Endpoints

### Restaurants
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/restaurants` | Cadastrar restaurante |
| GET | `/api/v1/restaurants` | Listar restaurantes |
| GET | `/api/v1/restaurants/{id}` | Buscar por ID |
| PUT | `/api/v1/restaurants/{id}` | Atualizar restaurante |
| PATCH | `/api/v1/restaurants/{id}/status` | Alterar status (OPEN/CLOSED) |
| DELETE | `/api/v1/restaurants/{id}` | Inativar restaurante |

### Categories
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/categories` | Cadastrar categoria |
| GET | `/api/v1/categories` | Listar categorias |
| GET | `/api/v1/categories/{id}` | Buscar por ID |
| PUT | `/api/v1/categories/{id}` | Atualizar categoria |
| DELETE | `/api/v1/categories/{id}` | Inativar categoria |

### Products
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/products` | Cadastrar produto |
| GET | `/api/v1/products` | Listar produtos |
| GET | `/api/v1/products/{id}` | Buscar por ID |
| GET | `/api/v1/products/restaurant/{restaurantId}` | Produtos do restaurante |
| GET | `/api/v1/products/restaurant/{restaurantId}/available` | Produtos disponíveis |
| PUT | `/api/v1/products/{id}` | Atualizar produto |
| PATCH | `/api/v1/products/{id}/status` | Alterar status |
| DELETE | `/api/v1/products/{id}` | Inativar produto |

## ⚙️ Regras de Negócio

- Restaurante `CLOSED` não pode ter produtos `AVAILABLE`
- Ao fechar um restaurante, **todos os produtos ficam `UNAVAILABLE` automaticamente**
- Produto deve pertencer a um restaurante e categoria **ativos**
- Categoria com produtos ativos **não pode ser inativada**
- Soft delete em todas as entidades — registros nunca são deletados do banco
- Eventos publicados no RabbitMQ após criação, atualização e mudança de status
- Dead Letter Queue configurada para mensagens com falha

## 🐳 Como Rodar

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 21
- Maven

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/delivery-api.git
cd delivery-api
```

### 2. Configure o `.env`
```env
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=delivery_db
MYSQL_USER=admin
MYSQL_PASSWORD=admin
RABBITMQ_USER=admin
RABBITMQ_PASSWORD=admin
```

### 3. Suba MySQL e RabbitMQ com Docker
```bash
docker-compose up -d
```

### 4. Rode a aplicação
```bash
mvn spring-boot:run
```

### 5. Acesse o Swagger
```
http://localhost:8080/swagger-ui/index.html
```

### 6. Acesse o painel do RabbitMQ
```
http://localhost:15672
usuário: admin | senha: admin
```
