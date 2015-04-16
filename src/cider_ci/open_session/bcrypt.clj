; Copyright (C) 2015 Dr. Thomas Schank  (DrTom@schank.ch, Thomas.Schank@algocon.ch)
; Licensed under the terms of the GNU Affero General Public License v3.
; See the "LICENSE.txt" file provided with this software.

(ns cider-ci.open-session.bcrypt
  (:import 
    [org.mindrot.jbcrypt BCrypt]
    ))

(defn hashpw [plain_password]
  (BCrypt/hashpw plain_password (BCrypt/gensalt)))

(defn checkpw [candidate_password stored_hash]
  (BCrypt/checkpw candidate_password stored_hash))

(defn checkpw! [candidate_password stored_hash]
  (if-not (checkpw candidate_password stored_hash)
    (throw (IllegalStateException. "Check password failed!"))
    true))

