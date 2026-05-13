# Blackboard

Sistema simplificado de Libro de Calificaciones que opera desde un comienzo en dos modos de presentación: modo consola y modo con interfaz gráfica. Implementa la gestión modular de un curso académico mediante el uso explícito de listas compuestas y pilas.

**Flujo:** Carga de archivos CSV → Evaluación de fórmulas con pilas → Generación de consultas y reportes dinámicos

---

## Stack Tecnológico

| Capa / Módulo | Tecnología |
| --- | --- |
| Lógica y Modelo | Java |
| Interfaz Gráfica | JavaFX |
| Persistencia | Archivos CSV y TXT |

---

## Módulos

El proyecto se estructura bajo un enfoque modular donde cada clase cumple una responsabilidad específica dentro del sistema.

* **Modelo**: Clases que representan las entidades principales del dominio (`Estudiante`, `Actividad`, `Entrega`, `Calculo`).
* **Estructuras**: Implementación de tipos genéricos con `ListaCompuesta<E, F>` y `NodoCompuesto<E, F>` para las relaciones de entidades y sus sublistas de entregas.
* **Coordinación y Lógica**: Clases dedicadas a la persistencia (`CargadorDatos`), ejecución de filtros mediante comparadores (`GestorConsultas`, `ServicioConsultas`) y procesamiento de fórmulas matemáticas (`GestorCalculos`, `EvaluadorCalculos`).
* **Presentación**: Capa gráfica desarrollada en JavaFX y sistema de menús en consola que comparten y reutilizan la misma lógica de negocio.

---

## Características Principales

| Módulo | Descripción |
| --- | --- |
| Carga de Datos | Lee archivos CSV de forma automatizada para construir las estructuras internas del sistema. |
| Evaluación de Cálculos | Procesa expresiones en notación posfija para cada estudiante mediante una pila. |
| Consultas | Filtra y realiza búsquedas de actividades, entregas y estudiantes usando múltiples comparadores. |
| Reportes | Genera tablas dinámicas de calificaciones con exportación directa a formato TXT y CSV. |

---

## Demostración

[Ver video de prueba de la aplicación en funcionamiento](https://drive.google.com/file/d/12ZgtqMgI5R5SIOCqYmEzz3WdIHWyEjGh/view?usp=drivesdk)

---

## Capturas de Pantalla

**Vista Principal**


---

## Ejecución

### Prerrequisitos

* JDK compatible con JavaFX
* Archivos de datos (`estudiantes.csv`, `actividades.csv`, `entregas.csv`, `calculos.csv`) en la raíz del directorio de ejecución

### Levantar la aplicación

```bash
java proyecto.Main

```

---

## Integrantes

| Integrante |
| --- |
| Karel González |
| Justin Soledispa |
| Juan Sotomayor |

---

*Proyecto académico — Estructura de Datos 1P · Universidad Espíritu Santo · 2026*
