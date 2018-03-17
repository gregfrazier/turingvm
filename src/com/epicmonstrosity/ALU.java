/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

class ALU {
    private Memory mem;
    private Registers regs;
    private Jump jmp;

    ALU(Memory mem, Registers regs, Jump jumper) {
        this.mem = mem;
        this.regs = regs;
        this.jmp = jumper;
    }

    void incRegisterValue(int dest) {
        int memLoc1 = regs.getRegister(dest);
        int val1 = mem.getVal(memLoc1);
        mem.setVal(memLoc1, val1 + 1);
    }

    void decRegisterValue(int dest) {
        int memLoc1 = regs.getRegister(dest);
        int val1 = mem.getVal(memLoc1);
        mem.setVal(memLoc1, val1 - 1);
    }

    int getRegisterValue(int dest) {
        int memLoc1 = regs.getRegister(dest);
        int val1 = mem.getVal(memLoc1);
        return val1;
    }

    int setRegisterValue(int dest, int value) {
        int memLoc1 = regs.getRegister(dest);
        mem.setVal(memLoc1, value);
        return value;
    }

    void cmpRegisterValue(int dest, int src) {
        int memLoc1 = regs.getRegister(dest);
        int memLoc2 = regs.getRegister(src);
        int val1 = mem.getVal(memLoc1);
        int val2 = mem.getVal(memLoc2);
        int e = val2 - val1;
        if(e == 0){
            regs.setZeroFlag(true);
        } else {
            regs.setZeroFlag(false);
        }
    }

    int jumpZero(String label) {
        if(regs.getZeroFlag()){
            return jmp.getLabelCodeLocation(label);
        }
        return -1;
    }
}
