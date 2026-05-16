# Blackboard - Sistema de Libro de Calificaciones

## Descripción General

Este proyecto consiste en un sistema académico tipo **Blackboard** desarrollado en **Java** para la gestión de calificaciones, actividades, entregas, consultas y reportes. La aplicación permite cargar información desde archivos CSV, procesar datos mediante estructuras de datos personalizadas y generar reportes de calificaciones en formato TXT o CSV.

El sistema puede ejecutarse en dos modalidades:

- **Modo consola**, mediante menús textuales.
- **Modo gráfico**, mediante una interfaz de escritorio desarrollada con **JavaFX**.

El proyecto fue desarrollado para la asignatura **Estructura de Datos** y aplica conceptos como listas compuestas, nodos genéricos, comparadores, pilas y manejo de archivos.

## Objetivo del Proyecto

El objetivo principal del proyecto es implementar un sistema de libro de calificaciones que permita administrar información académica mediante estructuras de datos propias.

El sistema permite:

- Cargar estudiantes desde archivos CSV.
- Cargar actividades académicas.
- Registrar y consultar entregas.
- Evaluar cálculos de calificaciones mediante fórmulas en notación posfija.
- Realizar consultas sobre actividades, entregas, estudiantes y cálculos.
- Crear actividades manualmente.
- Generar reportes personalizados de calificaciones.
- Exportar reportes en formato TXT y CSV.
- Usar la misma lógica principal tanto en consola como en interfaz gráfica.

## Tecnologías Utilizadas

- Java
- JavaFX
- Programación orientada a objetos
- Archivos CSV
- Archivos TXT
- Estructuras de datos personalizadas:
  - Lista compuesta genérica
  - Nodo compuesto genérico
  - Pila para evaluación de expresiones
- Comparadores mediante `Comparator`
- IntelliJ IDEA

## Arquitectura General

El proyecto separa la lógica del sistema, las estructuras de datos, los comparadores, el manejo de archivos y la interfaz gráfica. La clase `Main` permite elegir entre el modo consola y el modo gráfico.

```text
Usuario
│
├── Modo Consola
│   └── SistemaConsola
│       ├── GestorConsultas
│       ├── GestorReportes
│       └── GestorCalculos
│
└── Interfaz Gráfica JavaFX
    └── App / VistaBienvenida
        ├── ModuloConsulta
        ├── ModuloReporte
        └── ModuloCrearActividad

Núcleo del sistema
│
├── CargadorDatos
├── ServicioConsultas
├── EvaluadorCalculos
├── ListaCompuesta
└── NodoCompuesto
```

## Estructura del Repositorio

```text
proyecto_edd_blackboard/
│
├── README.md
│
├── app/
│   ├── Blackboard.iml
│   ├── actividades.csv
│   ├── calculos.csv
│   ├── entregas.csv
│   ├── estudiantes.csv
│   ├── reporte_calificaciones.csv
│   ├── reporte_calificaciones.txt
│   │
│   ├── src/
│   │   └── proyecto/
│   │       ├── Actividad.java
│   │       ├── Calculo.java
│   │       ├── CargadorDatos.java
│   │       ├── Entrega.java
│   │       ├── Estudiante.java
│   │       ├── EvaluadorCalculos.java
│   │       ├── GestorCalculos.java
│   │       ├── GestorConsultas.java
│   │       ├── GestorReportes.java
│   │       ├── ListaCompuesta.java
│   │       ├── Main.java
│   │       ├── NodoCompuesto.java
│   │       ├── ServicioConsultas.java
│   │       ├── SistemaConsola.java
│   │       │
│   │       ├── comparadores/
│   │       │   ├── ComparadorActividadPorFechaLimite.java
│   │       │   ├── ComparadorEntregaNoCalificada.java
│   │       │   ├── ComparadorEntregaPorActividad.java
│   │       │   ├── ComparadorEntregaPorActividadId.java
│   │       │   ├── ComparadorEntregaPorCalificacion.java
│   │       │   ├── ComparadorEntregaPorContenido.java
│   │       │   ├── ComparadorEntregaPorFechaLimite.java
│   │       │   ├── ComparadorEntregaPorLongitudContenido.java
│   │       │   ├── ComparadorEstudiantePorCalculo.java
│   │       │   └── CompararadorFechaLimite.java
│   │       │
│   │       └── interfaz/
│   │           ├── App.java
│   │           ├── ExportadorReporte.java
│   │           ├── FXListUtils.java
│   │           ├── ModeloDatosFX.java
│   │           ├── ModuloConsulta.java
│   │           ├── ModuloCrearActividad.java
│   │           ├── ModuloReporte.java
│   │           └── VistaBienvenida.java
│   │
│   └── out/
│
├── csv/
│   ├── actividades.csv
│   ├── calculos.csv
│   ├── entregas.csv
│   ├── estudiantes.csv
│   ├── reporte_calificaciones.csv
│   └── reporte_calificaciones.txt
│
└── docs/
    ├── Consigna - Proyecto P1.pdf
    └── Gonzalez-Soledispa-Sotomayor-DOCUMENTO DE DISEÑO.pdf
```

## Principales Clases del Proyecto

| Clase | Descripción |
|---|---|
| `Main` | Clase principal. Permite seleccionar entre modo consola e interfaz gráfica. |
| `SistemaConsola` | Controla el menú principal en consola. |
| `CargadorDatos` | Carga estudiantes, actividades, entregas y cálculos desde archivos CSV. |
| `ListaCompuesta` | Implementa una estructura genérica con nodos principales y listas secundarias. |
| `NodoCompuesto` | Representa cada nodo de la lista compuesta. |
| `Estudiante` | Modelo que representa a un estudiante. |
| `Actividad` | Modelo que representa una actividad académica. |
| `Entrega` | Modelo que representa la entrega de una actividad por parte de un estudiante. |
| `Calculo` | Modelo que representa una fórmula de calificación. |
| `EvaluadorCalculos` | Evalúa fórmulas en notación posfija mediante una pila. |
| `GestorCalculos` | Gestiona consultas relacionadas con cálculos ejecutables y actividades involucradas. |
| `GestorConsultas` | Controla el menú de consultas en modo consola. |
| `ServicioConsultas` | Ejecuta la lógica de búsqueda y filtrado sobre las estructuras del sistema. |
| `GestorReportes` | Genera reportes personalizados de calificaciones. |
| `App` | Inicializa la aplicación JavaFX. |
| `VistaBienvenida` | Pantalla principal de la interfaz gráfica. |
| `ModuloConsulta` | Módulo gráfico para ejecutar consultas. |
| `ModuloReporte` | Módulo gráfico para generar reportes. |
| `ModuloCrearActividad` | Módulo gráfico para registrar nuevas actividades. |

## Requisitos Previos

Para ejecutar el proyecto se necesita:

- JDK 11 o superior.
- JavaFX SDK compatible con la versión de JDK instalada.
- IntelliJ IDEA u otro IDE compatible con Java.
- Git, en caso de clonar el repositorio desde GitHub.

## Instalación

Clonar el repositorio:

```bash
git clone https://github.com/juandifrost17/proyecto_edd_blackboard.git
```

Ingresar al proyecto:

```bash
cd proyecto_edd_blackboard
```

La aplicación se encuentra dentro de la carpeta:

```bash
app/
```

## Ejecución del Proyecto

La clase principal del proyecto es:

```text
proyecto.Main
```

Al ejecutar esta clase, el sistema permite seleccionar el modo de uso:

```text
1. Modo Consola
2. Modo Interfaz Gráfica
```

### Ejecución en IntelliJ IDEA

1. Abrir el proyecto en IntelliJ IDEA.
2. Configurar un JDK compatible.
3. Agregar JavaFX SDK como librería del proyecto.
4. Configurar las opciones de VM para JavaFX:

```bash
--module-path /ruta/al/javafx-sdk/lib --add-modules javafx.controls
```

5. Ejecutar la clase:

```text
proyecto.Main
```

Para que la carga automática funcione correctamente, los archivos CSV deben estar disponibles en el directorio de ejecución. En este proyecto se encuentran dentro de la carpeta `app`.

### Ejecución desde Terminal

Desde la carpeta `app`:

```bash
cd app
```

Compilar el proyecto:

```bash
mkdir -p out/classes
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls -d out/classes $(find src -name "*.java")
```

Ejecutar la aplicación:

```bash
java --module-path "$PATH_TO_FX" --add-modules javafx.controls -cp out/classes proyecto.Main
```

En este caso, `PATH_TO_FX` debe apuntar a la carpeta `lib` del JavaFX SDK.

## Funcionalidades Principales

### Modo Consola

El modo consola permite acceder a las siguientes opciones principales:

```text
1. Consultas
2. Reporte de Calificaciones
3. Crear Actividad Manualmente
exit. Salir del programa
```

### Consultas Disponibles

El sistema incluye consultas sobre actividades, entregas, estudiantes y cálculos:

- Actividades cuya fecha límite ya venció.
- Actividades con entregas incompletas según cantidad de caracteres.
- Actividades con calificaciones menores a un valor dado.
- Entregas enviadas después de cierta fecha y aún no calificadas.
- Estudiantes con porcentaje de entregas mayor a un valor dado.
- Estudiantes que no han respondido actividades vencidas.
- Estudiantes que tienen la misma nota en dos actividades diferentes.
- Cálculos que no se pueden ejecutar por falta de calificaciones.
- Cálculos que involucran una actividad específica.
- Ordenamiento de estudiantes según un cálculo definido.

### Interfaz Gráfica

La interfaz gráfica desarrollada con JavaFX incluye módulos para:

- Ejecutar consultas.
- Crear actividades.
- Generar reportes personalizados.
- Descargar reportes en formato TXT o CSV.

## Manejo de Archivos CSV

El proyecto utiliza archivos CSV como fuente de datos para estudiantes, actividades, entregas y cálculos.

### `estudiantes.csv`

```csv
id,nombre,apellido,edad,email
```

Ejemplo:

```csv
E001,Justin,Soledispa,20,just@univ.edu
```

### `actividades.csv`

```csv
id,nombre,fecha_limite,tipo
```

Ejemplo:

```csv
A001,Tarea 1,2026-02-07,Tarea
```

### `entregas.csv`

```csv
id_estudiante,id_actividad,contenido,calificacion,fecha_entrega,calificada
```

Ejemplo:

```csv
E001,A001,"Solución completa",85,2026-02-06T20:00,true
```

### `calculos.csv`

```csv
id,nombre,formulaPosfija
```

Ejemplo:

```csv
C3,Nota Final Curso,Tarea 0.3 * Proyecto 0.3 * + Examen 0.4 * +
```

Los archivos de ejemplo están disponibles en:

```text
app/
csv/
```

## Reportes

El sistema permite generar reportes de calificaciones a partir de actividades y cálculos seleccionados por el usuario.

Los reportes pueden visualizarse en pantalla y exportarse en los siguientes formatos:

- TXT
- CSV

El repositorio incluye ejemplos de reportes generados:

```text
app/reporte_calificaciones.txt
app/reporte_calificaciones.csv
csv/reporte_calificaciones.txt
csv/reporte_calificaciones.csv
```

## Video Demostrativo

Puedes consultar el video demostrativo del proyecto en el siguiente enlace:

[Ver video en Google Drive](https://drive.google.com/file/d/12ZgtqMgI5R5SIOCqYmEzz3WdIHWyEjGh/view?usp=drivesdk)

## Integrantes del Grupo

- Karel González
- Justin Soledispa
- Juan Diego Sotomayor
