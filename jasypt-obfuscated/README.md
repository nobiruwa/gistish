# jasypt-obfuscated

Jasyptを使っていることを秘匿するために、以下の対処を施しました。

- fat jarを作成し、クラスをPro Guardで難読化
- maven-assembly-pluginでjasyptのpom.properties, pom.xmlを除去
  - META-INF/maven/org.jasypt/pom.xml のように、パッケージ名のディクトリを存在させたくないため

## ビルド

```bash
mvn package assembly:single
```

## 実行

```bash
java -jar target/app-with-dependencies.jar PLAIN_TEXT
```

## 難読化

```bash
proguard.sh @proguard.config
```

## 難読化されたjarの実行

```bash
java -jar target/app-final.jar PLAIN_TEXT
```
