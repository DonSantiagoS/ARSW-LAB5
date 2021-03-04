
### Escuela Colombiana de Ingeniería

### Arquitecturas de Software


## Laboratorio API REST para la gestión de planos

### Dependencias
* [Laboratorio Componentes y conectores Middleware- gestión de planos (Blueprints) Parte 1](https://github.com/ARSW-ECI-beta/REST_API-JAVA-BLUEPRINTS_PART1)

### Descripción
En este ejercicio se va a construír el componente BlueprintsRESTAPI, el cual permita gestionar los planos arquitectónicos de una prestigiosa compañia de diseño. La idea de este API es ofrecer un medio estandarizado e 'independiente de la plataforma' para que las herramientas que se desarrollen a futuro para la compañía puedan gestionar los planos de forma centralizada.
El siguiente, es el diagrama de componentes que corresponde a las decisiones arquitectónicas planteadas al inicio del proyecto:

![](img/CompDiag.png)

Donde se definió que:

* El componente BlueprintsRESTAPI debe resolver los servicios de su interfaz a través de un componente de servicios, el cual -a su vez- estará asociado con un componente que provea el esquema de persistencia. Es decir, se quiere un bajo acoplamiento entre el API, la implementación de los servicios, y el esquema de persistencia usado por los mismos.

Del anterior diagrama de componentes (de alto nivel), se desprendió el siguiente diseño detallado, cuando se decidió que el API estará implementado usando el esquema de inyección de dependencias de Spring (el cual requiere aplicar el principio de Inversión de Dependencias), la extensión SpringMVC para definir los servicios REST, y SpringBoot para la configurar la aplicación:


![](img/ClassDiagram.png)

### Parte I

1. Integre al proyecto base suministrado los Beans desarrollados en el ejercicio anterior. Sólo copie las clases, NO los archivos de configuración. Rectifique que se tenga correctamente configurado el esquema de inyección de dependencias con las anotaciones @Service y @Autowired.

Se realizo la adicion al directorio las carpetas correspondientes del **Laboratorio 4** Correspondientes a las implementaciones de los filtros, controladores, modelo, persostencia y servicios, ademas de rectificar el uso correcto de las inyecciones de dependencias

2. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.

Se realizo la implementacion de la siguiente forma:

```
public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts1=new Point[]{new Point(120, 120),new Point(100, 100)};
		Point[] pts2=new Point[]{new Point(13, 12),new Point(43, 42)};
		Point[] pts3=new Point[]{new Point(120, 150),new Point(54, 67)};
        Blueprint bp1=new Blueprint("Santiago", "bp1 ",pts1);
        blueprints.put(new Tuple<>(bp1.getAuthor(),bp1.getName()), bp1);
		Blueprint bp2=new Blueprint("Santiago", "bp2 ",pts2);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
		Blueprint bp3=new Blueprint("Steven", "bp3 ",pts3);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        
    }
```

3. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:

	* Modifique la clase BlueprintAPIController teniendo en cuenta el siguiente ejemplo de controlador REST hecho con SpringMVC/SpringBoot:

	```java
	@RestController
	@RequestMapping(value = "/url-raiz-recurso")
	public class XXController {
    
        
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX(){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }        
	}

	```
	
	* Haga que en esta misma clase se inyecte el bean de tipo BlueprintServices (al cual, a su vez, se le inyectarán sus dependencias de persisntecia y de filtrado de puntos).

	Se realizo de la siguiente forma con la clase del controlador
	
	```
@RestController
@RequestMapping(value="/blueprints")
public class BlueprintAPIController {
	
    @Autowired
    @Qualifier("BlueprintsServices")
	BlueprintsServices servicios;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorBlueprints(){
        try {
            return new ResponseEntity<>(servicios.getAllBlueprints(), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path  = "/{author}",method = RequestMethod.GET)
    public ResponseEntity<?>  manejadorBlueprintsByAuthor(@PathVariable("author") String author){
        try {
            return new ResponseEntity<>(servicios.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> manejadorPostBlueprint(@RequestBody Blueprint bp){
        try { 
			servicios.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/{author}/{bpname}",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> manejadorPutBlueprint(@PathVariable("author") String author,@PathVariable("name") String name,@RequestBody Blueprint bp ) {
        try {
            servicios.actualizarBlueprint(bp,author,name);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
}
	```
	

4. Verifique el funcionamiento de a aplicación lanzando la aplicación con maven:

	```bash
	$ mvn compile
	$ mvn spring-boot:run
	
	```
	
	![](./img/compila.PNG)
	
	Y luego enviando una petición GET a: http://localhost:8080/blueprints. Rectifique que, como respuesta, se obtenga un objeto jSON con una lista que contenga el detalle de los planos suministados por defecto, y que se haya aplicado el filtrado de puntos correspondiente.


5. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en [la documentación de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).

Se realizo la implementacion de la siguiente forma:

```
@RequestMapping(path  = "/{author}",method = RequestMethod.GET)
    public ResponseEntity<?>  manejadorBlueprintsByAuthor(@PathVariable("author") String author){
        try {
            return new ResponseEntity<>(servicios.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
```

6. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404. 

Se realizo la implementacion de la siguiente forma:

```
@RequestMapping(path  = "/{author}/{bpname}",method = RequestMethod.GET)
    public ResponseEntity<?> manejadorBlueprintsByAuthor(@PathVariable("author") String author,@PathVariable("bpname") String bpname){
        try {
            return new ResponseEntity<>( bp.getBlueprint(author, bpname),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
```


### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

	```	java
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
 	
	}
	```	
	
	Se realiza la implementacion quedando de la siguiente manera:
	
	```
	@RequestMapping(path = "/create",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> manejadorPostBlueprint(@RequestBody Blueprint bp){
        try { 
			servicios.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch(Exception ex){
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("",HttpStatus.FORBIDDEN);
        }
    }
	```

2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

	Con el siguiente comando desde la consola, se realiza el curl para verificar la respuesta:
	
	```	
	$  curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://localhost:8080/blueprints/create -d "{"""author""":"""jhon""","""points""":[{"""x""":10,"""y""":10},{"""x""":130,"""y""":130}],"""name""":"""ejemplocurlterminado"""}"
	```	

	Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
	

	Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.
	
	de forma que se evidencia lo siguiente:
	
	![](./img/curl.PNG)
	
3. Teniendo en cuenta el autor y nombre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.
	
	de forma que se evidencia lo siguiente:
	
	![](./img/curl1.PNG)
	

4. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.
	
	de forma que se evidencia lo siguiente:
	
	![](./img/curl2.PNG)

Se realiza la implementacion quedando de la siguiente manera:

	```
	@RequestMapping(path = "/{author}/{bpname}",method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> manejadorPutBlueprint(@PathVariable("author") String author,@PathVariable("name") String name,@RequestBody Blueprint bp ) {
        try {
            servicios.actualizarBlueprint(bp,author,name);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }
    }
	```


### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?

Existe la posibilidad de que que se cuenta con una condicion de carrera, al observar la lista de Bluesprints en el momento en el cual se accede al recurso compartido de los respectivos planos, ademas de evidenciarse una posible condicion de carrera en los casos en los cuales se desean actualizar los planos, de mas de uno plano a la vez, independientemente si es del mismo usuario o es de un usuario distinto

* Cuales son las respectivas regiones críticas?

Es posible contar con regiones criticas en lo que respecta a la solicitudes que se realizan de forma concurrente, ademas de al intentar actualizar los planos o al realizar cambios de mapeo MAP que contieen los planos al hacer la peticion PUT, debido que se debe tener en cuenta que en un mismo instante se puede contar con mas de un cliente o susuario que este realizando alguna de las dos actividades nombradas de forma que al hacerlo de manera simultanea se generar dichas regiones criticas que son de gran importancia considerar para manejarlo de manera optima.

Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt


La prueba se realiza de manera habitual con 

Compilacion:

![](./img/compila.PNG)

Empaquetado:

![](./img/empaquetado.PNG)

Test:

![](./img/test.PNG)

## Control de versiones

por: 
+ [Santiago Buitrago](https://github.com/DonSantiagoS) 
+ [Steven Garzon](https://github.com/stevengarzon7) 

Version: 1.0
Fecha: 02 de febrero 2021

### Autores

* **Santiago Buitrago** - *Laboratorio N°2* - [DonSantiagoS](https://github.com/DonSantiagoS)
* **Steven Garzon** - *Laboratorio N°2* - [stevengarzon7](https://github.com/stevengarzon7)

## Licencia 

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Agradecimientos

* Persistencia en lograr el objetivo

