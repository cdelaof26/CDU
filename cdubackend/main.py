import herramientas.utilidades as utilidades
import herramientas.recuadro as recuadro


import herramientas.definiciones as definiciones
# print(definiciones.convertir_unidades(definiciones.TDU.MASA, "kg", 1, "oz"))
# print(definiciones.convertir_unidades(definiciones.TDU.LONGITUD, "km", 1, "in"))
# print(definiciones.convertir_unidades(definiciones.TDU.TIEMPO, "d", 1, "ms"))

# print(definiciones.convertir_unidades(definiciones.TDU.MASA, "mg", 100, "oz"))
# print(definiciones.convertir_unidades(definiciones.TDU.TIEMPO, "w", 100, "d"))
# print(definiciones.convertir_unidades(definiciones.TDU.LONGITUD, "yd", 40, "m"))
# print(definiciones.convertir_unidades(definiciones.TDU.TIEMPO, "ms", 4000000000, "y"))
# print(definiciones.convertir_unidades(definiciones.TDU.MASA, "mg", 4000, "oz"))

# print(definiciones.convertir_unidades(definiciones.TDU.LONGITUD, "mm", 40000000, "mi"))
# print(definiciones.convertir_unidades(definiciones.TDU.LONGITUD, "mm", 40000000, "ft"))
# print(definiciones.convertir_unidades(definiciones.TDU.MASA, "lb", 100, "kg"))
# print(definiciones.convertir_unidades(definiciones.TDU.MASA, "mg", 10000, "kg"))

print(definiciones.convertir_unidades(definiciones.TDU.MASA, "g", 2, "kg"))

# WIP

exit()


def mostrar_menu():
    menu = recuadro.crear_recuadro(
        ["    Bienvenido a CDUP    ",
         "  Menu",
         "1. Convertir datos",
         "2. Crear definición",
         "P. Preferencias",
         "S. Salir"
         ]
    )

    while True:
        print(menu)
        print("Selecciona una opción")

        opcion = utilidades.seleccionar_opcion(["1", "2", "P", "S"])

        if opcion == "S":
            break

    print("Bye")


app_args = utilidades.encontrar_argumentos()

if not app_args:
    # Aplicación en Python (sin UI)
    mostrar_menu()
else:
    # Dado que es un proyecto "pequeño" que no creo compartir (y/o actualizar),
    # no tengo pensado documentarlo (sección de ayuda de cmd)
    modo_backend = False
    for flag, arg in app_args:
        if flag == "-m" and arg == "bd":
            modo_backend = True
            break

    if not modo_backend:
        print("Para ejecutar este software sin la interfaz gráfica utiliza:")
        print("  Linux/macOS")
        print("    python3 main.py")
        print("  Windows")
        print("    python main.py")
        exit(1)
