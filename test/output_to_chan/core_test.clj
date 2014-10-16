(ns output-to-chan.core-test
  (:require [midje.sweet :refer :all]
            [clojure.core.async :refer [chan <!! go timeout alt!!]]
            [output-to-chan.core :refer :all]))

(facts string-writer
  (let [writer (java.io.StringWriter.)]
    (.write writer (char-array "foobar") 0 6)
    (.toString writer) => "foobar"))

(defn try<!!
  "Take val from port. Will block until something is available or
   for maximum of timeout-msecs. Will throw a exception if
   timeout is reached.

   The default timeout is 1000ms."
  ([<chan & [timeout-msecs]]
   (let [<timeout-chan (timeout (or timeout-msecs 1000))]
     (alt!!
       <timeout-chan ([] (throw (IllegalStateException. "Timeout")))
       <chan ([v] v)))))

(facts chan-writer
  (let [<chan (chan)
        writer (chan-writer <chan)]
    (.write writer (char-array "foobar\n") 0 7)
    (.flush writer)
    (try<!! <chan) => "foobar"

    (.write writer (char-array "foo\nbar") 0 7)
    (.close writer)

    (try<!! <chan) => "foo"
    (try<!! <chan) => "bar"))

(fact with-chan-writer
  (let [<chan (chan)]
    (with-chan-writer <chan
                      (println "foobar"))
    (try<!! <chan) => "foobar"))
