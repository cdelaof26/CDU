import herramientas.conversiones as conversiones
import herramientas.utilidades as utilidades


# Utilidades para la interacción con las funciones del programa


app_args = utilidades.encontrar_argumentos()
FANCY_FORMAT = True

# Mini doc
#   Available flags
# -m:       Mode-Values: conv, man, get, get_t
# -in:      Input data
# -in_u:    Input unit
# -out_u:   Output unit
# -s:       Setting name
# -theme:   Theme-Values: get, toggle
# -rm:      Remove-Values: definition, all_data
# -add:     Add-Values: Data string: [type];[value][unit]=[value][unit]
# -id:      Index: integer value
#
#   Usage: python3 main.py -flag [data]
#
# Modes
#   conv: Convert mode
#        Flags: -in, -in_u, -out_u
#
#   man:  Manage mode
#        Flags: -theme, -add, -rm, -id
#
#   get:  Get definitions
#
#   get_t:  Get unit types
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


def obtener_nueva_definicion():
    global app_args

    # Se supone que ya se verificó que app_args contiene "-add"
    if "-add" not in app_args:
        print("BAD_CALL")
        exit(1)

    # Por problemas con las banderas, se deben cambiar los signos negativos por '~'

    tipo_datos = app_args["-add"].replace("~", "-").split(";")
    if len(tipo_datos) != 2:
        print("MISSING_DATA")
        exit(1)

    tipo: conversiones.TDUF

    try:
        tipo = conversiones.TDUF[tipo_datos[0]]
    except KeyError:
        print("UNK_TYPE")
        exit(1)

    datos = tipo_datos[1].split("=")

    if len(datos) != 2:
        print("INVALID_INPUT")
        exit(1)

    try:
        valor_entrada, unidad_entrada = conversiones.extraer_dato_unidad(datos[0])
    except KeyboardInterrupt:
        print("INVALID_INPUT_DATA")
        exit(1)

    try:
        valor_salida, unidad_salida = conversiones.extraer_dato_unidad(datos[1])
    except KeyboardInterrupt:
        print("INVALID_OUTPUT_DATA")
        exit(1)

    import herramientas.datos_del_app as dda

    dda.agregar_definicion(tipo, unidad_entrada, valor_entrada, unidad_salida, valor_salida)
    exit(0)


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

    import herramientas.datos_del_app as dda

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
                print("INVALID_IN_OUT_U")
                exit(1)
        else:
            resultado = conversiones.convertir_unidades(
                conversiones.determinar_tipo_de_unidad(datos[1]), datos[1], datos[0], datos[2], False, False
            )

        if resultado:
            print(resultado)
            exit(0)
        else:
            print("NO_DATA")
            exit(1)
    elif modo == "man":
        if "-theme" in app_args:
            if app_args["-theme"] == "toggle":
                dda.cambiar_tema()
                exit(0)
            elif app_args["-theme"] == "get":
                dda.obtener_tema_preferido()
                exit(0)

            print("NO_DATA")
            exit(1)

        if "-rm" in app_args:
            if app_args["-rm"] == "all_data":
                utilidades.eliminar_directorio(dda.DIRECTORIO_DE_DATOS)
                exit(0)
            if app_args["-rm"] == "definition":
                if "-id" in app_args:
                    try:
                        index = int(app_args["-id"]) + conversiones.DEFINICIONES_HARD_CODED
                        if 0 < index < len(conversiones.definiciones):
                            conversiones.definiciones.pop(index)
                            dda.guardar_definiciones()
                            exit(0)
                    except ValueError:
                        exit(1)

        if "-add" in app_args:
            obtener_nueva_definicion()

    elif modo == "get":
        for definicion in conversiones.definiciones:
            if definicion.definida_por_el_usuario:
                print(definicion.a_cadena(forzar_fancy=True))

        exit(0)
    elif modo == "get_t":
        for tipo in conversiones.TDUF:
            print(tipo.name)

        exit(0)

    exit(1)
