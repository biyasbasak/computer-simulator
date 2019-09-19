#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

OPCODES = [
    ("HLT", 0o00),
    ("TRAP", 0o36),
    ("LDR", 0o01),
    ("STR", 0o02),
    ("LDA", 0o03),
    ("LDX", 0o41),
    ("STX", 0o42),
    
    ("JZ", 0o10),
    ("JNE", 0o11),
    ("JCC", 0o12),
    ("JMA", 0o13),
    ("JSR", 0o14),
    ("RFS", 0o15),
    ("SOB", 0o16),
    ("JGE", 0o17),
    
    ("AMR", 0o04),
    ("SMR", 0o05),
    ("AIR", 0o06),
    ("SIR", 0o07),
    
    ("MLT", 0o20),
    ("DVD", 0o21),
    ("TRR", 0o22),
    ("AND", 0o23),
    ("ORR", 0o24),
    ("NOT", 0o25),

    ("SRC", 0o31),
    ("RRC", 0o32),

    ("IN", 0o61),
    ("OUT", 0o62),
    ("CHK", 0o63),

    ("FADD", 0o33),
    ("FSUB", 0o34),
    ("VADD", 0o35),
    ("VSUB", 0o36),
    ("CNVRT", 0o37),
    ("LDFR", 0o50),
    ("STFR", 0o51),
    ]

def build_opcode_table():
  result = []
  for pair in OPCODES:
    name, code = pair
    result.append('put("%s", %d);' % (name, code))
  return '\n'.join(result)

def main():
  code = build_opcode_table()
  with open("/tmp/opcodes.txt", 'wb') as f:
    f.write(code)

  print "Done!"
  return 0

if __name__ == '__main__':
  sys.exit(main())

