O Spring Boot é um framework de Java que pelo que entendi ele vai auxiliar na construção de uma aplicação que vai servir uma API Rest na web e que também vai se comunicar com um banco de dados fazendo a essa abstração com um ORM.

Toda essa sessão vai ser baseada na construção de um projeto de loja virtual, onde vamos criar as rotas, modelar as tabelas, criar as relações e associar as classes de entidades. Inicialmente para criar um projeto Spring do início, a gente pode utilizar a ferramenta de criação “[**Spring Initializr](https://start.spring.io/)”** que seria um programa oficial do Spring, e que vai gerar um projeto montado em formato zip.

Para montar o projeto dessa sessão, foi escolhido um projeto Maven, na linguagem Java, com a versão do Spring 3.3.2, packing como Jar, JDK na versão 21 e a dependência Spring Web para dar a utilidade de API Rest. Ao finalizar, vamos poder baixar o arquivo zip, que ao ser descompactado vai ser um projeto Spring já configurado.

Para o ambiente de desenvolvimento local, a gente vai utilizar um banco de dados em memória chamado H2, criar um perfil de teste com sistema de *seed*. Para essa configuração, a gente precisa adicionar as dependências do JPA para spring e do banco H2

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>
```

Além dessas dependências, a gente precisa fazer a configuração do perfil de teste local. Primeiramente a gente precisa ir no indicar no arquivo `src/main/resources/application.properties` que vamos utilizar um novo perfil com a instrução `spring.profiles.active=test`. Depois devemos criar um arquivo `application-test.properties` no mesmo caminho, com as seguintes configurações do H2

```
# DATASOURCE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
# H2 CLIENT
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
# JPA, SQL
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Agora com as configurações de Spring e JPA montadas, a gente pode fazer os mapeamentos das classes consideradas entidades para que elas possam ser convertidas para o modelo relacional.

Para marcar as classes como sendo um elemento direto de uma tabela, a gente utiliza a anotação `@Entity` sobre a classe que deve ser representada por uma tabela. Caso seja necessário fazer alguma conversão entre o nome da classe e da tabela, é possível fazer o mapeamento com `@Table(name = "")`.

Cada atributo da classe vai corresponder a uma coluna da tabela no banco de dados, e alguns desses atributos vão precisar ter anotações devido a comportamentos esperados na parte de relações. 

As colunas com “id” que normalmente servem como chave primária de uma tabela, vão receber a anotação `@Id`, e caso elas tenham um sistema de geração automática, como uma sequencia, deve receber a anotação `@GeneratedValue(strategy = GenerationType.TYPE)`.

Já os atributos que vão representar a relação com outra entidade, eles normalmente acontecem de forma cruzada, e devem apresentar a anotação da relação em ambas as classes, observando o tipo de relacionamento adequado. Por exemplo, entre as classes **User** e **Order**, da visão da classe **User**, ela deve receber um atributo de lista que vai representar todos os pedidos do usuário, e esse atributo deve apresentar a anotação `@OneToMany(mappedBy = "atributo da outra classe")`. Já na da visão da classe **Order**, ela deve ter um atributo do tipo usuário, e este atributo deve ter a anotação `@ManyToOne`.

Nesse tipo de relacionamento há outras considerações. Uma é que o atributo vai ser do tipo **User** dentro da classe **Order**, mas na tabela, isso vai ser representado apenas pelo informação da chave estrangeira, e normalmente essa coluna é o nome da entidade estrangeira concatenada com “id”, então, é preciso a anotação para fazer essa relação de nomes, indicando que ela vai ser a coluna de junção com a anotação `@JoinColumn(name = "entity_id")`.

Uma outra consideração, é que isso pode causar uma recursividade no acesso de dados, uma vez que um pedido vai trazer os dados de um usuário, que vai ter uma lista de pedidos, onde cada pedido vai ter o usuário, e assim por diante. Para evitar essa situação, normalmente nós colocamos na entidade que tem a coluna com a relação um para muitos a anotação `@JsonIgnore` na coluna de relação para avisar a biblioteca que quanto trouxer dados dessa entidade, não devem ser trazidos os dados da entidade relacionada.

Uma última consideração é que essa marcação de ignorar, normalmente é feita na entidade de com um para muitos, porque o que acontece é que ao buscar a os dados da entidade “muitos”, o Hibernate automaticamente faz o join com os dados da entidade “um”, mas ao buscar os dados da entidade “um” ele precisa fazer uma segunda query. Então além de resolver o problema de recursividade, também diminui a quantidade de consultas ao banco. Porém, esse join automático, só vai acontecer com sucesso caso a instrução `spring.jpa.open-in-view=true` esteja no arquivo de configurações.

Outra relação que pode acontecer é a relação *many-to-many*, e nesses casos a gente precisa ter a chamada tabela de junção. No projeto, essa relação vai aparecer entre as entidades **Product** e **Category**, assim ambas vão apresentar um atributo de lista, sendo uma com o tipo do outro. Para indicar a tabela de junção, em qualquer uma das entidades, a gente vai adicionar a anotação `@JoinTable(name ="", joinColums = @JoinColumn(name = "coluna com id dessa entidade"), inverseJoinColumns = @JoinColumn(name = "coluna com id da outra entidade")`. Essa anotação vai ser responsável por indicar a tabela de junção, qual coluna representa essa entidade e qual coluna representa a outra entidade.

Ambas as entidades vão apresentar a anotação `@ManyToMany` nos seus atributos de vínculo, sendo que a entidade que não tiver a indicação da tabela de junção, precisa ter o mapeamento com `@ManyToMany(mappedBy = "lista na outra entidade")`. Além disso, alguma delas deve apresentar o `@JsonIgnore` para evitar a recursividade.

Essa relação *many-to-many* pode ter uma situação onde além de termos o relacionamento, nós precisamos colocar outros dados que fazem parte da relação, e portanto a tabela de junção precisa ter mais do que apenas as duas colunas com ids. No projeto, essa relação acontece entre as entidades **Order** e **Product**, já um pedido pode ter vários produtos, mas um produto pode estar em vários pedidos. E quando um pedido é montado, nós temos informações adicionais como a quantidade de produtos, ou o preço no momento da compra.

Nestes casos, apenas a anotação de *JoinTable* não é suficiente, e então a gente precisa de uma entidade que vai representar essa tabela de junção, tendo o relacionamento com as duas entidades originais, mais os atributos extras. Acontece que essa tabela, vai acabar tendo como chave primária a combinação das colunas que vão relacionar as entidades originais, e para isso é preciso ter uma classe separada para identificar apenas essa *primary key*.

No nosso projeto, primeiro precisamos então criar uma classe que vai receber os dois campos do relacionamento, que combinados serão o **id** da tabela de junção. Essa classe é a **OrderItemPK**, ela vai ter apenas o atributos do relacionamento, e ambos vão ter a anotação `@ManyToOne` e `@JoinColumn(name = "entity_id")`.

Com a classe que vai representar o **id** montado  nós vamos criar a entidade **OrderItem** que vai representar a a tabela de junção, ela vai ter o atributo id do tipo da classe PK, e mais os atributos das informações extras.  Um detalhe, é que a classe PK não vai ter um construtor, e ela vai ser utilizada apenas dentro da entidade de junção. Então esta segunda vai receber os dados das duas entidades originais, usá-los para instanciar a classe PK. O atributo do tipo da classe PK, deve receber a anotação `@EmbeddedId`.

Para finalizar essa relação, nas entidades originais, a gente vai precisar ter os atributos em lista para o tipo **OrderItem**, onde em cada uma delas, esse atributo vai receber a anotação `@OneToMany(mappedBy = "id.relation")`, onde relation é o campo relacionado, product ou order. 

A última relação que vamos ter é a *one-to-one*. No nosso projeto ela vai acontecer ente as entidades **Order** e **Payment**. Em ambas vamos ter um atributo que vai indicar o cruzamento do relacionamento, e em ambos os atributos teremos a anotação `@OneToOne`. Na entidade principal a gente coloca a configuração de `mappedBy` com o nome recebido na entidade secundária, enquanto que na entidade secundária a gente também pode adicionar a anotação `@MapsId` para relacionar o id da entidade secundária com o id da entidade principal. 

Relações montadas e mapeadas nas entidades, o que falta é criar as camadas da estrutura de projeto. Para isso a gente vai ter a camada de repositório, que é quem vai adicionar métodos de persistência em banco de dados às nossas entidades. Cada entidade deve ter o seu repositório, e ao invés de ser uma classe cheia, o repositório é apenas uma interface, que estende um interface do JPA, e que por meio do Spring, injeta os métodos padrões.

```java
package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
} 
```

Outra camada que a gente precisa é a de recurso, que é a que vai receber a requisição, com os dados de entrada e mais coisas. A classe dessa camada precisa ter as anotações `@RestController` para indicar que é uma classe de recebe as requisições, a anotação `@RequestMapping(value = "/rota")` que vai indicar qual o rota vai disparar esse recurso.

Essa classe vai ter métodos que vão ser executados conforme o endpoint, como um sistema de middleware do express, esse método é desenvolvido de forma padrão, mas ele vai receber uma anotação para indicar qual o método HTTP para ele, e se vai ter algum parâmetro de rota que deva ser utilizado, por exemplo `@GetMapping(value = "/{id}")`.

Uma terceira camada que vai ligar as duas anteriores é a camada de serviço, onde vai ser realizado o desenvolvimento das regras de negócio. Então basicamente a camada de recurso precisa conhecer a de serviço, e a de serviço precisa conhecer a repositório, mas para que se obedeça aos melhores padrões de desenvolvimento, considerando a inversão de dependência, o que se faz é que ao invés de fazer a inversão manualmente, a gente adiciona a anotação `@Autowired` no atributo que representa a dependência da camada, e o próprio spring monta a inversão pra gente.