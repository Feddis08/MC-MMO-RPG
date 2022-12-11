package at.feddis08.mmorpg.logic.scripts;

import java.util.ArrayList;
import java.util.Objects;

public class Var_pool {
    public String name = "";
    public ArrayList<VarObject> varObjects = new ArrayList<>();
    public Boolean change_value_of_var(String name, String value){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
                varObjects.get(index).value = value;
            }
            index = index + 1;
        }
        return result;
    }
    public VarObject get_var_by_name(String name){
        Integer index = 0;
        VarObject result = null;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = varObjects.get(index);
            }
            index = index + 1;
        }
        return result;
    }
    public void register_new_var(String name, String type){
        Integer index = 0;
        Boolean result = false;
        while (index < varObjects.size()){
            if (Objects.equals(varObjects.get(index).name, name)){
                result = true;
            }
            index = index + 1;
        }
        if (result){
        }else{
            varObjects.add(new VarObject(name, type, ""));
        }
    }
}
