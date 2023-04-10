import herramientas.utilidades as utilidades
import herramientas.recuadro as recuadro
import herramientas.conversiones as conversiones
import herramientas.backend_api as b_api
import herramientas.datos_del_app as dda


def mostrar_menu():
    menu = recuadro.crear_recuadro(
        ["    Bienvenido a CDUP    ",
         "  Menu",
         "1. Convertir datos",
         "2. Crear definición",
         "P. Preferencias",
         "S. Salir"]
    )

    while True:
        try:
            utilidades.limpiar_pantalla()

            print(menu)
            print("Selecciona una opción")

            opcion = utilidades.seleccionar_opcion(["1", "2", "P", "S"])

            if opcion == "1":
                resultado = conversiones.procesar_entrada()
                if resultado:
                    print(resultado)
                else:
                    print("    No fue posible convertir las unidades!")
                input("  Presiona enter para continuar ")

            if opcion == "2":
                dda.crear_definicion()

            if opcion == "P":
                dda.mostrar_menu_de_configuracion()

            if opcion == "S":
                break
        except (KeyboardInterrupt, ValueError) as e:
            print("\n    Proceso interrumpido!")
            if str(e):
                print("Causa:", e, "\n")
            input("  Presiona enter para continuar ")

    print("Bye")


dda.importar_definiciones()

if not b_api.app_args:
    # Aplicación en Python (sin UI)
    mostrar_menu()
else:
    # Las opciones disponibles se encuentran en backend_api.py
    b_api.ejecutar_funcion()
