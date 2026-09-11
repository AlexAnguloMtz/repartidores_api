# API Contract - Guía de Arquitectura y Manejo de Errores

Este repositorio contiene las especificaciones y contratos OpenAPI (YAML) que definen la comunicación con la API.

---

## Filosofía General

Para mantener consistencia en la integración del cliente (Frontend / Consumidores), **todos los errores** devueltos por la API responden bajo una misma estructura basada en el estándar **RFC 7807 (Problem Details)** mediante el esquema `ProblemDetail`.

---

## Estructura de Respuesta de Error (`ProblemDetail`)

Cada vez que un endpoint retorne un código HTTP `4xx` o `5xx`, el cuerpo de la respuesta contendrá la siguiente estructura:

### Campos Principales

* **`error_code`** *(string)*: **Campo clave.** Es una cadena parseable de tipo *enum* en mayúsculas (ej. `BAD_CREDENTIALS`, `USER_NOT_FOUND`, `VALIDATION_FAILED`). El cliente debe utilizar este código para decidir qué mensaje o flujo mostrar al usuario final.
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