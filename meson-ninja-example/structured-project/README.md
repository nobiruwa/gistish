# structured-project

## create a project

```bash
$ mkdir structured-project
$ cd structured-project
$ meson init
$ mkdir src
$ mv structured_project.c src/
$ diff -u meson.build.orig meson.build
--- meson.build.orig	2020-08-15 13:48:19.110000000 +0900
+++ meson.build	2020-08-15 13:46:25.550000000 +0900
@@ -2,7 +2,7 @@
   version : '0.1',
   default_options : ['warning_level=3'])
 
-exe = executable('structured_project', 'structured_project.c',
+exe = executable('structured_project', 'src/structured_project.c',
   install : true)
 
 test('basic', exe)
$ meson compile -C build
$ ./build/structured_project
This is project structured-project.
```
