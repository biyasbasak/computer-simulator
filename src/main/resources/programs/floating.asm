# Floating test program
!LDR R0, 16256
STR R0, X0, 31
CNVRT 1, X0, 31
LDFR FR1, X0, 31
FADD FR1, X0, 31

!LDR R0, 16320
STR R0, X0, 31
FSUB FR1, X0, 31
FADD FR1, X0, 31
STFR FR1, X0, 30
LDFR FR0, X0, 30

# Vector test program
!LDR R0, 2
STR R0, X0, 8
AIR R0, 1
STR R0, X0, 9
AIR R0, 1
STR R0, X0, 10

!LDR R0, 5
STR R0, X0, 12
SIR R0, 1
STR R0, X0, 13
SIR R0, 1
STR R0, X0, 14

!LDR R0, 8
STR R0, X0, 16
!LDR R0, 12
STR R0, X0, 17

VADD FR0, X0, 16
VSUB FR0, X0, 16

HLT
