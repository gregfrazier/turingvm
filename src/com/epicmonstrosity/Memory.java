/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

import java.nio.BufferOverflowException;

class Memory {
    private Integer[] mem;
    private int memSize = 0;

    Memory(int size){
        mem = new Integer[size];
        memSize = size;
    }
    int getVal(int index){
        if(index < memSize && index > -1)
            return mem[index] == null ? 0 : mem[index];
        throw new BufferOverflowException();
    }
    int setVal(int index, int value){
        if(index < memSize && index > -1) {
            mem[index] = value;
            return value;
        }
        throw new BufferOverflowException();
    }
}
