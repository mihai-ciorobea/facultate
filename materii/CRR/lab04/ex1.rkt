
; n = level
; lastResult = lista de constante
; fct = lista de functii
; pred = lista de predicate
; lvl = level-ul curent
; n = lvl-ul cerut
(define (h_univers lastResult fct lvl n )
  (cond 
    ((eq? lvl n) lastResult)
    (else (let 
              ((currentResult (cartesian-product fct lastResult)))
            (append lastResult (h_univers currentResult fct (+ lvl 1) n))
            )
          )
    )
  )

(define (h_base const fct pred n) 
  (let 
      ((univers (h_univers const fct 0 n)))
    (display univers)
    (newline)
    (display (length univers))
    (newline)
    
    (cartesian-product pred univers)
    )
  )










(define (pairs x l)
  (define (aux accu x l)
    (if (null? l)
        accu
        (let ((y (car l))
              (tail (cdr l)))
          (aux (cons (list x y) accu) x tail))))
  (aux '() x l))

(define (cartesian-product l m)   
  (define (aux accu l)
    (if (null? l) 
        accu
        (let ((x (car l)) 
              (tail (cdr l)))
          (aux (append (pairs x m) accu) tail))))
  (aux '() l))