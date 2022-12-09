package at.feddis08.mmorpg.minecraft.tools.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Book {
    public ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    public BookMeta meta;
    public String title = "";
    public String display_name = "";
    public String author = "";

    public Book(String title, String display_name, String author){
        this.title = title;
        this.display_name = display_name;
        this.author = author;
        create();
    }
    public void create(){
        meta = (BookMeta) book.getItemMeta();
        meta.setTitle(this.title);
        meta.setDisplayName(this.display_name);
        meta.setAuthor(author);
        book.setItemMeta(meta);
    }
}
