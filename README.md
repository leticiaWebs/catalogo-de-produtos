## API - Catalogo de produtos
O microsserviço de produtos realiza o processo de carga dos produtos na base de dados. Ele utiliza o framework do spring batch para realizar a carga e 'escrita' dos dados em uma base de dados. 
## Pré requisitos

Para executar a aplicação localmente é preciso usar : 
- Java 17 
- Maven 
- Docker

Utilizar fazer o comando 

    mvn clean install 
    
Executar o docker localmente: 

    docker-compose up -d
  **Pontos de atenção:** 
  Importante adaptar os dados do application.properties/docker-compose para os dados da base existentes localmente. O projeto está criado e configurado para as credenciais da base local. 

### Tecnologias Usadas 
- Para o desenvolvimento do batch foi usado : 
- Spring Batch
- PostgreSQL
- Docker 
