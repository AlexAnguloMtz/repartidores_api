# Manejo de Errores

## Filosofía General

Para mantener consistencia en la integración del cliente (Frontend / Consumidores), **todos los errores** devueltos por la API responden bajo una misma estructura basada en el estándar **RFC 9457 (Problem Details for HTTP APIs)** mediante el esquema `ProblemDetail`.

Existen **CUATRO** códigos de error **GLOBALES** que pueden ocurrir en **TODOS** los endpoints:

* **`AUTENTICACION_INVALIDA`**: La token de acceso es inválida, o expiró. También cuando las credenciales para login son inválidas.
* **`PERMISOS_INSUFICIENTES`**: La autenticación es reconocida correctamente, pero no tienes los permisos suficientes para realizar la operación solicitada.
* **`CAMPOS_INVALIDOS`**: La petición contiene uno o más campos inválidos. El arreglo `errors` contiene el detalle de cada campo mediante objetos con la estructura `{ field, error }`.
* **`ERROR_SERVIDOR`**: Ocurrió un error interno inesperado en el servidor.

Además de estos códigos globales, cada operación puede definir sus propios códigos de error específicos. **Cada endpoint indica directamente en su documentación cuáles son los códigos de error específicos que pueden ocurrir según el caso.**

---

## Estructura de Respuesta de Error (`ProblemDetail`)

Cada vez que un endpoint retorne un error, el cuerpo de la respuesta contendrá la siguiente estructura:

### Campos Principales

* **`error_code`** *(string)*: **Campo clave.** Es una cadena parseable de tipo *enum* en mayúsculas. El cliente debe utilizar este código para decidir qué mensaje o flujo mostrar al usuario final.
* **`title`** *(string)*: Breve resumen legible por humanos del tipo de problema.
* **`status`** *(integer)*: El código de estado HTTP correspondiente.
* **`detail`** *(string)*: Explicación detallada del error particular.
* **`errors`** *(array)*: Cuando el `error_code` es `CAMPOS_INVALIDOS`, contiene objetos con la estructura `{ field, error }`, donde `field` identifica el campo inválido y `error` describe el fallo.

---

## Errores de Formulario / Validación (`errors`)

Cada objeto dentro de `errors` es un `ProblemDetailError`, el cual tiene la siguiente estructura:

* **`field`** *(string)*: Nombre del campo o propiedad que falló en la validación.
* **`error`** *(string)*: Clave o descripción técnica del fallo específico de ese campo.

---

## Ejemplos de Respuesta

### 1. Error General / Lógica de Negocio

```json
{
  "title": "Authentication Inválida",
  "status": 401,
  "detail": "La autenticación es inválida",
  "error_code": "AUTENTICACION_INVALIDA"
}
```

### 2. Error de campos de Formulario

Respuesta cuando falle la validación de uno o varios campos enviados por el cliente:

```json
{
  "title": "Validation failed",
  "status": 400,
  "detail": "Errores de validacion",
  "error_code": "CAMPOS_INVALIDOS",
  "errors": [
    {
      "field": "email",
      "error": "Correo invalido"
    },
    {
      "field": "password",
      "error": "Minimo 8 caracteres"
    }
  ]
}
```

---

## Guía para Clientes (Consumidores)

1. **Parseo primario:** Evalúa siempre el campo `error_code` para determinar el mensaje a mostrar al usuario. Los códigos de error específicos posibles están documentados directamente en cada endpoint.
2. **Formularios:** Si un formulario tiene campos inválidos, el código es `CAMPOS_INVALIDOS` y el arreglo `errors` debe ser iterado para manejar cada objeto de tipo `ProblemDetailError`, con la estructura `{ field, error }`.
3. **Campos que puedes ignorar:** Los campos obligatorios a utilizar de un `ProblemDetails` son `error_code` 
4. **Ignora los Status Code**: Para programar lo más rápido posible vamos a ignorar todos los Status Code diferente a `200 OK`.  Para tomar decisiones en el manejo de errores, vamos a usar  el campo `error_code` del objeto `ProblemDetails`.