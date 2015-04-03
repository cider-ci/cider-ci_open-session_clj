; Copyright (C) 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Licensed under the terms of the GNU Affero General Public License v3.
; See the "LICENSE.txt" file provided with this software.

(ns cider-ci.open-session.encryptor
  (:import 
    [javax.crypto KeyGenerator SecretKey Cipher]
    [javax.crypto.spec IvParameterSpec SecretKeySpec]
    [org.bouncycastle.jce.provider BouncyCastleProvider] 
    )
  (:require
    [pandect.algo.sha256 :refer [sha256-bytes]]
    [clojure.string :refer [split]]
    [clojure.data.json :as json]
    [cider-ci.open-session.encoder :refer [decode]]
    ))

(java.security.Security/addProvider
  (org.bouncycastle.jce.provider.BouncyCastleProvider.))

(defn decrypt [secret encrypted-message]
  (let [cipher (Cipher/getInstance "AES/CBC/PKCS5Padding")
        skey-spec (SecretKeySpec. (sha256-bytes secret) "AES")
        [msg,iv]  (->> (split encrypted-message #"~")
                       (map #(decode %)))
        iv_spec (IvParameterSpec. iv)]
    (.init cipher (Cipher/DECRYPT_MODE) skey-spec iv_spec)
    (->> (.doFinal cipher msg)
         (map char)
         (apply str)
         json/read-str  
         clojure.walk/keywordize-keys)))

;(decrypt "secret" "wHGrlC7kOvd0T90uNlEc_A~jfUriLEgN7mhM3eLQJ6WsA")

