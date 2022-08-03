package at.feddis08.mmorpg.io.files.file_objects;

import at.feddis08.mmorpg.MMORPG;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Inventory;
import at.feddis08.mmorpg.logic.game.trade.invObjects.Slot;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryParser {

    public Inventory inventory = new Inventory();

    public void parse_config_file(ArrayList<String> lines) {
        MMORPG.debugLog("Parsing inventory file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        Integer slot_count = -1;
        Boolean parsing_slot = false;
        Boolean parsing_lore = false;
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
            if (Objects.equals(params[0], "display_name:")) {
                if (params.length == 2) {
                    if (parsing_slot) {
                        inventory.slots.get(slot_count).display_name = params[1];
                        parse_ok = true;
                    } else {
                        inventory.display_name = params[1];
                        parse_ok = true;
                    }
                }
            }
            if (Objects.equals(params[0], "size:")) {
                if (params.length == 2) {
                    inventory.size = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "define_slots:")) {
                parse_ok = true;
                parsing_slot = true;
            }
            if (Objects.equals(params[0], "start:")) {
                if (parsing_slot) {
                    inventory.slots.add(new Slot());
                    slot_count = slot_count + 1;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "index:")) {
                if (parsing_slot) {
                    inventory.slots.get(slot_count).index = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "item_material:")) {
                if (parsing_slot) {
                    inventory.slots.get(slot_count).item_material = params[1];
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "define_lore:")) {
                if (parsing_slot) {
                    parsing_lore = true;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "add:")) {
                if (parsing_slot) {
                    if (parsing_lore){
                        Integer index2 = 1;
                        String string = "";
                        while (index2 < params.length){
                            string = string + params[index2] + " ";
                            index2 = index2 + 1;
                        }
                        inventory.slots.get(slot_count).lore.add(string);
                        parse_ok = true;
                    }
                }
            }
            if (Objects.equals(params[0], "end:")) {
                if (parsing_lore){
                    parsing_lore = false;
                    parse_ok = true;
                }else{
                    parsing_slot = false;
                    slot_count = -1;
                    parse_ok = true;
                }
            }



            index = index + 1;
            if (!(parse_ok))
                MMORPG.consoleLog("ERROR: Could not parse config file. Load default value. Error at line: " + String.valueOf(index));

        }
    }
}

