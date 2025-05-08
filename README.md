# Porcentaje API

Una API REST desarrollada con Spring Boot y java 21 para realizar suma de dos numero + porcentaje dinámico, historial y documentación integrada.

# Requisitos previos

- Docker y Docker Compose instalados.
- Java 21 (opcional, si deseas correr el proyecto sin Docker).
- Maven 3.9.9 (opcional, si deseas empaquetarlo tú mismo).

# Clonar el repositorio
git clone https://github.com/tu-usuario/porcentaje-api.git
cd porcentaje-api

# Empaquetar el proyecto(generar el jar)
Ejecutar el siguiente comando:  mvnw.cmd clean package -DskipTests desde cmd.

# Construir la imagen y levantar los servicios con docker
Ejecutamos el siguiente comando: docker-compose up --build

#Una vez queda levantado el proyecto.
hacemos la prueba desde Postman:

1.Sumar dos numeros y el porcentaje.
url: localhost:8080/api/v1/operacion/calcular
metodo. POST
request: ejemplo: {"num1":"400", "num2": "300" }
respuesta exitosa: { "resultado": 770.0, "porcentajeAplicado": 10.0 }

2. Obtenemos el historial de operaciones desde la base de postgres.
request:
url: http://localhost:8080/api/v1/historial?page=0&size=10
metodo:GET

# Nota: En este repositorio también se incluirá un zip de coleccion de postman listo para ejecutar.
