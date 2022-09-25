#!/usr/bin/env sh
set -euxo

_NATIVE_DIRECTORY="$(mktemp --directory --tmpdir="$(dirname "$(realpath "$0")")/jsgenerator-cli/target" native-XXXXXX)"
cat "$(dirname "$(realpath "$0")")/jsgenerator-cli/target/"*.original > "${_NATIVE_DIRECTORY}/jsgenerator-cli.jar"

unzip ./jsgenerator-cli/target/*.jar -d "${_NATIVE_DIRECTORY}/"
_CLASSPATH="$(find "${_NATIVE_DIRECTORY}/BOOT-INF/lib" | tr '\n' ':')"
_CLASSPATH="${_NATIVE_DIRECTORY}/jsgenerator-cli.jar:${_CLASSPATH}:${_NATIVE_DIRECTORY}/BOOT-INF/classes"

cp -R "${_NATIVE_DIRECTORY}/META-INF" "${_NATIVE_DIRECTORY}/BOOT-INF/classes"
native-image \
  "-H:Name=${1:-jsgenerator}" \
  --class-path "${_CLASSPATH}" \
  --module-path "${_CLASSPATH}" \
  --module com.osscameroon.jsgenerator.cli/com.osscameroon.jsgenerator.cli.JsGeneratorCli
