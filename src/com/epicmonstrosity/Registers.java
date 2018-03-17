/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

class Registers {
    private int[] _registers = new int[10];
    private boolean zeroFlag = false;
    Registers() { }
    int getRegister(int index){
        if(index > -1 && index < 10){
            return _registers[index];
        }
        throw new RuntimeException("great more crap - get register index out of range");
    }
    int setRegister(int index, int value) {
        if(index > -1 && index < 10){
            _registers[index] = value;
            return _registers[index];
        }
        throw new RuntimeException("fix or repair daily - set register index out of range");
    }
    int nextRegister(int index) {
        int v = getRegister(index);
        if(v > -1 && v <= Integer.MAX_VALUE)
            return setRegister(index, getRegister(index) + 1);
        else
            return setRegister(index, 0);
    }
    int prevRegister(int index) {
        int v = getRegister(index);
        if(v > 0 && v <= Integer.MAX_VALUE)
            return setRegister(index, getRegister(index) - 1);
        else
            return setRegister(index, Integer.MAX_VALUE);
    }
    void setZeroFlag(boolean f) {
        zeroFlag = f;
    }
    boolean getZeroFlag() {
        return zeroFlag;
    }
}
