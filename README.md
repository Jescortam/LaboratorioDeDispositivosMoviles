# Imprenta Express
Imprenta Express es una aplicación móvil que permite el manejo de productos en un almacén. Incluye funcionalidad de códigos QR para poder buscar y modificar los detalles de estos consumibles.

## Manual de usuario
### Inicio de sesión
Esta es la primera pantalla de la aplicación móvil, en esta pantalla encontraremos dos campos rellenables, el primer campo contiene el correo electrónico del usuario, 
el segundo campo contendrá la contraseña del usuario, al rellenar estos dos campos correctamente con información de una cuenta válida, presionamos el botón de ingresar 
para continuar a nuestra pantalla de Inventario. Si las credenciales son inválidas, en la parte inferior de la pantalla habrá un mensaje que invite al usuario a intentarlo 
nuevamente. Una vez sean válidas, se enviará al usuario a la vista de Inventario.  

![Iniciar sesión](https://github.com/Jescortam/imprenta-express/assets/69122617/feaa1ebf-ed52-4ca2-bf74-543c23d0d62e)

### Inventario 
En esta vista tiene cuatro botones arriba, de izquierda a derecha, el primero sirve para ir a Agregar producto, el segundo es para ver nuestro producto utilizando un código 
QR, el tercero nos sirve para vender una unidad de producto, y el cuarto botón nos sirve para cerrar sesión. Debajo de estos botones encontraremos una lista de productos 
en la cual encontramos los consumibles de nuestro almacén con los datos de: nombre, cantidad, precio, tipo, observaciones y su imagen. Esta lista de consumibles se actualiza 
en tiempo real, y al hacer clic sobre algún consumible de la lista nos enviará a su menú como vista “ver producto”.  

![Inventario](https://github.com/Jescortam/imprenta-express/assets/69122617/00cfd505-2d7e-43f4-9c29-261aa151d74b)

### Agregar consumible
En esta vista registramos los datos del consumible que queremos agregar, al terminar de ingresar la información requerida, podemos presionar siguiente para continuar a 
“Agregar imagen”. Si queremos regresar al inventario, simplemente seleccionamos el botón de “salir”. Es importante tener en cuenta que el valor de cantidad ingresado en 
esta pantalla será la cantidad inicial de consumible que tendremos en nuestro inventario, y esta podrá ser modificada posteriormente en nuestra aplicación para añadir o 
restarle unidades.  

![Agregar consumible](https://github.com/Jescortam/imprenta-express/assets/69122617/c453a1b6-1c5c-4176-9799-f2b25c060d4b)

### Agregar imagen
En esta pantalla podemos primeramente seleccionar dos botones, el primero que lee “CÁMARA”, nos habilitará la cámara de nuestro dispositivo para tomar una foto. El segundo 
botón (“GALERÍA”) al ser seleccionado nos habilita la galería de fotos para poder seleccionar una de estas y usarla como la fotografía designada de nuestro consumible. Una 
vez hayamos escogido una fotografía, ésta aparecerá en el cuadro blanco. Si se quiere regresar a la pantalla anterior, se selecciona el botón “ATRÁS”, si estamos satisfechos 
con la fotografía seleccionada, entonces podremos seleccionar nuestro botón de “AGREGAR” para regresar a nuestra pantalla del inventario.
Es importante saber que al seleccionar el botón de “AGREGAR” es normal que nuestro sistema tarde unos segundos procesando para seguir avanzando, esto es debido a nuestras 
bases de datos siendo actualizadas.  

![image](https://github.com/Jescortam/imprenta-express/assets/69122617/2d8c2792-3b41-4a8b-a346-6a88bd2fe711)

### Ver producto
Esta vista nos permite ver a detalle los datos de un consumible. Esta vista también tiene distintos botones. El primer botón que nos encontramos es “OBTENER QR” que nos 
despliega nuestro código QR para poder descargarlo si lo vemos necesario. El botón de “MOVIMIENTOS” nos enviará a su propia pantalla. Los botones de editar detalles y editar 
imagen nos enviarán de vuelta a un menú parecido a agregar consumible, pero con el propósito de cambiar los atributos que le hayamos asignado a esta. Finalmente tenemos los 
botones de salir, que te devuelve a la pantalla de inventario, y eliminar, que elimina nuestro consumible.  

![Ver producto](https://github.com/Jescortam/imprenta-express/assets/69122617/d21b65cf-c06b-492c-a8e1-e874557aa4cb)

### Obtener QR
En esta vista podremos observar nuestro código QR de consumible, el cual podremos descargar o tomar screenshot para poder usarlo como queramos.  

![Obtener QR](https://github.com/Jescortam/imprenta-express/assets/69122617/f0e113a6-a021-4017-83d1-20d81d028a1a)


### Movimientos
La pantalla de movimientos, esta vista mostrará los elementos restantes de nuestro producto, tendrá la función de agregar o restar unidades según se requiera. Se debe de 
ingresar un número para luego seleccionar el botón de “Agregar”, o “Restar”, según se necesite. Al realizar estas operaciones generamos una nueva entrada de movimiento, 
la cual es registrada e informada en la misma pantalla como una lista que incluye la fecha y la hora del movimiento, la cantidad de consumibles, y el tipo de cambio que se 
hizo, en este caso si es de entrada o salida. Si las unidades son cero o son negativas, nuestro código no permite que se realice la operación.  

![Movimientos](https://github.com/Jescortam/imprenta-express/assets/69122617/fc4f327b-257e-4954-83cd-200e826a7ed2)

### Editar información
Esta vista se puede sobrescribir todos los valores que se ingresaron en la vista de “agregar consumible”, con la excepción de la cantidad, debido a que esta solo se modifica 
mediante la pantalla de Movimientos.  

![Editar información](https://github.com/Jescortam/imprenta-express/assets/69122617/d8cb6290-048f-44af-9ec3-7365311de038)

### Editar imagen
En esta vista podremos volver a seleccionar una fotografía para utilizar como indicador de nuestro objeto, en este caso no tenemos un cuadro vacío donde seleccionaremos 
nuestra imagen, ahora tendremos nuestra imagen actual en el cuadro y podremos utilizar de nuevo la galería o la cámara para volver a definir nuestra imagen de consumible.  

![Editar imagen](https://github.com/Jescortam/imprenta-express/assets/69122617/0a182407-a414-4205-8d8d-08c8ac881c3b)

### Escáner
Nuestro escáner tiene la siguiente vista, y es para leer nuestros códigos QR que contengan los códigos de nuestros productos, y que también nos permite realizar la venta 
de una unidad de un consumible.  

![Escáner](https://github.com/Jescortam/imprenta-express/assets/69122617/f24528b1-863b-49f1-8f8d-a214eeb0ec10)

### Eliminar
Al hacer clic en el botón de eliminar nos aparecerá un diálogo para confirmar la selección, podremos cancelar o volver a seleccionar eliminar para borrar nuestro producto.  

![Eliminar](https://github.com/Jescortam/imprenta-express/assets/69122617/eb00f0f9-4dba-435a-837f-91aa84ba6e3d)
