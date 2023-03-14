package at.feddis08.tools.io.text_files.files.file_objects;

import at.feddis08.Boot;

import java.util.ArrayList;
import java.util.Objects;

public class ScriptsFileObject {

    public ArrayList<String> scripts = new ArrayList<>();

    public void parse_config_file(ArrayList<String> lines) {
        Boot.debugLog("Parsing Scripts file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        while ((index + 1) <= lines.size()) {
            parse_ok = false;
            String line = lines.get(index);
            String[] params = line.split(" ");

            if (Objects.equals(params[0], "#")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "")) {
                parse_ok = true;
            }
            if (Objects.equals(params[0], "add:")) {
                if (params.length == 2){
                    scripts.add(params[1]);
                    parse_ok = true;
                }else{
                    parse_ok = true;
                    Boot.consoleLog("ERROR: Could not parse config file. Missing argument. Load default value. Error at line: " + String.valueOf(index + 1));
                }
            }
            index = index + 1;
            if (!(parse_ok))
                Boot.consoleLog("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));

        }
    }
}
