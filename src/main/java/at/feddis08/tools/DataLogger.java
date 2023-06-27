package at.feddis08.tools;

import at.feddis08.Boot;
import at.feddis08.bukkit.minecraft.tools.Methods;
import at.feddis08.tools.io.database.Functions;
import at.feddis08.tools.io.database.objects.DataObject;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.sql.SQLException;

public class DataLogger {

    public static void start() throws SQLException {

        log_memory();
        log_cpu_load();


    }

    public static void log_memory() throws SQLException {

        int time = (int) System.currentTimeMillis();
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        long maxMB = heapMemoryUsage.getMax()/(1024*1024);
        long usedMB = heapMemoryUsage.getUsed()/(1024*1024);

        JSONObject data = new JSONObject();
        data.put("maxMB", maxMB);
        data.put("usedMB", usedMB);

        DataObject dataObject = new DataObject();
        dataObject.data_type = "memory_usage";
        dataObject.time = time;
        dataObject.data_json = data;
        dataObject.node_name = Boot.config.server_name;
        dataObject.id = Methods.get_hash(String.valueOf(time));
        dataObject.update_json();

        Functions.createData(dataObject);

    }

    public static void log_cpu_load() throws SQLException {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        int load = (int) (osBean.getCpuLoad()*100);
        int time = (int) System.currentTimeMillis();

        JSONObject data = new JSONObject();
        data.put("load", load);

        DataObject dataObject = new DataObject();
        dataObject.data_type = "cpu_load";
        dataObject.time = time;
        dataObject.data_json = data;
        dataObject.node_name = Boot.config.server_name;
        dataObject.id = Methods.get_hash(String.valueOf(time));
        dataObject.update_json();

        Functions.createData(dataObject);
    }


}
