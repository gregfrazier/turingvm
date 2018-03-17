/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

import java.io.IOException;
import java.util.List;

class VM {
    private ALU alu;
    private Memory mem;
    private Registers regs;
    private Jump jmp;

    private List<Integer> byteCode;
    private int PC = 0;
    VM(List<Integer> byteCode) {
        this.byteCode = byteCode;
        mem = new Memory(20000);
        regs = new Registers();
        jmp = new Jump();
        alu = new ALU(mem, regs, jmp);
        setPCMemory();
    }
    private void setPCMemory() {
        PC = byteCode.get(0);
        if(PC != 0){ // prime the memory with values
            for(int x = 1; x < PC; ++x)
                mem.setVal(x - 1, byteCode.get(x));
        }
    }
    boolean runSingleOp() {
        int inst = byteCode.get(PC);
        switch(inst & 0xFF) {
            case 0x00: {
                    int d = (inst & 0xFF000000) >> 24;
                    alu.incRegisterValue(d);
                }
                break;
            case 0x01: {
                    int d = (inst & 0xFF000000) >> 24;
                    alu.decRegisterValue(d);
                }
                break;
            case 0x02: {
                    int d = (inst & 0xFF000000) >> 24;
                    regs.nextRegister(d);
                }
                break;
            case 0x03: {
                    int d = (inst & 0xFF000000) >> 24;
                    regs.prevRegister(d);
                }
                break;
            case 0x04: {
                    int d = (inst & 0xFF000000) >> 24;
                    int val = alu.getRegisterValue(d);
                    System.out.print((char)val);
                }
                break;
            case 0x05: {
                    int d = (inst & 0xFF000000) >> 24;
                    try {
                        char ch = (char) System.in.read();
                        alu.setRegisterValue(d, ch);
                    } catch(IOException ex) {
                        System.out.printf("\nCannot read input\n");
                    }
                }
                break;
            case 0x06: {
                    int d = (inst & 0xFF000000) >> 24;
                    int e = (inst & 0x00FF0000) >> 16;
                    alu.cmpRegisterValue(d, e);
                }
                break;
            case 0x07: {
                    int d = (inst & 0xFFFF0000) >> 16;
                    boolean c = regs.getZeroFlag();
                    if(c)
                        PC = d - 1;
                }
                break;
        }
        ++PC;
        return byteCode.size() > PC;
    }
}
