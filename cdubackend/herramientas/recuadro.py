
# Utilidades para crear recuadros y tablas

def buscar_elemento_mas_grande(lista: list) -> int:
    mayor = 0
    for elemento in lista:
        if not isinstance(elemento, str) and not isinstance(elemento, list):
            elemento = str(elemento)

        if len(elemento) > mayor:
            mayor = len(elemento)

    return mayor


def crear_borde_horizontal(longitud_deseada: int) -> str:
    return "+" + "-" * (longitud_deseada + 2) + "+"


def agregar_espacios_a_cadena(cadena: str, longitud_deseada: int) -> str:
    cadena = str(cadena)
    longitud_deseada = longitud_deseada - len(cadena)
    if longitud_deseada < 0:
        return cadena

    return cadena + (" " * longitud_deseada)


def agregar_espacios_a_elementos_en_lista(lista: list, longitud_deseada: int) -> list:
    lista_expandida = list()
    for elemento in lista:
        lista_expandida.append(agregar_espacios_a_cadena(elemento, longitud_deseada))

    return lista_expandida


def agregar_elementos_a_lista_en_lista(lista: list, longitud_de_lista_deseada: int) -> list:
    lista_expandida = list()
    for columna in lista:
        while len(columna) < longitud_de_lista_deseada:
            columna.append("")

        lista_expandida.append(columna)

    return lista_expandida


def ensamblar_recuadro(lista: list, borde: str) -> str:
    recuadro = f"{borde}\n"
    i = 0

    while i < len(lista):
        recuadro += f"| {lista[i]} |\n"
        i += 1

    return recuadro + borde


def crear_recuadro(lista: list) -> str:
    if not lista:
        return ""

    elemento_mas_grande = buscar_elemento_mas_grande(lista)
    borde_horizontal = crear_borde_horizontal(elemento_mas_grande)
    lista_expandida = agregar_espacios_a_elementos_en_lista(lista, elemento_mas_grande)

    return ensamblar_recuadro(lista_expandida, borde_horizontal)
