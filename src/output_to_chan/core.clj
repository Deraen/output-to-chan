(ns output-to-chan.core
  (:import [output-to-chan ChanStream]))

(defn chan-writer
  [<chan]
  (clojure.java.io/writer (ChanStream. <chan)))

(defmacro with-chan-writer [<chan & body]
  `(with-open [stream# (ChanStream. ~<chan)]
     (with-open [o# (clojure.java.io/writer stream#)]
       (binding [*out* o#]
         ~@body))))
