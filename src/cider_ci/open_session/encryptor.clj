; Copyright (C) 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Licensed under the terms of the GNU Affero General Public License v3.
; See the "LICENSE.txt" file provided with this software.

(ns cider-ci.open-session.encryptor
  (:import
    [javax.crypto KeyGenerator SecretKey Cipher]
    [javax.crypto.spec IvParameterSpec SecretKeySpec]
    [java.security SecureRandom]
    [org.bouncycastle.jce.provider BouncyCastleProvider]
    )
  (:require
    [pandect.algo.sha256 :refer [sha256-bytes]]
    [clojure.string :refer [split]]
    [clojure.data.json :as json]
    [cider-ci.open-session.encoder :refer [decode encode]]
    ))

(java.security.Security/addProvider
  (org.bouncycastle.jce.provider.BouncyCastleProvider.))

(try (doto (.getDeclaredField (Class/forName "javax.crypto.JceSecurity") "isRestricted")
       (.setAccessible true)
       (.set nil false))
     (catch Throwable _ ))

(defn decrypt [secret encrypted-message]
  (let [cipher (Cipher/getInstance "AES/CBC/PKCS5Padding")
        skey-spec (SecretKeySpec. (sha256-bytes secret) "AES")
        [iv, msg]  (->> (split encrypted-message #"~")
                        (map decode ))
        iv_spec (IvParameterSpec. iv)]
    (.init cipher (Cipher/DECRYPT_MODE) skey-spec iv_spec)
    (->> (.doFinal cipher msg)
         (map char)
         (apply str)
         json/read-str
         clojure.walk/keywordize-keys)))

(def random (SecureRandom.))

(defn encrypt [secret data]
  (let [cipher (Cipher/getInstance "AES/CBC/PKCS5Padding")
        skey-spec (SecretKeySpec. (sha256-bytes secret) "AES")
        seed (.generateSeed random 16)
        iv_spec (IvParameterSpec. seed)
        _ (.init cipher (Cipher/ENCRYPT_MODE) skey-spec iv_spec)
        crypt (->> data
                   json/write-str
                   (map (comp byte int))
                   byte-array
                   (.doFinal cipher))]
    (->> [seed, crypt]
         (map encode)
         (clojure.string/join "~"))))
