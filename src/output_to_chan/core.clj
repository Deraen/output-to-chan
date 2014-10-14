(ns output-to-chan.core
  (:require [clojure.core.async :refer [put!]]
            [clojure.string :as str]))

(defn is-newline? [c]
  (= c \newline))

(defn chan-writer
  "Creates a java.io.Writer which will put each line of
   input into a channel."
  [<chan]
  (let [buffer (atom nil)]
    (proxy [java.io.Writer] []
      (write [^chars cbuf ^Long off ^Long len]
          (loop [i off]
            (let [c (aget cbuf i)]
              (cond
                (is-newline? c) (do
                                  (put! <chan @buffer)
                                  (reset! buffer nil))
                :default (swap! buffer #(str % c)))
              (when (< (inc i) len) (recur (inc i))))))

      (close []
        (when @buffer (put! <chan @buffer))))))

(defmacro with-chan-writer [<chan & [body]]
  `(with-open [o (chan-writer ~<chan)]
     (with-binding [*out* o]
                   ~@body)))
