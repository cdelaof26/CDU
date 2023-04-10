import herramientas.utilidades as utilidades
import herramientas.recuadro as recuadro
import herramientas.definiciones as definiciones


def mostrar_menu():
    menu = recuadro.crear_recuadro(
        ["    Bienvenido a CDUP    ",
         "  Menu",
         "1. Convertir datos",
         # "2. Crear definición",
         # "P. Preferencias",
         "S. Salir"
         ]
    )

    while True:
        try:
            utilidades.limpiar_pantalla()

            print(menu)
            print("Selecciona una opción")

            opcion = utilidades.seleccionar_opcion(["1", "2", "P", "S"])

            if opcion == "1":
                resultado = definiciones.procesar_entrada()
                if resultado:
                    print(resultado)
                else:
                    print("    No fue posible convertir las unidades!")
                input("  Presiona enter para continuar ")

            if opcion == "S":
                break
        except (KeyboardInterrupt, ValueError) as e:
            print("\n    Proceso interrumpido!")
            print("Causa:", e, "\n")
            input("  Presiona enter para continuar ")

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
