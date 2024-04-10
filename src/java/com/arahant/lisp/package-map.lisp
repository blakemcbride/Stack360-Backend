; Written by Blake McBride (blake@arahant.com)

(defpackage "PACKAGE-MAP"
  (:use "COMMON-LISP")
  (:export "LISP-FILE-NAME-FROM-PACKAGE-NAME"))
(in-package "PACKAGE-MAP")

(defparameter *package-map* (make-hash-table :test #'equal))

(defun fill-package-map (&rest lst)
  (loop
     while (consp lst)
     do (setf (gethash (car lst) *package-map*) (cadr lst))
     (setq lst (cddr lst))))

(fill-package-map
 "com.arahant.dynamic.services.standard.xyz"  "lisp file path A"
 "com.arahant.dynamic.services.standard.def"  "lisp file path B"
 "Group Critical Illness Calculations" "Group Critical Illness Calculations"
 "Lifetime Benefit Term Calculations" "Lifetime Benefit Term Calculations"
 "Madison Dental Calculations" "Madison Dental Calculations"
; "com.arahant.dynamic.services.standard.wizard.benefitWizard.LoadQuestionsAndAnswers" "/com/arahant/lisp/test42"
 )

(defun lisp-file-name-from-package-name (package)
  (gethash package *package-map*))
