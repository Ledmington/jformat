# jformat
Jformat is a configurable zero-dependency java formatter.

## How to use
```bash
java -jar jformat.jar -i <your_file>
```

## How to build
```bash
./gradlew fatjar
```

## How to contribute
```bash
./gradlew build
```

## TODO
 - [ ] align multiple chained method calls with the same indentation
 - [ ] license header
 - [ ] remove unused imports
 - [ ] sort imports in groups
 - [ ] format comments
 - [ ] format javadoc
 - [ ] enable/disable jformat with `// jformat off` and `// jformat on`
