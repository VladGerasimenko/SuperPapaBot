source ./setSourcePath.sh

if ! command -v java &> /dev/null
then
    echo "java not installed or not added to path"
    exit 1
fi

if ! command -v node &> /dev/null
then
    echo "node not installed or not added to path"
    exit 1
fi

node ${JS_PATH}/decryptor.js
java -jar ${JAR_PATH}/SuperPapaBot-1.0-SNAPSHOT -XmX1G -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=20M -XX:+UseGCLogFileRotation -Xloggc:../log/gc.%t.log -XX:+HeapDumpOnOutOfMemoryError XX:HeapDumpPath=../log/dump.hprof