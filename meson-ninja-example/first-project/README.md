# meson-ninja-example

## install meson and ninja

```bash
$ sudo apt-get install python3 python3-distutils
$ sudo apt-get install ninja-build
$ curl https://bootstrap.pypa.io/get-pip.py -o ~/bin/get-pip.py
$ python3 ~/bin/get-pip.py --user
$ pip install --user meson
```

## create a project

```bash
$ mkdir first-project
$ cd first-project
$ meson init
$ meson setup build
$ meson compile -C build
$ ./build/first_project
This is project first-project.
```
