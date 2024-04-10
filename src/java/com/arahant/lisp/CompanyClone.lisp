(defpackage "Company Clone"
  (:use "COMMON-LISP" "UTILS" "MAPPINGS")
  (:export "GET-GROUP-CODE"))
(in-package "Company Clone")


(defun company-clone (from-id company-name)
  (let* ((hsu (getHSU ArahantSession))
	 from-comp to-comp to-id)
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (setq from-comp (read-company BCompany from-id))

    (cond (from-comp
	       (println (getName from-comp))
	       (setq to-comp (new BCompany))
	       (setq to-id (CREATE to-comp))
	       (setName to-comp company-name)
	       (INSERT to-comp)
	       (println "New company id = " to-id)
	       (copy-employee-status hsu from-id from-comp to-id to-comp)
	  ))

    
    (commitTransaction hsu)))

(defun copy-employee-status (hsu from-id from-comp to-id to-comp)
  (let* ((hcu (createHCU hsu HrEmployeeStatus)))
    (eqVal hcu HrEmployeeStatus.
    (rec-loop (scroll hcu) rec HrEmployeeStatus ()
	      (println (getStatusId rec) "  " (getName rec)))))
