(ns output-to-chan.core
  (:require [clojure.core.async :refer [put!]]
            [clojure.string :as str]))

(defn is-newline? [c]
  (= c \newline))

(defn chan-writer
  "Creates a java.io.Writer which will put each line of
   input into a channel."
  [<chan]
  (let [buffer (atom [])]
    (proxy [java.io.Writer] []
      (append [c]
        (cond
          (is-newline? c) (when @buffer
                            (.flush this))
          :default (swap! buffer conj c))
        this)

      (write [^chars cbuf ^Long off ^Long len]
        (loop [i off]
          (.append this (aget cbuf i))
          (when (< (inc i) len) (recur (inc i)))))

      (flush []
        (when @buffer
          (put! <chan (apply str @buffer))
          (reset! buffer [])))

      (close []
        (.flush this)))))

(defmacro with-chan-writer [<chan & body]
  `(with-open [o# (chan-writer ~<chan)]
     (binding [*out* o#]
       ~@body)))
