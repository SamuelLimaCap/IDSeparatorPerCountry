# Separador de IDs por país


 * Sobre 
 * Como usar
 	* Pré-requisitos
 	* Como rodar a aplicação
 	* Cuidados
 * Programas gerados pelo arquivo
 * Créditos

## Sobre

Programa na qual mostra a quantidade de usuários e quantos desses estão classificados como "assinado" por país ,através do DDI e do log passado.

## Como usar

### Pré requisitos:
- java 8 pra cima
- Arquivo para obtenção dos dados
	- Os ids dos usuários devem conter o DDI no inicio
	- Formatado por linha da seguinte forma( sem as aspas):
		"userID, assinado/cancelado"
	

### Como rodar a aplicação 
 
```
# Clone este repositório
$ git clone

# Caminhe até a pasta do projeto

# Acesse a pasta do arquivo Main.java pelo terminal
$ cd src/com/geral/main


# Rode a aplicação passando o endereço do arquivo
	# como 2º argumento(1º precisa ser 'path')
	$ java Main.java path C:\Temp\bemobi\25-06-2020.log
	
	# Durante a execução do arquivo
	$ java Main.java
```
### Cuidados
 Durante o caminho passado, nenhuma pasta deve conter espaço no nome.
 Ex: C:\Temp\teste 25-08\25-05-2020.log
 
## Programas gerados pelo arquivo

O programa gera arquivo de saída no mesmo diretório que o arquivo Main.java. A extensão do arquivo de saída é o mesmo do arquivo passado. Os arquivo gerado é  nomeado da seguinte maneira:
`` out-nomeDoArquivoPassado``
 	
## Créditos
Foi utilizado a API restCountries para obtenção dos DDIs por país.
> https://github.com/apilayer/restcountries
