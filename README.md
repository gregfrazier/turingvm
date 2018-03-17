# Eight-Op Turing VM
Turing Complete VM with 8 Instructions

### About
Eight operation 32-bit VM with 80k memory (32-bit boundaries, so essentially 20k) preinitialized to 0.
Ten Registers (0-9) that point to memory locations, all start at location 0.
Technically Turing Complete.

### Operations
* INC Register# - Increments the value a register is pointing to by 1
* DEC Register# - Decrements the value a register is pointing to by 1
* NEXT Register# - Sets register pointer to next memory location
* PREV Register# - Sets register pointer to previous memory location
* PUTC Register# - Displays value of register to screen as an 8bit Char.
* GETC Register# - Stores single char from stdin to memory location
* CMP RegisterA# RegisterB# - Compares RegisterA to RegisterB, sets zero flag to true if they are equal
* JE LabelName - Jump to label if previous compare was equal
* LABEL LabelName - Not an operation, just sets memory location for jump command

### Macros
The "compiler" supports 2 macros, which requires a period to pre-pended to the command:
* .PRELOAD 00,01,33,A,1,0x45 - Comma separated values to preload into memory starting at location 0.
  * Values with single characters are treated as CHAR
  * Values with more than one character are treated as INT, unless 'x' is found as the second char, then treated as HEX integer
* .CODE_SEGMENT - Tells compiler where the start of the code is. Implicitly defined as 0, but if using PRELOAD macro, it needs to be explicity defined.

### Comments
Comments in code cannot be inline, they must be on a separate line starting with a semi-colon ';'

### Sample Code
```
; This will display text to the screen
.PRELOAD !,H,e,l,l,o,32,W,o,r,l,d,!
.CODE_SEGMENT
; Set Register 0 to next location in memory, location of 'H'
NEXT 0
LABEL printMessage
PUTC 0
NEXT 0
; Compare Reg 1 and Reg 0, Register 1 is at location 0, which is '!'
CMP 1 0
; If they are equal, that means we are done printing the text
JE complete
; If we get here, we need to unconditionally jump, so cmp a register to itself.
CMP 1 1
JE printMessage
LABEL complete
```
