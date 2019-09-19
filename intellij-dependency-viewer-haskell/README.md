# intellij-dependency-viewer

## Build

```
$ stack build
```

## Execute

```
$ stack exec intellij-dependency-viewer-exe 'samples/sample.xml' '$PROJECT_DIR$/src/main/java/org/example/websocket/WebSocketServer.java' | dot -Tpng -o /tmp/a.png
```
