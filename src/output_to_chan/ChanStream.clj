(ns output-to-chan.ChanStream
  (:require [clojure.core.async :refer [put!]])
  (:gen-class
    :name output-to-chan.ChanStream
    :extends java.io.OutputStream
    :state   state
    :init    init
    :exposes-methods {write writeSuper}
    :constructors {[Object] []}))

(defn is-newline? [c]
  (= c \newline))

(defn -init
  [<chan]
  [[]
   {:<chan <chan
    :buffer (atom [])}])

(defn -write
  ; ([this ^bytes b] (.writeSuper this b))
  ([this ^bytes b ^Long off ^Long len] (.writeSuper this b off len))
  ([this ^Long c]
   (let [{:keys [<chan buffer]} (.state this)
         c (char c)]
     (cond
       (is-newline? c) (when @buffer
                         (.flush this))
       :default (swap! buffer conj c)))))

(defn -flush [this]
  (let [{:keys [<chan buffer]} (.state this)]
    (when-not (empty? @buffer)
      (put! <chan (apply str @buffer))
      (reset! buffer []))))

(defn -close [this]
  (.flush this))
