(defproject output-to-chan "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/mit-license.php"
            :distribution :repo}
  :profiles {:dev {:plugins [[lein-midje "3.1.1"]]}}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.298.0-2a82a1-alpha"]
                 [midje "1.6.3"]])
