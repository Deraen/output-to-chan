# output-to-chan

Implements a java.io.Writer which puts the input lines to core.async channel.

## Usage

```clj
(ns foobar
  (:require [clojure.core.async :refer [chan <!]]))

(let [<output (chan)]
  (with-chan-writer <output
    (println "abc")
    (println "foobar"))
  (go-loop []
    (println "from go loop" (<! <output))))
```

## License

Copyright © 2014 Juho Teperi
