## trie-map

![Build Status](https://github.com/hiyainc-oss/trie-map/workflows/Scala%20CI/badge.svg)
[ ![Download](https://api.bintray.com/packages/hiyainc-oss/maven/trie-map/images/download.svg) ](https://bintray.com/hiyainc-oss/maven/trie-map/_latestVersion)


The trie map is:
- A prefix tree written for Scala, 
- Generic enough for storing any kind of `Seq[K]`-s
- Efficient (d\*log n, where d is the depth of the trie) for prefix 'string' (`Seq[K]`) search
- Can store any type of value with the keys, under their respective prefixes

### Adding it to an sbt project
Add the following to the build.sbt:
```scala
libraryDependencies += "com.hiya" %% "trie-map" % "0.0.4"
```
