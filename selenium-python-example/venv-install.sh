ENV_DIR=$HOME/.venvs/selenium

mkdir -p $ENV_DIR
virtualenv $ENV_DIR

. $ENV_DIR/bin/activate && \
pip install --upgrade pip wheel && \
pip install --upgrade selenium
