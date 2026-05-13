# Blackboard

Sistema simplificado de Libro de Calificaciones para la gestión modular de un curso mediante el uso explícito de listas compuestas y pilas.

**Flujo:** Carga de archivos CSV → Evaluación de fórmulas con pilas → Generación de consultas y reportes dinámicos

---

## Stack Tecnológico

| Capa / Módulo | Tecnología |
| --- | --- |
| Lógica y Modelo | Java |
| Estructuras de Datos | Colecciones nativas e implementaciones de TDA propios |
| Interfaz Gráfica | JavaFX |
| Persistencia | Archivos planos CSV y TXT |

---

## Módulos

El sistema se estructura bajo un enfoque netamente modular donde cada clase cumple una responsabilidad específica dentro del dominio.

* **Modelo de Dominio**: Entidades que representan la estructura académica central (`Estudiante`, `Actividad`, `Entrega`, `Calculo`).
* **Estructuras de Datos**: Implementación del TDA `ListaCompuesta<E, F>` y `NodoCompuesto<E, F>` genéricos para manejar relaciones principales con sublistas secundarias sin duplicación de código.
* **Procesamiento y Lógica**: Clases coordinadoras encargadas de la persistencia (`CargadorDatos`), filtrado de datos mediante múltiples comparadores (`GestorConsultas`, `ServicioConsultas`) y evaluación matemática de fórmulas en notación posfija (`GestorCalculos`, `EvaluadorCalculos`).
* **Capa de Presentación**: Arquitectura dual que reutiliza el núcleo de negocio para interactuar a través de consola (`SistemaConsola`) o de ventanas gráficas en JavaFX.

---

## Características Principales

| Módulo | Descripción |
| --- | --- |
| Carga de Datos | Construye las estructuras internas leyendo de forma automatizada los archivos CSV iniciales. |
| Evaluación de Cálculos | Procesa fórmulas agregadas en notación posfija utilizando una pila implementada con `ArrayDeque`. |
| Consultas Estratégicas | Ejecuta búsquedas y filtrados flexibles delegando los criterios a comparadores especializados. |
| Reportes Dinámicos | Genera tablas personalizadas exportables a TXT o CSV basándose en la selección de actividades y cálculos. |
| Presentación Dual | Permite operar el sistema de manera idéntica e independiente desde la consola o una interfaz gráfica. |

---

## Capturas de Pantalla

*Próximamente*

---

## Ejecución

### Prerrequisitos

* Java Development Kit (JDK) con soporte para JavaFX.
* Archivos de datos iniciales (`estudiantes.csv`, `actividades.csv`, `entregas.csv`, `calculos.csv`) disponibles en el directorio de ejecución.

### Levantar la aplicación

```bash
# Ejecutar la clase principal del sistema
java proyecto.Main

```

> **Nota:** Al iniciar la ejecución se solicitará elegir interactivamente entre el Modo Consola (opción 1) o el Modo Interfaz Gráfica (opción 2).

---

## Integrantes
| Karel González | Autor |
| Justin Soledispa | Autor |
| Juan Diego Sotomayor | Autor |

---

*Proyecto académico — Estructura de Datos · Universidad Espíritu Santo · 2026*
