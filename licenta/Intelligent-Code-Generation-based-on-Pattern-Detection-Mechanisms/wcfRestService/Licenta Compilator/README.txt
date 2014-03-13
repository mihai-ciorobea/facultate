Reflaction
programare functonala
greedy string matching
word cloud
am ales $ in loc de #
poate fi folosit pt generalizarea codului -- extragand codul identic
e foarte greu de identificat un block de sine statator intr-o secventa

- poate sa isi aleaga la denumiri
		CharacterCount,
        WordCount
- poate sa isi aleaga daca modificatori de access ( public / private ) sunt luati in consinderare la match-ing 




la variable
	- chiar daca nu au acelasi nume, daca sunt folsite in aceleasi zone inseamna ca au aceasi valoare structurala
	- 

	- contexte ( stiva de variabile ) 
	- cand creez o variabila pot sa o adaug la o lista
		- cand o folosesc pot sa ii atasez un stack 
		- cand compar variabile ale caror nume nu seamna, pot sa verific stack-urile intre ele
			- daca seamana => aceasi valoare structurala

			AssignmentExpression
			ExpressionStatement


	- Normal matching -- la variabile face match pe nume 
	- Extended matching -- la variabilele care nu s-au facut match( ca nu au acealasi nume, modificator, etc ) => sa faca blueprint si sa isi dea saeama
		ca reprezinta acelasi lucru. Pot parcuge inca odata toate nodurile. Retin la fiecare variabila stack-urile de labeluri ( field, variable, parameter, identifier) si 
		compar stackurile ( 90% sa fie incluse 1 in alta ). Dupa compare ma uit sa vad ce nu s-a facut match si daca sunt variable le compar.
	- utilizatorul poate speficica el ce variabile au aceeasi valoare structurala


					