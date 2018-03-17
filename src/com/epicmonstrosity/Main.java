/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

@SuppressWarnings("StatementWithEmptyBody")
public class Main {
    public static void main(String[] args) {
        if(args.length > 0) {
            System.out.println("Compiling...");
            Compile compiler = new Compile().File(args[0]);
            VM virtualMachine = new VM(compiler.getByteCode());
            System.out.println("Running...");
            while (virtualMachine.runSingleOp()) { }
            System.out.println("\nDone.");
        } else {
            System.out.println("filename required as param.");
        }
    }
}
