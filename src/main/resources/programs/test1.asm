# LoadStore & arithmetic instruction test
!ORG 0x100

LDA R0, X0, 17
STR R0, X0, 25
LDX X1, 25
STX X1, 26
LDR R2, X1, 1
LDA R0, X0, 31

# addition & subtraction
AIR R0, 31
AIR R0, 3
OUT R0, 1
#IN R0, 0
AIR R0, 3
SIR R0, 2
OUT R0, 1
LDA R0, X0, 2
STR R0, X0, 11
LDA R0, X0, 1
SMR R0, X0, 11
AMR R0, X0, 11
AMR R0, X0, 11

# shifting test
LDA R0, X0, 31
!LDR R0, 0x4444
RRC R0, 2, R, L
RRC R0, 3, L, L
SRC R0, 4, R, A
L0:
!PRINT L0

# bitwise operation
!LDR R0, 0x4444
!LDR R1, 0x3333
ORR R0, R1
!LDR R1, 0x5555
AND R0, R1
NOT R0

# multiplication & division 
!LDR R0, 0xffff
LDA R2, X0, 2
MLT R0, R2
!LDR R0, 0x1005
!LDR R2, 0x1000
DVD R0, R2
!LDR R0, 0xfffe
LDA R2, X0, 2
DVD R0, R2

HLT

