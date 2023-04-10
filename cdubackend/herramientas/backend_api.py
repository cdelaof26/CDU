import herramientas.conversiones as conversiones
import herramientas.utilidades as utilidades


# Utilidades para la interacción con las funciones del programa


app_args = utilidades.encontrar_argumentos()
FANCY_FORMAT = True

# Available flags
# -m:       Mode-Values: conv, man, add, rm
# -in:      Input data
# -in_u:    Input unit
# -out_u:   Output unit
# -s:       Setting name; Compatible with man, add and rm
#
#   Usage: python3 main.py -flag [data]
#
# conv: Convert mode
# man:  Manage mode
# add:  Add definition mode
# rm:   Remove definition mode
#


def encontrar_modo() -> str:
    global app_args

    try:
        return app_args["-m"]
    except KeyError:
        return ""


def obtener_datos_de_conversion() -> list:
    global app_args

    datos = list()

    try:
        datos.append(float(app_args["-in"]))
        datos.append(app_args["-in_u"])
        datos.append(app_args["-out_u"])
    except KeyError as e:
        print(f"MISSING_DATA_{e}")
        exit(1)
    except ValueError as e:
        print(str(e).replace(' ', '_').upper())
        exit(1)

    return datos


def ejecutar_funcion():
    global app_args, FANCY_FORMAT

    FANCY_FORMAT = False

    modo = encontrar_modo()

    # Dado que es un proyecto "pequeño" que no creo mantener,
    # no tengo pensado documentarlo (sección de ayuda de cmd)
    #
    if not modo:
        # Suficiente información para alguien curioso, supongo...
        print("[DEV] Call without -m flag, please refer to backend_api.py if you need to use CDU with other frontend")
        print()
        print("Para ejecutar este software sin la interfaz gráfica utiliza:")
        print("  Linux/macOS")
        print("    python3 main.py")
        print("  Windows")
        print("    python main.py")
        exit(1)

    if modo == "conv":
        # Los datos vienen como
        #   0. Valor
        #   1. Unidad
        #   2. Unidad a convertir
        #
        datos = obtener_datos_de_conversion()

        if "/" in datos[1]:
            try:
                resultado = conversiones.convertir_unidades_complejas(datos[1], datos[0], datos[2])
            except ValueError:
                print("INVALID_OUT_U")
                exit(1)
        else:
            resultado = conversiones.convertir_unidades(
                conversiones.determinar_tipo_de_unidad(datos[1]), datos[1], datos[0], datos[2], False
            )

        if resultado:
            print(resultado)
            exit(0)
        else:
            print("NO_DATA")
            exit(1)
    elif modo == "man":
        pass
    elif modo == "add":
        pass
    elif modo == "rm":
        pass
