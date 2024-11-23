<div align="center">
    <br>
    <img src="https://servoeste.com.br/wp-content/uploads/2023/11/Logo.png" alt="">
    <hr/>

  <p>
      <img loading="lazy" src="http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge" alt=""/>
  </p>
<p>Um projeto da criação de uma API utilizando <a href="https://spring.io/projects/spring-boot">Spring Boot</a> junto do seu consumo em uma aplicação <a href="https://flutter.dev">Flutter</a> utilizando MySql como o banco de dados.</p>

</div>

<hr/>

- [Como Utilizar](#Como-utilizar-a-API)
- [Headers](#Headers)
- [EndPoints](#Endpoints)
- [Erros](#Erros)


# Como utilizar a API

> _<ins>Ainda não foi feito o Deploy da API</ins>_

### _Url Base_: `http://localhost:8080/api/v1`
### _Url Swagger_: `http://localhost:8080/api/v1/swagger-ui/index.html`

# Headers

|   Headers    | Description                     |
|:------------:|:--------------------------------|
| Content-Type | application/json; charset=UTF-8 |

---

# Endpoints

| **EndPoints** | **Sub Endpoints** |                        Descrição                        |
|---------------|-------------------|:-------------------------------------------------------:|
| /tecnico      | /find<br/>/{id}   | GET, POST, PUT e DELETE do Técnico, junto de filtragem. |
| /cliente      | /{id}             | GET, POST, PUT e DELETE do Técnico, junto de filtragem. |
| /endereco     |                   |                  GET a partir do CEP.                   |
---

# Erros

~~~ JSON
{
    "id": 0
    "error": "Alguma mensagem de erro."
}
~~~

O que é o Id? Bem, é um identificador para saber sobre qual campo o erro se trata, sendo que os valores são
"universais" na API, ou seja, se o id for igual a 1 representa que deu algum problema no campo de Email.
Segue a tabela de valores:

| Id | Campo representado |
|----|:------------------:|
| 01 |  Nome e Sobrenome  |
| 02 |  Telefone Celular  |
| 03 |   Telefone Fixo    |
| 04 |     Telefones      |
| 05 |        Cep         |
| 06 |      Endereço      |
| 07 |     Município      |
| 08 |       Bairro       |
| 09 |      Cliente       |
| 10 |      Técnico       |
| 11 |    Equipamento     |
| 12 |       Marca        |
| 13 |     Descrição      |
| 14 |       Filial       |
| 15 |      Horário       |
| 16 |        Data        |
| 17 |    Conhecimento    |
---

# Especialidades

Id das especialidades e o valor que elas representam:

| Id | Conhecimento |
|----|:------------:|
| 1  |    Adega     |
| 2  |  Bebedouro   |
| 3  | Climatizador |
| 4  |    Cooler    |
| 5  |   Frigobar   |
| 6  |  Geladeira   |
| 7  |  Lava Louça  |
| 8  |  Lava Roupa  |
| 9  |  Microondas  |
| 10 | Purificador  |
| 11 |   Secadora   |
| 12 |    Outros    |

---