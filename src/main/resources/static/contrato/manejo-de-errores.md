# Manejo de Errores

## Filosofía General

Para mantener consistencia en la integración del cliente (Frontend / Consumidores), **todos los errores** devueltos por la API responden bajo una misma estructura basada en el estándar **RFC 7807 (Problem Details)** mediante el esquema `ProblemDetail`.

---

## Estructura de Respuesta de Error (`ProblemDetail`)

Cada vez que un endpoint retorne un código HTTP `4xx` o `5xx`, el cuerpo de la respuesta contendrá la siguiente estructura:

### Campos Principales

* **`error_code`** *(string)*: **Campo clave.** Es una cadena parseable de tipo *enum* en mayúsculas (ej. `BAD_CREDENTIALS`, `USER_NOT_FOUND`). El cliente debe utilizar este código para decidir qué mensaje o flujo mostrar al usuario final. Para conocer todos los codigos de error posible en un endpoint, dentro del archivo YAML de dicho endpoint se van a numerar todos los posibles.  
* **`title`** *(string)*: Breve resumen legible por humanos del tipo de problema.
* **`status`** *(integer)*: El código de estado HTTP correspondiente.
* **`detail`** *(string)*: Explicación detallada del error particular ocurrido.
* **`errors`** *(array, opcional)*: Presente **únicamente** cuando la petición involucra validación de formularios. Contiene el desglose de fallos campo por campo.

---

## Errores de Formulario / Validación (`errors`)

Cada objeto dentro de 'errors' es un  `ProblemDetailError`, el cual  tiene la siguiente estructura:

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

1. **Parseo primario:** Evalúa siempre el campo `error_code` para determinar el mensaje a mostrar al usuario. En cada archivo YAML de cada endpoint estarán todos los codigos de error posibles numerados. 

2. **Formularios:** Si un formulario tiene campos invalidos, el codigo es `INVALID_FIELDS` y el arreglo `errors` debe ser iterado para manejar cada objeto de tipo ProblemDetailError, con la estructura `{ field, error }`.

3. **Campos que puedes ignorar:** Los campos obligatorios a utilizar de un ProblemDetails son `error_code` y `errors`. Realmente, otros demas campos (`title`, `detail`, `instance`) puedes ignorarlos y no habrá mucha diferencia.

3. **Ignora los STATUS CODE:** Para programar lo más rápido posible, sólo nos importa si el status es `200 OK`. En caso de no serlo, ignora el status específico de error, y el campo `error_code` va a ser la autoridad absoluta en cuanto a cuál fue el error exacto. El error genérico 500 siempre vendrá con `error_code` "SERVER_ERROR".