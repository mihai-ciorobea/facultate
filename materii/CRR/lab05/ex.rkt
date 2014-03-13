(define (complet-rec l) 
  (cond 
    ((eq? 0 (length l)) '(() ()))
    (else 
     (let (
           (el (car l))
           (res (complet-rec (cdr l)))
           )
       (list (append (list el) res) (append (list (list '1 el)) res)))
     )
    )
  )


(define (complet l) 
  (append (list '-) (complet-rec l))
  )



(define (closedTree l S)
  (list '- 
   (list (closedT (cadr (complet l)) '() S))
   (list (closedT (caddr (complet l)) '() S)))
  )

(define (closedT tree before S) 

  (let (
        (el (car tree))
        (st (cadr tree))
        (dr (caddr tree))
        )
    (if (list? st)
        
        
        (if (eq? #t (contradictie (append before (list el)) S));cond
            el;treu
            (list el (closedT st (list el before) S) (closedT dr (list el before) S)) ;false
            )
        el
        )
    )
  )


;input (P, ~Q)   ((P), (~P,Q), (~Q))  
(define (contradictie expTree S)
  (display (map (lambda (el) (eliminaContradictii el S)) expTree))
  (eq? #f (memq '() (map (lambda (el) (eliminaContradictii el S)) expTree)))
  )

;input P sau (~Q)   ((P), (~P,Q), (~Q))  
(define (eliminaContradictii el S)
  (map (lambda (elS) (eliminaNeg el elS)) S)
  )

;input P/(~P)    (~P,Q)
(define (eliminaNeg el l)
  (if (eq? 0 (length l))
      '()
      (let (
            (first (car l))
            )
        (if (eq? #t (negatie el first))
            (cdr l)
            (append (eliminaNeg el (cdr l)) (list first))
            )
        )
      )
  )

;input P P sau P Q sau P (1 P) sau (Q X) (Q a) etc
;facuta pentru P/~P Q/~Q
(define (negatie el expr)
  ;(display el)
  ;(newline)
  ;(display expr)
  ;(newline)(newline)(newline)
  (if (list? el)
      (if (list? expr)
          #f
          (eq? (cadr el) expr)
          )
      (if (list? expr)
          (eq? (cadr expr) el)
          #f
          )
      )
  )

