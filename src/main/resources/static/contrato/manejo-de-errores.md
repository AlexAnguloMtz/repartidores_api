# Manejo de Errores

## Filosofía General

Para mantener consistencia en la integración del cliente (Frontend / Consumidores), **todos los errores** devueltos por la API responden bajo una misma estructura basada en el estándar **RFC 9457 (Problem Details for HTTP APIs)** mediante el esquema `ProblemDetail`.

Existen dos códigos de error **globales** que pueden ocurrir independientemente del endpoint:

* **`INVALID_FIELDS`**: La petición contiene uno o más campos inválidos. El arreglo `errors` contiene el detalle de cada campo mediante objetos con la estructura `{ field, error }`.
* **`SERVER_ERROR`**: Ocurrió un error interno inesperado en el servidor.

Además de estos códigos globales, cada operación puede definir sus propios códigos de error específicos. Estos se encuentran en el catálogo `error-codes.yml`.

---

## Códigos de Error Específicos por Operación

El archivo `error-codes.yml` indica qué códigos de error específicos pueden ocurrir en cada operación de la API.

Para consultar los códigos posibles de una operación, busca su **path + método HTTP** dentro de dicho archivo.

---

## Estructura de Respuesta de Error (`ProblemDetail`)

Cada vez que un endpoint retorne un error, el cuerpo de la respuesta contendrá la siguiente estructura:

### Campos Principales

* **`error_code`** *(string)*: **Campo clave.** Es una cadena parseable de tipo *enum* en mayúsculas. El cliente debe utilizar este código para decidir qué mensaje o flujo mostrar al usuario final.
* **`title`** *(string)*: Breve resumen legible por humanos del tipo de problema.
* **`status`** *(integer)*: El código de estado HTTP correspondiente.
* **`detail`** *(string)*: Explicación detallada del error particular ocurrido.
* **`errors`** *(array, opcional)*: Presente únicamente cuando la petición involucra validación de formularios. Contiene objetos con la estructura `{ field, error }`, donde `field` identifica el campo inválido y `error` describe el fallo.

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
  "title": "Authentication Failed",
  "status": 401,
  "detail": "The credentials provided are invalid or have expired.",
  "error_code": "BAD_CREDENTIALS"
}
```

### 2. Error de campos de Formulario

Respuesta cuando falle la validación de uno o varios campos enviados por el cliente:

```json
{
  "title": "Validation failed",
  "status": 400,
  "detail": "Errores de validacion",
  "error_code": "INVALID_FIELDS",
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

1. **Parseo primario:** Evalúa siempre el campo `error_code` para determinar el mensaje a mostrar al usuario. Los códigos específicos posibles de cada operación se encuentran en `error-codes.yml`.
2. **Formularios:** Si un formulario tiene campos inválidos, el código es `INVALID_FIELDS` y el arreglo `errors` debe ser iterado para manejar cada objeto de tipo `ProblemDetailError`, con la estructura `{ field, error }`.
3. **Campos que puedes ignorar:** Los campos obligatorios a utilizar de un `ProblemDetails` son `error_code` y `errors`. Los demás campos (`title`, `detail`, `instance`) pueden ignorarse.
4. **Ignora los STATUS CODE:** Para programar lo más rápido posible, sólo nos importa si el status es `200 OK`. En caso de no serlo, ignora el status específico de error, y el campo `error_code` será la autoridad absoluta en cuanto a cuál fue el error exacto.
