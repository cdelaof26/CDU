from enum import Enum


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


def permutar_unidades(tipo: TDU, longitud_del_elemento: int) -> list:
    unidades0 = enlistar_unidades_de(tipo)

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

    script += indentado + "permutaciones.append((" + " + ".join(variables) + ").split(\" \"))"

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


def expresar_conversion_directa(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str) -> str:
    for definicion in definiciones:
        if tipo == definicion.tipo:
            if unidad == definicion.unidad and unidad_a_convertir == definicion.unidad_equivalente:
                return f"{valor} {unidad} -> {definicion.valor_en_unidad_equivalente} {definicion.unidad_equivalente}"
            if unidad == definicion.unidad_equivalente and unidad_a_convertir == definicion.unidad:
                return f"{valor} {unidad} -> {definicion.valor_en_unidad} {definicion.unidad}"

    return ""


def convertir_unidades_directo(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str):
    conversion_directa = convertir_directo(tipo, unidad, valor, unidad_a_convertir)

    if conversion_directa is not None:
        # Existe la definición para la conversión directa
        if conversion_directa.is_integer():
            return int(conversion_directa)

    if conversion_directa:
        return conversion_directa

    return


def convertir_unidades(tipo: TDU, unidad: str, valor: float, unidad_a_convertir: str):
    conversion = convertir_unidades_directo(tipo, unidad, valor, unidad_a_convertir)
    if conversion is not None:
        return conversion

    unidades = enlistar_unidades_de(tipo)
    for i in range(3, len(unidades) + 1):
        permutaciones = permutar_unidades(tipo, i)

        permutaciones_posibles = list()
        for permutacion in permutaciones:
            # permutaciones_posibles son las permutaciones que inician con la
            # unidad original y terminan con la unidad a convertir
            #
            if permutacion[0] == unidad and permutacion[-1] == unidad_a_convertir:
                permutaciones_posibles.append(permutacion)

        # Con las permutaciones posibles se intenta hacer las conversiones
        # como las indica la propia permutación
        #  Por ejemplo: mg -> kg
        #       La conversión fallaría porque no hay una forma directa de
        #       convertir de mg a kg, por lo que continuaría con otras
        #       permutaciones hasta encontrar alguna que permita la conversión
        #           Como: mg -> g -> kg
        #
        conversion_fallida = True
        operaciones = ""
        for pp in permutaciones_posibles:
            operaciones = ""
            conversion_fallida = False
            unidad_de_conversion = unidad
            valor_convertido = float(valor)

            for nueva_unidad in pp[1:]:
                if valor_convertido.is_integer():
                    operaciones += f"Convierte {int(valor_convertido)} {unidad_de_conversion} a {nueva_unidad}\n"
                else:
                    operaciones += f"Convierte %.4f {unidad_de_conversion} a {nueva_unidad}\n" % valor_convertido

                resultado = convertir_directo(tipo, unidad_de_conversion, valor_convertido, nueva_unidad)
                if resultado is None:
                    conversion_fallida = True
                    break

                valor_convertido = resultado
                unidad_de_conversion = nueva_unidad
                if valor_convertido.is_integer():
                    operaciones += f"  Resultado: {int(valor_convertido)} {nueva_unidad}\n"
                else:
                    operaciones += f"  Resultado: %.4f {nueva_unidad}\n" % valor_convertido

            if not conversion_fallida:
                break

        if not conversion_fallida:
            return operaciones[:-1]

    return ""
