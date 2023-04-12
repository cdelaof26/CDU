import herramientas.conversiones as conversiones
import herramientas.utilidades as utilidades
import herramientas.recuadro as recuadro
from pathlib import Path
import re

# Herramientas para el manejo de las preferencias del app

DIRECTORIO_DE_DATOS = Path.home().joinpath(".cdu_data")
ARCHIVO_DE_DEFINICIONES = DIRECTORIO_DE_DATOS.joinpath("user_constants.txt")
USAR_TEMA_OSCURO = DIRECTORIO_DE_DATOS.joinpath("use_dark")


def importar_definiciones():
    global ARCHIVO_DE_DEFINICIONES

    definiciones = utilidades.leer_archivo(ARCHIVO_DE_DEFINICIONES).split("\n")

    for definicion in definiciones:
        if definicion.count('"') == 4 and re.sub(r"Definicion\(TDUF.+\)", "", definicion) == "":
            definicion = "conversiones." + definicion.replace("TDUF", "conversiones.TDUF")
            definicion = definicion.replace(")", ", True)")
            exec(f"conversiones.definiciones.append({definicion})")


def guardar_definiciones():
    definiciones = ""
    for definicion in conversiones.definiciones:
        if definicion.definida_por_el_usuario:
            definiciones += definicion.a_cadena(True) + "\n"

    utilidades.escribir_archivo(ARCHIVO_DE_DEFINICIONES, definiciones[:-1])


def agregar_definicion(tipo: conversiones.TDUF, unidad: str, valor: float, unidad_equivalente: str,
                       valor_equivalente: float):
    global DIRECTORIO_DE_DATOS, ARCHIVO_DE_DEFINICIONES

    if not DIRECTORIO_DE_DATOS.exists():
        DIRECTORIO_DE_DATOS.mkdir()

    conversiones.definiciones.append(
        conversiones.Definicion(tipo, unidad, valor, unidad_equivalente, valor_equivalente, True)
    )

    guardar_definiciones()


def crear_definicion():
    # Esta función solo es para la interfaz CLI

    utilidades.limpiar_pantalla()
    print("  Nueva definición")
    opciones = list()
    for i, tipo in enumerate(conversiones.TDUF):
        opciones.append(f"{i + 1}")
        print(f"{i + 1}. {tipo.name}")

    print("Selecciona de que tipo sera la equivalencia")
    seleccion = utilidades.seleccionar_opcion(opciones, list(conversiones.TDUF))

    valor, unidad = conversiones.obtener_dato_unidad("Ingresa el valor seguido de la unidad (ej. 1km)")
    valor_equivalente, unidad_equivalente = conversiones.obtener_dato_unidad(
        "Ingresa el valor equivalente seguido de la unidad"
    )

    agregar_definicion(seleccion, unidad, valor, unidad_equivalente, valor_equivalente)


def eliminar_definicion(definicion: str):
    try:
        conversiones.definiciones.remove(definicion)
        guardar_definiciones()
    except ValueError:
        pass


def mostrar_menu_de_configuracion():
    global DIRECTORIO_DE_DATOS

    menu = recuadro.crear_recuadro(
        ["  Ajustes    ",
         "1. Eliminar definición",
         "2. Eliminar todos los datos de configuración",
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
                utilidades.limpiar_pantalla()

                opciones = ["0"]
                definiciones_eliminables = ["Cancelar"]
                i = 0
                print("0. Cancelar")
                for definicion in conversiones.definiciones:
                    if definicion.definida_por_el_usuario:
                        definiciones_eliminables.append(definicion)
                        opciones.append(f"{i + 1}")
                        print(f"{i + 1}. {definicion.a_cadena()}")

                        i += 1
                print("Selecciona la definición a eliminar")
                seleccion = utilidades.seleccionar_opcion(opciones, definiciones_eliminables)
                if isinstance(seleccion, str):
                    continue

                eliminar_definicion(seleccion)

            if opcion == "2":
                utilidades.limpiar_pantalla()

                if not DIRECTORIO_DE_DATOS.exists():
                    print("No hay datos")
                    input("  Presiona enter para continuar ")
                    continue

                print("¿Estás seguro?")
                print("1. Si, eliminar todos los datos de configuración")
                print("2. No, cancelar")
                print("Selecciona una opción")
                if utilidades.seleccionar_opcion(["1", "2"], [True, False]):
                    utilidades.eliminar_directorio(DIRECTORIO_DE_DATOS)

            if opcion == "S":
                break
        except (KeyboardInterrupt, ValueError) as e:
            print("\n    Proceso interrumpido!")
            if str(e):
                print("Causa:", e, "\n")
            input("  Presiona enter para continuar ")


def cambiar_tema():
    global USAR_TEMA_OSCURO

    if USAR_TEMA_OSCURO.exists():
        USAR_TEMA_OSCURO.unlink()
    else:
        USAR_TEMA_OSCURO.touch()


def obtener_tema_preferido():
    global USAR_TEMA_OSCURO

    if USAR_TEMA_OSCURO.exists():
        print("Dark")
    else:
        print("Light")
