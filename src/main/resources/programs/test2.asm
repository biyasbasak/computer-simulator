# Transfer instructions test

!ORG 0x100
!LDR R0, FINE0
STR R0, X0, 16
JNE R0, X0, 16, I
#JZ R0, X0, 16, I
HLT

FINE0:

!LDR R0, FINE1
STR R0, X0, 16
TRR R2, R2
JCC 3, X0, 16, I
HLT 

FINE1:

!LDR R0, FINE2
STR R0, X0, 16
JMA X0, 16, I
HLT 

FINE2:

!LDR R0, FINE3
STR R0, X0, 16
JSR X0, 16, I
LDA R0, X0, 31
!LDR R0, FINE4
STR R0, X0, 16
JMA X0, 16, I
HLT 

FINE3:
RFS 0
HLT

FINE4:
LDA R1, X0, 2

FINE5:
!LDR R0, FINE5
STR R0, X0, 16
SOB R1, X0, 16, I

FINE6:
!LDR R0, FINE7
STR R0, X0, 16
LDA R1, X0, 3
JGE R1, X0, 16, I
HLT 

FINE7:
LDA R0, X0, 31
HLT

