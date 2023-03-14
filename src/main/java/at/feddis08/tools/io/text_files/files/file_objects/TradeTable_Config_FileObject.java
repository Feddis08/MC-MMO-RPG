package at.feddis08.tools.io.text_files.files.file_objects;

import at.feddis08.Boot;
import at.feddis08.bukkit.logic.game.trade.TradeTable;

import java.util.ArrayList;
import java.util.Objects;

public class TradeTable_Config_FileObject {

    public ArrayList<TradeTable> tradeTables = new ArrayList<>();
    public String inventory_display_name = "";

    public void parse_config_file(ArrayList<String> lines) {
        Boot.debugLog("Parsing discord_config file ...");
        Integer index = 0;
        Boolean parse_ok = false;
        Boolean parsing_trade_table = false;
        Integer trade_table_count = -1;
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
            if (Objects.equals(params[0], "inventory_display_name:")) {
                if (params.length == 2){
                    if (!(parsing_trade_table)){
                            inventory_display_name = params[1];
                        parse_ok = true;
                    }
                }
            }
            if (Objects.equals(params[0], "define_trades:")) {
                parse_ok = true;
                parsing_trade_table = true;
            }
            if (Objects.equals(params[0], "start:")) {
                if (parsing_trade_table){
                    tradeTables.add(new TradeTable());
                    trade_table_count = trade_table_count + 1;
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "sell_item:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).sell_item = params[1];
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "sell_price:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).sell_price = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "sell_index:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).sell_index = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "sell_amount:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).sell_amount = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "buy_item:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).buy_item = params[1];
                    parse_ok = true;

                }
            }
            if (Objects.equals(params[0], "buy_price:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).buy_price = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "buy_index:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).buy_index = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "buy_amount:")) {
                if (parsing_trade_table){
                    tradeTables.get(trade_table_count).buy_amount = Integer.parseInt(params[1]);
                    parse_ok = true;
                }
            }
            if (Objects.equals(params[0], "end:")) {
                parsing_trade_table = false;
                parse_ok = true;
            }
            index = index + 1;
            if (!(parse_ok))
                Boot.consoleLog("ERROR: Could not parse trade_table file. Load default value. Error at line: " + String.valueOf(index));

        }
    }
}
