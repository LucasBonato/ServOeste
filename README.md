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
- [Exemplos](#Exemplos)
    - [Técnico](#Técnico)
    - [Cliente](#Cliente)
    - [Endereço](#Endereço)


# Como utilizar a API

> _<ins>Ainda não foi feito o Deploy da API</ins>_

### _Url Base_: `http://localhost:8080/api/v1`

# Headers

|   Headers    | Description                     |
|:------------:|:--------------------------------|
| Content-Type | application/json; charset=UTF-8 |

---

# Endpoints

| **EndPoints** | **Sub Endpoints** | **Exemplos**          | **Body**                 |                        Descrição                        |
|---------------|-------------------|-----------------------|--------------------------|:-------------------------------------------------------:|
| /tecnico      | /find<br/>/{id}   | [tecnico](#Técnico)   | [Técnico](#Técnico-Body) | GET, POST, PUT e DELETE do Técnico, junto de filtragem. |
| /cliente      | /{id}             | [cliente](#Cliente)   | [Cliente](#Cliente-Body) | GET, POST, PUT e DELETE do Técnico, junto de filtragem. |
| /endereco     |                   | [endereco](#Endereço) |                          |                  GET a partir do CEP.                   |
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
| 1  |  Nome e Sobrenome  |
| 2  |   Telefone Fixo    |
| 3  |  Telefone Celular  |
| 4  |     Telefones      |
| 5  |      Endereço      |
| 6  |     Município      |
| 7  |       Bairro       |
| 8  |      Cliente       |
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

# Exemplos

## Técnico

### Encontrar Técnico

![GET](https://img.shields.io/static/v1?label=&message=GET&color=&style=for-the-badge)

> `{{baseUri}}/tecnico/{id}`

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<Tecnico?> getById(int id) async{
        var uri = Uri.parse("$baseUri/tecnico/$id");
        var response = await client.get(uri);
    
        var responseBodyUtf8 = utf8.decode(response.body.runes.toList());
        dynamic jsonResponse = json.decode(responseBodyUtf8);
        Tecnico tecnico = Tecnico.fromJson(jsonResponse);
        return tecnico;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?                |
|-------------|:-----------:|:-------------------------------------:|
| 200         |     OK      |           Retornou o valor            |            
| 404         |  NOT FOUND  | A entidade buscada não foi encontrada |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Encontrar Técnicos

![POST](https://img.shields.io/static/v1?label=&message=POST&color=yellow&style=for-the-badge)

> `{{baseUri}}/tecnico`

``` JSON
    "id": 0, // Id do ténico, sendo buscado se o valor estiver no meio também
    "nome": "nomeESobrenomeDoTénico",
    "situacao": "ativo" || "licença" || "desativado"
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<List<Tecnico>?> getTecnicos(int? id, String? nome, String? situacao) async{
    var uri = Uri.parse("${baseUri}/tecnico/find");
    var response = await client.post(
        uri,
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode({
          "id": id,
          "nome": nome,
          "situacao": situacao
        }
    ));

    var responseBodyUtf8 = utf8.decode(response.body.runes.toList());
    List<dynamic> jsonResponse = json.decode(responseBodyUtf8);
    List<Tecnico> tecnicos = jsonResponse.map((json) => Tecnico.fromJson(json)).toList();
    return tecnicos;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?                |
|-------------|:-----------:|:-------------------------------------:|
| 200         |     OK      |           Retornou o valor            |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Registrar Técnico

![POST](https://img.shields.io/static/v1?label=&message=POST&color=yellow&style=for-the-badge)

> `{{baseUri}}/tecnico`

#### Técnico Body:

``` JSON
    "nome": "nomeDoTécnico",
    "sobrenome": "sobrenomeDoTécnico",
    "telefoneFixo": "99999999999",
    "telefoneCelular": "99999999999",
    "especialidades_Ids": [...] // Conjunto de números que representam as especialidades
```

###### Id das [Especialidades](#Especialidades) :

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> register(Tecnico tecnico) async{
        var response = await client.post(
            Uri.parse("${baseUri}/tecnico"),
            headers: <String, String>{
             'Content-Type': 'application/json; charset=UTF-8',
            },
            body: jsonEncode({
                "nome": tecnico.nome,
                "sobrenome": tecnico.sobrenome,
                "telefoneFixo": tecnico.telefoneFixo,
                "telefoneCelular": tecnico.telefoneCelular,
                "especialidades_Ids": tecnico.especialidadesIds,
            }),
        );
        if(response.statusCode != 201){
            dynamic body = jsonDecode(utf8.decode(response.body.runes.toList()));
            return body;
        }
        return null;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?               |
|-------------|:-----------:|:------------------------------------:|
| 201         |   CREATED   |        Cadastrou com sucesso         |                
| 400         | BAD REQUEST | Alguma informação foi enviada errada |                 

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Alterar Técnico

![PUT](https://img.shields.io/static/v1?label=&message=PUT&color=blue&style=for-the-badge)

>`{{baseUri}}/tecnico/{id}`

``` JSON
    "nome": "nomeDoTécnico",
    "sobrenome": "sobrenomeDoTécnico",
    "telefoneFixo": "99999999999",
    "telefoneCelular": "99999999999",
    "situacao": "ativo" || "licença" || "desativado",
    "especialidades_Ids": [...] // Conjunto de números que representam as especialidades
```

###### Id das [Especialidades](#Especialidades) :

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> update(Tecnico tecnico) async{
        var response = await client.put(
          Uri.parse("$baseUri/tecnico/${tecnico.id}"),
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode({
            "nome": tecnico.nome,
            "sobrenome": tecnico.sobrenome,
            "telefoneFixo": tecnico.telefoneFixo,
            "telefoneCelular": tecnico.telefoneCelular,
            "situacao": tecnico.situacao,
            "especialidades_Ids": tecnico.especialidadesIds,
          }),
        );
        if(response.statusCode != 200){
          dynamic body = jsonDecode(utf8.decode(response.body.runes.toList()));
          return body;
        }
        return null;
  }
~~~

#### Responses:
| Status Code |   Meaning   |                           Why?                           |
|-------------|:-----------:|:--------------------------------------------------------:|
| 200         |     OK      |                   Alterou com sucesso                    |
| 400         | BAD REQUEST | Alguma informação foi enviada errada ou falta informação |
| 404         |  NOT FOUND  |          O objeto procurado não foi encontrado           |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Desativar ou Ativar Cliente

![DELETE](https://img.shields.io/static/v1?label=&message=DEL&color=red&style=for-the-badge)

>`{{baseUri}}/tecnico`

``` JSON
    [...] // Conjunto de ids para desativar ou ativar os técnicos
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> desativar(List<int> idTecnicos) async{
        var response = await client.delete(
          Uri.parse("${baseUri}/tecnico"),
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(idTecnicos)
        );
        if(response.statusCode != 200){
          Logger().e("Técnico não encontrado");
        }
        return;
  }
}
~~~

#### Responses:
| Status Code |  Meaning  |                 Why?                  |
|-------------|:---------:|:-------------------------------------:|
| 200         |    OK     |   Desativoui o tecnico com sucesso    |
| 404         | NOT FOUND | O objeto procurado não foi encontrado |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

## Cliente

### Encontrar Cliente

![GET](https://img.shields.io/static/v1?label=&message=GET&color=&style=for-the-badge)

> `{{baseUri}}/cliente/{id}`

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<Cliente?> getById(int id) async{
        var uri = Uri.parse("$baseUri/cliente/$id");
        var response = await client.get(uri);
    
        var responseBodyUtf8 = utf8.decode(response.body.runes.toList());
        dynamic jsonResponse = json.decode(responseBodyUtf8);
        Cliente cliente = Cliente.fromJson(jsonResponse);
        return cliente;
    }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?                |
|-------------|:-----------:|:-------------------------------------:|
| 200         |     OK      |           Retornou o valor            |            
| 404         |  NOT FOUND  | A entidade buscada não foi encontrada |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Encontrar Clientes

![POST](https://img.shields.io/static/v1?label=&message=POST&color=yellow&style=for-the-badge)

> `{{baseUri}}/cliente`

``` JSON
    "nome": "nomeESobrenomeDoCliente",
    "telefoneFixo": "99999999999",
    "telefoneCelular": "99999999999",
    "endereco": "Alguma parte do endereço do cliente"
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<List<Cliente>?> getClientes(String? nome, String? telefoneFixo, String? telefoneCelular, String? endereco) async{
    var uri = Uri.parse("${baseUri}/cliente/find");
    var response = await client.post(
        uri,
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode({
          "nome": nome,
          "telefoneFixo": telefoneFixo,
          "telefoneCelular": telefoneCelular,
          "endereco": endereco
        }
    ));

    var responseBodyUtf8 = utf8.decode(response.body.runes.toList());
    List<dynamic> jsonResponse = json.decode(responseBodyUtf8);
    List<Cliente> clientes = jsonResponse.map((json) => Cliente.fromJson(json)).toList();
    return clientes;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?                |
|-------------|:-----------:|:-------------------------------------:|
| 200         |     OK      |           Retornou o valor            |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Registrar Cliente

![POST](https://img.shields.io/static/v1?label=&message=POST&color=yellow&style=for-the-badge)

> `{{baseUri}}/cliente`

#### Cliente Body:

``` JSON
    "nome": "nomeDoCliente",
    "sobrenome": "sobrenomeDoCliente",
    "telefoneFixo": "99999999999",
    "telefoneCelular": "99999999999",
    "endereco": "ruaNúmeroEComplementoDoEndereço",
    "bairro": "bairro",
    "municipio": "municipio"
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> register(Cliente cliente) async{
        var response = await client.post(
            Uri.parse("${baseUri}/cliente"),
            headers: <String, String>{
             'Content-Type': 'application/json; charset=UTF-8',
            },
            body: jsonEncode({
                "nome": cliente.nome,
                "sobrenome": cliente.sobrenome,
                "telefoneFixo": cliente.telefoneFixo,
                "telefoneCelular": cliente.telefoneCelular,
                "endereco": cliente.endereco,
                "bairro": cliente.bairro,
                "municipio": cliente.municipio
            }),
        );
        if(response.statusCode != 201){
            dynamic body = jsonDecode(utf8.decode(response.body.runes.toList()));
            return body;
        }
        return null;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?               |
|-------------|:-----------:|:------------------------------------:|
| 201         |   CREATED   |        Cadastrou com sucesso         |                
| 400         | BAD REQUEST | Alguma informação foi enviada errada |                 

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Alterar Cliente

![PUT](https://img.shields.io/static/v1?label=&message=PUT&color=blue&style=for-the-badge)

>`{{baseUri}}/cliente?id=`

#### Cliente Body:

``` JSON
    "nome": "nomeDoCliente",
    "sobrenome": "sobrenomeDoCliente",
    "telefoneFixo": "99999999999",
    "telefoneCelular": "99999999999",
    "endereco": "ruaNúmeroEComplementoDoEndereço",
    "bairro": "bairro",
    "municipio": "municipio"
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> update(Cliente cliente) async{
        var response = await client.put(
          Uri.parse("$baseUri/cliente?id=${cliente.id}"),
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode({
            "nome": cliente.nome,
            "sobrenome": cliente.sobrenome,
            "telefoneFixo": cliente.telefoneFixo,
            "telefoneCelular": cliente.telefoneCelular,
            "endereco": cliente.endereco,
            "bairro": cliente.bairro,
            "municipio": cliente.municipio
          }),
        );
        if(response.statusCode != 200){
          dynamic body = jsonDecode(utf8.decode(response.body.runes.toList()));
          return body;
        }
        return null;
  }
~~~

#### Responses:
| Status Code |   Meaning   |                           Why?                           |
|-------------|:-----------:|:--------------------------------------------------------:|
| 200         |     OK      |                   Alterou com sucesso                    |
| 400         | BAD REQUEST | Alguma informação foi enviada errada ou falta informação |
| 404         |  NOT FOUND  |          O objeto procurado não foi encontrado           |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

### Deletar Clientes

![DELETE](https://img.shields.io/static/v1?label=&message=DEL&color=red&style=for-the-badge)

>`{{baseUri}}/cliente`

``` JSON
    [...] // Conjunto de ids para deletar clientes
```

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<dynamic> deletar(List<int> idClientes) async{
        var response = await client.delete(
          Uri.parse("${baseUri}/cliente"),
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(idClientes)
        );
        if(response.statusCode != 200){
          Logger().e("Cliente não encontrado");
        }
        return;
  }
}
~~~

#### Responses:
| Status Code |  Meaning  |                 Why?                  |
|-------------|:---------:|:-------------------------------------:|
| 200         |    OK     |   Desativoui o tecnico com sucesso    |
| 404         | NOT FOUND | O objeto procurado não foi encontrado |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---

## Endereço

![GET](https://img.shields.io/static/v1?label=&message=GET&color=&style=for-the-badge)

> `{{baseUri}}/endereco?cep=`

Dart
~~~dart
import 'dart:convert';
import 'package:http/http.dart' as http;

Class Api{
    var client = http.Client();
    String baseUri = "https//localhost:8080/api/v1";
    
    Future<Endereco?> getEndereco(Endereco cep) async{
        var uri = Uri.parse("$baseUri?cep=endereco.cep");
        var response = await client.get(uri);
    
        var responseBodyUtf8 = utf8.decode(response.body.runes.toList());
        dynamic jsonResponse = json.decode(responseBodyUtf8);
        Endereco endereco = Endereco.fromJson(jsonResponse);
        return endereco;
  }
}
~~~

#### Responses:
| Status Code | Significado |               Por quê?                |
|-------------|:-----------:|:-------------------------------------:|
| 200         |     OK      |           Retornou o valor            |            
| 404         |  NOT FOUND  | A entidade buscada não foi encontrada |

###### Alguma Dúvida sobre o corpo de um erro? [Erros](#Erros)

---
