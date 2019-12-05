ENV_DIR=$HOME/.venvs/tesseract-ocr

mkdir -p $ENV_DIR
virtualenv $ENV_DIR

. $ENV_DIR/bin/activate

pip install --upgrade pip wheel
pip install --upgrade pyocr Pillow numpy opencv-python matplotlib
