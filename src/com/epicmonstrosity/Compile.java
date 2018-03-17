/*
    turingvm
    (c) 2018 Greg Frazier
    Apache License 2.0
    https://github.com/gregfrazier/turingvm
*/
package com.epicmonstrosity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Compile {
    private List<Integer> byteCode;
    private Map<String, Integer> labelLocation;
    private Map<String, List<Integer>> jumpLocation;
    private int codeSegmentLocation = 1;
    Compile() {
        byteCode = new ArrayList<>();
        labelLocation = new HashMap<>();
        jumpLocation = new HashMap<>();
        byteCode.add(codeSegmentLocation); // this is the location where the code segment starts, implicitly it's location 0.
    }
    Compile File(String filename) {
        try {
            String line;
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                String cLine = line.trim();
                switch(cLine.trim().charAt(0)){
                    case ';': break;
                    case '.': { // macro
                        final String[] split = cLine.split(" ");
                        parseMacro(split);
                    }
                        break;
                    default: {
                        final String[] split = cLine.split(" ");
                        scanLine(split);
                    }
                    break;
                }
            }
            bufferedReader.close();
            fileReader.close();
            secondPass();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
            throw new RuntimeException("File not found");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filename + "'");
            throw new RuntimeException("File cannot be read");
        }
        return this;
    }
    Compile String(String code) {
        String[] codeSplitEOL = code.split("\n");
        for (String line: codeSplitEOL) {
            String cLine = line.trim();
            switch(cLine.trim().charAt(0)){
                case ';': break;
                case '.': // macro
                    break;
                default: {
                    final String[] split = cLine.split(" ");
                    scanLine(split);
                }
                break;
            }
        }
        secondPass();
        return this;
    }
    List<Integer> getByteCode() {
        return byteCode;
    }

    private void parseMacro(String[] codeLine) {
        if(codeLine.length > 0) {
            switch (codeLine[0].toUpperCase()) {
                case ".PRELOAD":
                    String[] loadedVars = codeLine[1].replaceAll(" ","").split(",");
                    // Preload treats single characters as CHAR, multiple chars as INT, and 0x as hex
                    for(String indVar : loadedVars)
                        byteCode.add(getValue(indVar));
                    break;
                case ".CODE_SEGMENT":
                    int cLocation = byteCode.size();
                    byteCode.set(0, cLocation);
                    break;
            }
        }
    }

    private int getValue(String var){
        if(var.length() > 0){
            if(var.length() > 1){ // number or hex
                if(var.toLowerCase().charAt(1) == 'x') // hex
                    return Integer.parseInt(var.substring(2), 16);
                return Integer.parseInt(var);
            }else{
                // character
                return var.charAt(0);
            }
        }
        return 0;
    }

    private void scanLine(String[] codeLine) {
        if(codeLine.length > 0){
            switch(codeLine[0].toUpperCase()) {
                case "INC":
                    byteCode.add(INC(codeLine[1].trim()));
                    break;
                case "DEC":
                    byteCode.add(DEC(codeLine[1].trim()));
                    break;
                case "NEXT":
                    byteCode.add(NEXT(codeLine[1].trim()));
                    break;
                case "PREV":
                    byteCode.add(PREV(codeLine[1].trim()));
                    break;
                case "PUTC":
                    byteCode.add(PUTC(codeLine[1].trim()));
                    break;
                case "GETC":
                    byteCode.add(GETC(codeLine[1].trim()));
                    break;
                case "CMP":
                    byteCode.add(CMP(codeLine[1].trim(), codeLine[2].trim()));
                    break;
                case "LABEL":
                    labelLocation.put(codeLine[1].toUpperCase().trim(), byteCode.size());
                    break;
                case "JE":
                    byteCode.add(0x00);
                    List<Integer> y;
                    if(jumpLocation.containsKey(codeLine[1].toUpperCase().trim()))
                        y = jumpLocation.get(codeLine[1].toUpperCase().trim());
                    else
                        y = new ArrayList<>();
                    y.add(byteCode.size() - 1);
                    jumpLocation.put(codeLine[1].toUpperCase().trim(), y);
                    break;
                default:
                    throw new RuntimeException("fix it again tony -- unknown operation, failing.");
            }
        }
    }
    private void secondPass() {
        for (String label : jumpLocation.keySet()) {
            final List<Integer> jloc = jumpLocation.get(label);
            final Integer loc = labelLocation.get(label);
            for (Integer r: jloc) {
                byteCode.set(r, JZ(loc));
            }
        }
    }
    private int INC(String register) {
        byte c = 0x00;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: inc");
    }
    private int DEC(String register) {
        byte c = 0x01;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: dec");
    }
    private int NEXT(String register) {
        byte c = 0x02;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: next");
    }
    private int PREV(String register) {
        byte c = 0x03;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: prev");
    }
    private int PUTC(String register) {
        byte c = 0x04;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: putc");
    }
    private int GETC(String register) {
        byte c = 0x05;
        int reg = Integer.parseInt(register);
        if(reg > -1 && reg < 10) {
            return ((reg << 24) | c);
        }
        throw new RuntimeException("compile error: getc");
    }
    private int CMP(String destRegister, String srcRegister) {
        byte c = 0x06;
        int dreg = Integer.parseInt(destRegister);
        int sreg = Integer.parseInt(srcRegister);
        if((dreg > -1 && dreg < 10) && (sreg > -1 && sreg < 10)) {
            return ((dreg << 24) | (sreg << 16) | c);
        }
        throw new RuntimeException("compile error: cmp");
    }
    private int JZ(int labelLocation) {
        byte c = 0x07;
        return ((labelLocation << 16) | c);
    }
}
