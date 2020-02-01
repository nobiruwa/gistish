ENV_DIR=$HOME/.venvs/intellij-dependency-viewer

mkdir -p $ENV_DIR
virtualenv $ENV_DIR

. $ENV_DIR/bin/activate

pip install --upgrade pip wheel networkx matplotlib pydot

