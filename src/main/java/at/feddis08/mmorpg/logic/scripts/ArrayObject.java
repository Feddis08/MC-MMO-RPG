package at.feddis08.mmorpg.logic.scripts;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ArrayObject extends VarObject{
    public ArrayList<VarObject> varList = new ArrayList<>();
    public ArrayObject(String name, ArrayList<VarObject> value){
        super(name, "ARRAY", "");
        this.varList = value;
        this.name = name;
    }
}
