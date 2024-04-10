
(defpackage "Imagine MoO Subgroup Codes"
  (:use "COMMON-LISP")
  (:export "GET-SUBGROUP-CODE"))
(in-package "Imagine MoO Subgroup Codes")

; Java usage:
;			LispPackage lp = new LispPackage("Imagine MoO Subgroup Codes", "com/arahant/lisp/Imagine-MoO-Subgroup-Codes");
;			String val = lp.executeLispReturnString("GET-SUBGROUP-CODE", "00001-0000000023");
;			lp.unload();

; Benefit Classes
(defparameter  Imagine-Schools       '(""))
(defparameter  Academy of Columbus   '("00001-0000005152"))
(defparameter  Venture Place         '("00001-0000005157"))
(defparameter  International         '("00001-0000005159"))
(defparameter  St Petersburg         '("00001-0000005160"))
(defparameter  Imagine Corp          '("00001-0000005162"))
(defparameter  Imagine Broward       '("00001-0000005163"))
(defparameter  Kissimmee Charter     '("00001-0000005164"))
(defparameter  Imagine North Port    '("00001-0000005165"))
(defparameter  Harvard Ave           '("00001-0000005166"))
(defparameter  Weston Elementary     '("00001-0000005167"))
(defparameter  Lantana Elem          '("00001-0000005168"))
(defparameter  Land o' Lakes         '("00001-0000005169"))
(defparameter  Sierra Vista          '("00001-0000005170"))
(defparameter  Early Learning        '("00001-0000005171"))
(defparameter  Bell Canyon           '("00001-0000005172"))
(defparameter  Cortez Park MS        '("00001-0000005156"))
(defparameter  Cortez Park E         '("00001-0000005173"))
(defparameter  West Melbourne        '("00001-0000005175"))
(defparameter  N Lauderdale MS       '("00001-0000005161"))
(defparameter  Signal Peak Coolidge  '("00001-0000005176"))
(defparameter  Canoe Creek M         '("00001-0000005177"))
(defparameter  N Lauderdale E        '("00001-0000005178"))
(defparameter  Canoe Creek M&E       '("00001-0000005179"))
(defparameter  N Lauderdale HS       '("00001-0000005180"))
(defparameter  Nau Charter           '("00001-0000005181"))
(defparameter  Avondale              '("00001-0000005182"))
(defparameter  Kleplinger            '("00001-0000005183"))
(defparameter  Evening Rose          '("00001-0000005185"))
(defparameter  Sullivant Avenue      '("00001-0000005186"))
(defparameter  Harrisburg Pike       '("00001-0000005187"))
(defparameter  Madison Avenue        '("00001-0000005188"))
(defparameter  Broadway              '("00001-0000005189"))
(defparameter  Superstition HS       '("00001-0000005190"))
(defparameter  Lakewood Ranch        '("00001-0000005191"))
(defparameter  Town Center           '("00001-0000005192"))
(defparameter  Surprise Prep HS      '("00001-0000005193"))
(defparameter  SE Public Charter     '("00001-0000005194"))
(defparameter  Avondale MS           '("00001-0000005195"))
(defparameter  St Petersburg M       '("00001-0000005196"))
(defparameter  Broward MS            '("00001-0000005197"))
(defparameter  Hope-Tolson           '("00001-0000005198"))
(defparameter  100 Academy           '("00001-0000005200"))
(defparameter  Tempe                 '("00001-0000005201"))
(defparameter  Renaissance Wallace   '("00001-0000005202"))
(defparameter  Renaissance Academy   '("00001-0000005203"))
(defparameter  College Prep          '("00001-0000005204"))
(defparameter  West Gilbert E        '("00001-0000005174"))
(defparameter  Renaissance Ken.      '("00001-0000005206"))
(defparameter  Rosefield Charter     '("00001-0000005208"))
(defparameter  South Lake Charter    '("00001-0000005207"))
(defparameter  E Mesa Charter        '("00001-0000005209"))
(defparameter  Great Western         '("00001-0000005211"))
(defparameter  Marietta Charter      '("00001-0000005212"))
(defparameter  Camelback             '("00001-0000005213"))
(defparameter  Desert West           '("00001-0000005214"))
(defparameter  Groveport             '("00001-0000005215"))
(defparameter  East Mesa Middle      '("00001-0000005216"))
(defparameter  North Manatee         '("00001-0000005218"))
(defparameter  Indigo Ranch          '("00001-0000005219"))
(defparameter  Smyrna                '("00001-0000005220"))
(defparameter  Careers               '("00001-0000005221"))
(defparameter  Academy of Success    '("00001-0000005222"))
(defparameter  ESM                   '("00001-0000005223"))
(defparameter  Masters Academy       '("00001-0000005224"))
(defparameter  Wesley Academy        '("00001-0000005225"))
(defparameter  Clay Avenue           '("00001-0000005226"))
(defparameter  Mableton              '("00001-0000005227"))
(defparameter  Camelback MS          '("00001-0000005228"))
(defparameter  Romig Road            '("00001-0000005229"))
(defparameter  Imagine Life Sci E    '("00001-0000005184"))
(defparameter  Firestone             '("00001-0000005231"))
(defparameter  Hope-Lamond           '("00001-0000005232"))
(defparameter  Careers MS            '("00001-0000005233"))
(defparameter  South Vero            '("00001-0000005234"))
(defparameter  West Gilbert          '("00001-0000005235"))
(defparameter  Imagine Life Sci W    '("00001-0000005236"))
(defparameter  Coolidge MS           '("00001-0000005237"))
(defparameter  Palmer Ranch          '("00001-0000005238"))
(defparameter  Bella Academy         '("00001-0000005239"))
(defparameter  Imperial Valley       '("00001-0000005240"))
(defparameter  Imagine Cultural Art  '("00001-0000005242"))
(defparameter  Imagine Superstition  '("00001-0000005244"))
(defparameter  Imagine Valle GB      '("00001-0000005245"))
(defparameter  Imagine Akron         '("00001-0000005246"))
(defparameter  Imagine Canton        '("00001-0000005247"))
(defparameter  Imagine Foundation    '("00001-0000005248"))
(defparameter  Imagine Discovery     '("00001-0000005249"))
(defparameter  Desert West MS        '("00001-0000005257"))
(defparameter  Surprise Prep Middle  '("00001-0000005217"))
(defparameter  Imagine Valle         '("00001-0000005230"))



;;;;;;;;;
;NOT SURE IF THIS IS RIGHT
;;;;;;;;;
(defmacro add-map (benefit-class)
  `(setq ,map (cons (list ,benefit-class) ,map)))




(add-map  Imagine-Schools       "0001")
(add-map  Academy of Columbus   "0004")
(add-map  Venture Place         "")
(add-map  International         "0036")
(add-map  St Petersburg         "0064")
(add-map  Imagine Corp          "")
(add-map  Imagine Broward       "0081")
(add-map  Kissimmee Charter     "0039")
(add-map  Imagine North Port    "0052")
(add-map  Harvard Ave           "0031")
(add-map  Weston Elementary     "0076")
(add-map  Lantana Elem          "0043")
(add-map  Land o' Lakes         "0042")
(add-map  Sierra Vista          "0059")
(add-map  Early Learning        "0022")
(add-map  Bell Canyon           "0007")
(add-map  Cortez Park MS        "0019")
(add-map  Cortez Park E         "0018")
(add-map  West Melbourne        "0075")
(add-map  N Lauderdale MS       "0049")
(add-map  Signal Peak Coolidge  "")
(add-map  Canoe Creek M         "")
(add-map  N Lauderdale E        "0048")
(add-map  Canoe Creek M&E       "")
(add-map  N Lauderdale HS       "")
(add-map  Nau Charter           "0050")
(add-map  Avondale              "0077")
(add-map  Kleplinger            "0040")
(add-map  Evening Rose          "0026")
(add-map  Sullivant Avenue      "0065")
(add-map  Harrisburg Pike       "0030")
(add-map  Madison Avenue        "0045")
(add-map  Broadway              "0009")
(add-map  Superstition HS       '("00001-0000005190"))
(add-map  Lakewood Ranch        '("00001-0000005191"))
(add-map  Town Center           '("00001-0000005192"))
(add-map  Surprise Prep HS      '("00001-0000005193"))
(add-map  SE Public Charter     '("00001-0000005194"))
(add-map  Avondale MS           '("00001-0000005195"))
(add-map  St Petersburg M       '("00001-0000005196"))
(add-map  Broward MS            '("00001-0000005197"))
(add-map  Hope-Tolson           '("00001-0000005198"))
(add-map  100 Academy           '("00001-0000005200"))
(add-map  Tempe                 '("00001-0000005201"))
(add-map  Renaissance Wallace   '("00001-0000005202"))
(add-map  Renaissance Academy   '("00001-0000005203"))
(add-map  College Prep          '("00001-0000005204"))
(add-map  West Gilbert E        '("00001-0000005174"))
(add-map  Renaissance Ken.      '("00001-0000005206"))
(add-map  Rosefield Charter     '("00001-0000005208"))
(add-map  South Lake Charter    '("00001-0000005207"))
(add-map  E Mesa Charter        '("00001-0000005209"))
(add-map  Great Western         '("00001-0000005211"))
(add-map  Marietta Charter      '("00001-0000005212"))
(add-map  Camelback             '("00001-0000005213"))
(add-map  Desert West           '("00001-0000005214"))
(add-map  Groveport             '("00001-0000005215"))
(add-map  East Mesa Middle      '("00001-0000005216"))
(add-map  North Manatee         '("00001-0000005218"))
(add-map  Indigo Ranch          '("00001-0000005219"))
(add-map  Smyrna                '("00001-0000005220"))
(add-map  Careers               '("00001-0000005221"))
(add-map  Academy of Success    '("00001-0000005222"))
(add-map  ESM                   '("00001-0000005223"))
(add-map  Masters Academy       '("00001-0000005224"))
(add-map  Wesley Academy        '("00001-0000005225"))
(add-map  Clay Avenue           '("00001-0000005226"))
(add-map  Mableton              '("00001-0000005227"))
(add-map  Camelback MS          '("00001-0000005228"))
(add-map  Romig Road            '("00001-0000005229"))
(add-map  Imagine Life Sci E    '("00001-0000005184"))
(add-map  Firestone             '("00001-0000005231"))
(add-map  Hope-Lamond           '("00001-0000005232"))
(add-map  Careers MS            '("00001-0000005233"))
(add-map  South Vero            '("00001-0000005234"))
(add-map  West Gilbert          '("00001-0000005235"))
(add-map  Imagine Life Sci W    '("00001-0000005236"))
(add-map  Coolidge MS           '("00001-0000005237"))
(add-map  Palmer Ranch          '("00001-0000005238"))
(add-map  Bella Academy         '("00001-0000005239"))
(add-map  Imperial Valley       '("00001-0000005240"))
(add-map  Imagine Cultural Art  '("00001-0000005242"))
(add-map  Imagine Superstition  '("00001-0000005244"))
(add-map  Imagine Valle GB      '("00001-0000005245"))
(add-map  Imagine Akron         '("00001-0000005246"))
(add-map  Imagine Canton        '("00001-0000005247"))
(add-map  Imagine Foundation    '("00001-0000005248"))
(add-map  Imagine Discovery     '("00001-0000005249"))
(add-map  Desert West MS        '("00001-0000005257"))
(add-map  Surprise Prep Middle  '("00001-0000005217"))
(add-map  Imagine Valle         '("00001-0000005230"))




;;;;;;;;;
;NOT SURE WHAT TO DO BELOW
;;;;;;;;;
(defun get-group-code (benefit-id org-group-id employee-status-id)
  (let (table)
    (cond ((member benefit-id Deductible-plan :test #'equal)
	   (setq table Deductible-Plan-Table))
	  ((member benefit-id Co-Pay-plan :test #'equal)
	   (setq table Co-Pay-Plan-Table))
	  ((member benefit-id Rx-Only-Retiree-plan :test #'equal)
	   (setq table Rx-Only-Retiree-Table))
	  (t
	   (setq table nil)))
    (let ((res (find-idc (find-org-group table org-group-id) employee-status-id)))
      (if (and res table)
	  (format nil "~3,'0d" res)
	  ""))))


(defun find-org-group (table org-group-id)
  (if table
      (let* ((group (car table))
	     (group-list (car group)))
	(if (member org-group-id group-list :test #'equal)
	    (cdr group)
	    (find-org-group (cdr table) org-group-id)))))

(defun find-idc (lst employee-status-id)
  (if lst
      (cond ((member employee-status-id Status-Active :test #'equal)
	     (car lst))
	    ((member employee-status-id Status-Retiree :test #'equal)
	     (cadr lst))
	    ((member employee-status-id Status-Cobra :test #'equal)
	     (caddr lst)))))
