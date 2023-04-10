from subprocess import call
from pathlib import Path
import sys
import re


NOMBRE_DEL_SISTEMA = ""


def obtener_nombre_del_sistema():
    global NOMBRE_DEL_SISTEMA

    try:
        from os import uname
        NOMBRE_DEL_SISTEMA = uname()[0]
    except ImportError:
        NOMBRE_DEL_SISTEMA = "nt"


def limpiar_pantalla():
    global NOMBRE_DEL_SISTEMA

    if not NOMBRE_DEL_SISTEMA:
        obtener_nombre_del_sistema()

    if NOMBRE_DEL_SISTEMA == "nt":
        call("cls", shell=True)
    else:
        call("clear", shell=True)


def encontrar_argumentos() -> dict:
    cmd = " ".join(sys.argv).replace(sys.argv[0], "")

    if "help" in cmd:
        print("Unavailable option, please refer to backend_api.py for more information about available CLI args")
        exit(1)

    app_args = dict()
    argumentos = re.findall(r"-[\w /.]+", cmd)
    argumento_invalido = ""

    for argumento in argumentos:
        argumento = argumento.strip()
        if argumento.count(" ") > 1:
            argumento_invalido = argumento
            break

        datos = argumento.split(" ")
        if len(datos) != 2:
            argumento_invalido = argumento
            break

        app_args[datos[0]] = datos[1]

        cmd = cmd.replace(argumento, "")

    if cmd.strip():
        print(f"No es posible interpretar \"{' '.join(sys.argv)}\"")
        print(f"    Error cerca de \"{argumento_invalido.strip()}\"")
        exit(1)

    return app_args


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

    return nueva_lista


def escribir_archivo(ruta: Path, datos: str) -> bool:
    try:
        with open(ruta, "w") as archivo:
            archivo.write(datos)
            return True
    except (IsADirectoryError, FileNotFoundError, UnicodeError):
        return False


def leer_archivo(ruta: Path) -> str:
    if not ruta.exists():
        return ""

    try:
        with open(ruta, "r") as archivo:
            return archivo.read()
    except (IsADirectoryError, UnicodeError):
        return ""


def eliminar_directorio(directorio: Path):
    if directorio.exists():
        archivos = list()
        directorios_eliminables = list()
        directorios = [directorio]

        while directorios:
            for elemento in directorios[0].iterdir():
                if elemento.is_dir():
                    directorios.append(elemento)
                else:
                    archivos.append(elemento)

            directorios_eliminables.append(directorios[0])
            directorios.pop(0)

        for archivo in archivos:
            archivo.unlink()

        for dir in directorios_eliminables:
            dir.rmdir()

        directorio.rmdir()
