# Hibernate

La aplicación una vez inicia crea automaticamente las tablas. Por la configuración utilizada en el persistence.xml, cada
vez que se ejecuta también borra las tablas anteriores junto a sus datos.

Las tablas no se poblan automaticamente, debes poblarlas tu com una de las opciones que proporciona el programa.
Debido a estas configuraciones, si borras una tabla, para volver a crearla debes reiniciar el programa.

## Datos importantes

No se han probado todas las posibilidades, pues, es posible que alguna un tanto rebuscada pueda causar un error a la hora
de borrar o realizar un select.

Las relaciones son: **Muchos a 1 (Muchas partidas pueden tener un mapa) y Muchos a Muchos (Muchos jugadores pueden formar
parte de muchas partidas)**



