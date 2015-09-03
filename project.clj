(defproject cider-ci/open-session "1.1.1"
  :description "Platform and language agnostic encoding, encryption, signature and password handling."
  :url "https://github.com/cider-ci/cider-ci_open-session_clj"
  :license {:name "APGL"}
  :dependencies [
                 [org.bouncycastle/bcprov-jdk15on "1.52"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.6"]
                 [pandect "0.5.1"]
                 [ring/ring-core "1.3.2"]
                 ]
  :java-source-paths ["lib/jBCrypt/src"]
  )
