# Estudo-Spring-API_Rest(Em Construçao)

<pre>
#Projeto de estudos 
A API consiste em operaçoes CRUD de cadastro de vagas de estacionamentos em edificios, utilizando JAVA,Spring e mySQL. 

Requisitos
-Java 11
-Maven 4.0
-mySQL 8.0

#DEPENDENCIAS E BIBLIOTECAS UTILIZADAS:
1 Maven(gerenciamento de dependencias).	
2 Lombok, para agilidade e evitar scripts longos e verbosos.
3 Spring-boot-starter-data-jpa, básico para persistir dados.
4 Spring-boot-starter-validation e Hibernate Validator, novamente para ganhar agilidade e codigos mais limpos, validando dados/inputs.
5 Spring-boot-starter-hateoas para criar hiperlinks de navegaçao e auxilio aos recursos da API.
6 MySQL

ROTAS:
* Put Vaga. @RequestBody  http://localhost:8080/parking-control/parking-spot/registration
{
  "parkingSpotNumber" :
}
* Put Usuario. @RequestBody  http://localhost:8080/parking-control/user/registration
{
  "name" :
  "cpf" :
  "password" :
}
* Put Carro. @RequestBody, @PathVariable do id usuario que o carro pertence e @RequestParam do numero da vaga que o carro ocupara
  http://localhost:8080/parking-control/car/registration/{id}
{
  "licensePlateNumber" :
  "brandCar" :
  "modelCar" :
  "colorCar" :
}
@RequestParam parkingSpotNumber

* Get Todas Vagas  http://localhost:8080/parking-control/parking-spot/all
* Get Todos Carros  http://localhost:8080/parking-control/car/all
* Get Todas User  http://localhost:8080/parking-control/user/all

* Get Vaga ID. @PathVariable id    http://localhost:8080/parking-control/parking-spot/{id}
* Get Usuario ID. @PathVariable id http://localhost:8080/parking-control/user/{id}
* Get Carro ID. @PathVariable id   http://localhost:8080/parking-control/car/{id}

* Get dinamico de carros de acordo com @RequestParam opcionais.  http://localhost:8080/parking-spot/?brandCar&modelCar&colorCar
  @RequestParam brandCar
  @RequestParam modelCar
  @RequestParam colorCar

*Patch parcial ou completo de vaga por @PathVariable id  http://localhost:8080/parking-control/parking-spot/patchId/{id}
*Patch parcial ou completo de usuario por @PathVariable id.  http://localhost:8080/parking-control/user/patchId/{id}


</pre>
