import sys
import re


def encontrar_argumentos():
    cmd = " ".join(sys.argv).replace(sys.argv[0], "")

    argumentos = re.findall(r"-[\w ]+", cmd)
    argumento_invalido = ""

    for i, argumento in enumerate(argumentos):
        argumento = argumento.strip()

        if argumento.count(" ") > 1:
            argumento_invalido = argumento
            break

        argumentos[i] = argumento.split(" ")
        if len(argumentos[i]) != 2:
            argumento_invalido = argumento
            break

        cmd = cmd.replace(argumento, "")

    if cmd.strip():
        print(f"No es posible interpretar \"{' '.join(sys.argv)}\"")
        print(f"    Error cerca de \"{argumento_invalido.strip()}\"")
        exit(1)

    return argumentos


def seleccionar_opcion(opciones: list, valores=None):
    seleccion = ""

    while not seleccion:
        seleccion = input("> ").upper()

        if seleccion not in opciones:
            print("  Opción inválida")
            seleccion = ""

    if valores is not None:
        return valores[opciones.index(seleccion)]

    return seleccion


def filtrar_duplicados_en(lista: list) -> list:
    filtrados = list()

    for elemento in lista:
        if elemento not in filtrados:
            filtrados.append(elemento)

    return filtrados


def quitar_elemento_en_lista(lista: list, elemento) -> list:
    nueva_lista = lista.copy()

    try:
        nueva_lista.remove(elemento)
    except IndexError:
        pass

    # print("n", nueva_lista)
    return nueva_lista
