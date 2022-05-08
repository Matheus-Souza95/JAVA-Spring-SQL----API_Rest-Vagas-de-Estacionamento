# Estudo-Spring-API_Rest(Em Construçao)<h1>

<pre>
<h2>Projeto de estudos<h5>
A API consiste em operaçoes CRUD de cadastro de veiculos em vagas de estacionamento, utilizando JAVA, Spring e mySQL. 

<h2>Requisitos<h5>
-Java 11
-Maven 4.0
-mySQL 8.0

<h2>Instruçoes de uso<h5>
1 - Usuário e senha padrões do banco: 'root' e 'root'.
2 - No MySQL utilizar comando CREATE DATABASE parking_control_db;
3 - Startar o projeto na IDE.
2 - No MySQL, utilizar o comando INSERT INTO profile_access(name, id) VALUES('ROLE_DEFAULT','1'); para criar uma Role de acesso.
3 - Criar usuario no postman/insomnia etc e anotar ID.
4 - Logar usuario e anotar Token.
5 - Criar vaga no postman/insomnia e anotar o parkingSpotNumber da vaga.
6 - Criar carro no postman/insomnia e passar ID do usuario e numero da vaga.


<h2>ROTAS:<h5>

 Get Loggin http://localhost:8080/loggin
{
	"userName":
	"password":
}


 Put Usuario. @RequestBody  http://localhost:8080/parking-control/user/registration
{
  "name" :
  "cpf" :
  "password" :
}


 Put Vaga. @RequestBody  http://localhost:8080/parking-control/parking-spot/registration
{
  "parkingSpotNumber" :
}


 Put Carro. @RequestBody, @PathVariable do id usuario que o carro pertence e @RequestParam do numero da vaga que o carro ocupará
  http://localhost:8080/parking-control/car/registration/{id}
{
  "licensePlateNumber" :
  "brandCar" :
  "modelCar" :
  "colorCar" :
}
@RequestParam parkingSpotNumber


 Get Todas Vagas  http://localhost:8080/parking-control/parking-spot/all
 Get Todos Carros  http://localhost:8080/parking-control/car/all
 Get Todas User  http://localhost:8080/parking-control/user/all


 Get Vaga ID. @PathVariable id    http://localhost:8080/parking-control/parking-spot/{id}
 Get Usuario ID. @PathVariable id http://localhost:8080/parking-control/user/{id}
 Get Carro ID. @PathVariable id   http://localhost:8080/parking-control/car/{id}


 Get dinamico de carros de acordo com @RequestParam opcionais.  http://localhost:8080/parking-spot/?brandCar&modelCar&colorCar
 @RequestParam brandCar
 @RequestParam modelCar
 @RequestParam colorCar


 Patch parcial ou completo de vaga por @PathVariable id  http://localhost:8080/parking-control/parking-spot/patchId/{id}
 Patch parcial ou completo de usuario por @PathVariable id.  http://localhost:8080/parking-control/user/patchId/{id}


</pre>
