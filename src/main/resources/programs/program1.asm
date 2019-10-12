# Assignment program 1
# Finding the nearest number

!DEFINE SP, 9
!DEFINE NUM, 5

_START:
# setup stack
!ORG 0x100
!LDR R0, 0x7ff
STR R0, X0, SP

# setup index register
!LDX X1, 0x600
!LDX X2, 0x680

# read 20 numbers
LDA R0, X0, NUM 
!PUSH R0

_READNUM:
!LDR R2, PROMPT
!CALL PRINTS
!CALL READI

HLT

# take absolute value, R2 = input, R1 = output
ABS:
!PUSH R3

!LDR R0, _FINABS
STR R0, X0, 31
JGE R2, X0, 31, I
!NEG R2
_FINABS:
!MOV R1, R2

!POP R3
RFS 0

# read a digit, R1 = returned ascii
READD:
!PUSH R3

IN R1, 0
!MOV R2, R1
!LDR R0, 0x30
STR R0, X0, 31
SMR R2, X0, 31

!LDR R0, _FINE1
STR R0, X0, 31
JGE R2, X0, 31, I

!LDR R0, _DEAD
STR R0, X0, 31
JMA X0, 31, I

_FINE1:
!MOV R2, R1
!LDR R0, 0x3A
STR R0, X0, 31
SMR R2, X0, 31

!LDR R0, _DEAD
STR R0, X0, 31
JGE R2, X0, 31, I

!JMP _FINE2

_FINE2:
!LDR R2, 0x30
STR R2, X0, 31
SMR R1, X0, 31

!LDR R0, _FINISH
STR R0, X0, 31
JMA X0, 31, I

_DEAD:
LDA R1, X0, 31
_FINISH:
!POP R3
RFS 0

# read an integer, R1 = return value
READI:
!PUSH R3

!LDR R0, 0

!PUSH R0
LOOPI:

!CALL READD
LDA R2, X0, 31

!LDR R0, _FINISHI
STR R0, X0, 31
TRR R1, R2
JCC 3, X0, 31, I

STR R1, X0, 31
!POP R0
LDA R2, X0, 10
MLT R0, R2
AMR R1, X0, 31
!PUSH R1

!JMP LOOPI

_FINISHI:
!POP R1

!POP R3
RFS 0


# print a string, R2 = string address
PRINTS:
!PUSH R3

LOOP:
STR R2, X0, 31
LDR R1, X0, 31, I

!LDR R0, FINISH
STR R0, X0, 31
JZ R1, X0, 31, I

OUT R1, 1

AIR R2, 1
!LDR R0, LOOP
STR R0, X0, 31
JMA X0, 31, I

FINISH:
!POP R3
RFS 0

# print a number, R2 = number
PRINTI:
!PUSH R3

LDA R1, X0, 0
_LOOPP:
LDA R0, X0, 10
DVD R2, R0

!LDR R0, 0x30
STR R0, X0, 31
AMR R3, X0, 31
!PUSH R3
AIR R1, 1

!LDR R0, _PP
STR R0, X0, 31
JZ R2, X0, 31, I
!JMP _LOOPP

_PP:
!POP R3
OUT R3, 1
SIR R1, 1

!LDR R0, _FINP
STR R0, X0, 31
JZ R1, X0, 31, I
!JMP _PP

_FINP:
!POP R3
RFS 0

# print a newline, '\r\n'
PRINTLN:
!PUSH R3
LDA R0, X0, 13
OUT R0, 1
LDA R0, X0, 10
OUT R0, 1
!POP R3
RFS 0

PROMPT:
!ASCIZ "Please input a number: "

PROMPT2:
!ASCIZ "Please input target number: "

RESULT:
!ASCIZ "Result: "

