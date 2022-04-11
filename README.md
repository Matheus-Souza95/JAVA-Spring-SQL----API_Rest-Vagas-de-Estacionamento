# Estudo-Spring-API_Rest(Em Construçao)

<pre>
#Projeto de estudos utilizando como base a aula extremamente didatica de Michelli Brito, onde personalizei o codigo utilizando outros conhecimentos adquiridos.
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
* Get Busca todas vagas cadastradas. http://localhost:8080/parking-spot/all
* Get por ID vaga cadastrada. Param: id http://localhost:8080/parking-spot/
* Get dinamico de vagas de acordo com parametros opcionais. Params: marca, modelo e cor do veiculo 
  http://localhost:8080/parking-spot/?brandCar&modelCar&colorCar
*Post faz cadastro de vaga. Params request body:
{
	"parkingSpotNumber" :"",
	"licensePlateCar" :	"",
 	"brandCar" :"",
	"modelCar" :"",
	"colorCar" :"",
	"responsibleName" :"",
 	"apartment" :"",
  	"block" :""
}
*Put atualizaçao de vaga cadastrada por ID. Param: id http://localhost:8080/parking-spot/updateId/
*Put atualizaçao de vaga cadastrada por numero da vaga. Param parkingSpotNumber http://localhost:8080/parking-spot/update/
*Patch atualizaçao parcial de vaga cadastrada. Param id http://localhost:8080/parking-spot/patchId/

</pre>
