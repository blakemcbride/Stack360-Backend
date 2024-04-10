(defpackage "TEST"
  (:use "COMMON-LISP" "UTILS" "MAPPINGS")
  (:export "GET-GROUP-CODE"))
(in-package "TEST")


(defun mainJDBC (con)
  (let* ((db (new Database con))
	 (stmt (createStatement db)))

    (execute stmt "select * from hr_eeo_category")

    (while-more stmt
    		(println (getString stmt "eeo_category_id")
    			 " "
    			 (getString stmt "name")))


    (closeStatement stmt)
    (release db)
    (println "Exiting Lisp function main")))

; Hibernate01 equivalent

(defun main01 (con)
  (let* ((hsu (getHSU ArahantSession)))
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)
    
    (commitTransaction hsu)))
  


; Hibernate02 equivalent

(defun main02 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu HrEeoCategory)))
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)
    (orderBy hcu HrEeoCategory.NAME)

    (rec-loop (getList hcu) rec HrEeoCategory ()
	      (println (getEeoCategoryId rec) "  " (getName rec)))
    
    (commitTransaction hsu)))


; Hibernate03 equivalent

(defun main03 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (lst (getList (orderBy (createHCU hsu HrEeoCategory) HrEeoCategory.NAME))))

    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (rec-loop lst rec HrEeoCategory ()
	      (println (getEeoCategoryId rec) "  " (getName rec)))
    
    (commitTransaction hsu)))


; Hibernate04 equivalent

(defun main04 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu HrEeoCategory))
	 scr)

    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (orderBy hcu HrEeoCategory.NAME)
    (setq scr (scroll hcu))

    (rec-loop scr rec HrEeoCategory ()
	      (println (getEeoCategoryId rec) "  " (getName rec)))
    
    (commitTransaction hsu)))

; Hibernate05 equivalent

(defun main05 (con)
  (let* ((hsu (getHSU ArahantSession))
	 v)

    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (setq v (getRecord hsu HrEeoCategory "00000-0000000004"))

    (println (getEeoCategoryId v) "  " (getName v))
    
    (commitTransaction hsu)))


; Hibernate06 equivalent

(defun main06 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu HrEeoCategory))
	 lst)

    (eqString hcu HrEeoCategory.NAME "Technicians")
    (orderBy hcu HrEeoCategory.NAME)
    (setq lst (getList hcu))
    
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (rec-loop lst rec HrEeoCategory ()
	      (println (getEeoCategoryId rec) "  " (getName rec)))
    
    (commitTransaction hsu)))

; Hibernate07 equivalent

(defun main07 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu Employee))
	 lst)

    (setq lst (getList hcu))
    
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (rec-loop lst rec Employee ()
	      (println (getLname rec)))
    
    (commitTransaction hsu)))

; Hibernate08 equivalent

(defun main08 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu Employee))
	 lst
	 c)

    (setq lst (getList hcu))
    
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (rec-loop lst rec Employee ()
      (setq c (getHrEeoCategory rec))
      (if c
	  (println (getLname rec) "  " (getEeoCategoryId c) "  " (getName c))
	  (println (getLname rec) " has no EEO Category")))
    
    (commitTransaction hsu)))

; Hibernate09 equivalent

(defun main09 (con)
  (let* ((hsu (getHSU ArahantSession))
	 (hcu (createHCU hsu Employee))
	 lst
	 c)

    (setq hcu (joinTo hcu Employee.HREEOCATEGORY))

    (setq lst (getList hcu))
    
    (setCurrentPersonToArahant hsu)
    (beginTransaction hsu)

    (rec-loop lst rec Employee ()
      (setq c (getHrEeoCategory rec))
      (println (getLname rec) "  " (getEeoCategoryId c) "  " (getName c)))
    
    (commitTransaction hsu)))


