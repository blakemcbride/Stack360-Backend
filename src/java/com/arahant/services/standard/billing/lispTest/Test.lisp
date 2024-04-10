

(defpackage "com.arahant.rest.standard.billing.lispTest"
  (:use "COMMON-LISP" "UTILS" "CLOS-UTILS" "JAVA" "MAPPINGS")
  (:export))
(in-package "com.arahant.rest.standard.billing.lispTest")


(defun main (in out hsu this)
  (let ((num1 (getInt in "num1"))
        (num2 (getInt in "num2")))
   (putInt out "res" (+ num1 num2))))




