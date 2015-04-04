; Copyright (C) 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Licensed under the terms of the GNU Affero General Public License v3.
; See the "LICENSE.txt" file provided with this software.

(ns cider-ci.open-session.signature
  (:require
    [pandect.core :refer [sha1-hmac]]
    [clojure.string :refer [split]]
    [clojure.data.json :as json]
    ))

(defn create [secret message]
  (sha1-hmac message secret))

(defn valid? [signature secret message]
  (= signature (sha1-hmac message secret)))

(defn validate! [signature secret message]
  (when-not (valid? signature secret message)
    (throw (IllegalStateException. "Signature validation failed!"))))
