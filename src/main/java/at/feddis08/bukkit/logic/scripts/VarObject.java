package at.feddis08.bukkit.logic.scripts;

public class VarObject {
    public String name = "";
    public String type = "STRING";
    public String value = "";
    public VarObject(String name, String type, String value){
        this.type = type;
        this.value = value;
        this.name = name;
    }
}
