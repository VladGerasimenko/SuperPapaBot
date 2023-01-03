source ./setSourcePath.sh

if ! command -v mvn &> /dev/null
then
    echo "mvn not installed or not added to path"
    exit 1
fi

if ! command -v npm &> /dev/null
then
    echo "npm not installed or not added to path"
    exit 1
fi

cd ${ROOT_DIR}
git fetch
git pull origin master
mvn clean package
cd ${JS_DIR} & npm install
