import herramientas.utilidades as utilidades
from enum import Enum
import re


# Utilidades para el manejo de definiciones


class TDU(Enum):  # Tipos De Unidades
    MASA = 1
    LONGITUD = 2
    TIEMPO = 3


# Las definiciones son objetos que tienen cuatro atributos
# y permiten al programa "reconocer" cuáles son las equivalencias
# entre unidades predefinidas y creadas por el usuario
#
#   Por ejemplo:
#           d = Definicion(TDU.TIEMPO, "d", 1, "min", 1440)
#       El objeto 'd' sería la representación de la
#       equivalencia de tiempo "1 día = 1440 minutos"
#
class Definicion:
    def __init__(self, tipo: TDU, unidad: str, valor_en_unidad: float, unidad_equivalente: str,
                 valor_en_unidad_equivalente: float):
        self.tipo = tipo
        self.unidad = unidad
        self.valor_en_unidad = valor_en_unidad
        self.unidad_equivalente = unidad_equivalente
        self.valor_en_unidad_equivalente = valor_en_unidad_equivalente


# Definiciones elementales
definiciones = [
    # Unidades de longitud
    Definicion(TDU.LONGITUD, "km", 1, "m", 1000),
    Definicion(TDU.LONGITUD, "m", 1, "cm", 100),
    Definicion(TDU.LONGITUD, "cm", 1, "mm", 10),
    #  Sistema anglosajón
    Definicion(TDU.LONGITUD, "mi", 1, "km", 1.609344),
    Definicion(TDU.LONGITUD, "ft", 1, "m", 0.3048),
    Definicion(TDU.LONGITUD, "ft", 1, "cm", 30.48),
    Definicion(TDU.LONGITUD, "yd", 1, "ft", 3),
    Definicion(TDU.LONGITUD, "yd", 1, "cm", 91.44),
    Definicion(TDU.LONGITUD, "in", 1, "cm", 2.54),

    # Unidades de masa
    Definicion(TDU.MASA, "kg", 1, "g", 1000),
    Definicion(TDU.MASA, "g", 1, "mg", 1000),
    #  Sistema anglosajón
    Definicion(TDU.MASA, "oz", 1, "g", 28.3495),
    Definicion(TDU.MASA, "lb", 1, "g", 453.5923),

    # Unidades de tiempo
    Definicion(TDU.TIEMPO, "y", 1, "d", 365),
    Definicion(TDU.TIEMPO, "w", 1, "d", 7),
    Definicion(TDU.TIEMPO, "d", 1, "h", 24),
    Definicion(TDU.TIEMPO, "h", 1, "min", 60),
    Definicion(TDU.TIEMPO, "min", 1, "s", 60),
    Definicion(TDU.TIEMPO, "s", 1, "ms", 1000)
]


def enlistar_unidades_de(tipo: TDU) -> list:
    global definiciones

    unidades = list()

    for definicion in definiciones:
        if definicion.tipo == tipo:
            if definicion.unidad not in unidades:
                unidades.append(definicion.unidad)

            if definicion.unidad_equivalente not in unidades:
                unidades.append(definicion.unidad_equivalente)

    return unidades


def determinar_tipo_de_unidad(unidad: str):
    global definiciones

    for definicion in definiciones:
        if unidad == definicion.unidad or unidad == definicion.unidad_equivalente:
            return definicion.tipo

    raise ValueError("Unidad desconocida")


def permutar_unidades(tipo: TDU, longitud_del_elemento: int, unidad_inicial: str, unidad_final: str) -> list:
    unidades0 = enlistar_unidades_de(tipo)
    try:
        unidades0.remove(unidad_inicial)
        unidades0.remove(unidad_final)
    except ValueError:
        return []

    longitud_del_elemento -= 2

    permutaciones = list()
    script = ""
    estructura_for = "for c%a in unidades%a:"
    estructura_rem = "unidades%a = utilidades.quitar_elemento_en_lista(unidades%a, c%a)"

    indentado = ""
    variables = list()

    for i in range(longitud_del_elemento):
        script += indentado + estructura_for % (i, i) + "\n"
        indentado += "    "
        if i == 0:
            variables.append(f"str(c{i})")
        else:
            variables.append(f"' ' + str(c{i})")
        if i + 1 < longitud_del_elemento:
            script += indentado + estructura_rem % (i + 1, i, i) + "\n"

    script += indentado + "permutaciones.append((\"" + unidad_inicial + " \" + " + " + ".join(variables) + " + \" " + \
              unidad_final + "\").split(\" \"))"

    exec(script)

    return permutaciones


def convertir_directo(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str):
    for definicion in definiciones:
        if tipo == definicion.tipo:
            if unidad == definicion.unidad and unidad_a_convertir == definicion.unidad_equivalente:
                return valor / definicion.valor_en_unidad * definicion.valor_en_unidad_equivalente
            if unidad == definicion.unidad_equivalente and unidad_a_convertir == definicion.unidad:
                return valor * definicion.valor_en_unidad / definicion.valor_en_unidad_equivalente

    return None


def expresar_conversion_directa(tipo: TDU, unidad: str, unidad_a_convertir: str):
    for definicion in definiciones:
        if tipo == definicion.tipo:
            if unidad == definicion.unidad and unidad_a_convertir == definicion.unidad_equivalente:
                return f"{definicion.valor_en_unidad} {unidad} " \
                       f"-> {definicion.valor_en_unidad_equivalente} {unidad_a_convertir}"
            if unidad_a_convertir == definicion.unidad and unidad == definicion.unidad_equivalente:
                return f"1 {definicion.unidad} " \
                       f"-> {definicion.valor_en_unidad / definicion.valor_en_unidad_equivalente} {definicion.unidad_equivalente}"

    return


def convertir_unidades_directo(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str):
    conversion_directa = convertir_directo(tipo, unidad, valor, unidad_a_convertir)

    if conversion_directa is not None:
        # Existe la definición para la conversión directa
        if conversion_directa.is_integer():
            return int(conversion_directa)

    if conversion_directa:
        return conversion_directa

    return


def aplicar_conversiones_seguidas(tipo: TDU, unidad: str, unidades: str,
                                  solo_expresar: bool, valor=None, unidad_a_convertir=None) -> str:
    operaciones = ""
    valor_convertido = float(valor)

    for nueva_unidad in unidades[1:]:
        if not solo_expresar:
            if valor_convertido.is_integer():
                operaciones += f"Convierte {int(valor_convertido)} {unidad} a {nueva_unidad}\n"
            else:
                operaciones += f"Convierte %.4f {unidad} a {nueva_unidad}\n" % valor_convertido

        if solo_expresar:
            resultado = expresar_conversion_directa(tipo, unidad, nueva_unidad)
        else:
            resultado = convertir_directo(tipo, unidad, valor_convertido, nueva_unidad)

        if resultado is None:
            return ""

        valor_convertido = resultado
        unidad = nueva_unidad

        if solo_expresar:
            operaciones += resultado + ";"
        else:
            if valor_convertido.is_integer():
                operaciones += f"   Resultado: {int(valor_convertido)} {nueva_unidad}\n"
            else:
                operaciones += f"   Resultado: %.4f {nueva_unidad}\n" % valor_convertido

    return operaciones


def convertir_unidades(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str, solo_expresar: bool) -> str:
    if solo_expresar:
        conversion = expresar_conversion_directa(tipo, unidad, unidad_a_convertir)
    else:
        conversion = convertir_unidades_directo(tipo, unidad, valor, unidad_a_convertir)

    if conversion is not None and not solo_expresar:
        return f"{valor} {unidad} son {conversion} {unidad_a_convertir}"
    elif conversion is not None:
        return conversion

    unidades = enlistar_unidades_de(tipo)
    for i in range(3, len(unidades) + 1):
        permutaciones = permutar_unidades(tipo, i, unidad, unidad_a_convertir)

        operaciones = ""
        for permutacion in permutaciones:
            # Con las permutaciones posibles se intenta hacer las conversiones
            # como las indica la propia permutación
            operaciones = aplicar_conversiones_seguidas(tipo, unidad, permutacion, solo_expresar,
                                                        valor=valor, unidad_a_convertir=unidad_a_convertir)

            if operaciones:
                break

        if operaciones:
            if solo_expresar:
                return operaciones
            return operaciones[:-1]

    return ""


def ensamblar_expresion(cadena_de_conversiones: str, es_numerador: bool) -> str:
    if cadena_de_conversiones[-1] == ";":
        cadena_de_conversiones = cadena_de_conversiones[:-1]

    cadena_de_conversiones = cadena_de_conversiones.split(";")

    equivalencias = list()
    for expresion in cadena_de_conversiones:
        equivalencias += expresion.split(" -> ")

    operaciones = ""
    i = 1
    while i < len(equivalencias):
        if es_numerador:
            operaciones += f"{equivalencias[i]}/{equivalencias[i - 1]} * "
        else:
            operaciones += f"{equivalencias[i - 1]}/{equivalencias[i]} * "

        i += 2

    if operaciones:
        return operaciones[:-3]

    return ""


def convertir_unidades_complejas(unidad: str, valor: float, unidad_a_convertir: str) -> str:
    unidades_in = unidad.split("/")
    if len(unidades_in) != 2:
        raise ValueError("Unidad de entrada inválida")

    unidades_out = unidad_a_convertir.split("/")
    if len(unidades_out) != 2:
        raise ValueError("Unidad de salida inválida")

    # Se expresan solo las conversiones
    cadena_de_conversiones_numerador = convertir_unidades(determinar_tipo_de_unidad(unidades_in[0]), unidades_in[0], valor,
                                                          unidades_out[0], True)
    cadena_de_conversiones_denominador = convertir_unidades(determinar_tipo_de_unidad(unidades_in[1]), unidades_in[1], valor,
                                                            unidades_out[1], True)

    convertir_numerador = unidades_in[0] != unidades_out[0]
    convertir_denominador = unidades_in[1] != unidades_out[1]

    conversion = ""

    if convertir_numerador and not cadena_de_conversiones_numerador:
        return ""
    elif cadena_de_conversiones_numerador:
        equivalencias_numerador = ensamblar_expresion(cadena_de_conversiones_numerador, True)
        conversion = equivalencias_numerador

    if convertir_denominador and not cadena_de_conversiones_denominador:
        return ""
    elif cadena_de_conversiones_denominador:
        equivalencias_denominador = ensamblar_expresion(cadena_de_conversiones_denominador, False)
        if not conversion:
            conversion += f"{equivalencias_denominador}"
        else:
            conversion += f" * {equivalencias_denominador}"

    entrada = f"{valor} {unidad}"

    resultado = f"{entrada} = {entrada} * {conversion}"

    conversion = conversion.replace(" ", "")
    conversion = re.sub(r"[a-zA-Z]+", "", conversion)
    conversion = f"{valor}*{conversion}"

    resultado += "\n" + " " * len(entrada) + f" = %.4f {unidad_a_convertir}" % eval(conversion)

    return resultado


def procesar_entrada():
    # Esta función, solo es para la interfaz CLI

    utilidades.limpiar_pantalla()

    print("Ingresa los datos a procesar [dato][unidad]")
    dato_unidad = input("> ").replace(" ", "")

    valor = re.findall(r"^-?\d*?.?\d+(?=[a-zA-Z])", dato_unidad)
    if not valor:
        raise KeyboardInterrupt("No hay un valor numérico")

    unidad = dato_unidad.replace(valor[0], "")
    if not unidad:
        raise KeyboardInterrupt("No hay una unidad")

    print("Ingresa la unidad a la que se convertirá")
    unidad_a_convertir = input("> ")

    if not unidad_a_convertir:
        raise KeyboardInterrupt("No hay una unidad a convertir")

    valor = float(valor[0])

    if "/" in unidad:  # "Unidad compleja"
        return convertir_unidades_complejas(unidad, valor, unidad_a_convertir)

    return convertir_unidades(determinar_tipo_de_unidad(unidad), unidad, valor, unidad_a_convertir, False)
