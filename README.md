# **SIGDOC – Sistema de Gestión de Docentes**

SIGDOC es un sistema web orientado a centralizar, organizar y administrar la información integral de los docentes de una facultad universitaria. Permite registrar datos personales, académicos, laborales y profesionales, con soporte para documentos digitales y auditoría completa.

---

## 🚀 Características Principales

### **1. Gestión de Información Personal**
- Datos de identificación.
- Correos y teléfonos.
- Dirección y ubigeo.
- Fotografía del docente.

### **2. Información Académica**
- Grados y títulos obtenidos.
- Institución de egreso.
- Especialidades y fechas.
- Documentos de respaldo.

### **3. Información Laboral**
- Categoría docente y régimen laboral.
- Departamento académico.
- Código institucional y correo institucional.
- Documentos de contrato o resolución.

### **4. Experiencia Profesional**
- Empleos previos y cargos desempeñados.
- Periodo y descripción.
- Documento de respaldo.

### **5. Capacitaciones**
- Cursos y talleres realizados.
- Institución, horas, créditos y fechas.
- Certificaciones en PDF.

### **6. Idiomas**
- Idioma y nivel de dominio.
- Institución certificadora.
- Documento de respaldo.

### **7. Gestión de Documentos**
- Repositorio central de PDFs e imágenes.
- Denominación, tipo y fecha de subida.

### **8. Tabla Maestra**
- Catálogo configurable para combos.
- Códigos, abreviaturas y denominaciones.

### **9. Auditoría**
- Campos de creación y actualización.
- Eliminación lógica en todas las tablas.

---

## 🗂️ Estructura de la Base de Datos (Resumen)

- `TablaMaestra`
- `Docente`
- `DocenteAcademico`
- `DocenteLaboral`
- `DocenteIdioma`
- `ExperienciaProfesional`
- `Capacitacion`
- `Documento`

Extensible con:
- `Facultad`
- `DepartamentoAcademico`
- `Usuario`, `Rol`, `Permiso`

---

## 🏛️ Arquitectura del Sistema

- **Base de datos:** PostgreSQL  
- **Backend:** API REST  
- **Frontend:** Web modular e intuitiva  
- **Diseño:** Escalable y orientado a módulos  

---

## 🎯 Objetivo

Optimizar la gestión de información docente, reduciendo tareas manuales y asegurando trazabilidad y confiabilidad de los datos institucionales.

