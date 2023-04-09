# CDU

![Concepto](https://github.com/cdelaof26/CDU/blob/main/images/Concepto.jpeg?raw=true)

### ¿Qué es esto?

CDU (**C**onvertidor **D**e **U**nidades) es un paquete de dos 
pequeños proyectos (CDUui y CDUP) para la visualización y conversión 
de unidades por el método de la regla de tres implícita

CDUP (CDU **P**ortable) es la parte esencial del proyecto, 
ya que se encarga de realizar las conversiones, este programa 
puede ejecutarse por separado de CDUui requiriendo únicamente el 
intérprete de python

### Dependencias

1. Python 3.6 ó superior
2. Oracle Java 8 ó superior, alternativamente OpenJDK 8 o superior
   - Para compilar el proyecto se requiere el JDK de Java

**Nota**: Java 8 es opcional, el proyecto se puede ejecutar
          exclusivamente con Python (CDUP) en la línea de comandos

### Descargo de responsabilidad

Este proyecto se creó con fines educativos

Antes de utilizarlo, por favor lee la [LICENCIA](LICENSE)


### Ejecutar el proyecto

- Abre tu consola de comandos preferida

- Clona este repositorio

<pre>
$ git clone https://github.com/cdelaof26/CDU.git
</pre>

- Ingresa al directorio del proyecto

<pre>
$ cd CDU
</pre>

- ### **Ejecución con Java y Python**: Compila y ejecuta

<pre>
$ javac -classpath . cdu/CDU.java
$ java cdu/CDU
</pre>

- Alternativamente, puede ser empacado en un ejecutable JAR
  (Probado con Java 8)

  - Elimina o mueve el directorio `.git` para prevenir que la 
    ejecución de `jar` incluya estos archivos en el paquete,

<pre>
$ rm -r .git

    ó

$ mv .git /nueva/ruta
</pre>

- Empaqueta y ejecuta

<pre>
$ javac -classpath . cdu/CDU.java
$ jar cmvf ./manifest.mf CDU.jar -classpath . cdu/CDU
$ java -jar CDU.jar

# Alternativamente se puede hacer doble click sobre el archivo JAR
# (en lugar de escribir "java -jar CDU.jar" en la terminal)
</pre>


- ### **Ejecución con Python**: Ejecuta

- Ingresa al directorio del sub-proyecto

<pre>
$ cd cdubackend
</pre>

- Ejecuta

<pre>
# Si te encuentras en Linux/macOS
$ cd python3 main.py

# Si te encuentras en Windows
$ cd python main.py
</pre>


### Historial de cambios

### v0.0.1-0 [08-04-23]
- Proyecto inicial
  - Se agrega README.md

### v0.0.1 [08-04-23]

- Proyecto inicial
  - Por ahora solo es posible hacer conversiones de unidades 
    elementales y por medio de la modificación del programa
